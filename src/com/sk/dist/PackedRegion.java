package com.sk.dist;

public class PackedRegion extends Packed {
	public byte[][][] flags;

	public static final int WIDTH = 64, HEIGHT = 64;
	private static final byte[][] empty = new byte[WIDTH][HEIGHT];

	public byte[][] getFlags(int plane) {
		if (plane < flags.length && plane >= 0)
			return flags[plane];
		else
			return empty;
	}

	public int getFlag(int x, int y, int plane) {
		if (x < 0 || y < 0 || x >= WIDTH || y >= WIDTH)
			return 0;
		else
			return getFlags(plane)[x][y];
	}
}
