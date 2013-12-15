package com.sk.dist;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sk.wrappers.Wrapper;
import com.sk.wrappers.WrapperLoader;

public class ProtocolPacker<T extends Packed> extends Packer<T> {

	private final List<Field> booleans = new ArrayList<>();
	private final List<Field> strings = new ArrayList<>();
	private final List<Field> integers = new ArrayList<>();
	private final Map<String, Field> sourceFields;

	private void initializeFields() {
		for (Field f : storage.getDeclaredFields()) {
			if (f.getType().equals(boolean.class)) {
				this.booleans.add(f);
			} else if (f.getType().equals(String.class)) {
				this.strings.add(f);
			} else if (f.getType().equals(byte.class) || f.getType().equals(short.class)
					|| f.getType().equals(int.class)) {
				this.integers.add(f);
			}
		}
	}

	public ProtocolPacker(Class<T> storage, RandomAccessFile src) {
		super(storage, src);
		this.sourceFields = null;
		initializeFields();
	}

	public <E extends WrapperLoader> ProtocolPacker(E loader, Class<? extends Wrapper<E>> source, Class<T> storage) {
		super(loader, source, storage);
		this.sourceFields = new HashMap<>();
		for (Field f : source.getDeclaredFields()) {
			sourceFields.put(f.getName(), f);
		}
		initializeFields();
	}

	@Override
	public int pack(Wrapper<?> input, OutputStream output) throws IOException {
		if (!checkWrapper(input))
			throw new IllegalArgumentException("Bad wrapper type");
		int size = 0;
		try {
			if (booleans.size() > 0) {
				size += writeValue(output, packBooleans(input));
			}
			for (Field fint : integers) {
				size += writeValue(output, sourceFields.get(fint.getName()).getInt(input));
			}
			for (Field fstr : strings) {
				size += writeString(output, (String) sourceFields.get(fstr.getName()).get(input));
			}
		} catch (IllegalAccessException ignored) {
		}
		return size;
	}

	private int packBooleans(Wrapper<?> input) throws IllegalArgumentException, IllegalAccessException {
		if (booleans.size() > 30)
			throw new IllegalArgumentException("Too many booleans");
		int ret = 0;
		for (int i = booleans.size() - 1; i >= 0; --i) {
			Field sourceField = sourceFields.get(booleans.get(i).getName());
			boolean value = sourceField.getBoolean(input);
			ret <<= 1;
			ret |= value ? 1 : 0;
		}
		return ret;
	}

	@Override
	public Packed unpack(byte[] input) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(input);
		Packed ret = null;
		try {
			ret = storage.newInstance();
			if (booleans.size() > 0) {
				unpackBooleans(ret, readValue(bais));
			}
			for (Field fint : integers) {
				int value = readValue(bais);
				if (fint.getClass().equals(byte.class)) {
					fint.setByte(ret, (byte) value);
				} else if (fint.getClass().equals(short.class)) {
					fint.setShort(ret, (short) value);
				} else {
					fint.setInt(ret, value);
				}
			}
			for (Field fstr : strings) {
				String value = readString(bais);
				fstr.set(ret, value);
			}
		} catch (IllegalAccessException | InstantiationException ignored) {
		}
		return ret;
	}

	private void unpackBooleans(Packed pack, int value) throws IllegalArgumentException, IllegalAccessException {
		for (Field bool : booleans) {
			bool.setBoolean(pack, (value & 1) == 1);
			value >>= 1;
		}
	}
}
