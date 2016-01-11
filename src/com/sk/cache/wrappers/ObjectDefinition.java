package com.sk.cache.wrappers;

import java.util.Map;

import com.sk.cache.wrappers.loaders.ModelLoader;
import com.sk.cache.wrappers.loaders.ObjectDefinitionLoader;
import com.sk.datastream.Stream;

public class ObjectDefinition extends ProtocolWrapper {

	public String name;
	public int type = -1;
	public int width = 1;
	public int height = 1;
	public boolean walkable = true;
	public boolean walkable2 = false;
	public int blockType = 2;
	public int accessibleMask = -1;
	public String[] actions = new String[5];

	public int scriptId = -1;
	public int configId = -1;
	public int[] childrenIds;

	public int[] modelTypes;
	public int[][] modelIds;

	public ObjectDefinition(ObjectDefinitionLoader loader, int id) {
		super(loader, id);
	}

	public ModelBounds getModelBounds(int modelIndex) {
		if (modelIds != null && 0 <= modelIndex && modelIndex < modelIds.length) {
			final Model[] models = new Model[modelIds[modelIndex].length];
			ModelLoader loader = getLoader().getCacheSystem().modelLoader;
			for (int i = 0; i < models.length; ++i) {
				if (loader.canLoad(modelIds[modelIndex][i]))
					models[i] = loader.load(modelIds[modelIndex][i]);
				else
					return null;
			}
			return new ModelBounds(models);
		}
		return null;
	}

