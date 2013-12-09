package com.sk.wrappers;

import com.sk.datastream.Stream;
import com.sk.wrappers.protocol.ProtocolGroup;

public class ProtocolWrapper<T extends WrapperLoader> extends Wrapper<T> {

	private final ProtocolGroup protocol;
	
	public ProtocolWrapper(T loader, int id, ProtocolGroup protocol) {
		super(loader, id);
		this.protocol = protocol;
	}

	@Override
	public void decode(Stream stream) {
		int opcode;
		while ((opcode = stream.getUByte()) != 0) {
			protocol.read(this, opcode, stream);
		}
	}

}
