package com.sk.wrappers;

import com.sk.datastream.Stream;
import com.sk.wrappers.protocol.FieldExtractor;
import com.sk.wrappers.protocol.ProtocolGroup;
import com.sk.wrappers.protocol.StaticLocReader;


public class Script extends ProtocolWrapper<ScriptLoader> {

	public int configId = -1;
	public int configType = -1;
	public int lowerBitIndex = -1;
	public int upperBitIndex = -1;
	
	public Script(ScriptLoader loader, int id) {
		super(loader, id, protocol );
	}

	private static final ProtocolGroup protocol = new ProtocolGroup();
	
	static {
		new StaticLocReader(1) {
			@Override
			public void read(Object destination, int type, Stream s) {
				FieldExtractor.setValue(destination, type, type, "configType", s.getUByte());
				FieldExtractor.setValue(destination, type, type, "configId", s.getBigSmart());
			}
		}.addSelfToGroup(protocol);
		new StaticLocReader(2) {
			@Override
			public void read(Object destination, int type, Stream s) {
				FieldExtractor.setValue(destination, type, type, "lowerBitIndex", s.getUByte());
				FieldExtractor.setValue(destination, type, type, "upperBitIndex", s.getUByte());
			}
		}.addSelfToGroup(protocol);
	}
}
