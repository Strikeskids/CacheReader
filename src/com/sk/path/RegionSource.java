package com.sk.path;

import com.sk.cache.dist.unpack.PackedRegion;

public interface RegionSource {
	public PackedRegion getRegion(int rx, int ry);
}
