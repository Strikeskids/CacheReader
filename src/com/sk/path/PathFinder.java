package com.sk.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import com.sk.cache.dist.unpack.PackedRegion;

public class PathFinder {

	private static final double STAIR_WEIGHT = 5, FLAG_BIT_WEIGHT = 0.1, PLANE_DISTANCE = 1, CROSS_WEIGHT = 0.001,
			DIAGONAL_MOVE_WEIGHT = 1.2;

	private final RegionSource source;
	private final Map<Integer, PackedRegion> regionCache = new HashMap<>();

	protected PathNode start;
	protected PathNode goal;

	public PathFinder(RegionSource source) {
		this.source = source;
	}

	public synchronized List<PathNode> findPath(PathNode start, PathNode goal) {
		this.start = start;
		this.goal = goal;

		PriorityQueue<PathNode> queue = new PriorityQueue<PathNode>();
		Set<PathNode> seen = new HashSet<>();

		start.distance = 0;
		setHeuristic(start);

		queue.add(start);
		int nodes = 0;
		while (!queue.isEmpty()) {
			PathNode current = queue.poll();
			if (seen.contains(current))
				continue;
			seen.add(current);
			nodes++;
			if (current.equals(this.goal)) {
				this.goal = current;
				break;
			}
			double nodeWeight = getNodeWeight(current) + current.distance;
			for (PathNode adj : getAdjacent(current)) {
				double dist = nodeWeight + getPathWeight(current, adj);
				if (!seen.contains(adj)) {
					adj.distance = dist;
					adj.prev = current;
					setHeuristic(adj);
					queue.add(adj);
				}
			}
		}
		regionCache.clear();
		if (this.goal.prev != null) {
			return createPath();
		}
		return null;
	}

	private double getNodeWeight(PathNode node) {
		int bits = 0;
		for (int flag = getFlag(node); flag != 0; bits++, flag ^= flag & -flag)
			;
		return bits * FLAG_BIT_WEIGHT;
	}

	private double getPathWeight(PathNode source, PathNode destination) {
		if (source.z == destination.z) {
			if ((source.x - destination.x & source.y - destination.y) == 0)
				return 1;
			else
				return DIAGONAL_MOVE_WEIGHT;
		} else {
			return STAIR_WEIGHT;
		}
	}

	private double setHeuristic(PathNode current) {
		if (Double.isNaN(current.heuristic)) {
			int dx1 = current.x - goal.x;
			int dx2 = start.x - goal.x;
			int dy1 = current.y - goal.y;
			int dy2 = start.y - goal.y;
			double planeDistance = Math.max(Math.abs(dx1), Math.abs(dy1));
			double totalDistance = planeDistance + Math.abs(current.z - goal.z) * PLANE_DISTANCE;
			double crossAdjust = Math.abs(dx1 * dy2 - dy1 * dx2) * CROSS_WEIGHT;
			current.heuristic = planeDistance + totalDistance + crossAdjust;
		}
		return current.heuristic;
	}

	private List<PathNode> createPath() {
		List<PathNode> path = new ArrayList<>();
		for (PathNode cur = goal; cur != null; cur = cur.prev) {
			path.add(cur);
		}
		Collections.reverse(path);
		return path;
	}

	private PackedRegion getRegion(int x, int y) {
		int rx = x / PackedRegion.WIDTH;
		int ry = y / PackedRegion.HEIGHT;
		int rh = rx | ry << 7;
		PackedRegion region = regionCache.get(rh);
		if (region == null) {
			region = source.getRegion(rx, ry);
			regionCache.put(rh, region);
		}
		return region;
	}

	private int xoff(int dir) {
		switch (dir) {
		case 0:
		case 7:
		case 6:
			return -1;
		case 1:
		case 5:
			return 0;
		case 2:
		case 3:
		case 4:
			return 1;
		}
		return 0;
	}

	private int yoff(int dir) {
		switch (dir) {
		case 0:
		case 1:
		case 2:
			return 1;
		case 3:
		case 7:
			return 0;
		case 4:
		case 5:
		case 6:
			return -1;
		}
		return 0;
	}

