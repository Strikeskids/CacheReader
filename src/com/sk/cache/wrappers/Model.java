package com.sk.cache.wrappers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sk.cache.wrappers.loaders.ModelLoader;
import com.sk.datastream.ByteStream;
import com.sk.datastream.Stream;

public class Model extends StreamedWrapper {

	public Model(ModelLoader loader, int id) {
		super(loader, id);
	}

	public int vertexCount;
	public int triangleCount;
	public int texTriangleCount;
	public int[] verticesX;
	public int[] verticesY;
	public int[] verticesZ;
	public short[] trianglesA;
	public short[] trianglesB;
	public short[] trianglesC;
	public short[] texTrianglesA;
	public short[] texTrianglesB;
	public short[] texTrianglesC;
	public int[] vertexSkins;
	public int[] triangleAlphas;
	public int[] triangleColors;
	public int[] trianglePriorities;
	public int[] triangleSkins;

	public int _jet;
	public int _jev;
	public byte[] texTrianglesType;
	public int _jgz;
	public int[] _jfn;
	public int _jfq;
	public int[] _jfv;
	public int[] _jfs;
	public int[] _jgl;
	public int[] _jgm;
	public int[] _jgn;
	public byte[] _jgu;
	public byte[] _jgv;
	public byte[] _jgo;
	public byte[] _jgq;
	public byte[] _jgs;
	public int[] _jfh;
	public int[] _jfy;
	public int[] _jfz;
	public int[] _jgb;
	public float[] u;
	public float[] _jhb;
	public int maxVertex;
	public List<Map<String, Number>> _jhg;
	public List<Map<String, Number>> _jhe;
	public List<Map<String, Number>> _jhd;

