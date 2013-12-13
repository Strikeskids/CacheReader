package com.sk.wrappers;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public abstract class Wrapper<T extends WrapperLoader> {

	protected final T loader;
	protected final int id;

	public Wrapper(T loader, int id) {
		this.loader = loader;
		this.id = id;
	}

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
		output.append(" ");
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
			if (f.getType().isArray()) {
				output.append("[");
				for (int i = 0, len = Array.getLength(o); i < len; ++i) {
					if (i != 0)
						output.append(", ");
					output.append(Array.get(o, i));
				}
				output.append("]");
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
