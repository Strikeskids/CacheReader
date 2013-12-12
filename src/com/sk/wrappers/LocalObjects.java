package com.sk.wrappers;

import java.util.ArrayList;
import java.util.List;

import com.sk.datastream.Stream;

public class LocalObjects extends Wrapper<LocalObjectLoader> {

	private final List<LocalObject> objects = new ArrayList<>();

	public LocalObjects(LocalObjectLoader loader, int regionHash) {
		super(loader, regionHash);
	}

	@Override
	public void decode(Stream stream) {
		while (stream.getLeft() > 0) {
			for (int id = -1, idOff = stream.getSmart(); idOff != 0; idOff = stream.getSmart()) {
				id += idOff;
				for (int loc = 0, locOff = stream.getSmart(); locOff != 0; locOff = stream.getSmart()) {
					loc += locOff - 1;
					int ly = loc & 0x3f;
					int lx = (loc >> 6) & 0x3f;
					int plane = loc >> 12;
					int data = stream.getUByte();
					int type = data >> 2;
					int orientation = data & 0x3;
					objects.add(new LocalObject(id, type, orientation, lx, ly, plane));
				}
			}
		}
	}

	public List<LocalObject> getObjects() {
		return objects;
	}

}
