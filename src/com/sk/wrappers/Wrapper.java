package com.sk.wrappers;

import com.sk.datastream.Stream;

public abstract class Wrapper<T extends WrapperLoader> {

	private final T loader;
	private final int id;

	public Wrapper(T loader, int id) {
		this.loader = loader;
		this.id = id;
	}

	public abstract void decode(Stream stream);

	public int getId() {
		return id;
	}

	public T getLoader() {
		return loader;
	}
}
