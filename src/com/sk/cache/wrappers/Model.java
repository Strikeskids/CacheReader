package com.sk.cache.wrappers;

import com.sk.cache.wrappers.loaders.ModelLoader;
import com.sk.datastream.ByteStream;
import com.sk.datastream.Stream;

public class Model extends StreamedWrapper {

	public Model(ModelLoader loader, int id) {
		super(loader, id);
	}

	byte trianglePriorities[];
	int verticesX[];
	short aShortArray2327[];
	int verticesY[];
	int triangleCount;
	short trianglesA[];
	int texTriangleCount;
	short aShortArray2334[];
	byte aByteArray2336[];
	int vertexCount;
	short aShortArray2341[];
	byte triangleAlphas[];
	int verticesZ[];
	short aShortArray2345[];
	byte aByteArray2346[];
	short texTrianglesC[];
	short trianglesC[];
	byte aByteArray2349[];
	byte aByteArray2350[];
	static int anInt2351;
	static int anInt2352;
	short aShortArray2353[];
	byte aByte2354;
	static int anInt2355;
	short texTrianglesA[];
	static boolean isJSEnabled = false;
	static int anInt2358;
	short trianglesB[];
	static int anInt2360;
	// Class154 aClass154Array2361[];
	static int anInt2362;
	byte aByteArray2363[];
	static int anInt2364;
	// Class102 aClass102Array2365[];
	static int anInt2366;
	byte aByteArray2367[];
	short texTrianglesB[];
	byte aByteArray2369[];
	byte aByteArray2370[];
	int triangleSkins[];
	short aShortArray2372[];
	static int anInt2373;
	int anInt2374;
	int vertexSkins[];
	short triangleColors[];

