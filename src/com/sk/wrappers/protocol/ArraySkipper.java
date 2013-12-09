package com.sk.wrappers.protocol;


public class ArraySkipper extends ArrayProtocol {

	public ArraySkipper(StreamExtractor counter, StreamExtractor[] repeat, int... locs) {
		super(counter, new BasicSkipper(repeat, locs), locs);
	}

}
