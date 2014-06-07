package com.sk.cache.wrappers.region;

import com.sk.util.ArrayHelper;

public class DefinedFlagger implements Flagger {
	private final int x, y, plane, flag;

	public DefinedFlagger(int x, int y, int plane, int flag) {
		this.x = x;
		this.y = y;
		this.plane = plane;
		this.flag = flag;
	}

	private boolean checkBounds(Region r) {
		return ArrayHelper.checkInBounds(r.flags, plane, x, y);
	}

	public void flag(Region r) {
		if (checkBounds(r))
			r.flags[plane][x][y] |= flag;
	}

	public void unflag(Region r) {
		if (checkBounds(r))
			r.flags[plane][x][y] &= ~flag;
	}
}
