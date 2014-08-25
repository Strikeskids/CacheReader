package com.sk.cache.wrappers;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sk.cache.wrappers.loaders.WrapperLoader;

public abstract class Wrapper {

	protected static final Logger WRAPPER_LOGGER = Logger.getLogger(Wrapper.class.getCanonicalName());

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

	protected void skipValue(int opcode, Object... os) {
		StringBuilder builder = new StringBuilder();
		builder.append(opcode);
		builder.append(" ");
		appendObject(builder, os != null && os.length == 1 ? os[0] : os);
		WRAPPER_LOGGER.logp(Level.FINE, this.getClass().getName(), "skip", builder.toString());
	}

	private void appendObject(StringBuilder output, Object o) {
		if (o == null) {
			output.append("null");
		} else if (o instanceof Long) {
			output.append(String.format("%016x", ((Long) o).longValue()));
		} else if (o instanceof Double) {
			output.append(o);
		} else if (o instanceof Float) {
			output.append(o);
		} else if (o instanceof Number) {
			output.append(String.format("%08x", ((Number) o).intValue()));
		} else if (o instanceof Boolean) {
			output.append((Boolean) o ? "T" : "F");
		} else if (o.getClass().isArray()) {
			output.append("[");
			for (int i = 0, len = Array.getLength(o); i < len; ++i) {
				if (i != 0)
					output.append(", ");
				appendObject(output, Array.get(o, i));
			}
			output.append("]");
		} else if (o instanceof Collection<?>) {
			output.append("[");
			boolean first = true;
			for (Object child : (Collection<?>) o) {
				if (first & !(first = false))
					output.append(", ");
				appendObject(output, child);
			}
			output.append("]");
		} else {
			output.append(o);
		}
	}
}
