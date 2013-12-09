package com.sk.wrappers.protocol;

public class ArrayReader extends ArrayProtocol {

	public ArrayReader(StreamExtractor repeater, FieldExtractor[] extractors, int... locs) {
		super(repeater, new BasicReader(extractors, locs), locs);
	}
}
