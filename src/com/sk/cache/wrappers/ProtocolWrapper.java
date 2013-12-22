package com.sk.cache.wrappers;

import com.sk.cache.wrappers.protocol.ProtocolGroup;
import com.sk.datastream.Stream;

public class ProtocolWrapper<T extends WrapperLoader> extends StreamedWrapper<T> {

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
