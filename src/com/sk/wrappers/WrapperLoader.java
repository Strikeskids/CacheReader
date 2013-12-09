package com.sk.wrappers;

import com.sk.cache.fs.CacheSystem;

public abstract class WrapperLoader {

	protected final CacheSystem cacheSystem;

	public WrapperLoader(CacheSystem cacheSystem) {
		this.cacheSystem = cacheSystem;
	}

	public CacheSystem getCacheSystem() {
		return cacheSystem;
	}

	public abstract Wrapper<?> load(int id);

}
