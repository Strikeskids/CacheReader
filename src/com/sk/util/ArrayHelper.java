package com.sk.util;

import java.lang.reflect.Array;

public class ArrayHelper {

	public static boolean checkInBounds(Object array, int... coords) {
		for (int i = 0; i < coords.length; ++i) {
			if (i > 0)
				array = Array.get(array, coords[i - 1]);
			if (!array.getClass().isArray())
				return false;
			if (coords[i] < 0 || coords[i] >= Array.getLength(array))
				return false;
		}
		return true;
	}

}
