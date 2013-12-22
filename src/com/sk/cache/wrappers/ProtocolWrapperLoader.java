package com.sk.cache.wrappers;

import com.sk.cache.fs.Archive;
import com.sk.cache.fs.CacheSystem;
import com.sk.cache.fs.CacheType;
import com.sk.cache.fs.FileData;

public abstract class ProtocolWrapperLoader extends WrapperLoader {

	protected final CacheType cache;

	public ProtocolWrapperLoader(CacheSystem cacheSystem, CacheType cache) {
		super(cacheSystem);
		this.cache = cache;
	}

	protected FileData getValidFile(int id) {
		FileData ret = getFile(id);
		if (ret == null)
			throw new IllegalArgumentException("Bad id");
		return ret;
	}

	protected FileData getFile(int id) {
		Archive archive = cache.getArchive(id >>> 8);
		if (archive == null)
			return null;
		return archive.getFile(id & 0xff);
	}

	@Override
	public boolean canLoad(int id) {
		return getFile(id) != null;
	}

}
