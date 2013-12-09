package com.sk.wrappers.protocol;

import java.util.Arrays;

public abstract class StaticLocReader extends ProtocolReader {
	
	private final int[] locs;
	
	public StaticLocReader(int... locs) {
		this.locs = locs;
		Arrays.sort(this.locs);
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
