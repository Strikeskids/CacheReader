package com.sk.cache.wrappers;

import java.util.Map;

import com.sk.cache.wrappers.loaders.NpcDefinitionLoader;
import com.sk.datastream.Stream;

public class NpcDefinition extends ProtocolWrapper {

	public int combatLevel = -1;
	public int headIcon = -1;
	public boolean clickable = true;
	public boolean visible = true;
	public String[] actions = new String[5];
	public String name;

	public int scriptId, configId;
	public int[] childrenIds;

	public int[] modelIds;
	public int[][] modelOffsets;

	public NpcDefinition(NpcDefinitionLoader loader, int id) {
		super(loader, id);
	}

	public ModelBounds getModelBounds() {
		if (modelIds == null || modelIds.length == 0)
			return null;
		Model[] models = new Model[modelIds.length];
		for (int i = 0; i < modelIds.length; ++i) {
			Model cur = getLoader().getCacheSystem().modelLoader.load(modelIds[i]);
			if (modelOffsets != null && modelOffsets[i] != null) {
				cur.offsetVertices(modelOffsets[i][0], modelOffsets[i][1], modelOffsets[i][2]);
			}
		}
		return new ModelBounds(models);
	}

	@Override
	protected void decodeOpcode(Stream data, int opcode) {
		if (opcode == 1) {
			int A = data.getUByte();
			modelIds = new int[A];
			for (int i = 0; i < A; i++) {
				modelIds[i] = data.getBigSmart();
			}
		} else if (opcode == 2) {
			name = data.getString();
		} else if (opcode == 12) {
			skipValue(opcode, data.getUByte());
		} else if (opcode >= 30 && opcode < 35) {
			actions[opcode - 30] = data.getString();
		} else if (opcode == 39) {
			skipValue(opcode, data.getByte() * 5);
		} else if (opcode == 40) {
			int B = data.getUByte();
			int[] originalColors = new int[B];
			int[] modifiedColors = new int[B];
			for (int E = 0; E < B; E++) {
				originalColors[E] = data.getUShort();
				modifiedColors[E] = data.getUShort();
			}
			skipValue(opcode, B, originalColors, modifiedColors);
		} else if (opcode == 41) {
			int M = data.getUByte();
			int[] arr1 = new int[M];
			int[] arr2 = new int[M];
			for (int v = 0; v < M; v++) {
				arr1[v] = data.getShort();
				arr2[v] = data.getShort();
			}
			skipValue(opcode, M, arr1, arr2);
		} else if (opcode == 42) {
			int g = data.getUByte();
			int[] arr = new int[g];
			for (int F = 0; F < g; F++) {
				arr[F] = data.getByte();
			}
			skipValue(opcode, g, arr);
		} else if (opcode == 44) {
			int L = data.getUShort();
			skipValue(opcode, L);
			// int j = 0;
			// for (int C = L; C > 0; C = C >> 1) {
			// j++;
			// }
			// _kud = new Array(j);
			// int e = 0;
			// for (int C = 0; C < j; C++) {
			// if ((L & (1 << C)) > 0) {
			// _kud[C] = e++;
			// } else {
			// _kud[C] = -1;
			// }
			// }
		} else if (opcode == 45) {
			int J = data.getUShort();
			skipValue(opcode, J);
			// int K = 0;
			// for (int C = J; C > 0; C = C >> 1) {
			// K++;
			// }
			// _kui = new Array(K);
			// int e = 0;
			// for (int C = 0; C < K; C++) {
			// if ((J & (1 << C)) > 0) {
			// _kui[C] = e++;
			// } else {
			// _kui[C] = -1;
			// }
			// }
		} else if (opcode == 60) {
			int size = data.getUByte();
			int[] arr = new int[size];
			for (int i = 0; i < size; i++) {
				arr[i] = data.getBigSmart();
			}
			skipValue(opcode, size, arr);
		} else if (opcode == 93) {
			visible = false;
		} else if (opcode == 95) {
			combatLevel = data.getUShort();
		} else if (opcode == 97 || opcode == 98) {
			float[] resize = null;
			if (resize == null)
				resize = new float[] { 128, 128, 128 };
			if (opcode == 97) {
				resize[0] = resize[2] = data.getUShort();
			} else {
				resize[1] = data.getUShort();
			}
			skipValue(opcode, resize);
		} else if (opcode == 99) {
			skipValue(opcode, true);
		} else if (opcode == 100) {
			skipValue(opcode, data.getByte() + 64);
		} else if (opcode == 101) {
			skipValue(opcode, data.getByte() * 5);
		} else if (opcode == 102) {
			int G = data.getUByte();
			int x = 0;
			int C = G;
			while (C != 0) {
				x++;
				C >>= 1;
			}
			int[] arr1 = new int[x];
			int[] arr2 = new int[x];
			for (int k = 0; k < x; k++) {
				if ((G & (1 << k)) == 0) {
					arr1[k] = -1;
					arr2[k] = -1;
				} else {
					arr1[k] = data.getBigSmart();
					arr2[k] = data.getSmartMinusOne();
				}
			}
			skipValue(opcode, G, arr1, arr2);
		} else if (opcode == 103) {
			skipValue(data.getUShort());
		} else if (opcode == 106 || opcode == 118) {
			scriptId = data.getUShort();
			if (scriptId == 65535) {
				scriptId = -1;
			}
			configId = data.getUShort();
			if (configId == 65535) {
				configId = -1;
			}
			int defaultChildId = -1;
			if (opcode == 118) {
				defaultChildId = data.getUShort();
				if (defaultChildId == 65535) {
					defaultChildId = -1;
				}
			}
			int childCount = data.getUByte();
			childrenIds = new int[childCount + 2];
			for (int o = 0; o <= childCount; o++) {
				childrenIds[o] = data.getUShort();
				if (childrenIds[o] == 65535) {
					childrenIds[o] = -1;
				}
			}
			childrenIds[childCount + 1] = defaultChildId;
		} else if (opcode == 107) {
			clickable = false;
		} else if (opcode == 109) {
			skipValue(opcode, false);
		} else if (opcode == 111) {
			skipValue(opcode, false);
		} else if (opcode == 113) {
			data.getUShort();
			data.getUShort();
		} else if (opcode == 114) {
			data.getByte();
			data.getByte();
		} else if (opcode == 119) {
			skipValue(opcode, data.getUByte());
		} else if (opcode == 121) {
			modelOffsets = new int[modelIds.length][3];
			int t = data.getUByte();
			for (int r = 0; r < t; r++) {
				int u = data.getUByte();
				modelOffsets[u][0] = data.getByte();
				modelOffsets[u][1] = data.getByte();
				modelOffsets[u][2] = data.getByte();
			}
		} else if (opcode == 123) {
			skipValue(opcode, data.getUShort());
		} else if (opcode == 125) {
			skipValue(opcode, data.getByte());
		} else if (opcode == 127) {
			skipValue(opcode, data.getUShort());
		} else if (opcode == 128) {
			skipValue(opcode, data.getUByte());
		} else if (opcode == 134) {
			int _txc = data.getUShort();
			if (_txc == 65535) {
				_txc = -1;
			}
			int _txe = data.getUShort();
			if (_txe == 65535) {
				_txe = -1;
			}
			int _txf = data.getUShort();
			if (_txf == 65535) {
				_txf = -1;
			}
			int _txh = data.getUShort();
			if (_txh == 65535) {
				_txh = -1;
			}
			int _txi = data.getUByte();
			skipValue(opcode, _txc, _txe, _txf, _txh, _txi);
		} else if (opcode == 137) {
			// arr len 6 default -1 index 1
			skipValue(opcode, 1, data.getUShort());
		} else if (opcode == 138) {
			headIcon = data.getBigSmart();
		} else if (opcode == 139) {
			skipValue(opcode, data.getBigSmart());
		} else if (opcode == 140) {
			skipValue(opcode, data.getUByte());
		} else if (opcode == 141) {
			skipValue(opcode, true);
		} else if (opcode == 142) {
			skipValue(opcode, data.getUShort());
		} else if (opcode == 143) {
			skipValue(opcode, true);
		} else if (opcode >= 150 && opcode < 155) {
			actions[opcode - 150] = data.getString();
		} else if (opcode == 155) {
			skipValue(opcode, data.getByte(), data.getByte(), data.getByte(), data.getByte());
		} else if (opcode == 158) {
			skipValue(opcode, 1);
		} else if (opcode == 159) {
			skipValue(opcode, 0);
		} else if (opcode == 160) {
			int I = data.getUByte();
			int[] arr = new int[I];
			for (int D = 0; D < I; D++) {
				arr[D] = data.getUShort();
			}
			skipValue(I, arr);
		} else if (opcode == 162) {
			skipValue(opcode, true);
		} else if (opcode == 163) {
			data.getUByte();
		} else if (opcode == 164) {
			skipValue(opcode, data.getUShort(), data.getUShort());
		} else if (opcode == 165) {
			skipValue(opcode, data.getUByte());
		} else if (opcode == 168) {
			skipValue(opcode, data.getUByte());
		} else if (opcode == 169) {
			skipValue(opcode, false);
		} else if (opcode >= 170 && opcode < 176) {
			// array len 6 default -1 index opcode-170
			skipValue(opcode, opcode - 170, data.getUShort());
		} else if (opcode == 178) {
			skipValue(opcode, false);
		} else if (opcode == 179) {
			skipValue(opcode, data.getSmart(), data.getSmart(), data.getSmart(), data.getSmart(), data.getSmart(), data.getSmart());
		} else if (opcode == 180) {
			skipValue(opcode, data.getUByte() * 1000 / 50);
		} else if (opcode == 181) {
			skipValue(opcode, data.getUShort(), data.getUByte());
		} else if (opcode == 182) {
			skipValue(opcode, true);
		} else if (opcode == 249) {
			Map<Integer, Object> params = decodeParams(data);
			skipValue(opcode, params);
		}
	}
}
