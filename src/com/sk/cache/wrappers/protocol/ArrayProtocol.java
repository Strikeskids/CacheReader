package com.sk.cache.wrappers.protocol;

import com.sk.cache.wrappers.protocol.extractor.StreamExtractor;
import com.sk.datastream.Stream;

public class ArrayProtocol extends StaticLocReader {

	private final StreamExtractor counter;
	private final ProtocolReader repeat;

	public ArrayProtocol(StreamExtractor counter, StreamExtractor extractor, int... locs) {
		this(counter, new BasicProtocol(extractor, locs), locs);
	}

	public ArrayProtocol(StreamExtractor counter, FieldExtractor extractor, int... locs) {
		this(counter, new BasicProtocol(extractor, locs), locs);
	}

	public ArrayProtocol(StreamExtractor counter, StreamExtractor[] extractors, int... locs) {
		this(counter, new BasicProtocol(extractors, locs), locs);
	}

	public ArrayProtocol(StreamExtractor counter, FieldExtractor[] extractors, int... locs) {
		this(counter, new BasicProtocol(extractors, locs), locs);
	}

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
