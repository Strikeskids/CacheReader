package com.sk.cache.wrappers;

import java.util.Map;

import com.sk.cache.wrappers.loaders.QuestDefinitionLoader;
import com.sk.datastream.Stream;

public class QuestDefinition extends ProtocolWrapper {

	public String name;
	public int[] scriptId, scriptStartValue, scriptEndValue, configId, configStartValue, configEndValue;
	public int questPoints;

	public QuestDefinition(QuestDefinitionLoader loader, int id) {
		super(loader, id);
	}

	@Override
	protected void decodeOpcode(Stream data, int opcode) {
		switch (opcode) {
		case 1:
			this.name = data.getJagString();
			return;
		case 2:
			skipValue(opcode, data.getJagString());
			return;
		case 3:
			int size = data.getUByte();
			configId = new int[size];
			configStartValue = new int[size];
			configEndValue = new int[size];
			for (int e = 0; e < size; e++) {
				configId[e] = data.getUShort();
				configStartValue[e] = data.getInt();
				configEndValue[e] = data.getInt();
			}
			return;
		case 4:
			size = data.getUByte();
			scriptId = new int[size];
			scriptStartValue = new int[size];
			scriptEndValue = new int[size];
			for (int e = 0; e < size; e++) {
				scriptId[e] = data.getUShort();
				scriptStartValue[e] = data.getInt();
				scriptEndValue[e] = data.getInt();
			}
			return;
		case 5:
			skipValue(opcode, data.getUShort());
			return;
		case 6:
			int type = data.getUByte();
			skipValue(opcode, type);
			return;
		case 7:
			skipValue(opcode, data.getUByte());
			return;
		case 8:
			skipValue(opcode, true);
			return;
		case 9:
			questPoints = data.getUByte();
			return;
		case 10:
			int b = data.getUByte();
			int[] _bgs = new int[b];
			for (int m = 0; m < b; m++) {
				_bgs[m] = data.getInt();
			}
			skipValue(opcode, b, _bgs);
			return;
		case 12:
			skipValue(opcode, data.getInt());
			return;
		case 13:
			b = data.getUByte();
			int[] _bgu = new int[b];
			for (int m = 0; m < b; m++) {
				_bgu[m] = data.getUShort();
			}
			skipValue(opcode, b, _bgu);
			return;
		case 14:
			b = data.getUByte();
			int[] _bgv = new int[b];
			int[] _bgw = new int[b];
			for (int m = 0; m < b; m++) {
				_bgv[m] = data.getUByte();
				_bgw[m] = data.getUByte();
			}
			skipValue(opcode, b, _bgv, _bgw);
			return;
		case 15:
			skipValue(data.getUShort());
			return;
		case 17:
			int icon = data.getBigSmart();
			skipValue(opcode, icon);
			return;
		case 18:
			b = data.getUByte();
			int[] arr1 = new int[b];
			int[] arr2 = new int[b];
			int[] arr3 = new int[b];
			String[] arr4 = new String[b];
			for (int m = 0; m < b; m++) {
				arr1[m] = data.getInt();
				arr2[m] = data.getInt();
				arr3[m] = data.getInt();
				arr4[m] = data.getString();
			}
			return;
		case 19:
			b = data.getUByte();
			arr1 = new int[b];
			arr2 = new int[b];
			arr3 = new int[b];
			arr4 = new String[b];
			for (int m = 0; m < b; m++) {
				arr1[m] = data.getInt();
				arr2[m] = data.getInt();
				arr3[m] = data.getInt();
				arr4[m] = data.getString();
			}
			return;
		case 249:
			Map<Integer, Object> params = decodeParams(data);
			skipValue(opcode, params);
			return;
		}
	}
}
