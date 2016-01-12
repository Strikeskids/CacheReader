package com.sk.cache.wrappers;

import java.util.Map;

import com.sk.cache.wrappers.loaders.ItemDefinitionLoader;
import com.sk.datastream.Stream;

public class ItemDefinition extends ProtocolWrapper {

	public boolean stackable;
	public boolean members;
	public boolean edible;
	public boolean noted;
	public boolean lent;
	public boolean cosmetic;
	public boolean tradeable;
	public boolean equipable;
	public int slot = -1;
	public int noteId = -1;
	public int value = 0;
	public int lentId = -1;
	public String[] actions = { null, null, null, null, "Drop" };
	public String[] groundActions = { null, null, "Take", null, null };
	public String[] equipActions;
	public int noteTemplateId = -1;
	public int lentTemplateId = -1;
	public int cosmeticId = -1;
	public int cosmeticTemplateId = -1;
	public int team;
	public ItemStats stats;

	public String name;

	public ItemDefinition(ItemDefinitionLoader loader, int id) {
		super(loader, id);
	}

	@Override
	protected void decodeOpcode(Stream stream, int opcode) {
		if (opcode == 1) {
			int modelId = stream.getBigSmart();
			skipValue(opcode, modelId);
		} else if (opcode == 2) {
			this.name = stream.getString();
		} else if (opcode == 4) {
			int modelZoom = stream.getUShort();
			skipValue(opcode, modelZoom);
		} else if (opcode == 5) {
			int modelRotation1 = stream.getUShort();
			skipValue(opcode, modelRotation1);
		} else if (opcode == 6) {
			int modelRotation2 = stream.getUShort();
			skipValue(opcode, modelRotation2);
		} else if (opcode == 7) {
			int modelOffset1 = stream.getUShort();
			if (modelOffset1 > 32767) {
				modelOffset1 = 65536;
			}
			skipValue(opcode, modelOffset1);
		} else if (opcode == 8) {
			int modelOffset2 = stream.getUShort();
			if (modelOffset2 > 32767) {
				modelOffset2 -= 65536;
			}
			skipValue(opcode, modelOffset2);
		} else if (opcode == 11) {
			this.stackable = true;
		} else if (opcode == 12) {
			this.value = stream.getInt();
		} else if (opcode == 13) {
			this.slot = stream.getUByte();
			this.equipable = this.slot >= 0;
		} else if (opcode == 14) {
			skipValue(opcode, stream.getUByte());
		} else if (opcode == 16) {
			this.members = true;
		} else if (opcode == 18) {
			skipValue(opcode, stream.getUShort());
		} else if (opcode == 23) {
			int maleEquip1 = stream.getBigSmart();
			skipValue(opcode, maleEquip1);
		} else if (opcode == 24) {
			int femaleEquip1 = stream.getBigSmart();
			skipValue(opcode, femaleEquip1);
		} else if (opcode == 25) {
			int maleEquip2 = stream.getBigSmart();
			skipValue(opcode, maleEquip2);
		} else if (opcode == 26) {
			int femaleEquip2 = stream.getBigSmart();
			skipValue(opcode, femaleEquip2);
		} else if (opcode == 27) {
			skipValue(opcode, stream.getUByte());
		} else if (opcode >= 30 && opcode < 35) {
			this.groundActions[opcode - 30] = stream.getString();
		} else if (opcode >= 35 && opcode < 40) {
			this.actions[opcode - 35] = stream.getString();
		} else if (opcode == 40) {
			int size = stream.getUByte();
			int[] originalColors = new int[size];
			int[] modifiedColors = new int[size];
			for (int u = 0; u < size; u++) {
				originalColors[u] = stream.getUShort();
				modifiedColors[u] = stream.getUShort();
			}
			skipValue(opcode, size, originalColors, modifiedColors);
		} else if (opcode == 41) {
			int size = stream.getUByte();
			int[] arr1 = new int[size];
			int[] arr2 = new int[size];
			for (int o = 0; o < size; o++) {
				arr1[o] = stream.getShort();
				arr2[o] = stream.getShort();
			}
			skipValue(opcode, size, arr1, arr2);
		} else if (opcode == 42) {
			int g = stream.getUByte();
			int[] arr = new int[g];
			for (int x = 0; x < g; x++) {
				arr[x] = stream.getByte();
			}
		} else if (opcode == 43) {
			int value1 = stream.getInt();
			boolean value2 = true;
			skipValue(opcode, value1, value2);
		} else if (opcode == 44) {
			int C = stream.getUShort();
			skipValue(opcode, C);
			// var i = 0;
			// for (var t = C; t > 0; t = t >> 1) {
			// i++;
			// }
			// this._kud = new Array(i);
			// var e = 0;
			// for (var t = 0; t < i; t++) {
			// if ((C & (1 << t)) > 0) {
			// this._kud[t] = e++;
			// } else {
			// this._kud[t] = -1;
			// }
			// }
		} else if (opcode == 45) {
			int z = stream.getUShort();
			skipValue(opcode, z);
			// int B = 0;
			// for (int t = z; t > 0; t = t >> 1) {
			// B++;
			// }
			// this._kui = new int[B];
			// int e = 0;
			// for (int t = 0; t < B; t++) {
			// if ((z & (1 << t)) > 0) {
			// this._kui[t] = e++;
			// } else {
			// this._kui[t] = -1;
			// }
			// }
		} else if (opcode == 65) {
			tradeable = true;
		} else if (opcode == 78) {
			skipValue(opcode, stream.getBigSmart());
		} else if (opcode == 79) {
			skipValue(opcode, stream.getBigSmart());
		} else if (opcode == 90) {
			skipValue(opcode, stream.getBigSmart());
		} else if (opcode == 91) {
			skipValue(opcode, stream.getBigSmart());
		} else if (opcode == 92) {
			skipValue(opcode, stream.getBigSmart());
		} else if (opcode == 93) {
			skipValue(opcode, stream.getBigSmart());
		} else if (opcode == 94) {
			skipValue(opcode, stream.getUShort());
		} else if (opcode == 95) {
			int modelRotation3 = stream.getUShort();
			skipValue(opcode, modelRotation3);
		} else if (opcode == 96) {
			skipValue(opcode, stream.getUByte());
		} else if (opcode == 97) {
			this.noteId = stream.getUShort();
		} else if (opcode == 98) {
			this.noteTemplateId = stream.getUShort();
		} else if (opcode >= 100 && opcode < 110) {
			// array index k-100
			int stackId = stream.getUShort();
			int stackAmount = stream.getUShort();
			skipValue(opcode, opcode - 100, stackId, stackAmount);
		} else if (opcode >= 110 && opcode <= 112) {
			// array index k-110 default 128
			float resize = stream.getUShort();
			skipValue(opcode, opcode - 110, resize);
		} else if (opcode == 113) {
			skipValue(opcode, stream.getByte());
		} else if (opcode == 114) {
			skipValue(opcode, stream.getByte() * 5);
		} else if (opcode == 115) {
			this.team = stream.getUByte();
		} else if (opcode == 121) {
			this.lentId = stream.getUShort();
		} else if (opcode == 122) {
			this.lentTemplateId = stream.getUShort();
		} else if (opcode == 125) {
			skipValue(opcode, stream.getByte() << 2, stream.getByte() << 2, stream.getByte() << 2);
		} else if (opcode == 126) {
			skipValue(opcode, stream.getByte() << 2, stream.getByte() << 2, stream.getByte() << 2);
		} else if (opcode == 132) {
			int size = stream.getUByte();
			int[] arr = new int[size];
			for (int v = 0; v < size; v++) {
				arr[v] = stream.getUShort();
			}
			skipValue(opcode, size, arr);
		} else if (opcode == 134) {
			skipValue(opcode, stream.getUByte());
		} else if (opcode == 139) {
			cosmeticId = stream.getUShort();
			skipValue(opcode, cosmeticId);
		} else if (opcode == 140) {
			cosmeticTemplateId = stream.getUShort();
			skipValue(opcode, cosmeticTemplateId);
		} else if (opcode >= 142 && opcode < 147) {
			// array index k-142 default -1
			int value = stream.getUShort();
			skipValue(opcode, opcode - 142, value);
		} else if (opcode >= 150 && opcode < 155) {
			// array index k-150 default -1
			int value = stream.getUShort();
			skipValue(opcode, opcode - 150, value);
		} else if (opcode == 156) {
			skipValue(opcode, false);
		} else if (opcode == 157) {
			skipValue(opcode, true);
		} else if (opcode == 161) {
			skipValue(opcode, stream.getUShort());
		} else if (opcode == 162) {
			skipValue(opcode, stream.getUShort());
		} else if (opcode == 163) {
			skipValue(opcode, stream.getUShort());
		} else if (opcode == 164) {
			skipValue(opcode, stream.getString());
		} else if (opcode == 165) {
			skipValue(opcode, 2);
		} else if (opcode == 249) {
			Map<Integer, Object> params = decodeParams(stream);
			loadEquipActions(params);
			loadItemStats(params);
			skipValue(opcode, params);
		}
	}

	private void loadItemStats(Map<Integer, Object> params) {
		ItemStats stats = new ItemStats();
		stats.load(params);
		if (stats.isDefault()) {
			this.stats = stats;
		}
	}

	private void loadEquipActions(Map<Integer, Object> params) {
		int count = 0, idx = 0;
		for (int id : EQUIP_ACTION_PARAMS) {
			if (params.containsKey(id)) {
				count++;
			}
		}
		if (count == 0)
			return;
		this.equipActions = new String[count];
		for (int id : EQUIP_ACTION_PARAMS) {
			String action = (String) params.get(id);
			if (action != null) {
				equipActions[idx++] = action;
			}
		}
	}

	private static final int[] EQUIP_ACTION_PARAMS = { 528, 529, 530, 531, 1211 };
}
