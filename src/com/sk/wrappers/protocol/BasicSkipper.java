package com.sk.wrappers.protocol;

import com.sk.Debug;
import com.sk.datastream.Stream;

public class BasicSkipper extends StaticLocReader {

	private final StreamExtractor extractor;
	
	public BasicSkipper(StreamExtractor extractor, int... locs) {
		super(locs);
		this.extractor = extractor;
	}
	
	@Override
	public void read(Object destination, int type, Stream data) {
		Object value = extractor.get(data);
		if (Debug.on) {
			System.out.println(value);
		}
	}

}
