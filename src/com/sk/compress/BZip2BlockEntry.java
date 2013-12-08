package com.sk.compress;
// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst nonlb 


final class BZip2BlockEntry {

	int anIntArray2200[];
	byte aByte2201;
	int anInt2202;
	int anInt2203;
	byte aByteArray2204[];
	boolean aBooleanArray2205[];
	int decompressedSize;
	int anInt2207;
	int anInt2208;
	int offset;
	int anIntArrayArray2210[][];
	byte aByteArray2211[];
	byte dest[];
	boolean aBooleanArray2213[];
	byte aByteArray2214[];
	int anInt2215;
	int anInt2216;
	int anInt2217;
	int anIntArrayArray2218[][];
	byte aByteArray2219[];
	int anIntArray2220[];
	int anInt2221;
	int anInt2222;
	int anInt2223;
	byte source[];
	int anInt2225;
	int anIntArray2226[];
	int anInt2227;
	int anIntArray2228[];
	byte aByteArrayArray2229[][];
	int anIntArrayArray2230[][];
	int anInt2232;

	BZip2BlockEntry() {
		anIntArray2200 = new int[6];
		anInt2203 = 0;
		aByteArray2204 = new byte[4096];
		aByteArray2211 = new byte[256];
		aByteArray2214 = new byte[18002];
		aByteArray2219 = new byte[18002];
		anIntArray2220 = new int[257];
		anIntArrayArray2218 = new int[6][258];
		aBooleanArray2205 = new boolean[16];
		aBooleanArray2213 = new boolean[256];
		offset = 0;
		anIntArray2226 = new int[16];
		anIntArrayArray2210 = new int[6][258];
		aByteArrayArray2229 = new byte[6][258];
		anIntArrayArray2230 = new int[6][258];
		anIntArray2228 = new int[256];
	}

}
