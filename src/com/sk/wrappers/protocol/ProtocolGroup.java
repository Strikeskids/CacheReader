package com.sk.wrappers.protocol;

import java.util.HashMap;
import java.util.Map;

import com.sk.datastream.Stream;

public class ProtocolGroup extends ProtocolReader {

	private Map<Integer, ProtocolReader> readers = new HashMap<>();

	public void addReader(ProtocolReader reader, int... locs) {
		for (int loc : locs) {
			readers.put(loc, reader);
		}
	}
	
	public ProtocolReader getReader(int type) {
		return readers.get(type);
	}

	@Override
	public void read(Object destination, int type, Stream data) {
		if (!validateType(type))
			throw new IllegalArgumentException("Bad type");
		readers.get(type).read(destination, type, data);
	}

	@Override
	public boolean validateType(int type) {
		return readers.containsKey(type);
	}

	@Override
	public void addSelfToGroup(ProtocolGroup group) {
		throw new UnsupportedOperationException();
	}
}
