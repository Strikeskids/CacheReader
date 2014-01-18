package com.sk.cache.dist.pack;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.sk.cache.dist.unpack.PackedRegion;
import com.sk.cache.wrappers.ObjectDefinition;
import com.sk.cache.wrappers.region.Flagger;
import com.sk.cache.wrappers.region.LocalObject;
import com.sk.cache.wrappers.region.Region;

public class SanitizedRegion extends PackedRegion {

	private Region source;
	private List<LocalObject> removed;
	private List<LocalObject> tmpList;
	public List<StairPair> stairs = new ArrayList<>();

	public static class StairPair {
		public final LocalObject up;
		public final List<Point> upList;
		public final LocalObject down;
		public final List<Point> downList;

		public StairPair(LocalObject u, List<Point> ul, LocalObject d, List<Point> dl) {
			this.up = u;
			this.upList = ul;
			this.down = d;
			this.downList = dl;
		}
	}

	public SanitizedRegion(Region source) {
		this.source = source;
		removed = new ArrayList<>();
		tmpList = new ArrayList<>();
		simplify();
		this.source = null;
		this.removed = null;
		this.tmpList = null;
	}

	private void simplify() {
		for (LocalObject obj : source.objects.getObjects()) {
			if (isPassable(obj)) {
				addPassableObject(obj);
			}
		}
		initializeFlags();
		for (LocalObject obj : source.objects.getObjects()) {
			if (isClimbable(obj, true)) {
				findStairPair(obj);
			}
		}
		resetFlags();
	}

	private void initializeFlags() {
		flags = new byte[source.flags.length][Region.width][Region.height];
		for (int plane = 0; plane < source.flags.length; plane++) {
			boolean different = false;
			for (int x = 0; x < source.flags[plane].length; ++x) {
				for (int y = 0; y < source.flags[plane][x].length; ++y) {
					flags[plane][x][y] = (byte) source.flags[plane][x][y];
					if ((source.flags[plane][x][y] & 0x200100) != 0)
						flags[plane][x][y] = -1;
					if (flags[plane][x][y] != 0)
						different = true;
				}
			}
			if (!different)
				flags[plane] = null;
		}
	}

	private void findStairPair(LocalObject up) {
		tmpList.clear();
		List<Point> area = getUnderneath(up);
		for (Point p : getSurrounding(up)) {
			for (LocalObject obj : source.objects.getObjectsAt(p.x, p.y, up.plane)) {
				if (isClimbDecoration(obj)) {
					area.addAll(getUnderneath(obj));
				}
			}
		}
		for (Point p : area) {
			for (LocalObject possible : source.objects.getObjectsAt(p.x, p.y, up.plane + 1)) {
				if (isClimbable(possible, false)) {
					tmpList.add(possible);
				}
			}
		}
		if (tmpList.size() == 1) {
			findConnection(up, tmpList.get(0));
		}
		if (tmpList.size() > 1) {
			System.out.println("Found multiple stair pairs " + up + " " + tmpList);
		}
	}

	private void findConnection(LocalObject up, LocalObject down) {
		List<Point> passUp = findPassableSurrounding(up, false);
		List<Point> passDown = findPassableSurrounding(down, false);
		stairs.add(new StairPair(up, passUp, down, passDown));
	}

