package com.sk.wrappers.protocol;

import com.sk.datastream.Stream;

public class BasicSetter extends StaticLocReader {

	private final String fieldName;
	private final Object value;

	public BasicSetter(String fieldName, Object value, int... locs) {
		super(locs);
		this.fieldName = fieldName;
		this.value = value;
	}

	@Override
	public void read(Object destination, int type, Stream s) {
		FieldExtractor.setValue(destination, getMinimumType(), type, fieldName, value);
	}

}
