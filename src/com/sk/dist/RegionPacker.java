package com.sk.dist;

import com.sk.wrappers.region.Region;
import com.sk.wrappers.region.RegionLoader;

public class RegionPacker extends ProtocolPacker<PackedRegion> {

	public RegionPacker(RegionLoader loader) {
		super(loader, SanitizedRegion.class, PackedRegion.class, 0xfff);
	}

	@Override
	public SanitizedRegion sanitize(Object o) {
		if (o == null || !(o instanceof Region))
			return null;
		return new SanitizedRegion((Region) o);
	}
}
