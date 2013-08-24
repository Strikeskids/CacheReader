import java.nio.channels.FileChannel;


public class MetaIndexFile extends IndexFile {

	public MetaIndexFile(FileSystem system) {
		super(system, FileSystem.META_INDEX_FILE_NUM);
	}
	
	@Override
	public FileChannel getChannel() {
		return source.getMetaIndexChannel();
	}

}
