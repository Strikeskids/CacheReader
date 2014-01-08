package com.sk.cache.fs;
import java.io.IOException;

import com.sk.cache.DataSource;
import com.sk.cache.meta.ReferenceTable;

public class CacheSource {

	private final DataSource source;
	private final CacheType[] types = new CacheType[DataSource.MAX_INDEX_FILES];
	private final IndexFile metaIndex;

	public CacheSource(DataSource source) {
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

	public ReferenceTable getReferenceTable(int cacheType) {
		return getCacheType(cacheType).getTable();
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
	
	public DataSource getSourceSystem() {
		return source;
	}

}