	private final void initNewFormat(Stream stream) {
		Stream stream_1 = new ByteStream(stream.getAllBytes());
		Stream class3_sub2_2 = new ByteStream(stream.getAllBytes());
		Stream class3_sub2_3 = new ByteStream(stream.getAllBytes());
		Stream class3_sub2_4 = new ByteStream(stream.getAllBytes());
		Stream class3_sub2_5 = new ByteStream(stream.getAllBytes());
		Stream class3_sub2_6 = new ByteStream(stream.getAllBytes());
		stream.seek(-23 + stream.getLength());
		vertexCount = stream.getUShort();
		triangleCount = stream.getUShort();
		texTriangleCount = stream.getUByte();
		int i = stream.getUByte();
		boolean flag = (1 & i) == 1;
		boolean flag1 = ~(i & 2) == -3;
		int j = stream.getUByte();
		int k = stream.getUByte();
		int l = stream.getUByte();
		int i1 = stream.getUByte();
		int j1 = stream.getUByte();
		int k1 = stream.getUShort();
		int l1 = stream.getUShort();
		int i2 = stream.getUShort();
		int j2 = stream.getUShort();
		int k2 = stream.getUShort();
		int l2 = 0;
		int i3 = 0;
		int j3 = 0;
		if (texTriangleCount > 0) {
			aByteArray2363 = new byte[texTriangleCount];
			stream.seek(0);
			for (int k3 = 0; texTriangleCount > k3; k3++) {
				byte byte1 = aByteArray2363[k3] = stream.getByte();
				if (byte1 == 0) {
					l2++;
				}
				if (byte1 == 2) {
					j3++;
				}
				if (byte1 >= 1 && byte1 <= 3) {
					i3++;
				}
			}

		}
		int l3 = texTriangleCount;
		int i4 = l3;
		l3 += vertexCount;
		int j4 = l3;
		if (flag) {
			l3 += triangleCount;
		}
		int k4 = l3;
		l3 += triangleCount;
		int l4 = l3;
		if (j == 255) {
			l3 += triangleCount;
		}
		int i5 = l3;
		if (l == 1) {
			l3 += triangleCount;
		}
		int j5 = l3;
		if (j1 == 1) {
			l3 += vertexCount;
		}
		int k5 = l3;
		if (k == 1) {
			l3 += triangleCount;
		}
		int l5 = l3;
		l3 += j2;
		int i6 = l3;
		if (i1 == 1) {
			l3 += 2 * triangleCount;
		}
		int j6 = l3;
		l3 += k2;
		int k6 = l3;
		l3 += triangleCount * 2;
		int l6 = l3;
		l3 += k1;
		int i7 = l3;
		l3 += l1;
		int j7 = l3;
		l3 += i2;
		int k7 = l3;
		l3 += 6 * l2;
		int l7 = l3;
		l3 += i3 * 6;
		int i8 = l3;
		l3 += 6 * i3;
		int j8 = l3;
		l3 += i3;
		int k8 = l3;
		l3 += i3;
		int l8 = l3;
		l3 += i3 + (2 * j3);
		if (flag) {
			aByteArray2346 = new byte[triangleCount];
		}
		verticesZ = new int[vertexCount];
		trianglesB = new short[triangleCount];
		trianglesA = new short[triangleCount];
		if (i1 == 1) {
			aShortArray2334 = new short[triangleCount];
		}
		if (i1 == 1 && texTriangleCount > 0) {
			aByteArray2370 = new byte[triangleCount];
		}
		stream.seek(i4);
		if (texTriangleCount > 0) {
			texTrianglesC = new short[texTriangleCount];
			texTrianglesB = new short[texTriangleCount];
			if (j3 > 0) {
				aByteArray2336 = new byte[j3];
				aByteArray2369 = new byte[j3];
			}
			if (i3 > 0) {
				aByteArray2350 = new byte[i3];
				aShortArray2341 = new short[i3];
				aByteArray2367 = new byte[i3];
				aShortArray2353 = new short[i3];
				aShortArray2327 = new short[i3];
				aByteArray2349 = new byte[i3];
			}
			texTrianglesA = new short[texTriangleCount];
		}
		int i9 = l3;
		if (l == 1) {
			triangleSkins = new int[triangleCount];
		}
		if (j1 == 1) {
			vertexSkins = new int[vertexCount];
		}
		verticesY = new int[vertexCount];
		if (k == 1) {
			triangleAlphas = new byte[triangleCount];
		}
		triangleColors = new short[triangleCount];
		trianglesC = new short[triangleCount];
		verticesX = new int[vertexCount];
		if (j == 255) {
			trianglePriorities = new byte[triangleCount];
		} else {
			aByte2354 = (byte) j;
		}
		stream_1.seek(l6);
		class3_sub2_2.seek(i7);
		class3_sub2_3.seek(j7);
		class3_sub2_4.seek(j5);
		int j9 = 0;
		int k9 = 0;
		int l9 = 0;
		for (int i10 = 0; vertexCount > i10; i10++) {
			int j10 = stream.getUByte();
			int l10 = 0;
			if ((1 & j10) != 0) {
				l10 = stream_1.getSmart();
			}
			int j11 = 0;
			if ((2 & j10) != 0) {
				j11 = class3_sub2_2.getSmart();
			}
			int l11 = 0;
			if (~(j10 & 4) != -1) {
				l11 = class3_sub2_3.getSmart();
			}
			verticesX[i10] = j9 + l10;
			verticesY[i10] = j11 + k9;
			verticesZ[i10] = l9 + l11;
			k9 = verticesY[i10];
			j9 = verticesX[i10];
			l9 = verticesZ[i10];
			if (j1 == 1) {
				vertexSkins[i10] = class3_sub2_4.getUByte();
			}
		}

		stream.seek(k6);
		stream_1.seek(j4);
		class3_sub2_2.seek(l4);
		class3_sub2_3.seek(k5);
		class3_sub2_4.seek(i5);
		class3_sub2_5.seek(i6);
		class3_sub2_6.seek(j6);
		for (int k10 = 0; triangleCount > k10; k10++) {
			triangleColors[k10] = (short) stream.getUShort();
			if (flag) {
				aByteArray2346[k10] = stream_1.getByte();
			}
			if (j == 255) {
				trianglePriorities[k10] = class3_sub2_2.getByte();
			}
			if (k == 1) {
				triangleAlphas[k10] = class3_sub2_3.getByte();
			}
			if (l == 1) {
				triangleSkins[k10] = class3_sub2_4.getUByte();
			}
			if (i1 == 1) {
				aShortArray2334[k10] = (short) (-1 + class3_sub2_5.getUShort());
			}
			if (aByteArray2370 != null) {
				if (aShortArray2334[k10] == -1) {
					aByteArray2370[k10] = -1;
				} else {
					aByteArray2370[k10] = (byte) (class3_sub2_6.getUByte() - 1);
				}
			}
		}

		stream.seek(l5);
		anInt2374 = -1;
		stream_1.seek(k4);
		int i11 = 0;
		int k11 = 0;
		int i12 = 0;
		int j12 = 0;
		for (int k12 = 0; triangleCount > k12; k12++) {
			int l12 = stream_1.getUByte();
			if (l12 == 1) {
				i11 = (short) (j12 + stream.getSmart());
				j12 = i11;
				k11 = (short) (stream.getSmart() + j12);
				j12 = k11;
				i12 = (short) (stream.getSmart() + j12);
				j12 = i12;
				trianglesA[k12] = (short) i11;
				trianglesB[k12] = (short) k11;
				trianglesC[k12] = (short) i12;
				if (anInt2374 < i11) {
					anInt2374 = i11;
				}
				if (anInt2374 < k11) {
					anInt2374 = k11;
				}
				if (anInt2374 < i12) {
					anInt2374 = i12;
				}
			}
			if (l12 == 2) {
				k11 = i12;
				i12 = (short) (stream.getSmart() + j12);
				j12 = i12;
				trianglesA[k12] = (short) i11;
				trianglesB[k12] = (short) k11;
				trianglesC[k12] = (short) i12;
				if (anInt2374 < i12) {
					anInt2374 = i12;
				}
			}
			if (l12 == 3) {
				i11 = i12;
				i12 = (short) (stream.getSmart() + j12);
				trianglesA[k12] = (short) i11;
				j12 = i12;
				trianglesB[k12] = (short) k11;
				trianglesC[k12] = (short) i12;
				if (anInt2374 < i12) {
					anInt2374 = i12;
				}
			}
			if (l12 == 4) {
				int j13 = i11;
				i11 = k11;
				k11 = j13;
				i12 = (short) (stream.getSmart() + j12);
				j12 = i12;
				trianglesA[k12] = (short) i11;
				trianglesB[k12] = (short) k11;
				trianglesC[k12] = (short) i12;
				if (i12 > anInt2374) {
					anInt2374 = i12;
				}
			}
		}

		anInt2374++;
		stream.seek(k7);
		stream_1.seek(l7);
		class3_sub2_2.seek(i8);
		class3_sub2_3.seek(j8);
		class3_sub2_4.seek(k8);
		class3_sub2_5.seek(l8);
		for (int i13 = 0; i13 < texTriangleCount; i13++) {
			int k13 = aByteArray2363[i13] & 0xff;
			if (k13 == 0) {
				texTrianglesA[i13] = (short) stream.getUShort();
				texTrianglesB[i13] = (short) stream.getUShort();
				texTrianglesC[i13] = (short) stream.getUShort();
			}
			if (k13 == 1) {
				texTrianglesA[i13] = (short) stream_1.getUShort();
				texTrianglesB[i13] = (short) stream_1.getUShort();
				texTrianglesC[i13] = (short) stream_1.getUShort();
				aShortArray2327[i13] = (short) class3_sub2_2.getUShort();
				aShortArray2341[i13] = (short) class3_sub2_2.getUShort();
				aShortArray2353[i13] = (short) class3_sub2_2.getUShort();
				aByteArray2367[i13] = class3_sub2_3.getByte();
				aByteArray2350[i13] = class3_sub2_4.getByte();
				aByteArray2349[i13] = class3_sub2_5.getByte();
			}
			if (k13 == 2) {
				texTrianglesA[i13] = (short) stream_1.getUShort();
				texTrianglesB[i13] = (short) stream_1.getUShort();
				texTrianglesC[i13] = (short) stream_1.getUShort();
				aShortArray2327[i13] = (short) class3_sub2_2.getUShort();
				aShortArray2341[i13] = (short) class3_sub2_2.getUShort();
				aShortArray2353[i13] = (short) class3_sub2_2.getUShort();
				aByteArray2367[i13] = class3_sub2_3.getByte();
				aByteArray2350[i13] = class3_sub2_4.getByte();
				aByteArray2349[i13] = class3_sub2_5.getByte();
				aByteArray2369[i13] = class3_sub2_5.getByte();
				aByteArray2336[i13] = class3_sub2_5.getByte();
			}
			if (k13 == 3) {
				texTrianglesA[i13] = (short) stream_1.getUShort();
				texTrianglesB[i13] = (short) stream_1.getUShort();
				texTrianglesC[i13] = (short) stream_1.getUShort();
				aShortArray2327[i13] = (short) class3_sub2_2.getUShort();
				aShortArray2341[i13] = (short) class3_sub2_2.getUShort();
				aShortArray2353[i13] = (short) class3_sub2_2.getUShort();
				aByteArray2367[i13] = class3_sub2_3.getByte();
				aByteArray2350[i13] = class3_sub2_4.getByte();
				aByteArray2349[i13] = class3_sub2_5.getByte();
			}
		}

		/*
		 * if (flag1) { stream.seek(i9); int l13 = stream.getUByte(); if (l13 > 0) { aClass102Array2365 = new
		 * Class102[l13]; for (int i14 = 0; i14 < l13; i14++) { int k14 = stream.getUShort(); int i15 =
		 * stream.getUShort(); byte byte2; if (j != 255) { byte2 = (byte) j; } else { byte2 =
		 * trianglePriorities[i15]; } aClass102Array2365[i14] = new Class102(k14, trianglesA[i15],
		 * trianglesB[i15], trianglesC[i15], byte2); }
		 * 
		 * } int j14 = stream.getUByte(); if (j14 > 0) { aClass154Array2361 = new Class154[j14]; for (int l14
		 * = 0; j14 > l14; l14++) { aClass154Array2361[l14] = new Class154(stream.getUShort(),
		 * stream.getUShort()); }
		 * 
		 * } }
		 */
	}