	private final void init(Stream al) {
		final int G = 26;
		final int initialBytesCount = 3;
		final int B = -1;
		final int E = 65535;
		final int I = 65535;
		final int _jer = 1;
		int aC;
		Stream W = new ByteStream(al.getAllBytes());
		Stream V = new ByteStream(al.getAllBytes());
		Stream T = new ByteStream(al.getAllBytes());
		Stream R = new ByteStream(al.getAllBytes());
		Stream P = new ByteStream(al.getAllBytes());
		Stream N = new ByteStream(al.getAllBytes());
		Stream M = new ByteStream(al.getAllBytes());
		int ag = W.getUByte();
		if (ag != _jer) {
			throw new Error("482 " + ag);
		}
		this._jet = W.getUByte();
		this._jev = W.getUByte();
		W.seek(al.getLength() - G);
		this.vertexCount = W.getUShort();
		this.triangleCount = W.getUShort();
		this.texTriangleCount = W.getUShort();
		int mainFlags = W.getUByte();
		boolean aJ = (mainFlags & 1) == 1;
		boolean bG = (mainFlags & 2) == 2;
		boolean av = (mainFlags & 4) == 4;
		boolean aG = (mainFlags & 16) == 16;
		boolean aq = (mainFlags & 32) == 32;
		boolean X = (mainFlags & 64) == 64;
		boolean aA = (mainFlags & 128) == 128;
		int af = W.getUByte();
		int aB = W.getUByte();
		int at = W.getUByte();
		int aH = W.getUByte();
		int O = W.getUByte();
		int verticesXDataSize = W.getUShort();
		int verticesYDataSize = W.getUShort();
		int verticesZDataSize = W.getUShort();
		int aZ = W.getUShort();
		int bi = W.getUShort();
		int bF = W.getUShort();
		int a7 = W.getUShort();
		if (!aG) {
			if (O == 1) {
				bF = this.vertexCount;
			} else {
				bF = 0;
			}
		}
		if (!aq) {
			if (at == 1) {
				a7 = this.triangleCount;
			} else {
				a7 = 0;
			}
		}
		int a6 = 0, aK = 0, a5 = 0;
		if (this.texTriangleCount > 0) {
			this.texTrianglesType = new byte[this.texTriangleCount];
			W.seek(initialBytesCount);
			for (aC = 0; aC < this.texTriangleCount; aC++) {
				byte aI = this.texTrianglesType[aC] = W.getByte();
				if (aI == 0) {
					a6++;
				}
				if (aI >= 1 && aI <= 3) {
					aK++;
				}
				if (aI == 2) {
					a5++;
				}
			}
		}
		int offsetAccum = initialBytesCount + this.texTriangleCount;
		int verticesMaskDataOffset = offsetAccum;
		offsetAccum += this.vertexCount;
		int ao = offsetAccum;
		if (aJ) {
			offsetAccum += this.triangleCount;
		}
		int triangleTypeDataOffset = offsetAccum;
		offsetAccum += this.triangleCount;
		int trianglePrioritiesDataOffset = offsetAccum;
		if (af == 255) {
			offsetAccum += this.triangleCount;
		}
		int triangleSkinsDataOffset = offsetAccum;
		offsetAccum += a7;
		int aP = offsetAccum;
		offsetAccum += bF;
		int triangleAlphasDataOffset = offsetAccum;
		if (aB == 1) {
			offsetAccum += this.triangleCount;
		}
		int triangleDataOffset = offsetAccum;
		offsetAccum += aZ;
		int ai = offsetAccum;
		if (aH == 1) {
			offsetAccum += this.triangleCount * 2;
		}
		int ah = offsetAccum;
		offsetAccum += bi;
		int ap = offsetAccum;
		offsetAccum += this.triangleCount * 2;
		int verticesXDataOffset = offsetAccum;
		offsetAccum += verticesXDataSize;
		int verticesYDataOffset = offsetAccum;
		offsetAccum += verticesYDataSize;
		int verticesZDataOffset = offsetAccum;
		offsetAccum += verticesZDataSize;
		int bz = offsetAccum;
		offsetAccum += a6 * 6;
		int bv = offsetAccum;
		offsetAccum += aK * 6;
		int ad = 6;
		if (this._jev == 14) {
			ad = 7;
		} else {
			if (this._jev >= 15) {
				ad = 9;
			}
		}
		int bt = offsetAccum;
		offsetAccum += aK * ad;
		int br = offsetAccum;
		offsetAccum += aK;
		int bq = offsetAccum;
		offsetAccum += aK;
		int bl = offsetAccum;
		offsetAccum += aK + a5 * 2;
		int U = offsetAccum;
		int aU = al.getLength();
		int aO = al.getLength();
		int aN = al.getLength();
		int aL = al.getLength();
		if (aA) {
			Stream bk = new ByteStream(al.getAllBytes());
			bk.seek(al.getLength() - G - 1);
			bk.seek(bk.getLocation() - (bk.getUByte() - 1));
			this._jgz = bk.getUShort();
			int ac = bk.getUShort();
			int ab = bk.getUShort();
			aU = U + ac;
			aO = aU + ab;
			aN = aO + this.vertexCount;
			aL = aN + (this._jgz * 2);
		}
		this.verticesX = new int[this.vertexCount];
		this.verticesY = new int[this.vertexCount];
		this.verticesZ = new int[this.vertexCount];
		this.trianglesA = new short[this.triangleCount];
		this.trianglesB = new short[this.triangleCount];
		this.trianglesC = new short[this.triangleCount];
		if (O == 1) {
			this.vertexSkins = new int[this.vertexCount];
		}
		if (aJ) {
			this._jfn = new int[this.triangleCount];
		}
		if (af == 255) {
			this.trianglePriorities = new int[this.triangleCount];
		} else {
			this._jfq = af;
		}
		if (aB != 0) {
			this.triangleAlphas = new int[this.triangleCount];
		}
		if (at != 0) {
			this.triangleSkins = new int[this.triangleCount];
		}
		if (aH == 1) {
			this._jfv = new int[this.triangleCount];
			if (this.texTriangleCount > 0 || this._jgz > 0) {
				this._jfs = new int[this.triangleCount];
			}
		}
		this.triangleColors = new int[this.triangleCount];
		if (this.texTriangleCount > 0) {
			this.texTrianglesA = new short[this.texTriangleCount];
			this.texTrianglesB = new short[this.texTriangleCount];
			this.texTrianglesC = new short[this.texTriangleCount];
			this._jgl = new int[this.texTriangleCount];
			this._jgm = new int[this.texTriangleCount];
			this._jgn = new int[this.texTriangleCount];
			this._jgu = new byte[this.texTriangleCount];
			this._jgv = new byte[this.texTriangleCount];
			this._jgo = new byte[this.texTriangleCount];
			this._jgq = new byte[this.texTriangleCount];
			this._jgs = new byte[this.texTriangleCount];
		}
		W.seek(verticesMaskDataOffset);
		V.seek(verticesXDataOffset);
		T.seek(verticesYDataOffset);
		R.seek(verticesZDataOffset);
		P.seek(aP);
		int xaccum = 0, yaccum = 0, zaccum = 0;
		for (aC = 0; aC < this.vertexCount; aC++) {
			int verticesMask = W.getUByte();
			int xoff = 0;
			if ((verticesMask & 1) != 0) {
				xoff = V.getSignedSmart();
			}
			int yoff = 0;
			if ((verticesMask & 2) != 0) {
				yoff = -T.getSignedSmart();
			}
			int zoff = 0;
			if ((verticesMask & 4) != 0) {
				zoff = R.getSignedSmart();
			}
			this.verticesX[aC] = xaccum += xoff;
			this.verticesY[aC] = yaccum += yoff;
			this.verticesZ[aC] = zaccum += zoff;
			if (O == 1) {
				if (aG) {
					this.vertexSkins[aC] = P.getSmartMinusOne();
				} else {
					this.vertexSkins[aC] = P.getUByte();
					if (this.vertexSkins[aC] == 255) {
						this.vertexSkins[aC] = B;
					}
				}
			}
		}
		if (this._jgz > 0) {
			W.seek(aO);
			V.seek(aN);
			T.seek(aL);
			this._jfh = new int[this.vertexCount];
			for (int az = 0, ay = 0; az < this.vertexCount; ++az) {
				this._jfh[az] = ay;
				ay += W.getUByte();
			}
			this._jfy = new int[this.triangleCount];
			this._jfz = new int[this.triangleCount];
			this._jgb = new int[this.triangleCount];
			this.u = new float[this._jgz];
			this._jhb = new float[this._jgz];
			for (int az = 0; az < this._jgz; ++az) {
				this.u[az] = V.getShort() / 4096f;
				this._jhb[az] = T.getShort() / 4096f;
			}
		}
		W.seek(ap);
		V.seek(ao);
		T.seek(trianglePrioritiesDataOffset);
		R.seek(triangleAlphasDataOffset);
		P.seek(triangleSkinsDataOffset);
		N.seek(ai);
		M.seek(ah);
		for (aC = 0; aC < this.triangleCount; aC++) {
			this.triangleColors[aC] = W.getUShort();
			if (aJ) {
				this._jfn[aC] = V.getUByte();
			}
			if (af == 255) {
				this.trianglePriorities[aC] = T.getUByte();
			}
			if (aB == 1) {
				this.triangleAlphas[aC] = R.getUByte();
			}
			if (at == 1) {
				if (aq) {
					this.triangleSkins[aC] = P.getSmartMinusOne();
				} else {
					this.triangleSkins[aC] = P.getUByte();
					if (this.triangleSkins[aC] == 255) {
						this.triangleSkins[aC] = -1;
					}
				}
			}
			if (aH == 1) {
				this._jfv[aC] = N.getUShort() - 1;
			}
			if (this._jfs != null) {
				if (this._jfv[aC] != E) {
					if (this._jev >= 16) {
						this._jfs[aC] = M.getSmart() - 1;
					} else {
						this._jfs[aC] = M.getUByte() - 1;
					}
				} else {
					this._jfs[aC] = I;
				}
			}
		}
		this.maxVertex = -1;
		W.seek(triangleDataOffset);
		V.seek(triangleTypeDataOffset);
		T.seek(aU);
		int triangleA = 0, triangleB = 0, triangleC = 0;
		int triangleAccum = 0;
		for (aC = 0; aC < this.triangleCount; aC++) {
			int triangleMask = V.getUByte();
			int triangleType = triangleMask & 7;
			if (triangleType == 1) {
				triangleA = triangleAccum += W.getSignedSmart();
				triangleB = triangleAccum += W.getSignedSmart();
				triangleC = triangleAccum += W.getSignedSmart();
				this.trianglesA[aC] = (short) triangleA;
				this.trianglesB[aC] = (short) triangleB;
				this.trianglesC[aC] = (short) triangleC;
				if (triangleA > this.maxVertex) {
					this.maxVertex = triangleA;
				}
				if (triangleB > this.maxVertex) {
					this.maxVertex = triangleB;
				}
				if (triangleC > this.maxVertex) {
					this.maxVertex = triangleC;
				}
			} else if (triangleType == 2) {
				triangleB = triangleC;
				triangleC = triangleAccum += W.getSignedSmart();
				this.trianglesA[aC] = (short) triangleA;
				this.trianglesB[aC] = (short) triangleB;
				this.trianglesC[aC] = (short) triangleC;
				if (triangleC > this.maxVertex) {
					this.maxVertex = triangleC;
				}
			} else if (triangleType == 3) {
				triangleA = triangleC;
				triangleC = triangleAccum += W.getSignedSmart();
				this.trianglesA[aC] = (short) triangleA;
				this.trianglesB[aC] = (short) triangleB;
				this.trianglesC[aC] = (short) triangleC;
				if (triangleC > this.maxVertex) {
					this.maxVertex = triangleC;
				}
			} else if (triangleType == 4) {
				int aM = triangleA;
				triangleA = triangleB;
				triangleB = aM;
				triangleC = triangleAccum += W.getSignedSmart();
				this.trianglesA[aC] = (short) triangleA;
				this.trianglesB[aC] = (short) triangleB;
				this.trianglesC[aC] = (short) triangleC;
				if (triangleC > this.maxVertex) {
					this.maxVertex = triangleC;
				}
			}
			if (this._jgz > 0) {
				if ((triangleMask & 8) != 0) {
					this._jfy[aC] = T.getUByte();
					this._jfz[aC] = T.getUByte();
					this._jgb[aC] = T.getUByte();
				}
			}
		}
		this.maxVertex++;
		W.seek(bz);
		V.seek(bv);
		T.seek(bt);
		R.seek(br);
		P.seek(bq);
		N.seek(bl);
		for (aC = 0; aC < this.texTriangleCount; aC++) {
			int texTriangleType = this.texTrianglesType[aC];
			if (texTriangleType == 0) {
				this.texTrianglesA[aC] = W.getShort();
				this.texTrianglesB[aC] = W.getShort();
				this.texTrianglesC[aC] = W.getShort();
			} else {
				this.texTrianglesA[aC] = V.getShort();
				this.texTrianglesB[aC] = V.getShort();
				this.texTrianglesC[aC] = V.getShort();
				if (this._jev < 15) {
					this._jgl[aC] = T.getUShort();
					if (this._jev < 14) {
						this._jgm[aC] = T.getUShort();
					} else {
						this._jgm[aC] = T.getUInt24();
					}
					this._jgn[aC] = T.getUShort();
				} else {
					this._jgl[aC] = T.getUInt24();
					this._jgm[aC] = T.getUInt24();
					this._jgn[aC] = T.getUInt24();
				}
				this._jgu[aC] = R.getByte();
				this._jgv[aC] = P.getByte();
				this._jgo[aC] = N.getByte();
				if (texTriangleType == 2) {
					this._jgq[aC] = N.getByte();
					this._jgs[aC] = N.getByte();
				}
			}
		}
		W.seek(U);
		if (bG) {
			int au = W.getUByte();
			if (au > 0) {
				this._jhd = new ArrayList<Map<String, Number>>(au);
				for (aC = 0; aC < au; aC++) {
					int a4 = W.getUShort();
					int a3 = W.getUShort();
					int a2 = af;
					if (af == 255) {
						a2 = this.trianglePriorities[a3];
					}
					this._jhd.add(generateMap("_jhi", a4, "_jhk", a3, "_bor", this.trianglesA[a3], "_bos",
							this.trianglesB[a3], "_bou", this.trianglesC[a3], "_jhl", a2));
				}
			}
			int ae = W.getUByte();
			if (ae > 0) {
				this._jhe = new ArrayList<Map<String, Number>>(ae);
				for (aC = 0; aC < ae; aC++) {
					int a1 = W.getUShort();
					int a0 = W.getUShort();
					this._jhe.add(generateMap("_jhn", a1, "_jho", a0));
				}
			}
		}
		if (av) {
			int aw = W.getUByte();
			if (aw > 0) {
				this._jhg = new ArrayList<Map<String, Number>>(aw);
				for (aC = 0; aC < aw; aC++) {
					int bA = W.getUShort();
					int aY = W.getUShort();
					Integer aX;
					if (X) {
						aX = W.getSmartMinusOne();
						if (aX == -1) {
							aX = null;
						}
					} else {
						aX = W.getUByte();
						if (aX == 255) {
							aX = null;
						}
					}
					int be = W.getByte();
					this._jhg.add(generateMap("_bbt", bA, "_jhq", aY, "_jhr", aX, "_bca", be));
				}
			}
		}
	}

	private final Map<String, Number> generateMap(Object... objs) {
		Map<String, Number> ret = new LinkedHashMap<String, Number>();
		for (int i = 0; i < objs.length; i += 2) {
			ret.put((String) objs[i], (Number) objs[i + 1]);
		}
		return ret;
	}

	final void offsetVertices(int xoff, int yoff, int zoff) {
		for (int i1 = 0; vertexCount > i1; i1++) {
			verticesX[i1] += xoff;
			verticesY[i1] += yoff;
			verticesZ[i1] += zoff;
		}
	}

	final int addVertex(int x, int y, int z) {
		for (int i1 = 0; i1 < vertexCount; i1++) {
			if (verticesX[i1] == x && y == verticesY[i1] && verticesZ[i1] == z) {
				return i1;
			}
		}

		verticesX[vertexCount] = x;
		verticesY[vertexCount] = y;
		verticesZ[vertexCount] = z;
		maxVertex = 1 + vertexCount;
		return vertexCount++;
	}

	@Override
	public void decode(Stream stream) {
		texTriangleCount = 0;
		_jfq = 0;
		vertexCount = 0;
		triangleCount = 0;
		maxVertex = 0;
		init(stream);
	}
}
