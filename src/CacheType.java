import java.io.IOException;

public class CacheType {

	private final ReferenceTable table;
	private final IndexFile index;
	private final FileSystem source;
	private final int id;

	public CacheType(CacheSystem source, int id) {
		this.source = source.getSourceSystem();
		this.id = id;
		this.table = new ReferenceTable(source, id);
		this.index = new IndexFile(this.source, id);
	}

	public Archive getArchive(int archive) {
		byte[] data = getArchiveData(archive);
		if (data == null)
			return null;
	}

	private byte[] getArchiveData(int archive) {
		ArchiveMeta query = index.getArchiveMeta(archive);
		if (query == null)
			return null;
		else
			return source.readFile(query);
	}

	public ReferenceTable getTable() {
		return table;
	}

	public IndexFile getIndex() {
		return index;
	}
	
	public boolean init() {
		try {
			this.getTable().init();
			this.getIndex().init();
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
