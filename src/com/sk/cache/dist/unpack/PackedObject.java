package com.sk.cache.dist.unpack;

public class PackedObject extends Packed {
	public String name;
	public int settingId;
	public byte lowSettingBit, highSettingBit;
	public int[] childrenIds;
}
