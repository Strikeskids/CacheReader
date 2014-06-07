package com.sk.cache.wrappers.region;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sk.cache.wrappers.StreamedWrapper;
import com.sk.cache.wrappers.loaders.LocalObjectLoader;
import com.sk.datastream.Stream;
import com.sk.util.ArrayHelper;

public class LocalObjects extends StreamedWrapper {

	private final List<LocalObject> objects = new ArrayList<>();
	@SuppressWarnings("unchecked")
	private final List<LocalObject> located[][][] = new List[4][64][64];

	public LocalObjects(LocalObjectLoader loader, int regionHash) {
		super(loader, regionHash);
	}

	@Override
	public void decode(Stream stream) {
		for (int id = -1, idOff = stream.getSmart(); idOff != 0; idOff = (stream.getLeft() > 0 ? stream.getSmart()
				: 0)) {
			id += idOff;
			for (int loc = 0, locOff = stream.getSmart(); locOff != 0; locOff = stream.getSmart()) {
				loc += locOff - 1;
				int plane = loc >> 12;
				int ly = loc & 0x3f;
				int lx = (loc >> 6) & 0x3f;
				int data = stream.getUByte();
				int type = data >> 2;
				int orientation = data & 0x3;
				final LocalObject object = new LocalObject(this.getLoader(), id, lx, ly, plane, type, orientation);
				addObject(object);
			}
		}
	}

	private void addObject(LocalObject o) {
		objects.add(o);
		final Dimension size = o.getSize();
		for (int x = 0; x < size.width; ++x) {
			for (int y = 0; y < size.height; ++y) {
				addObject(o, o.plane, o.x + x, o.y + y);
			}
		}
	}

	private void addObject(LocalObject o, int plane, int x, int y) {
		if (ArrayHelper.checkInBounds(located, plane, x, y)) {
			if (located[plane][x][y] == null)
				located[plane][x][y] = new ArrayList<>(3);
			located[plane][x][y].add(o);
		}
	}

	public List<LocalObject> getObjectsAt(int x, int y, int plane) {
		if (!ArrayHelper.checkInBounds(located, plane, x, y))
			return Arrays.asList();
		List<LocalObject> ret = located[plane][x][y];
		if (ret == null)
			return Arrays.asList();
		else
			return ret;
	}

	public List<LocalObject> getObjects() {
		return objects;
	}
}
