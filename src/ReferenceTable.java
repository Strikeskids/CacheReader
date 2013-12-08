import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReferenceTable {

	private static final int MINIMUM_FORMAT_FOR_VERSION = 6;
	private static final int FLAG_IDENTIFIERS = 0x1, FLAG_WHIRLPOOL = 0x2;
	private final CacheSystem cache;
	private final int id;

	private int flags;
	private int version;
	private int format;

	private Map<Integer, MetaEntry> entries;

	private Stream data;
	private int[] ids;
	private int[][] children;

	public ReferenceTable(CacheSystem cache, int id) {
		this.cache = cache;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public int getVersion() {
		return version;
	}

	public int getFlags() {
		return flags;
	}

	public int getFormat() {
		return format;
	}

	public MetaEntry getEntry(int id) {
		return entries.get(id);
	}

	public void init() throws IOException {
		if (entries == null) {
			synchronized (this) {
				if (entries == null) {
					entries = new HashMap<>();
					ByteStream compressed = new ByteStream(getTableData());
					byte[] data = compressed.decompress();
					decode(new ByteStream(data));
				}
			}
		}
	}

	private ArchiveMeta getQuery() {
		return cache.getMetaIndex().getArchiveMeta(id);
	}

	private byte[] getTableData() {
		ArchiveMeta query = getQuery();
		return cache.getSourceSystem().readFile(query);
	}

	private void decode(Stream data) {
		this.data = data;
		entries = new HashMap<>();
		decode();
		this.data = null;
	}

	private void decode() {
		decodeFormat();
		if (hasVersion())
			decodeVersion();
		decodeFlags();
		decodeEntries();
	}

	private void decodeFormat() {
		format = data.getUByte();
	}

	private boolean hasVersion() {
		return format >= MINIMUM_FORMAT_FOR_VERSION;
	}

	private void decodeVersion() {
		version = data.getInt();
	}

	private void decodeFlags() {
		flags = data.getUByte();
	}

	private void decodeEntries() {
		int entryCount = data.getUShort();
		ids = getIds(entryCount);
		addEntries();
		if (hasIdentifiers())
			decodeEntryIdentifiers();
		decodeCrcs();
		if (hasWhirlpool())
			decodeWhirlpools();
		decodeVersions();
		decodeChildCounts();
		decodeChildren();
		ids = null;
	}

	private int[] getIds(int size) {
		int[] ids = new int[size];
		int accumulator = 0;
		for (int i = 0; i < ids.length; ++i) {
			int delta = data.getUShort();
			accumulator += delta;
			ids[i] = accumulator;
		}
		return ids;
	}

	private void addEntries() {
		for (int id : ids) {
			entries.put(id, new MetaEntry(id));
		}
	}

	private boolean hasIdentifiers() {
		return (flags & FLAG_IDENTIFIERS) == FLAG_IDENTIFIERS;
	}

	private void decodeEntryIdentifiers() {
		for (int id : ids) {
			entries.get(id).setIdentifier(data.getInt());
		}
	}

	private void decodeCrcs() {
		for (int id : ids) {
			entries.get(id).setCrc(data.getInt());
		}
	}

	private boolean hasWhirlpool() {
		return (flags & FLAG_WHIRLPOOL) == FLAG_WHIRLPOOL;
	}

	private void decodeWhirlpools() {
		for (int id : ids) {
			byte[] rawWhirlpool = entries.get(id).getAndInitializeWhirlpool();
			data.getBytes(rawWhirlpool);
		}
	}

	private void decodeVersions() {
		for (int id : ids) {
			entries.get(id).setVersion(data.getInt());
		}
	}

	private void decodeChildCounts() {
		for (int id : ids) {
			entries.get(id).setChildCount(data.getUShort());
		}
	}

	private void decodeChildren() {
		children = new int[ids.length][];
		for (int i = 0; i < ids.length; ++i) {
			MetaEntry currentEntry = getEntry(ids[i]);
			children[i] = getIds(currentEntry.getChildCount());
			addEntryChildren(currentEntry, children[i]);
		}
		if (hasIdentifiers())
			decodeChildrenIdentifiers();
		children = null;
	}

	private void addEntryChildren(MetaEntry entry, int... ids) {
		for (int id : ids)
			entry.addChild(id);
	}

	private void decodeChildrenIdentifiers() {
		for (int i = 0; i < ids.length; ++i) {
			decodeChildIdentifiers(i, ids[i]);
		}
	}

	private void decodeChildIdentifiers(int index, int entryId) {
		for (int childId : children[index]) {
			entries.get(entryId).getChild(childId).setIdentifier(data.getInt());
		}
	}

}
