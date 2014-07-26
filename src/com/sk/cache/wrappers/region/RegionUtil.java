package com.sk.cache.wrappers.region;

public class RegionUtil {
	private static final int REGION_SHIFT = 7;
	private static final int REGION_MASK = 0x7f;
	public static final int REGION_WIDTH = 64;
	public static final int REGION_HEIGHT = 64;

	public static int getRegionId(int regionX, int regionY) {
		return regionX | regionY << REGION_SHIFT;
	}
	
	public static int getRegionX(int regionId) {
		return regionId & REGION_MASK;
	}
	
	public static int getRegionY(int regionId) {
		return regionId >>> REGION_SHIFT;
	}
	
	public static boolean validateRegionCoordinates(int regionX, int regionY) {
		return regionX >= 0 && regionY >= 0 && regionX <= REGION_MASK;
	}
}
