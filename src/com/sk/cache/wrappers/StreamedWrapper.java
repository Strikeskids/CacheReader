package com.sk.cache.wrappers;

import com.sk.datastream.Stream;

public abstract class StreamedWrapper<T extends WrapperLoader> extends Wrapper<T> {

	public StreamedWrapper(T loader, int id) {
		super(loader, id);
	}

	public abstract void decode(Stream stream);

}
