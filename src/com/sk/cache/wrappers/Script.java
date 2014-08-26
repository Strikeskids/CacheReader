package com.sk.cache.wrappers;

import com.sk.cache.wrappers.loaders.ScriptLoader;
import com.sk.datastream.Stream;

public class Script extends ProtocolWrapper {

	public int configId = -1;
	public int configType = -1;
	public int lowerBitIndex = -1;
	public int upperBitIndex = -1;

	public Script(ScriptLoader loader, int id) {
		super(loader, id);
	}

	@Override
	protected void decodeOpcode(Stream stream, int opcode) {
		if (opcode == 1) {
			configType = stream.getUByte();
			configId = stream.getBigSmart();
		} else if (opcode == 2) {
			lowerBitIndex = stream.getUByte();
			upperBitIndex = stream.getUByte();
		}
	}
}
