package com.sk.wrappers;

import com.sk.cache.fs.CacheSystem;
import com.sk.datastream.Stream;

public class LocalObjectLoader extends RegionDataLoader<LocalObjects> {

	public LocalObjectLoader(CacheSystem cacheSystem) {
		super(cacheSystem);
	}

	@Override
	public LocalObjects load(int regionHash) {
		Stream data = getData(regionHash, 0, 1);
		LocalObjects ret = new LocalObjects(this, regionHash);
		ret.decode(data);
		return ret;
	}
}
