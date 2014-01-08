package com.sk.cache.wrappers;

import com.sk.cache.wrappers.loaders.WrapperLoader;
import com.sk.datastream.Stream;

public abstract class StreamedWrapper extends Wrapper {

	public StreamedWrapper(WrapperLoader<?> loader, int id) {
		super(loader, id);
	}

	public abstract void decode(Stream stream);

}
