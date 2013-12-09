package com.sk.wrappers.protocol;

import com.sk.datastream.Stream;

public class StaticExtractor implements StreamExtractor {

	private final Object returnValue;
	
	public StaticExtractor(Object returnValue) {
		this.returnValue = returnValue;
	}
	
	@Override
	public Object get(Stream s) {
		return returnValue;
	}

}
