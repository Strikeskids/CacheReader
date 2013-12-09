package com.sk.wrappers.protocol;

import java.lang.reflect.Field;

import com.sk.datastream.Stream;

public class BasicReader extends StaticLocReader {

	private final StreamExtractor extractor;
	private final String fieldName;

	public BasicReader(StreamExtractor type, String fieldName, int... locs) {
		super(locs);
		this.extractor = type;
		this.fieldName = fieldName;
	}

	@Override
	public void read(Object destination, int type, Stream data) {
		Class<?> clazz = destination.getClass();
		try {
			Field field = clazz.getField(fieldName);
			Object newValue = extractor.get(data);
			field.set(destination, newValue);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(String.format("Failed to read data into destination object %s field %s",
					destination, fieldName));
		}
	}

}
