package com.sk.wrappers;

import com.sk.datastream.Stream;

public class GroundRegion extends StreamedWrapper<GroundLoader> {

	public GroundRegion(GroundLoader loader, int id) {
		super(loader, id);
	}

	int[][][] landscapeData;

	@Override
	public void decode(Stream stream) {
		landscapeData = new int[4][64][64];
		for (int plane = 0; plane < 4; plane++) {
			for (int x = 0; x < 64; x++) {
				for (int y = 0; y < 64; y++) {
					int operation = stream.getUByte();
					if ((operation & 0x1) != 0) {
						stream.getSmart();
					}
					if ((operation & 0x2) != 0)
						landscapeData[plane][x][y] = stream.getUByte();
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
							landscapeData[plane][x][y] |= 0x200000;
						}
					}
				}
			}
		}
	}

}
