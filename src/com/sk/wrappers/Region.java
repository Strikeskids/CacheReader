package com.sk.wrappers;

import com.sk.datastream.Stream;

public class Region extends StreamedWrapper<RegionLoader> {

	public int[][][] flags;
	public LocalObjects objects;
	public final static int width = 64;
	public final static int height = 64;
	
	public Region(RegionLoader loader, int id) {
		super(loader, id);
	}
	
	@Override
	public void decode(Stream stream) {
		flags = new int[4][64][64];
		for (int plane = 0; plane < 4; plane++) {
			for (int x = 0; x < 64; x++) {
				for (int y = 0; y < 64; y++) {
					int operation = stream.getUByte();
					if ((operation & 0x1) != 0) {
						stream.getUByte();
						stream.getSmart();
					}
					if ((operation & 0x2) != 0)
						flags[plane][x][y] = stream.getUByte();
					if ((operation & 0x4) != 0)
						stream.getSmart();
					if ((operation & 0x8) != 0)
						stream.getUByte();
				}
			}
		}
		System.out.println(stream.getLeft());
		for (int plane = 0; plane < 4; plane++) {
			for (int x = 0; x < 64; x++) {
				for (int y = 0; y < 64; y++) {
					if ((flags[plane][x][y] & 1) == 1) {
						int z = plane;
						if ((flags[1][x][y] & 2) == 2) {
							z--;
						}
						if (z >= 0 && z <= 3) {
							flags[plane][x][y] |= 0x200000;
						}
					}
				}
			}
		}
	}

	public void addObjects(LocalObjects objects) {
		this.objects = objects;
		for (LocalObject obj : objects.getObjects()) {
			ObjectDefinition def = this.loader.objectDefinitionLoader.load(obj.id);
			if (def == null)
				continue;

			int lenx = def.type % 2 == 0 ? def.width : def.height;
			int leny = def.type % 2 == 0 ? def.height : def.width;

			if (obj.type == 22) {
				if (def.blockType == 1) {
					setFloorDecorationFlags(obj.x, obj.y, obj.plane);
				}
			} else if (9 <= obj.type && obj.type <= 21) {
				if (def.blockType != 0) {
					setInteractableFlags(obj.x, lenx, obj.y, leny, obj.plane, def.walkable, !def.walkable2);
				}
			} else if (0 <= obj.type && obj.type <= 3) {
				if (def.blockType != 0) {
					setBoundaryFlags(obj.x, obj.y, obj.plane, obj.type, obj.orientation, def.walkable, !def.walkable2);
				}
			}
		}
	}

	private void setFloorDecorationFlags(int lx, int ly, int plane) {
		setFlag(lx, ly, plane, 0x40000);
	}

