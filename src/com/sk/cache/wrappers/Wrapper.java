package com.sk.cache.wrappers;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sk.cache.wrappers.loaders.WrapperLoader;

public abstract class Wrapper {

	protected final WrapperLoader<?> loader;
	protected final int id;

	public Wrapper(WrapperLoader<?> loader, int id) {
		this.loader = loader;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public WrapperLoader<?> getLoader() {
		return loader;
	}

	public Map<String, Object> getDeclaredFields() {
		Map<String, Object> ret = new LinkedHashMap<String, Object>();
		for (Field f : getClass().getDeclaredFields()) {
			Object o;
			try {
				o = f.get(this);
			} catch (Exception e) {
				o = null;
			}
			ret.put(f.getName(), o);
		}
		return ret;
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append(getClass().getSimpleName());
		output.append(" ");
		output.append(getId());
		output.append(" {");
		for (Field f : getClass().getDeclaredFields()) {
			if ((f.getModifiers() & (Modifier.STATIC | Modifier.FINAL)) != 0)
				continue;
			Object o;
			try {
				o = f.get(this);
			} catch (Exception e) {
				continue;
			}
			if (o == null)
				continue;
			output.append(f.getName());
			output.append(": ");
			appendObject(output, o);
			output.append(", ");
		}
		output.delete(output.length() - 2, output.length());
		output.append("}");
		return output.toString();
	}

	private void appendObject(StringBuilder output, Object o) {
		if (o == null)
			return;
		if (o.getClass().isArray()) {
			output.append("[");
			for (int i = 0, len = Array.getLength(o); i < len; ++i) {
				if (i != 0)
					output.append(", ");
				appendObject(output, Array.get(o, i));
			}
			output.append("]");
		} else {
			output.append(o);
		}
	}
}
