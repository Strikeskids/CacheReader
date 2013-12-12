package com.sk.wrappers;

public class LocalObject {

	public final int id;
	public final byte x, y, plane;
	public final byte type, orientation;

	public LocalObject(int id, int lx, int ly, int plane, int type, int orientation) {
		this.id = id;
		this.x = (byte) lx;
		this.y = (byte) ly;
		this.plane = (byte) plane;
		this.type = (byte) type;
		this.orientation = (byte) orientation;
	}
}