	private void setBoundaryFlags(int x, int y, int plane, int type, int orientation, boolean flag, boolean flag1) {
		if (type == 0) {
			if (orientation == 0) {
				setFlag(x, y, plane, 128);
				setFlag(x - 1, y, plane, 8);
			}
			if (orientation == 1) {
				setFlag(x, y, plane, 2);
				setFlag(x, 1 + y, plane, 32);
			}
			if (orientation == 2) {
				setFlag(x, y, plane, 8);
				setFlag(1 + x, y, plane, 128);
			}
			if (orientation == 3) {
				setFlag(x, y, plane, 32);
				setFlag(x, -1 + y, plane, 2);
			}
		}
		if (type == 1 || type == 3) {
			if (orientation == 0) {
				setFlag(x, y, plane, 1);
				setFlag(-1 + x, 1 + y, plane, 16);
			}
			if (orientation == 1) {
				setFlag(x, y, plane, 4);
				setFlag(1 + x, 1 + y, plane, 64);
			}
			if (orientation == 2) {
				setFlag(x, y, plane, 16);
				setFlag(x + 1, -1 + y, plane, 1);
			}
			if (orientation == 3) {
				setFlag(x, y, plane, 64);
				setFlag(x - 1, -1 + y, plane, 4);
			}
		}
		if (type == 2) {
			if (orientation == 0) {
				setFlag(x, y, plane, 130);
				setFlag(-1 + x, y, plane, 8);
				setFlag(x, y + 1, plane, 32);
			}
			if (orientation == 1) {
				setFlag(x, y, plane, 10);
				setFlag(x, 1 + y, plane, 32);
				setFlag(1 + x, y, plane, 128);
			}
			if (orientation == 2) {
				setFlag(x, y, plane, 40);
				setFlag(1 + x, y, plane, 128);
				setFlag(x, -1 + y, plane, 2);
			}
			if (orientation == 3) {
				setFlag(x, y, plane, 160);
				setFlag(x, -1 + y, plane, 2);
				setFlag(-1 + x, y, plane, 8);
			}
		}
		if (flag) {
			if (type == 0) {
				if (orientation == 0) {
					setFlag(x, y, plane, 0x10000);
					setFlag(x - 1, y, plane, 0x1000);
				}
				if (orientation == 1) {
					setFlag(x, y, plane, 0x400);
					setFlag(x, 1 + y, plane, 0x4000);
				}
				if (orientation == 2) {
					setFlag(x, y, plane, 0x1000);
					setFlag(x + 1, y, plane, 0x10000);
				}
				if (orientation == 3) {
					setFlag(x, y, plane, 0x4000);
					setFlag(x, -1 + y, plane, 0x400);
				}
			}
			if (type == 1 || type == 3) {
				if (orientation == 0) {
					setFlag(x, y, plane, 0x200);
					setFlag(x - 1, y + 1, plane, 0x2000);
				}
				if (orientation == 1) {
					setFlag(x, y, plane, 0x800);
					setFlag(x + 1, 1 + y, plane, 0x800);
				}
				if (orientation == 2) {
					setFlag(x, y, plane, 0x2000);
					setFlag(x + 1, y - 1, plane, 0x200);
				}
				if (orientation == 3) {
					setFlag(x, y, plane, 0x800);
					setFlag(x - 1, -1 + y, plane, 0x800);
				}
			}
			if (type == 2) {
				if (orientation == 0) {
					setFlag(x, y, plane, 0x10400);
					setFlag(-1 + x, y, plane, 0x1000);
					setFlag(x, y + 1, plane, 0x4000);
				}
				if (orientation == 1) {
					setFlag(x, y, plane, 0x1400);
					setFlag(x, y + 1, plane, 0x4000);
					setFlag(1 + x, y, plane, 0x10000);
				}
				if (orientation == 2) {
					setFlag(x, y, plane, 0x5000);
					setFlag(x + 1, y, plane, 0x10000);
					setFlag(x, y - 1, plane, 0x400);
				}
				if (orientation == 3) {
					setFlag(x, y, plane, 0x14000);
					setFlag(x, -1 + y, plane, 0x400);
					setFlag(x - 1, y, plane, 0x1000);
				}
			}
		}
		if (flag1) {
			if (type == 0) {
				if (orientation == 0) {
					setFlag(x, y, plane, 0x20000000);
					setFlag(x - 1, y, plane, 0x2000000);
				}
				if (orientation == 1) {
					setFlag(x, y, plane, 0x800000);
					setFlag(x, y + 1, plane, 0x8000000);
				}
				if (orientation == 2) {
					setFlag(x, y, plane, 0x2000000);
					setFlag(x + 1, y, plane, 0x20000000);
				}
				if (orientation == 3) {
					setFlag(x, y, plane, 0x8000000);
					setFlag(x, y - 1, plane, 0x800000);
				}
			}
			if (type == 1 || type == 3) {
				if (orientation == 0) {
					setFlag(x, y, plane, 0x400000);
					setFlag(x - 1, y + 1, plane, 0x4000000);
				}
				if (orientation == 1) {
					setFlag(x, y, plane, 0x1000000);
					setFlag(1 + x, 1 + y, plane, 0x10000000);
				}
				if (orientation == 2) {
					setFlag(x, y, plane, 0x4000000);
					setFlag(x + 1, -1 + y, plane, 0x400000);
				}
				if (orientation == 3) {
					setFlag(x, y, plane, 0x10000000);
					setFlag(-1 + x, y - 1, plane, 0x1000000);
				}
			}
			if (type == 2) {
				if (orientation == 0) {
					setFlag(x, y, plane, 0x20800000);
					setFlag(-1 + x, y, plane, 0x2000000);
					setFlag(x, 1 + y, plane, 0x8000000);
				}
				if (orientation == 1) {
					setFlag(x, y, plane, 0x2800000);
					setFlag(x, 1 + y, plane, 0x8000000);
					setFlag(x + 1, y, plane, 0x20000000);
				}
				if (orientation == 2) {
					setFlag(x, y, plane, 0xa000000);
					setFlag(1 + x, y, plane, 0x20000000);
					setFlag(x, y - 1, plane, 0x800000);
				}
				if (orientation == 3) {
					setFlag(x, y, plane, 0x28000000);
					setFlag(x, y - 1, plane, 0x800000);
					setFlag(-1 + x, y, plane, 0x2000000);
				}
			}
		}
	}

	private void setInteractableFlags(int startx, int xlen, int starty, int ylen, int plane, boolean isWalkable,
			boolean flag1) {
		int mask = 0x100;
		if (isWalkable) {
			mask |= 0x20000;
		}
		if (flag1) {
			mask |= 0x40000000;
		}
		for (int x = startx; x < xlen + startx; ++x) {
			if (x >= 0 && x < width) {
				for (int y = starty; y < ylen + starty; ++y) {
					if (y >= 0 && y < height) {
						setFlag(x, y, plane, mask);
					}
				}
			}
		}
	}

	private void setFlag(int lx, int ly, int plane, int flag) {
		if (lx < 0 || lx >= width || ly < 0 || ly >= height || plane < 0 || plane >= flags.length)
			return;
		System.out.printf("Flag %2d %2d %8x %8x %8x\n", lx, ly, flags[plane][lx][ly], flag, flags[plane][lx][ly] | flag);
		flags[plane][lx][ly] |= flag;
	}
}
