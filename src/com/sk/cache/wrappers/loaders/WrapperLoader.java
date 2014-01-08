package com.sk.cache.wrappers.loaders;

import com.sk.cache.fs.CacheSystem;
import com.sk.cache.wrappers.Wrapper;

public abstract class WrapperLoader<T extends Wrapper> {

	protected final CacheSystem cacheSystem;

	public WrapperLoader(CacheSystem cacheSystem) {
		this.cacheSystem = cacheSystem;
	}

	public CacheSystem getCacheSystem() {
		return cacheSystem;
	}

	public abstract T load(int id);
	
	public abstract boolean canLoad(int id);

}
