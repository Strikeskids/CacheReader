package com.sk.cache.wrappers.loaders;

import com.sk.cache.fs.CacheSystem;
import com.sk.cache.wrappers.region.LocalObjects;
import com.sk.cache.wrappers.region.Region;

public class RegionLoader extends RegionDataLoader<Region> {

	public RegionLoader(CacheSystem cacheSystem) {
		super(cacheSystem);
	}

	@Override
	public Region load(int regionHash) {
		Region ret = new Region(this, regionHash);
		ret.decode(getData(regionHash, 3));
		ret.addObjects(cacheSystem.getLoader(LocalObjects.class).load(regionHash));
		return ret;
	}
}
