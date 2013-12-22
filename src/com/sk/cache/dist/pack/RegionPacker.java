package com.sk.cache.dist.pack;

import com.sk.cache.dist.unpack.PackedRegion;
import com.sk.cache.wrappers.region.Region;
import com.sk.cache.wrappers.region.RegionLoader;

public class RegionPacker extends ProtocolPacker<PackedRegion> {

	public RegionPacker(RegionLoader loader) {
		super(loader, SanitizedRegion.class, PackedRegion.class, 0x3fff);
	}

	@Override
	public SanitizedRegion sanitize(Object o) {
		if (o == null || !(o instanceof Region))
			return null;
		return new SanitizedRegion((Region) o);
	}
}