	@Override
	protected void decodeOpcode(Stream data, int opcode) {
		if (opcode == 1) {
			int k = data.getUByte();
			modelTypes = new int[k];
			modelIds = new int[k][];
			for (int y = 0; y < k; y++) {
				modelTypes[y] = data.getByte();
				int z = data.getUByte();
				modelIds[y] = new int[z];
				for (int h = 0; h < z; h++) {
					modelIds[y][h] = data.getBigSmart();
				}
			}
		} else if (opcode == 2) {
			name = data.getString();
		} else if (opcode == 14) {
			width = data.getUByte();
		} else if (opcode == 15) {
			height = data.getUByte();
		} else if (opcode == 17) {
			blockType = 0;
			walkable = false;
		} else if (opcode == 18) {
			walkable = false;
		} else if (opcode == 19) {
			type = data.getUByte();
		} else if (opcode == 21) {
			skipValue(opcode, 1);
		} else if (opcode == 22) {
		} else if (opcode == 23) {
		} else if (opcode == 24) {
			skipValue(opcode, data.getBigSmart());
		} else if (opcode == 27) {
			blockType = 1;
		} else if (opcode == 28) {
			skipValue(opcode, data.getUByte() << 2);
		} else if (opcode == 29) {
			skipValue(opcode, data.getByte() + 64);
		} else if (opcode >= 30 && opcode < 35) {
			actions[opcode - 30] = data.getString();
		} else if (opcode == 39) {
			skipValue(opcode, data.getByte() * 5);
		} else if (opcode == 40) {
			int A = data.getUByte();
			int[] originalColors = new int[A];
			int[] modifiedColors = new int[A];
			for (int G = 0; G < A; G++) {
				originalColors[G] = data.getUShort();
				modifiedColors[G] = data.getUShort();
			}
			skipValue(opcode, A, originalColors, modifiedColors);
		} else if (opcode == 41) {
			int O = data.getUByte();
			int[] arr1 = new int[O];
			int[] arr2 = new int[O];
			for (int t = 0; t < O; t++) {
				arr1[t] = data.getShort();
				arr2[t] = data.getShort();
			}
			skipValue(opcode, O, arr1, arr2);
		} else if (opcode == 42) {
			int e = data.getUByte();
			int[] arr = new int[e];
			for (int I = 0; I < e; I++) {
				arr[I] = data.getByte();
			}
			skipValue(opcode, e, arr);
		} else if (opcode == 44) {
			int N = data.getUShort();
			int i = 0;
			for (int E = N; E > 0; E = E >> 1) {
				i++;
			}
			int[] arr = new int[i];
			int u = 0;
			for (int E = 0; E < i; E++) {
				if ((N & (1 << E)) > 0) {
					arr[E] = u++;
				} else {
					arr[E] = -1;
				}
			}
			skipValue(opcode, N, arr);
		} else if (opcode == 45) {
			int L = data.getUShort();
			int M = 0;
			for (int E = L; E > 0; E = E >> 1) {
				M++;
			}
			int[] arr = new int[M];
			int u = 0;
			for (int E = 0; E < M; E++) {
				if ((L & (1 << E)) > 0) {
					arr[E] = u++;
				} else {
					arr[E] = -1;
				}
			}
			skipValue(opcode, L, arr);
		} else if (opcode == 62) {
			skipValue(opcode, true);
		} else if (opcode == 64) {
		} else if (opcode == 65 || opcode == 66 || opcode == 67) {
			float[] resize = null;
			if (resize == null) {
				resize = new float[] { 128, 128, 128 };
			}
			resize[opcode - 65] = data.getUShort();
			skipValue(opcode, resize);
		} else if (opcode == 69) {
			accessibleMask = data.getUByte();
		} else if (opcode == 70 || opcode == 71 || opcode == 72) {
			int[] offset = null;
			if (offset == null) {
				offset = new int[3];
			}
			if (opcode == 71) {
				offset[opcode - 70] = -(data.getShort() << 2);
			} else {
				offset[opcode - 70] = data.getShort() << 2;
			}
			skipValue(opcode, offset);
		} else if (opcode == 73) {
		} else if (opcode == 74) {
			walkable2 = true;
		} else if (opcode == 75) {
			skipValue(opcode, data.getUByte());
		} else if (opcode == 77 || opcode == 92) {
			scriptId = data.getUShort();
			if (scriptId == 65535) {
				scriptId = -1;
			}
			configId = data.getUShort();
			if (configId == 65535) {
				configId = -1;
			}
			int defaultId = -1;
			if (opcode == 92) {
				defaultId = data.getBigSmart();
			}
			int q = data.getUByte();
			childrenIds = new int[q + 2];
			for (int o = 0; o <= q; o++) {
				childrenIds[o] = data.getBigSmart();
			}
			childrenIds[q + 1] = defaultId;
		} else if (opcode == 78) {
			skipValue(opcode, data.getUShort(), data.getUByte());
		} else if (opcode == 79) {
			int _nkn = data.getUShort() * 10;
			int _nkp = data.getUShort() * 10;
			int _txi = data.getUByte();
			int C = data.getUByte();
			int[] _nkl = new int[C];
			for (int B = 0; B < C; B++) {
				_nkl[B] = data.getUShort();
			}
			skipValue(opcode, _nkn, _nkp, _txi, _nkl);
		} else if (opcode == 81) {
			skipValue(opcode, 2, data.getUByte() * 256);
		} else if (opcode == 82) {
			skipValue(opcode, true);
		} else if (opcode == 88) {
			skipValue(opcode, false);
		} else if (opcode == 89) {
			skipValue(opcode, false);
		} else if (opcode == 91) {
			skipValue(opcode, true);
		} else if (opcode == 93) {
			skipValue(opcode, 3, data.getUShort());
		} else if (opcode == 94) {
			skipValue(opcode, 4);
		} else if (opcode == 95) {
			skipValue(opcode, 5, data.getShort());
		} else if (opcode == 97) {
			skipValue(opcode, true);
		} else if (opcode == 98) {
		} else if (opcode == 101) {
			skipValue(opcode, data.getUByte());
		} else if (opcode == 102) {
			skipValue(opcode, data.getUShort());
		} else if (opcode == 103) {
		} else if (opcode == 104) {
			skipValue(opcode, data.getUByte());
		} else if (opcode == 105) {
			skipValue(opcode, true);
		} else if (opcode == 106) {
			int H = data.getUByte();
			int g = 0;
			int[] _wwy = new int[H];
			int[] _wwz = new int[H];
			for (int s = 0; s < H; s++) {
				_wwy[s] = data.getBigSmart();
				g += _wwz[s] = data.getUByte();
			}
			for (int s = 0; s < H; s++) {
				_wwz[s] = _wwz[s] * 65535 / g;
			}
			skipValue(opcode, H, _wwy, _wwz);
		} else if (opcode == 107) {
			skipValue(opcode, data.getUShort());
		} else if (opcode >= 150 && opcode < 155) {
			actions[opcode - 150] = data.getString();
		} else if (opcode == 160) {
			int K = data.getUByte();
			int[] _qg = new int[K];
			for (int F = 0; F < K; F++) {
				_qg[F] = data.getUShort();
			}
			skipValue(K, _qg);
		} else if (opcode == 162) {
			skipValue(opcode, 3, data.getInt());
		} else if (opcode == 163) {
			skipValue(opcode, data.getByte(), data.getByte(), data.getByte(), data.getByte());
		} else if (opcode == 164 || opcode == 165 || opcode == 166) {
			// array index m-164
			skipValue(opcode, opcode - 164, data.getShort());
		} else if (opcode == 167) {
			skipValue(opcode, data.getUShort());
		} else if (opcode == 168) {
		} else if (opcode == 169) {
		} else if (opcode == 170) {
			data.getSignedSmart();
		} else if (opcode == 171) {
			data.getSignedSmart();
		} else if (opcode == 173) {
			skipValue(opcode, data.getUShort(), data.getUShort());
		} else if (opcode == 177) {
		} else if (opcode == 178) {
			skipValue(opcode, data.getUByte());
		} else if (opcode == 179) {
			skipValue(opcode, true);
		} else if (opcode == 186) {
			skipValue(opcode, data.getUByte());
		} else if (opcode == 189) {
			skipValue(opcode, true);
		} else if (opcode >= 190 && opcode < 196) {
			// array index opcode-190 default -1
			skipValue(opcode, opcode - 190, data.getUShort());
		} else if (opcode == 196) {
			skipValue(opcode, data.getUByte());
		} else if (opcode == 197) {
			skipValue(opcode, data.getUByte());
		} else if (opcode == 198) {
			skipValue(opcode, true);
		} else if (opcode == 199) {
			skipValue(opcode, false);
		} else if (opcode == 200) {
			skipValue(opcode, true);
		} else if (opcode == 201) {
			skipValue(opcode, data.getSmart(), data.getSmart(), data.getSmart(), data.getSmart(), data.getSmart(),
					data.getSmart());
		} else if (opcode == 249) {
			Map<Integer, Object> params = decodeParams(data);
			skipValue(opcode, params);
		} else if (opcode == 250) {
			skipValue(opcode, data.getUByte());
		} else if (opcode == 251) {
			skipValue(opcode, data.getUByte());
		} else if (opcode == 252) {
			skipValue(opcode, data.getUShort(), data.getUShort(), (data.getUShort() / 1000));
		} else if (opcode == 253) {
			skipValue(opcode, data.getUByte());
		} else if (opcode == 254) {
			skipValue(opcode, data.getUByte());
		} else if (opcode == 255) {
			skipValue(opcode, data.getUShort(), data.getUShort(), (data.getUShort() / 1000));
		}
	}
}
