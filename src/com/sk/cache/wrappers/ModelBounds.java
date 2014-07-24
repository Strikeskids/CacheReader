package com.sk.cache.wrappers;

import java.util.Arrays;

public class ModelBounds {

	private final int minX, maxX, minY, maxY, minZ, maxZ;

	public ModelBounds(Model... models) {
		this(0.25d, models);
	}

	public ModelBounds(double partition, Model... models) {
		int[] joined;
		int[][] vals = new int[models.length][];
		for (int i = 0; i < models.length; ++i) {
			vals[i] = models[i].verticesX;
		}
		joined = joinSort(vals);
		minX = getLower(joined, partition);
		maxX = getHigher(joined, partition);
		for (int i = 0; i < models.length; ++i) {
			vals[i] = models[i].verticesY;
		}
		joined = joinSort(vals);
		minY = -getHigher(joined, partition);
		maxY = -getLower(joined, partition);
		for (int i = 0; i < models.length; ++i) {
			vals[i] = models[i].verticesZ;
		}
		joined = joinSort(vals);
		minZ = getLower(joined, partition);
		maxZ = getHigher(joined, partition);
	}

	private int[] joinSort(int[]... arrs) {
		int len = 0;
		for (int i = 0; i < arrs.length; len += arrs[i++].length)
			;
		int[] joined = new int[len];
		for (int arr = 0, ind = 0; arr < arrs.length; ind += arrs[arr++].length) {
			System.arraycopy(arrs[arr], 0, joined, ind, arrs[arr].length);
		}
		Arrays.sort(joined);
		return joined;
	}

	public int[] toBoundsArray() {
		return new int[] { minX, maxX, minY, maxY, minZ, maxZ };
	}

	@Override
	public String toString() {
		return "Model bounds " + Arrays.toString(toBoundsArray());
	}

	private static int getLower(int[] arr, double part) {
		return arr[(int) (arr.length * part)];
	}

	private static int getHigher(int[] arr, double part) {
		return arr[(int) (arr.length * (1 - part))];
	}
}