	final int[][] method2074() {
		int ai[] = new int[256];
		int i = 0;
		for (int j = 0; triangleCount > j; j++) {
			int k = triangleSkins[j];
			if (k >= 0) {
				if (k > i) {
					i = k;
				}
				ai[k]++;
			}
		}

		int ai1[][] = new int[1 + i][];
		for (int l = 0; l <= i; l++) {
			ai1[l] = new int[ai[l]];
			ai[l] = 0;
		}

		for (int i1 = 0; triangleCount > i1; i1++) {
			int j1 = triangleSkins[i1];
			if (j1 >= 0) {
				ai1[j1][ai[j1]++] = i1;
			}
		}

		return ai1;
	}

	private final void initOldFormat(Stream stream) {
		boolean flag1 = false;
		boolean flag2 = false;
		Stream stream_1 = new ByteStream(stream.getAllBytes());
		Stream stream_2 = new ByteStream(stream.getAllBytes());
		Stream stream_3 = new ByteStream(stream.getAllBytes());
		Stream stream_4 = new ByteStream(stream.getAllBytes());
		stream.seek(stream.getLength() - 18);
		vertexCount = stream.getUShort();
		triangleCount = stream.getUShort();
		texTriangleCount = stream.getUByte();
		int useTextures = stream.getUByte();
		int useTriPriorities = stream.getUByte();
		int useTransparency = stream.getUByte();
		int useTriangleSkinning = stream.getUByte();
		int useVertexSkinning = stream.getUByte();
		int xlen = stream.getUShort();
		int ylen = stream.getUShort();
		int zlen = stream.getUShort();
		int trilen = stream.getUShort();
		int off = 0;
		int vertexModOffset = off;
		off += vertexCount;
		int triMeshLinkOffset = off;
		off += triangleCount;
		int i3 = off;
		if (useTriPriorities == 255) {
			off += triangleCount;
		}
		int j3 = off;
		if (useTriangleSkinning == 1) {
			off += triangleCount;
		}
		int k3 = off;
		if (useTextures == 1) {
			off += triangleCount;
		}
		int l3 = off;
		if (useVertexSkinning == 1) {
			off += vertexCount;
		}
		int i4 = off;
		if (useTransparency == 1) {
			off += triangleCount;
		}
		int triVPointOffset = off;
		off += trilen;
		int triColorOffset = off;
		off += triangleCount * 2;
		int l4 = off;
		off += texTriangleCount * 6;
		int vertexXOffset = off;
		off += xlen;
		int vertexYOffset = off;
		off += ylen;
		int vertexZOffset = off;
		triangleColors = new short[triangleCount];
		if (useTriangleSkinning == 1) {
			triangleSkins = new int[triangleCount];
		}
		verticesY = new int[vertexCount];
		off += zlen;
		if (useVertexSkinning == 1) {
			vertexSkins = new int[vertexCount];
		}
		if (texTriangleCount > 0) {
			texTrianglesC = new short[texTriangleCount];
			texTrianglesB = new short[texTriangleCount];
			texTrianglesA = new short[texTriangleCount];
			aByteArray2363 = new byte[texTriangleCount];
		}
		if (useTextures == 1) {
			aByteArray2346 = new byte[triangleCount];
			aShortArray2334 = new short[triangleCount];
			aByteArray2370 = new byte[triangleCount];
		}
		stream.seek(vertexModOffset);
		if (useTransparency == 1) {
			triangleAlphas = new byte[triangleCount];
		}
		if (useTriPriorities != 255) {
			aByte2354 = (byte) useTriPriorities;
		} else {
			trianglePriorities = new byte[triangleCount];
		}
		verticesZ = new int[vertexCount];
		trianglesB = new short[triangleCount];
		verticesX = new int[vertexCount];
		trianglesA = new short[triangleCount];
		trianglesC = new short[triangleCount];
		stream_1.seek(vertexXOffset);
		stream_2.seek(vertexYOffset);
		stream_3.seek(vertexZOffset);
		stream_4.seek(l3);
		int xoff = 0;
		int yoff = 0;
		int zoff = 0;
		for (int k6 = 0; k6 < vertexCount; k6++) {
			int offsetFlag = stream.getUByte();
			int x = 0;
			if ((offsetFlag & 1) != 0) {
				x = stream_1.getSmart();
			}
			int y = 0;
			if ((offsetFlag & 2) != 0) {
				y = stream_2.getSmart();
			}
			int z = 0;
			if ((offsetFlag & 4) != 0) {
				z = stream_3.getSmart();
			}
			verticesX[k6] = xoff + x;
			verticesY[k6] = yoff + y;
			verticesZ[k6] = z + zoff;
			xoff = verticesX[k6];
			yoff = verticesY[k6];
			zoff = verticesZ[k6];
			if (useVertexSkinning == 1) {
				vertexSkins[k6] = stream_4.getUByte();
			}
		}

		stream.seek(triColorOffset);
		stream_1.seek(k3);
		stream_2.seek(i3);
		stream_3.seek(i4);
		stream_4.seek(j3);
		for (int i7 = 0; i7 < triangleCount; i7++) {
			triangleColors[i7] = (short) stream.getUShort();
			if (useTextures == 1) {
				int k7 = stream_1.getUByte();
				if ((k7 & 1) != 1) {
					aByteArray2346[i7] = 0;
				} else {
					aByteArray2346[i7] = 1;
					flag1 = true;
				}
				if ((k7 & 2) == 2) {
					aByteArray2370[i7] = (byte) (k7 >> 2);
					aShortArray2334[i7] = triangleColors[i7];
					triangleColors[i7] = 127;
					if (aShortArray2334[i7] != -1) {
						flag2 = true;
					}
				} else {
					aByteArray2370[i7] = -1;
					aShortArray2334[i7] = -1;
				}
			}
			if (useTriPriorities == 255) {
				trianglePriorities[i7] = stream_2.getByte();
			}
			if (useTransparency == 1) {
				triangleAlphas[i7] = stream_3.getByte();
			}
			if (useTriangleSkinning == 1) {
				triangleSkins[i7] = stream_4.getUByte();
			}
		}

		anInt2374 = -1;
		stream.seek(triVPointOffset);
		stream_1.seek(triMeshLinkOffset);
		int a = 0;
		int b = 0;
		int c = 0;
		int i9 = 0;
		for (int j9 = 0; j9 < triangleCount; j9++) {
			int type = stream_1.getUByte();
			if (type == 1) {
				a = (short) (i9 + stream.getSmart());
				i9 = a;
				b = (short) (stream.getSmart() + i9);
				i9 = b;
				c = (short) (i9 + stream.getSmart());
				i9 = c;
				trianglesA[j9] = (short) a;
				trianglesB[j9] = (short) b;
				trianglesC[j9] = (short) c;
				if (a > anInt2374) {
					anInt2374 = a;
				}
				if (b > anInt2374) {
					anInt2374 = b;
				}
				if (c > anInt2374) {
					anInt2374 = c;
				}
			}
			if (type == 2) {
				b = c;
				c = (short) (stream.getSmart() + i9);
				i9 = c;
				trianglesA[j9] = (short) a;
				trianglesB[j9] = (short) b;
				trianglesC[j9] = (short) c;
				if (anInt2374 < c) {
					anInt2374 = c;
				}
			}
			if (type == 3) {
				a = c;
				c = (short) (i9 + stream.getSmart());
				i9 = c;
				trianglesA[j9] = (short) a;
				trianglesB[j9] = (short) b;
				trianglesC[j9] = (short) c;
				if (anInt2374 < c) {
					anInt2374 = c;
				}
			}
			if (type == 4) {
				int i10 = a;
				a = b;
				b = i10;
				c = (short) (stream.getSmart() + i9);
				i9 = c;
				trianglesA[j9] = (short) a;
				trianglesB[j9] = (short) b;
				trianglesC[j9] = (short) c;
				if (anInt2374 < c) {
					anInt2374 = c;
				}
			}
		}

		stream.seek(l4);
		anInt2374++;
		for (int l9 = 0; texTriangleCount > l9; l9++) {
			aByteArray2363[l9] = 0;
			texTrianglesA[l9] = (short) stream.getUShort();
			texTrianglesB[l9] = (short) stream.getUShort();
			texTrianglesC[l9] = (short) stream.getUShort();
		}

		if (aByteArray2370 != null) {
			boolean flag3 = false;
			for (int j10 = 0; j10 < triangleCount; j10++) {
				int k10 = aByteArray2370[j10] & 0xff;
				if (k10 != 255) {
					if ((0xffff & texTrianglesA[k10]) == trianglesA[j10]
							&& (0xffff & texTrianglesB[k10]) == trianglesB[j10]
							&& trianglesC[j10] == (texTrianglesC[k10] & 0xffff)) {
						aByteArray2370[j10] = -1;
					} else {
						flag3 = true;
					}
				}
			}

			if (!flag3) {
				aByteArray2370 = null;
			}
		}
		if (!flag2) {
			aShortArray2334 = null;
		}
		if (!flag1) {
			aByteArray2346 = null;
		}
	}

