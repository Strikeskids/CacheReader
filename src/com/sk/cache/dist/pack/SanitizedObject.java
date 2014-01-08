package com.sk.cache.dist.pack;

import com.sk.cache.wrappers.ObjectDefinition;
import com.sk.cache.wrappers.Script;

public class SanitizedObject {
	public String name;
	public String[] actions;
	public int settingId = -1;
	public byte lowSettingBit = -1, highSettingBit = -1;
	public int[] childrenIds;

	public SanitizedObject(ObjectDefinition source) {
		this.name = source.name;
		this.actions = source.actions;
		if (source.configId != -1) {
			this.settingId = source.configId;
		} else if (source.scriptId != -1) {
			Script loaded = source.getLoader().getCacheSystem().scriptLoader.load(source.scriptId);
			this.settingId = loaded.configId;
			this.lowSettingBit = (byte) loaded.lowerBitIndex;
			this.highSettingBit = (byte) loaded.upperBitIndex;
		}
		this.childrenIds = source.childrenIds;
	}

}
