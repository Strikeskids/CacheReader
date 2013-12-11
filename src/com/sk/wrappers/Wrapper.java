package com.sk.wrappers;

import java.lang.reflect.Field;
import java.util.Arrays;

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

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append(getClass().getSimpleName());
		output.append(getId());
		output.append(" {");
		for (Field f : getClass().getDeclaredFields()) {
			Object o;
			try {
				o = f.get(this);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				continue;
			}
			if (o == null)
				continue;
			output.append(f.getName());
			output.append(": ");
			if (o instanceof Object[]) {
				output.append(Arrays.toString((Object[]) o));
			} else {
				output.append(o);
			}
			output.append(", ");
		}
		output.delete(output.length() - 2, output.length());
		output.append("}");
		return output.toString();
	}
}
