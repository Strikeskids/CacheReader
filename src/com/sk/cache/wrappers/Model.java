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
	public byte[] _jgh;
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
	public List<Map<String, Integer>> _jhg;
	public List<Map<String, Integer>> _jhe;
	public List<Map<String, Integer>> _jhd;

	private final void init(Stream al) {
		final int G = 26;
		final int J = 3;
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
		int an = W.getUByte();
		boolean aJ = (an & 1) == 1;
		boolean bG = (an & 2) == 2;
		boolean av = (an & 4) == 4;
		boolean aG = (an & 16) == 16;
		boolean aq = (an & 32) == 32;
		boolean X = (an & 64) == 64;
		boolean aA = (an & 128) == 128;
		int af = W.getUByte();
		int aB = W.getUByte();
		int at = W.getUByte();
		int aH = W.getUByte();
		int O = W.getUByte();
		int bg = W.getUShort();
		int bm = W.getUShort();
		int bB = W.getUShort();
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
			this._jgh = new byte[this.texTriangleCount];
			W.seek(J);
			for (aC = 0; aC < this.texTriangleCount; aC++) {
				byte aI = this._jgh[aC] = W.getByte();
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
		int offsetAccum = J + this.texTriangleCount;
		int aT = offsetAccum;
		offsetAccum += this.vertexCount;
		int ao = offsetAccum;
		if (aJ) {
			offsetAccum += this.triangleCount;
		}
		int aV = offsetAccum;
		offsetAccum += this.triangleCount;
		int am = offsetAccum;
		if (af == 255) {
			offsetAccum += this.triangleCount;
		}
		int aj = offsetAccum;
		offsetAccum += a7;
		int aP = offsetAccum;
		offsetAccum += bF;
		int ak = offsetAccum;
		if (aB == 1) {
			offsetAccum += this.triangleCount;
		}
		int aW = offsetAccum;
		offsetAccum += aZ;
		int ai = offsetAccum;
		if (aH == 1) {
			offsetAccum += this.triangleCount * 2;
		}
		int ah = offsetAccum;
		offsetAccum += bi;
		int ap = offsetAccum;
		offsetAccum += this.triangleCount * 2;
		int aS = offsetAccum;
		offsetAccum += bg;
		int aR = offsetAccum;
		offsetAccum += bm;
		int aQ = offsetAccum;
		offsetAccum += bB;
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
		W.seek(aT);
		V.seek(aS);
		T.seek(aR);
		R.seek(aQ);
		P.seek(aP);
		int bb = 0, a9 = 0, a8 = 0;
		for (aC = 0; aC < this.vertexCount; aC++) {
			int Q = W.getUByte();
			int aa = 0;
			if ((Q & 1) != 0) {
				aa = V.getSignedSmart();
			}
			int Z = 0;
			if ((Q & 2) != 0) {
				Z = -T.getSignedSmart();
			}
			int Y = 0;
			if ((Q & 4) != 0) {
				Y = R.getSignedSmart();
			}
			bb = this.verticesX[aC] = bb + aa;
			a9 = this.verticesY[aC] = a9 + Z;
			a8 = this.verticesZ[aC] = a8 + Y;
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
		T.seek(am);
		R.seek(ak);
		P.seek(aj);
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
		W.seek(aW);
		V.seek(aV);
		T.seek(aU);
		int aF = 0, aE = 0, aD = 0;
		int S = 0;
		for (aC = 0; aC < this.triangleCount; aC++) {
			int aI = V.getUByte();
			int ax = aI & 7;
			if (ax == 1) {
				aF = W.getSignedSmart() + S;
				S = aF;
				aE = W.getSignedSmart() + S;
				S = aE;
				aD = W.getSignedSmart() + S;
				S = aD;
				this.trianglesA[aC] = (short) aF;
				this.trianglesB[aC] = (short) aE;
				this.trianglesC[aC] = (short) aD;
				if (aF > this.maxVertex) {
					this.maxVertex = aF;
				}
				if (aE > this.maxVertex) {
					this.maxVertex = aE;
				}
				if (aD > this.maxVertex) {
					this.maxVertex = aD;
				}
			} else if (ax == 2) {
				aE = aD;
				aD = W.getSignedSmart() + S;
				S = aD;
				this.trianglesA[aC] = (short) aF;
				this.trianglesB[aC] = (short) aE;
				this.trianglesC[aC] = (short) aD;
				if (aD > this.maxVertex) {
					this.maxVertex = aD;
				}
			} else if (ax == 3) {
				aF = aD;
				aD = W.getSignedSmart() + S;
				S = aD;
				this.trianglesA[aC] = (short) aF;
				this.trianglesB[aC] = (short) aE;
				this.trianglesC[aC] = (short) aD;
				if (aD > this.maxVertex) {
					this.maxVertex = aD;
				}
			} else if (ax == 4) {
				int aM = aF;
				aF = aE;
				aE = aM;
				aD = W.getSignedSmart() + S;
				S = aD;
				this.trianglesA[aC] = (short) aF;
				this.trianglesB[aC] = (short) aE;
				this.trianglesC[aC] = (short) aD;
				if (aD > this.maxVertex) {
					this.maxVertex = aD;
				}
			}
			if (this._jgz > 0) {
				if ((aI & 8) != 0) {
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
			int aI = this._jgh[aC];
			if (aI == 0) {
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
				if (aI == 2) {
					this._jgq[aC] = N.getByte();
					this._jgs[aC] = N.getByte();
				}
			}
		}
		W.seek(U);
		if (bG) {
			int au = W.getUByte();
			if (au > 0) {
				this._jhd = new ArrayList<Map<String, Integer>>(au);
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
				this._jhe = new ArrayList<Map<String, Integer>>(ae);
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
				this._jhg = new ArrayList<Map<String, Integer>>(aw);
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

	private final Map<String, Integer> generateMap(Object... objs) {
		Map<String, Integer> ret = new LinkedHashMap<String, Integer>();
		for (int i = 0; i < objs.length; i += 2) {
			ret.put((String) objs[i], (Integer) objs[i + 1]);
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
