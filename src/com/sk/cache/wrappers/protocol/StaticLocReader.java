package com.sk.cache.wrappers.protocol;

import java.util.Arrays;

public abstract class StaticLocReader extends ProtocolReader {

	private final int[] locs;
	private final int minLoc;

	public StaticLocReader(int... locs) {
		this.locs = locs;
		int min = Integer.MAX_VALUE;
		for (int i : locs) {
			min = Math.min(i, min);
		}
		this.minLoc = min;
		Arrays.sort(this.locs);
	}

	public int[] getTypes() {
		return locs;
	}

	public int getMinimumType() {
		return minLoc;
	}

	@Override
	public boolean validateType(int type) {
		return Arrays.binarySearch(this.locs, type) >= 0;
	}

	@Override
	public void addSelfToGroup(ProtocolGroup group) {
		group.addReader(this, locs);
	}
}