	private int getDirection(int dx, int dy) {
		if (dx == 0) {
			if (dy == 0)
				return -1;
			else if (dy < 0)
				return 5;
			else
				return 1;
		} else if (dx < 0) {
			if (dy > 0)
				return 0;
			else if (dy == 0)
				return 7;
			else
				return 6;
		} else {
			if (dy < 0)
				return 4;
			else if (dy == 0)
				return 3;
			else
				return 2;
		}
	}

	private boolean canPass(PathNode p, int dx, int dy) {
		int mask;
		if ((dx & dy) != 0) {
			mask = (1 << getDirection(dx, dy)) | (1 << getDirection(0, dy)) | (1 << getDirection(dx, 0));
		} else {
			mask = (1 << getDirection(dx, dy));
		}
		return (getFlag(p) & mask) == 0;
	}

	private boolean canWalk(PathNode p, int dx, int dy) {
		if (!canPass(p, dx, dy))
			return false;
		if (!canPass(p.derive(dx, dy), -dx, -dy))
			return false;
		if ((dx & dy) != 0) {
			return canWalk(p, dx, 0) && canWalk(p.derive(dx, 0), 0, dy) || canWalk(p, 0, dy)
					&& canWalk(p.derive(0, dy), dx, 0);
		}
		return true;
	}

	private boolean isAdjacentToObject(PathNode p, PathNode o) {
		if (o.z != p.z)
			return false;
		int dx = o.x - p.x, dy = o.y - p.y;
		if (Math.abs(dx) > 1 || Math.abs(dy) > 1)
			return false;
		if ((dx & dy) != 0)
			return false;
		return canPass(p, dx, dy);
	}

	private int getFlag(PathNode p) {
		PackedRegion region = getRegion(p.x, p.y);
		return region.getFlag(p.x & 0x3f, p.y & 0x3f, p.z);
	}

	private byte[] getStairData(PathNode p) {
		PackedRegion region = getRegion(p.x, p.y);
		return region.getStairData(p.x & 0x3f, p.y & 0x3f, p.z);
	}

	private boolean isStair(PathNode p) {
		return getStairData(p) != null;
	}

	private List<PathNode> findStairEnds(PathNode origin, PathNode stair) {
		List<PathNode> ret = new ArrayList<PathNode>(2);
		byte[] stairData = getStairData(stair);
		if ((stairData[4] & 1) == 1) {
			PathNode end = findStairEnd(origin, stair, 1);
			if (end != null)
				ret.add(end);
		}
		if ((stairData[4] & 2) == 2) {
			PathNode end = findStairEnd(origin, stair, -1);
			if (end != null)
				ret.add(end);
		}
		return ret;
	}

	private PathNode findStairEnd(PathNode origin, PathNode stair, int dz) {
		byte[] stairData = getStairData(stair);
		int sx = -stairData[2], sy = -stairData[3];
		int w = stairData[0], h = stairData[1];
		PathNode stairStart = stair.derive(sx, sy, dz);
		if (origin != null && canPass(origin.derive(dz), stair.x - origin.x, stair.y - origin.y))
			return origin.derive(dz);
		for (int dx = 0; dx < w; ++dx) {
			PathNode off = stairStart.derive(dx, -1);
			if (canPass(off, 0, 1))
				return off;
			off = stairStart.derive(dx, h);
			if (canPass(off, 0, -1))
				return off;
		}
		for (int dy = 0; dy < h; ++dy) {
			PathNode off = stairStart.derive(-1, dy);
			if (canPass(off, 1, 0))
				return off;
			off = stairStart.derive(w, dy);
			if (canPass(off, -1, 0))
				return off;
		}
		return null;
	}

	private List<PathNode> getAdjacent(PathNode cur) {
		List<PathNode> ret = new ArrayList<PathNode>();
		for (int i = 0; i < 8; ++i) {
			int dx = xoff(i);
			int dy = yoff(i);
			PathNode adj = cur.derive(dx, dy);
			if (isStair(adj) && isAdjacentToObject(cur, adj)) {
				ret.addAll(findStairEnds(cur, adj));
			} else if (canWalk(cur, dx, dy))
				ret.add(cur.derive(dx, dy));
		}
		return ret;
	}
}
