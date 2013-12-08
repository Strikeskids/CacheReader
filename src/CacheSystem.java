import java.io.IOException;

public class CacheSystem {

	private final FileSystem source;
	private final CacheType[] types = new CacheType[FileSystem.MAX_INDEX_FILES];
	private final IndexFile metaIndex;

	public CacheSystem(FileSystem source) {
		this.source = source;
		this.metaIndex = new MetaIndexFile(source);
	}

	public IndexFile getMetaIndex() {
		try {
			metaIndex.init();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return metaIndex;
	}

	public IndexFile getIndex(int cacheType) {
		return getCacheType(cacheType).getIndex();
	}

	public CacheType getCacheType(int cacheType) {
		if (!source.validateType(cacheType))
			throw new IndexOutOfBoundsException();
		if (types[cacheType] == null) {
			types[cacheType] = new CacheType(this, cacheType);
			if (!types[cacheType].init())
				throw new RuntimeException("Failed to initialize table and index");
		}
		return types[cacheType];
	}

	public ReferenceTable getReferenceTable(int cacheType) {
		return getCacheType(cacheType).getTable();
	}
	
	public FileSystem getSourceSystem() {
		return source;
	}

}
