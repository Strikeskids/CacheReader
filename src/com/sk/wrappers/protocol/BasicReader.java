package com.sk.wrappers.protocol;

import com.sk.datastream.Stream;

public class BasicReader extends StaticLocReader {

	private final FieldExtractor[] extractors;

	public BasicReader(StreamExtractor type, String fieldName, int... locs) {
		this(new FieldExtractor[] { new FieldExtractor(type, fieldName) }, locs);
	}

	public BasicReader(FieldExtractor[] extractors, int... locs) {
		super(locs);
		this.extractors = extractors;
	}

	@Override
	public void read(Object destination, int type, Stream data) {
		for (FieldExtractor fe : extractors) {
			fe.read(destination, getMinimumType(), type, data);
		}
	}

}
