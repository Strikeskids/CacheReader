import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class IndexFile {

	private static final int ENTRY_SIZE = 6;

	protected final FileSystem source;
	protected final int cacheType;

	private ArchiveQuery[] queries;

	public IndexFile(FileSystem system, int cacheType) {
		this.source = system;
		this.cacheType = cacheType;
	}

	public ArchiveQuery getQuery(int archive) {
		if (0 <= archive && archive < queries.length)
			return queries[archive];
		else
			return null;
	}

	protected FileChannel getChannel() {
		return source.getIndexChannel(cacheType);
	}

	public void init() throws IOException {
		if (queries == null) {
			synchronized (this) {
				if (queries == null) {
					FileChannel channel = source.getIndexChannel(cacheType);
					int size = (int) channel.size();

					ByteBuffer data = ByteBuffer.allocate(size);
					channel.read(data);

					Stream dataStream = new ByteStream(data.array());
					decode(dataStream);
				}
			}
		}
	}

	private void decode(Stream dataStream) {
		int size = dataStream.getLeft();
		int queryCount = size / ENTRY_SIZE;
		queries = new ArchiveQuery[queryCount];
		for (int i = 0; i < queries.length; ++i) {
			queries[i] = getQuery(dataStream, i);
		}
	}

	private ArchiveQuery getQuery(Stream dataStream, int index) {
		return new ArchiveQuery(cacheType, index, dataStream.getUInt24(), dataStream.getUInt24());
	}

}
