package com.sk.cache.dist.unpack;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public abstract class Packed {
	public int id;

	public void initialize() {
		
	}
	
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append(getClass().getSimpleName());
		output.append(" ");
		output.append(id);
		output.append(" {");
		for (Field f : getClass().getDeclaredFields()) {
			Object o;
			try {
				o = f.get(this);
			} catch (IllegalAccessException e) {
				continue;
			} catch (IllegalArgumentException e) {
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
