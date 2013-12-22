package com.sk.dist.unpack;

public class PackedRegion extends Packed {
	public byte[][][] flags;

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
}