	final void offsetVertices(int xoff, int yoff, int zoff) {
		for (int i1 = 0; vertexCount > i1; i1++) {
			verticesX[i1] += xoff;
			verticesY[i1] += yoff;
			verticesZ[i1] += zoff;
		}
	}

	private final int method2078(short word0, int i, Model model) {
		int vertX = model.verticesX[i];
		int vertY = model.verticesY[i];
		int vertZ = model.verticesZ[i];
		for (int i1 = 0; i1 < vertexCount; i1++) {
			if (verticesX[i1] == vertX && verticesY[i1] == vertY && vertZ == verticesZ[i1]) {
				aShortArray2372[i1] = (short) (aShortArray2372[i1] | word0);
				return i1;
			}
		}

		verticesX[vertexCount] = vertX;
		verticesY[vertexCount] = vertY;
		verticesZ[vertexCount] = vertZ;
		aShortArray2372[vertexCount] = word0;
		vertexSkins[vertexCount] = model.vertexSkins == null ? -1 : model.vertexSkins[i];
		return vertexCount++;
	}

	final void method2079(short word0, short word1) {
		if (aShortArray2334 == null) {
			return;
		}
		for (int i = 0; triangleCount > i; i++) {
			if (word1 == aShortArray2334[i]) {
				aShortArray2334[i] = word0;
			}
		}

	}

