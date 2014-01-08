package com.sk.cache.wrappers.loaders;

import com.sk.cache.fs.CacheSource;
import com.sk.cache.wrappers.region.Region;

public class RegionLoader extends RegionDataLoader<Region> {

	public final LocalObjectLoader localObjectLoader;
	public final ObjectDefinitionLoader objectDefinitionLoader;

	public RegionLoader(CacheSource cacheSystem) {
		super(cacheSystem);
		this.localObjectLoader = new LocalObjectLoader(cacheSystem);
		this.objectDefinitionLoader = new ObjectDefinitionLoader(cacheSystem);
	}

	@Override
	public Region load(int regionHash) {
		Region ret = new Region(this, regionHash);
		ret.decode(getData(regionHash, 3));
		ret.addObjects(localObjectLoader.load(regionHash));
		return ret;
	}
}
