package com.sk.cache.wrappers.protocol.extractor;

import com.sk.datastream.Stream;

public class SizedStreamExtractor implements StreamExtractor {

	private final int byteCount;

	public SizedStreamExtractor(int byteCount) {
		this.byteCount = byteCount;
	}

	@Override
	public Object get(Stream s) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < byteCount; ++i) {
			if (i > 0)
				ret.append(" ");
			ret.append(String.format("%2x", s.getUByte()));
		}
		return ret.toString();
	}
}
