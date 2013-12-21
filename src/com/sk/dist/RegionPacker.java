package com.sk.dist;

import com.sk.wrappers.region.Region;
import com.sk.wrappers.region.RegionLoader;

public class RegionPacker extends ProtocolPacker<PackedRegion> {

	public RegionPacker(RegionLoader loader) {
		super(loader, SanitizedRegion.class, PackedRegion.class);
	}

	@Override
	public SanitizedRegion sanitize(Object o) {
		return new SanitizedRegion((Region) o);
	}
}
