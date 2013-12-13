package com.sk.wrappers;

import com.sk.cache.fs.CacheSystem;

public class GroundLoader extends RegionDataLoader<GroundRegion> {

	public GroundLoader(CacheSystem cacheSystem) {
		super(cacheSystem);
	}

	@Override
	public GroundRegion load(int regionHash) {
		GroundRegion ret = new GroundRegion(this, regionHash);
		ret.decode(getData(regionHash, 3));
		return ret;
	}

}