	final int addTriangle(int a, int b, int c, short color, byte alpha, byte byte1, short word1, byte byte2) {
		trianglesA[triangleCount] = (short) a;
		trianglesB[triangleCount] = (short) b;
		trianglesC[triangleCount] = (short) c;
		aByteArray2346[triangleCount] = byte1;
		aByteArray2370[triangleCount] = byte2;
		triangleColors[triangleCount] = color;
		triangleAlphas[triangleCount] = alpha;
		aShortArray2334[triangleCount] = word1;
		return triangleCount++;
	}

	final int[][] method2082() {
		int ai[] = new int[256];
		int i = 0;
		for (int j = 0; j < anInt2374; j++) {
			int k = vertexSkins[j];
			if (k >= 0) {
				if (i < k) {
					i = k;
				}
				ai[k]++;
			}
		}

		int ai1[][] = new int[1 + i][];
		for (int l = 0; l <= i; l++) {
			ai1[l] = new int[ai[l]];
			ai[l] = 0;
		}

		for (int i1 = 0; anInt2374 > i1; i1++) {
			int j1 = vertexSkins[i1];
			if (j1 >= 0) {
				ai1[j1][ai[j1]++] = i1;
			}
		}

		return ai1;
	}

	final byte addTexTriangle(short a, short b, short c, byte byte0, byte byte1, byte byte2, short word2,
			short word3, short word4) {
		if (texTriangleCount >= 255) {
			throw new IllegalStateException();
		}
		aByteArray2363[texTriangleCount] = 3;
		texTrianglesA[texTriangleCount] = a;
		texTrianglesB[texTriangleCount] = b;
		texTrianglesC[texTriangleCount] = c;
		aShortArray2327[texTriangleCount] = word3;
		aShortArray2341[texTriangleCount] = word2;
		aShortArray2353[texTriangleCount] = word4;
		aByteArray2367[texTriangleCount] = byte0;
		aByteArray2350[texTriangleCount] = byte2;
		aByteArray2349[texTriangleCount] = byte1;
		return (byte) (texTriangleCount++);
	}

