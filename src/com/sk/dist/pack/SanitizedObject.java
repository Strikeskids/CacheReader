package com.sk.dist.pack;

import com.sk.wrappers.ObjectDefinition;
import com.sk.wrappers.Script;
import com.sk.wrappers.ScriptLoader;

public class SanitizedObject {
	public String name;
	public int settingId = -1;
	public byte lowSettingBit = -1, highSettingBit = -1;
	public int[] childrenIds;

	public SanitizedObject(ObjectDefinition source) {
		this.name = source.name;
		if (source.configId != -1) {
			this.settingId = source.configId;
		} else if (source.scriptId != -1) {
			Script loaded = new ScriptLoader(source.getLoader().getCacheSystem()).load(source.scriptId);
			this.settingId = loaded.configId;
			this.lowSettingBit = (byte) loaded.lowerBitIndex;
			this.highSettingBit = (byte) loaded.upperBitIndex;
		}
		this.childrenIds = source.childrenIds;
	}

}
