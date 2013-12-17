package com.sk.dist;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sk.wrappers.Wrapper;
import com.sk.wrappers.WrapperLoader;

public class ProtocolPacker<T extends Packed> extends Packer<T> {

	private final List<ProtocolField> booleans;
	private final List<ProtocolField> fields;
	private final Map<String, Field> sourceFields;

	public <E extends WrapperLoader> ProtocolPacker(E loader, Class<? extends Wrapper<E>> source, Class<T> storage) {
		super(loader, source, storage);
		this.sourceFields = new HashMap<>();
		for (Field f : source.getDeclaredFields()) {
			sourceFields.put(f.getName(), f);
		}
		this.fields = ProtocolType.extractAllFields(storage, false);
		this.booleans = ProtocolType.BOOLEAN.extractFields(storage);
		Collections.reverse(this.booleans);
	}

	@Override
	public int pack(Wrapper<?> input, OutputStream output) throws IOException {
		if (!checkWrapper(input))
			throw new IllegalArgumentException("Bad wrapper type");
		int size = 0;
		try {
			if (booleans.size() > 0) {
				size += packBooleans(input, output);
			}
			for (ProtocolField f : fields) {
				size += packField(input, output, f);
			}
		} catch (IllegalAccessException ignored) {
			ignored.printStackTrace();
		}
		return size;
	}

	private int packField(Wrapper<?> input, OutputStream out, ProtocolField field)
			throws IllegalArgumentException, IllegalAccessException, IOException {
		Object value = getFromSource(input, field.getField());
		return packField(out, value);
	}

	private int packField(OutputStream out, Object value) throws IOException {
		int ret = 0;
		if (value == null)
			value = "";
		Class<?> type = value.getClass();
		if (ProtocolType.ARRAY.isType(type)) {
			int len = Array.getLength(value);
			ret += writeValue(out, Array.getLength(value));
			for (int i = 0; i < len; ++i) {
				ret += packField(out, Array.get(value, i));
			}
		} else if (ProtocolType.INTEGER.isType(type)) {
			ret += writeValue(out, (long) value);
		} else if (ProtocolType.STRING.isType(type)) {
			ret += writeString(out, String.valueOf(value));
		}
		return ret;
	}

	private Object getFromSource(Wrapper<?> source, Field field) throws IllegalArgumentException,
			IllegalAccessException {
		Field sourceField = sourceFields.get(field.getName());
		Object value = sourceField.get(source);
		if (value == null) {
			return "";
		}
		if (ProtocolType.INTEGER.isType(value.getClass())) {
			if (Number.class.isAssignableFrom(value.getClass())) {
				value = ((Number) value).longValue();
			}
			value = (long) value;
		}
		return value;
	}

	private int packBooleans(Wrapper<?> input, OutputStream out) throws IllegalArgumentException,
			IllegalAccessException, IOException {
		int packed = 0, ret = 0, count = 0;
		for (ProtocolField f : booleans) {
			boolean value = (boolean) getFromSource(input, f.getField());
			packed <<= 1;
			packed |= value ? 1 : 0;
			if (count++ >= 30) {
				ret += writeValue(out, packed);
				packed = 0;
				count = 0;
			}
		}
		if (count > 0) {
			ret += writeValue(out, packed);
		}
		return ret;
	}
}
