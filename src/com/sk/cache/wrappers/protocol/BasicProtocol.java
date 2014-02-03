package com.sk.cache.wrappers.protocol;

import com.sk.cache.wrappers.protocol.extractor.FieldExtractor;
import com.sk.cache.wrappers.protocol.extractor.StreamExtractor;
import com.sk.datastream.Stream;

public class BasicProtocol extends StaticLocReader {

	private final FieldExtractor[] extractors;

	public BasicProtocol(StreamExtractor extractor, int... locs) {
		this(FieldExtractor.wrap(extractor), locs);
	}

	public BasicProtocol(FieldExtractor extractor, int... locs) {
		this(new FieldExtractor[] { extractor }, locs);
	}

	public BasicProtocol(StreamExtractor[] extractors, int... locs) {
		this(FieldExtractor.wrap(extractors), locs);
	}

	public BasicProtocol(FieldExtractor[] extractors, int... locs) {
		super(locs);
		this.extractors = extractors;
	}

	@Override
	public void read(Object destination, int type, Stream s) {
		for (FieldExtractor ext : extractors) {
			ext.read(destination, getMinimumType(), type, s);
		}
	}

}
