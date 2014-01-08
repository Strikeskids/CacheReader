package com.sk.cache.wrappers.loaders;

import com.sk.cache.fs.CacheSource;
import com.sk.cache.wrappers.Wrapper;

public abstract class WrapperLoader {

	protected final CacheSource cacheSystem;

	public WrapperLoader(CacheSource cacheSystem) {
		this.cacheSystem = cacheSystem;
	}

	public CacheSource getCacheSystem() {
		return cacheSystem;
	}

	public abstract Wrapper<?> load(int id);
	
	public abstract boolean canLoad(int id);

}
