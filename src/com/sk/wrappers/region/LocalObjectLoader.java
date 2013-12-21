package com.sk.wrappers.region;

import com.sk.cache.fs.CacheSystem;
import com.sk.datastream.Stream;

public class LocalObjectLoader extends RegionDataLoader<LocalObjects> {

	public LocalObjectLoader(CacheSystem cacheSystem) {
		super(cacheSystem);
	}

	@Override
	public LocalObjects load(int regionHash) {
		LocalObjects ret = new LocalObjects(this, regionHash);
		for (int i = 0; i < 2; ++i) {
			Stream data = getData(regionHash, i);
			if (data != null)
				ret.decode(data);
		}
		return ret;

	}
}
