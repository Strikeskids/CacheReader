package com.sk.dist;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ProtocolUnpacker<T extends Packed> extends Unpacker<T> {

	private List<ProtocolField> fields;
	private List<ProtocolField> booleans;

	public ProtocolUnpacker(Class<T> storage, RandomAccessFile src) throws IOException {
		super(storage, src);
		fields = ProtocolType.extractAllFields(storage, false);
		booleans = ProtocolType.BOOLEAN.extractFields(storage);
	}

	@Override
	public T unpack(byte[] input) throws IOException {

		ByteArrayInputStream data = new ByteArrayInputStream(input);
		T ret = null;
		try {
			ret = storage.newInstance();
			if (booleans.size() > 0) {
				unpackBooleans(ret, data);
			}
			for (ProtocolField pf : fields) {
				unpack(ret, pf, data);
			}
		} catch (IllegalAccessException | InstantiationException ignored) {
		}
		return ret;
	}

	private void unpack(Packed store, ProtocolField pf, InputStream input) throws IOException,
			IllegalArgumentException, IllegalAccessException {
		Field field = pf.getField();
		Class<?> type = field.getType();
		Object value = unpack(type, input);

		field.set(store, value);
	}

	private Object unpack(Class<?> type, InputStream input) throws IOException {
		if (ProtocolType.BYTE.isType(type)) {
			return (byte) input.read();
		} else if (ProtocolType.INTEGER.isType(type)) {
			Object ret = readValue(input);
			try {
				return ProtocolType.EXTRACTORS.get(type).invoke(ret);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		} else if (ProtocolType.ARRAY.isType(type)) {
			long size = readValue(input);
			Class<?> subType = type.getComponentType();
			Object ret = Array.newInstance(subType, (int) size);
			for (int i = 0; i < size; ++i) {
				Array.set(ret, i, unpack(subType, input));
			}
			return ret;
		} else if (ProtocolType.STRING.isType(type)) {
			return readString(input);
		}
		return null;
	}

	private void unpackBooleans(Packed pack, InputStream data) throws IllegalArgumentException,
			IllegalAccessException, IOException {
		int passed = 0;
		long value = 0;
		for (ProtocolField bool : booleans) {
			Field field = bool.getField();
			if (passed++ % 62 == 0)
				value = readValue(data);
			field.setBoolean(pack, (value & 1) == 1);
			value >>= 1;
		}
	}

}
