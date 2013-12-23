package com.sk.cache.dist.unpack;

import java.util.HashMap;
import java.util.Map;

public class PackedRegion extends Packed {
	public byte[][][] flags;
	public byte[][] stairs;

	public Map<Integer, byte[]> stairMap;

	public static final int WIDTH = 64, HEIGHT = 64;
	private static final byte[][] empty = new byte[WIDTH][];
	private static final byte[] emptyRow = new byte[HEIGHT];
	static {
		for (int i = 0; i < emptyRow.length; ++i)
			emptyRow[i] = -1;
		for (int i = 0; i < empty.length; ++i)
			empty[i] = emptyRow;
	}

	private byte[][] getFlags(int plane) {
		if (plane < flags.length && plane >= 0)
			return flags[plane];
		else
			return empty;
	}

	private byte[] getFlagRow(int plane, int x) {
		byte[][] flags = getFlags(plane);
		if (x < flags.length && x >= 0)
			return flags[x];
		else
			return emptyRow;
	}

	public int getFlag(int x, int y, int plane) {
		byte[] flagRow = getFlagRow(plane, x);
		if (y < flagRow.length && y >= 0)
			return flagRow[y] & 0xff;
		else
			return 0xff;
	}

	public byte[] getStairData(int x, int y, int z) {
		return stairMap.get(x | (y << 6) | (z << 12));
	}

	@Override
	public void initialize() {
		if (stairMap == null)
			return;
		stairMap = new HashMap<Integer, byte[]>();
		for (byte[] stair : stairs) {
			for (int dx = 0; dx < stair[3]; ++dx) {
				for (int dy = 0; dy < stair[4]; ++dy) {
					stairMap.put((stair[0] + dx) | (stair[1] + dy) << 6 | (stair[2]) << 12, new byte[] { stair[3],
							stair[4], (byte) dx, (byte) dy, stair[5] });
				}
			}
		}
		stairs = null;
	}
}
