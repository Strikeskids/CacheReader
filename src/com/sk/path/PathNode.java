package com.sk.path;

public class PathNode implements Comparable<PathNode> {
	public int x, y, z;

	public double distance = Double.NaN;
	public double heuristic = Double.NaN;
	public PathNode prev;

	public PathNode(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double f() {
		return distance + heuristic;
	}

	@Override
	public int compareTo(PathNode p) {
		int ret = (int) Math.signum(this.f() - p.f());
		if (ret == 0)
			ret = this.x - p.x;
		if (ret == 0)
			ret = this.y - p.y;
		if (ret == 0)
			ret = this.z - p.z;
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PathNode) {
			PathNode p = (PathNode) o;
			return p.x == this.x && p.y == this.y && p.z == this.z;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.x | (this.y << 15) | (this.z << 30);
	}

	public PathNode derive(int dx, int dy) {
		return derive(dx, dy, 0);
	}

	public PathNode derive(int dz) {
		return derive(0, 0, dz);
	}

	public PathNode derive(int dx, int dy, int dz) {
		return new PathNode(x + dx, y + dy, z + dz);
	}

	@Override
	public String toString() {
		return "PathNode(" + x + ", " + y + ", " + z + ")";
	}
}
