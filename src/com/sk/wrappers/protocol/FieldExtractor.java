package com.sk.wrappers.protocol;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import com.sk.datastream.Stream;

public class FieldExtractor {

	private final String fieldName;
	private final StreamExtractor extractor;

	public FieldExtractor(StreamExtractor ext, String fieldName) {
		this.fieldName = fieldName;
		this.extractor = ext;
	}

	public void read(Object destination, int minLoc, int type, Stream s) {
		Class<?> clazz = destination.getClass();
		try {
			Object newValue = extractor.get(s);
			Field field = clazz.getField(fieldName);
			if (field.getType().isArray()) {
				Array.set(field.get(destination), type - minLoc, newValue);
			} else {
				field.set(destination, newValue);
			}
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(String.format("Failed to read data into destination object %s field %s",
					destination, fieldName));
		}
	}
}
