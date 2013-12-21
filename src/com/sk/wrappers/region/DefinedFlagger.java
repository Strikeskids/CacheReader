package com.sk.wrappers.region;

public class DefinedFlagger implements Flagger {
	private final int x, y, plane, flag;
	
	public DefinedFlagger(int x, int y, int plane, int flag) {
		this.x = x;
		this.y = y;
		this.plane = plane;
		this.flag = flag;
	}

	private boolean checkBounds(Region r) {
		if (plane < 0 || plane >= r.flags.length)
			return false;
		if (x < 0 || x >= r.flags[plane].length)
			return false;
		if (y < 0 || y >= r.flags[plane][x].length)
			return false;
		return true;
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
