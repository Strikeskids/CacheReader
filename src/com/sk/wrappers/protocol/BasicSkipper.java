package com.sk.wrappers.protocol;

import java.util.ArrayList;
import java.util.List;

import com.sk.Debug;
import com.sk.datastream.Stream;

public class BasicSkipper extends StaticLocReader {

	private final StreamExtractor[] extractors;

	public BasicSkipper(StreamExtractor extractor, int... locs) {
		this(new StreamExtractor[] { extractor }, locs);
	}

	public BasicSkipper(StreamExtractor[] extractors, int... locs) {
		super(locs);
		this.extractors = extractors;
	}

	@Override
	public void read(Object destination, int type, Stream data) {
		List<Object> values = new ArrayList<>();
		for (StreamExtractor ex : extractors) {
			Object value = ex.get(data);
			if (Debug.on) {
				values.add(value);
			}
		}
		if (Debug.on) {
			System.out.printf("Skip %d %s", type, values.size() == 1 ? values.get(0) : values);
		}
	}

}