	final void method2084(short word0, short word1) {
		for (int i = 0; triangleCount > i; i++) {
			if (triangleColors[i] == word0) {
				triangleColors[i] = word1;
			}
		}
	}

	/*
	 * final void rotate(int i, int j, int k) { if (j != 0) { int sin = GraphicsToolkit.SINES[j]; int cos =
	 * GraphicsToolkit.COSINES[j]; for (int k2 = 0; vertexCount > k2; k2++) { int j3 = sin * verticesY[k2] +
	 * (verticesX[k2] * cos) >> 15; verticesY[k2] = -(sin * verticesX[k2]) + cos * verticesY[k2] >> 15;
	 * verticesX[k2] = j3; }
	 * 
	 * } if (k != 0) { int sin = GraphicsToolkit.SINES[k]; int cos = GraphicsToolkit.COSINES[k]; for (int l2 =
	 * 0; vertexCount > l2; l2++) { int k3 = -(verticesZ[l2] * sin) + cos * verticesY[l2] >> 15; verticesZ[l2]
	 * = verticesY[l2] * sin + (cos * verticesZ[l2]) >> 15; verticesY[l2] = k3; }
	 * 
	 * } if (i != 0) { int sin = GraphicsToolkit.SINES[i]; int cos = GraphicsToolkit.COSINES[i]; for (int i3 =
	 * 0; vertexCount > i3; i3++) { int l3 = sin * verticesZ[i3] + verticesX[i3] * cos >> 15; verticesZ[i3] =
	 * -(sin * verticesX[i3]) + verticesZ[i3] * cos >> 15; verticesX[i3] = l3; }
	 * 
	 * } }
	 */

	final int addVertex(int x, int y, int z) {
		for (int i1 = 0; i1 < vertexCount; i1++) {
			if (verticesX[i1] == x && y == verticesY[i1] && verticesZ[i1] == z) {
				return i1;
			}
		}

		verticesX[vertexCount] = x;
		verticesY[vertexCount] = y;
		verticesZ[vertexCount] = z;
		anInt2374 = 1 + vertexCount;
		return vertexCount++;
	}

