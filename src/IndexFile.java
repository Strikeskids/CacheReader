import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class IndexFile {

	private static final int ENTRY_SIZE = 6;

	protected final FileSystem source;
	protected final int cacheType;

	private ArchiveMeta[] metas;

	public IndexFile(FileSystem system, int cacheType) {
		this.source = system;
		this.cacheType = cacheType;
	}

	public ArchiveMeta getArchiveMeta(int archive) {
		if (0 <= archive && archive < metas.length)
			return metas[archive];
		else
			return null;
	}

	protected FileChannel getChannel() {
		return source.getIndexChannel(cacheType);
	}

	public void init() throws IOException {
		if (metas == null) {
			synchronized (this) {
				if (metas == null) {
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
		metas = new ArchiveMeta[queryCount];
		for (int i = 0; i < metas.length; ++i) {
			metas[i] = getMeta(dataStream, i);
		}
	}

	private ArchiveMeta getMeta(Stream dataStream, int index) {
		return new ArchiveMeta(cacheType, index, dataStream.getUInt24(), dataStream.getUInt24());
	}

}