	private List<Point> getUnderneath(LocalObject obj) {
		List<Point> ret = new ArrayList<>();
		int width = obj.getSize().width;
		int height = obj.getSize().height;
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				ret.add(new Point(obj.x + i, obj.y + j));
			}
		}
		return ret;
	}

	private List<Point> getSurrounding(LocalObject obj) {
		List<Point> ret = new ArrayList<>();
		int width = obj.getSize().width;
		int height = obj.getSize().height;
		for (int dx = 0; dx < width; ++dx) {
			ret.add(new Point(obj.x + dx, obj.y - 1));
			ret.add(new Point(obj.x + dx, obj.y + height));
		}
		for (int dy = 0; dy < height; ++dy) {
			ret.add(new Point(obj.x - 1, obj.y + dy));
			ret.add(new Point(obj.x + width, obj.y + dy));
		}
		return ret;
	}

	private List<Point> findPassableSurrounding(LocalObject obj, boolean orientationLimit) {
		List<Point> pass = new ArrayList<>();
		int width = obj.getSize().width;
		int height = obj.getSize().height;
		if (!orientationLimit || obj.orientation % 2 == 0)
			for (int dx = 0; dx < width; ++dx) {
				if (canPass(obj.x + dx, obj.y - 1, obj.plane, 0, 1)) {
					pass.add(new Point(obj.x + dx, obj.y - 1));
				}
				if (canPass(obj.x + dx, obj.y + height, obj.plane, 0, -1)) {
					pass.add(new Point(obj.x + dx, obj.y + height));
				}
			}
		if (!orientationLimit || obj.orientation % 2 != 0)
			for (int dy = 0; dy < height; ++dy) {
				if (canPass(obj.x - 1, obj.y + dy, obj.plane, 1, 0)) {
					pass.add(new Point(obj.x - 1, obj.y + dy));
				}
				if (canPass(obj.x + width, obj.y + dy, obj.plane, -1, 0)) {
					pass.add(new Point(obj.x + width, obj.y + dy));
				}
			}
		return pass;
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

	private boolean canPass(int x, int y, int z, int dx, int dy) {
		if (!validateLocation(x, y, z) || !validateLocation(x + dx, y + dy, z))
			return false;
		int mask;
		if ((dx & dy) != 0) {
			mask = (1 << getDirection(dx, dy)) | (1 << getDirection(0, dy)) | (1 << getDirection(dx, 0));
		} else {
			mask = (1 << getDirection(dx, dy));
		}
		if (z > 0 && (source.landscapeData[z - 1][x][y] & 4) == 0)
			return false;
		return (getFlag(x, y, z) & mask) == 0;
	}

	private void resetFlags() {
		for (LocalObject o : removed) {
			flagSource(o);
		}
	}

	private boolean flagSource(LocalObject obj) {
		Flagger flagger = obj.createFlagger(source);
		if (flagger != null) {
			flagger.flag(source);
			return true;
		}
		return false;
	}

	private boolean unflagSource(LocalObject obj) {
		Flagger flagger = obj.createFlagger(source);
		if (flagger != null) {
			flagger.unflag(source);
			return true;
		}
		return false;
	}

	private void addPassableObject(LocalObject obj) {
		removed.add(obj);
		unflagSource(obj);
	}

	private boolean isClimbDecoration(LocalObject obj) {
		return isOfType(obj, CLIMB_NAMES);
	}

	private boolean isClimbable(LocalObject obj) {
		return isOfType(obj, CLIMB_NAMES, CLIMB_ACTIONS);
	}

	private boolean isClimbable(LocalObject obj, boolean up) {
		return isOfType(obj, CLIMB_NAMES, up ? CLIMB_UP : CLIMB_DOWN);
	}

	private boolean isPassable(LocalObject obj) {
		return isOfType(obj, PASS_NAMES, PASS_ACTIONS);
	}

	private boolean isOfType(LocalObject obj, String[] names, String... actions) {
		ObjectDefinition def = obj.getDefinition();
		return def != null && (names.length == 0 || checkName(def, names))
				&& (actions.length == 0 || checkActions(def, actions));
	}

	private boolean checkName(ObjectDefinition def, String... names) {
		if (def.name == null)
			return false;
		String name = def.name.toLowerCase();
		for (String s : names) {
			if (name.contains(s))
				return true;
		}
		return false;
	}

	private boolean checkActions(ObjectDefinition def, String... actions) {
		for (String action : def.actions) {
			if (action == null)
				continue;
			action = action.toLowerCase();
			for (String s : actions) {
				if (action.contains(s))
					return true;
			}
		}
		return false;
	}

	private boolean validateLocation(int x, int y, int z) {
		return x >= 0 && y >= 0 && z >= 0 && x < WIDTH && y < HEIGHT && z < 4;
	}

	private static final String[] PASS_ACTIONS = { "open", "close", "climb", "squeeze" };
	private static final String[] PASS_NAMES = { "door", "gate", "stile", "vine" };

	private static final String[] CLIMB_NAMES = { "stair", "ladder" };
	private static final String CLIMB_UP = "climb-up", CLIMB_DOWN = "climb-down", CLIMB_ACTIONS[] = { CLIMB_UP,
			CLIMB_DOWN };
}