	/*
	 * Model(byte abyte0[]) { texTriangleCount = 0; aByte2354 = 0; vertexCount = 0; triangleCount = 0;
	 * anInt2374 = 0; if (abyte0[abyte0.length - 1] != -1 || abyte0[abyte0.length - 2] != -1) {
	 * initOldFormat(abyte0); } else { initNewFormat(abyte0); } }
	 * 
	 * Model(int i, int j, int k) { texTriangleCount = 0; aByte2354 = 0; vertexCount = 0; triangleCount = 0;
	 * anInt2374 = 0; trianglePriorities = new byte[j]; if (k > 0) { aShortArray2327 = new short[k];
	 * aShortArray2353 = new short[k]; aByteArray2369 = new byte[k]; aByteArray2367 = new byte[k];
	 * aByteArray2350 = new byte[k]; aShortArray2341 = new short[k]; texTrianglesC = new short[k];
	 * texTrianglesA = new short[k]; texTrianglesB = new short[k]; aByteArray2349 = new byte[k];
	 * aByteArray2363 = new byte[k]; aByteArray2336 = new byte[k]; } verticesX = new int[i]; triangleSkins =
	 * new int[j]; trianglesC = new short[j]; aByteArray2370 = new byte[j]; verticesY = new int[i];
	 * vertexSkins = new int[i]; trianglesB = new short[j]; triangleAlphas = new byte[j]; aByteArray2346 = new
	 * byte[j]; aShortArray2334 = new short[j]; trianglesA = new short[j]; triangleColors = new short[j];
	 * verticesZ = new int[i]; }
	 * 
	 * Model(Model models[], int i) { texTriangleCount = 0; aByte2354 = 0; vertexCount = 0; triangleCount = 0;
	 * anInt2374 = 0; texTriangleCount = 0; vertexCount = 0; triangleCount = 0; int j = 0; int k = 0; boolean
	 * flag = false; boolean flag1 = false; boolean flag2 = false; boolean flag3 = false; boolean flag4 =
	 * false; boolean flag5 = false; aByte2354 = -1; for (int l = 0; l < i; l++) { Model model = models[l]; if
	 * (model != null) { vertexCount += model.vertexCount; texTriangleCount += model.texTriangleCount;
	 * triangleCount += model.triangleCount; if (model.aClass102Array2365 != null) { j +=
	 * model.aClass102Array2365.length; } if (model.aClass154Array2361 != null) { k +=
	 * model.aClass154Array2361.length; } flag |= model.aByteArray2346 != null; flag4 |= model.aShortArray2334
	 * != null; flag3 |= model.aByteArray2370 != null; flag5 |= model.triangleSkins != null; flag2 |=
	 * model.triangleAlphas != null; if (model.trianglePriorities == null) { if (aByte2354 == -1) { aByte2354
	 * = model.aByte2354; } if (model.aByte2354 != aByte2354) { flag1 = true; } } else { flag1 = true; } } }
	 * 
	 * vertexSkins = new int[vertexCount]; if (texTriangleCount > 0) { aByteArray2350 = new
	 * byte[texTriangleCount]; texTrianglesA = new short[texTriangleCount]; aByteArray2363 = new
	 * byte[texTriangleCount]; aShortArray2353 = new short[texTriangleCount]; aByteArray2349 = new
	 * byte[texTriangleCount]; aByteArray2369 = new byte[texTriangleCount]; aShortArray2341 = new
	 * short[texTriangleCount]; aByteArray2336 = new byte[texTriangleCount]; aByteArray2367 = new
	 * byte[texTriangleCount]; texTrianglesC = new short[texTriangleCount]; aShortArray2327 = new
	 * short[texTriangleCount]; texTrianglesB = new short[texTriangleCount]; } trianglesB = new
	 * short[triangleCount]; if (j > 0) { aClass102Array2365 = new Class102[j]; } verticesX = new
	 * int[vertexCount]; trianglesA = new short[triangleCount]; if (flag) { aByteArray2346 = new
	 * byte[triangleCount]; } if (flag4) { aShortArray2334 = new short[triangleCount]; } verticesZ = new
	 * int[vertexCount]; if (flag3) { aByteArray2370 = new byte[triangleCount]; } aShortArray2345 = new
	 * short[triangleCount]; if (flag1) { trianglePriorities = new byte[triangleCount]; } trianglesC = new
	 * short[triangleCount]; aShortArray2372 = new short[vertexCount]; if (k > 0) { aClass154Array2361 = new
	 * Class154[k]; } if (flag5) { triangleSkins = new int[triangleCount]; } if (flag2) { triangleAlphas = new
	 * byte[triangleCount]; } verticesY = new int[vertexCount]; triangleColors = new short[triangleCount];
	 * vertexCount = 0; k = 0; texTriangleCount = 0; triangleCount = 0; j = 0; for (int i1 = 0; i > i1; i1++)
	 * { short word0 = (short) (1 << i1); Model model_1 = models[i1]; if (model_1 != null) { for (int l1 = 0;
	 * l1 < model_1.triangleCount; l1++) { if (flag && model_1.aByteArray2346 != null) {
	 * aByteArray2346[triangleCount] = model_1.aByteArray2346[l1]; } if (flag1) { if
	 * (model_1.trianglePriorities == null) { trianglePriorities[triangleCount] = model_1.aByte2354; } else {
	 * trianglePriorities[triangleCount] = model_1.trianglePriorities[l1]; } } if (flag2 &&
	 * model_1.triangleAlphas != null) { triangleAlphas[triangleCount] = model_1.triangleAlphas[l1]; } if
	 * (flag4) { if (model_1.aShortArray2334 != null) { aShortArray2334[triangleCount] =
	 * model_1.aShortArray2334[l1]; } else { aShortArray2334[triangleCount] = -1; } } if (flag5) { if
	 * (model_1.triangleSkins == null) { triangleSkins[triangleCount] = -1; } else {
	 * triangleSkins[triangleCount] = model_1.triangleSkins[l1]; } } trianglesA[triangleCount] = (short)
	 * method2078(word0, model_1.trianglesA[l1], model_1); trianglesB[triangleCount] = (short)
	 * method2078(word0, model_1.trianglesB[l1], model_1); trianglesC[triangleCount] = (short)
	 * method2078(word0, model_1.trianglesC[l1], model_1); aShortArray2345[triangleCount] = word0;
	 * triangleColors[triangleCount] = model_1.triangleColors[l1]; triangleCount++; }
	 * 
	 * if (model_1.aClass102Array2365 != null) { for (int i2 = 0; ~i2 > ~model_1.aClass102Array2365.length;
	 * i2++) { int k2 = method2078(word0, model_1.aClass102Array2365[i2].anInt1271, model_1); int j3 =
	 * method2078(word0, model_1.aClass102Array2365[i2].anInt1279, model_1); int l3 = method2078(word0,
	 * model_1.aClass102Array2365[i2].anInt1273, model_1); aClass102Array2365[j] = new
	 * Class102(model_1.aClass102Array2365[i2].anInt1285, k2, j3, l3,
	 * model_1.aClass102Array2365[i2].aByte1265); j++; }
	 * 
	 * } if (model_1.aClass154Array2361 != null) { for (int j2 = 0; j2 < model_1.aClass154Array2361.length;
	 * j2++) { int l2 = method2078(word0, model_1.aClass154Array2361[j2].anInt2063, model_1);
	 * aClass154Array2361[k] = new Class154(model_1.aClass154Array2361[j2].anInt2060, l2); k++; }
	 * 
	 * } } }
	 * 
	 * int j1 = 0; anInt2374 = vertexCount; for (int k1 = 0; k1 < i; k1++) { short word1 = (short) (1 << k1);
	 * Model model_2 = models[k1]; if (model_2 != null) { for (int i3 = 0; i3 < model_2.triangleCount; i3++) {
	 * if (flag3) { aByteArray2370[j1++] = (byte) (model_2.aByteArray2370 == null ||
	 * model_2.aByteArray2370[i3] == -1 ? -1 : texTriangleCount + model_2.aByteArray2370[i3]); } }
	 * 
	 * for (int k3 = 0; model_2.texTriangleCount > k3; k3++) { byte byte0 = aByteArray2363[texTriangleCount] =
	 * model_2.aByteArray2363[k3]; if (byte0 == 0) { texTrianglesA[texTriangleCount] = (short)
	 * method2078(word1, model_2.texTrianglesA[k3], model_2); texTrianglesB[texTriangleCount] = (short)
	 * method2078(word1, model_2.texTrianglesB[k3], model_2); texTrianglesC[texTriangleCount] = (short)
	 * method2078(word1, model_2.texTrianglesC[k3], model_2); } if (byte0 >= 1 && byte0 <= 3) {
	 * texTrianglesA[texTriangleCount] = model_2.texTrianglesA[k3]; texTrianglesB[texTriangleCount] =
	 * model_2.texTrianglesB[k3]; texTrianglesC[texTriangleCount] = model_2.texTrianglesC[k3];
	 * aShortArray2327[texTriangleCount] = model_2.aShortArray2327[k3]; aShortArray2341[texTriangleCount] =
	 * model_2.aShortArray2341[k3]; aShortArray2353[texTriangleCount] = model_2.aShortArray2353[k3];
	 * aByteArray2367[texTriangleCount] = model_2.aByteArray2367[k3]; aByteArray2350[texTriangleCount] =
	 * model_2.aByteArray2350[k3]; aByteArray2349[texTriangleCount] = model_2.aByteArray2349[k3]; } if (byte0
	 * == 2) { aByteArray2369[texTriangleCount] = model_2.aByteArray2369[k3]; aByteArray2336[texTriangleCount]
	 * = model_2.aByteArray2336[k3]; } texTriangleCount++; }
	 * 
	 * } }
	 * 
	 * }
	 */

	@Override
	public void decode(Stream stream) {
		texTriangleCount = 0;
		aByte2354 = 0;
		vertexCount = 0;
		triangleCount = 0;
		anInt2374 = 0;
		stream.seek(stream.getLength() - 2);
		if (stream.getByte() != -1 || stream.getByte() != -1) {
			initOldFormat(stream);
		} else {
			initNewFormat(stream);
		}
	}
}
