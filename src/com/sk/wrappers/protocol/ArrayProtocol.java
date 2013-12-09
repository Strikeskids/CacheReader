package com.sk.wrappers.protocol;

import com.sk.datastream.Stream;

public class ArrayProtocol extends StaticLocReader {

	private final StreamExtractor counter;
	private final ProtocolReader repeat;

	public ArrayProtocol(StreamExtractor counter, ProtocolReader repeat, int... locs) {
		super(locs);
		this.counter = counter;
		this.repeat = repeat;
	}

	@Override
	public void read(Object destination, int type, Stream s) {
		int count = ((Number) counter.get(s)).intValue();
		for (int i = 0; i < count; ++i) {
			repeat.read(destination, type, s);
		}
	}

}
