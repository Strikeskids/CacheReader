package com.sk.cache.wrappers.region;

import com.sk.cache.wrappers.StreamedWrapper;
import com.sk.cache.wrappers.loaders.RegionLoader;
import com.sk.datastream.Stream;
import com.sk.util.ArrayHelper;

public class Region extends StreamedWrapper {

	public int[][][] flags = new int[4][RegionUtil.REGION_WIDTH][RegionUtil.REGION_HEIGHT];
	public byte[][][] landscapeData = new byte[4][RegionUtil.REGION_WIDTH][RegionUtil.REGION_HEIGHT];
	public LocalObjects objects;

	public Region(RegionLoader loader, int id) {
		super(loader, id);
	}

	@Override
	public void decode(Stream stream) {
		for (int plane = 0; plane < 4; plane++) {
			for (int x = 0; x < 64; x++) {
				for (int y = 0; y < 64; y++) {
					int operation = stream.getUByte();
					if ((operation & 0x1) != 0) {
						stream.getUByte();
						stream.getSmart();
					}
					if ((operation & 0x2) != 0)
						landscapeData[plane][x][y] = stream.getByte();
					if ((operation & 0x4) != 0)
						stream.getSmart();
					if ((operation & 0x8) != 0)
						stream.getUByte();
				}
			}
		}
		for (int plane = 0; plane < 4; plane++) {
			for (int x = 0; x < 64; x++) {
				for (int y = 0; y < 64; y++) {
					if ((landscapeData[plane][x][y] & 1) == 1) {
						int z = plane;
						if ((landscapeData[1][x][y] & 2) == 2) {
							z--;
						}
						if (z >= 0 && z <= 3) {
							setFlag(x, y, plane, 0x200000);
						}
					}
				}
			}
		}
	}

	public void addObjects(LocalObjects objects) {
		this.objects = objects;
		for (LocalObject obj : objects.getObjects()) {
			Flagger f = obj.createFlagger(this);
			if (f != null)
				f.flag(this);
		}
	}

	private void setFlag(int lx, int ly, int plane, int flag) {
		if (ArrayHelper.checkInBounds(flags, plane, lx, ly))
			flags[plane][lx][ly] |= flag;
	}
}
