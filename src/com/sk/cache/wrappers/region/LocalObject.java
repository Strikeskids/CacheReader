package com.sk.cache.wrappers.region;

import java.awt.Dimension;
import java.lang.ref.SoftReference;

import com.sk.cache.wrappers.ObjectDefinition;
import com.sk.cache.wrappers.loaders.WrapperLoader;

public class LocalObject {

	private final WrapperLoader<?> loader;
	public final int id;
	public final byte x, y, plane;
	public final byte type, orientation;
	private SoftReference<ObjectDefinition> def = new SoftReference<>(null);
	private Dimension dim;

	public LocalObject(WrapperLoader<?> loader, int id, int lx, int ly, int plane, int type, int orientation) {
		this.id = id;
		this.x = (byte) lx;
		this.y = (byte) ly;
		this.plane = (byte) plane;
		this.type = (byte) type;
		this.orientation = (byte) orientation;
		this.loader = loader;
		if (ly == 9 && lx == 30 || ly <= 8 && ly >= 7 && lx >= 4 && lx <= 5)
			System.out.println(this + " " + getDefinition());
	}

	public ObjectDefinition getDefinition() {
		if (loader == null)
			return null;
		ObjectDefinition ret = def.get();
		if (ret == null) {
			ret = loader.getCacheSystem().objectLoader.load(this.id);
		}
		def = new SoftReference<>(ret);
		return ret;
	}

	public Flagger createFlagger(Region r) {
		int plane = this.plane;
		if ((r.landscapeData[1][x][y] & 2) != 0) {
			plane--;
		}
		if (plane < 0)
			return Flagger.DEFAULT;
		ObjectDefinition def = getDefinition();
		if (def == null)
			return null;
		if (type == 22 && def.blockType == 1) {
			return getFloorFlagger(x, y, plane);
		} else if (9 <= type && type <= 21 && def.blockType != 0) {
			Dimension size = getSize();
			return getInteractiveFlagger(x, size.width, y, size.height, plane, def.walkable, !def.walkable2);
		} else if (0 <= type && type <= 3) {
			return getBoundaryFlagger(x, y, plane, type, orientation, def.walkable, !def.walkable2);
		}
		return null;
	}

	public Dimension getSize() {
		if (this.dim == null) {
			this.dim = createSize();
		}
		return this.dim;
	}

	private Dimension createSize() {
		ObjectDefinition def = getDefinition();
		if (def != null) {
			int lenx = orientation % 2 == 0 ? def.width : def.height;
			int leny = orientation % 2 == 0 ? def.height : def.width;
			return new Dimension(lenx, leny);
		}
		return new Dimension(1, 1);
	}

	private int flipDirection(int dir) {
		return (dir + 4) & 0x7;
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

	private int getBase(boolean flag, boolean flag2) {
		return 1 | (flag ? 0x200 : 0) | (flag2 ? 0x400000 : 0);
	}

	private Flagger getInteractiveFlagger(int startx, int xlen, int starty, int ylen, int plane,
			boolean isWalkable, boolean flag1) {
		FlagGroup group = new FlagGroup();
		int mask = getBase(isWalkable, flag1) << 8;
		for (int x = startx; x < xlen + startx; ++x) {
			for (int y = starty; y < ylen + starty; ++y) {
				group.add(new DefinedFlagger(x, y, plane, mask));
			}
		}
		return group;
	}

	private Flagger getFloorFlagger(int x, int y, int plane) {
		return new DefinedFlagger(x, y, plane, 0x40000);
	}

	private Flagger getBoundaryFlagger(int x, int y, int plane, int type, int orientation, boolean flag,
			boolean flag2) {
		FlagGroup group = new FlagGroup();
		int base = getBase(flag, flag2);
		switch (type) {
		case 0:
			int dir = ((orientation * 2 - 1) & 0x7);
			group.add(new DefinedFlagger(x, y, plane, base << dir));
			group.add(new DefinedFlagger(x + xoff(dir), y + yoff(dir), plane, base << flipDirection(dir)));
			break;
		case 1:
		case 3:
			dir = orientation << 1;
			group.add(new DefinedFlagger(x, y, plane, base << dir));
			group.add(new DefinedFlagger(x + xoff(dir), y + yoff(dir), plane, base << flipDirection(dir)));
			break;
		case 2:
			int dir1 = ((orientation << 1) + 1) & 0x7;
			int dir2 = ((orientation << 1) - 1) & 0x7;
			group.add(new DefinedFlagger(x, y, plane, base << (dir1 | dir2)));
			group.add(new DefinedFlagger(x + xoff(dir1), y + yoff(dir1), plane, base << flipDirection(dir1)));
			group.add(new DefinedFlagger(x + xoff(dir2), y + yoff(dir2), plane, base << flipDirection(dir2)));
			break;
		}
		return group;
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("LocalObject ");
		ret.append(id);
		ret.append(" (");
		ret.append(x);
		ret.append(", ");
		ret.append(y);
		ret.append(", ");
		ret.append(plane);
		ret.append(") ");
		ret.append(type);
		ret.append(" ");
		ret.append(orientation);
		return ret.toString();
	}
}
