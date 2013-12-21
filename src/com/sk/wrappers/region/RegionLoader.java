package com.sk.wrappers.region;

import com.sk.cache.fs.CacheSystem;
import com.sk.wrappers.ObjectDefinitionLoader;

public class RegionLoader extends RegionDataLoader<Region> {

	public final LocalObjectLoader localObjectLoader;
	public final ObjectDefinitionLoader objectDefinitionLoader;
	
	public RegionLoader(CacheSystem cacheSystem) {
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
