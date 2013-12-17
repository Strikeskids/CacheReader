package com.sk.dist;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ProtocolType {
	BOOLEAN {
		@Override
		public boolean isType(Class<?> type) {
			return type.equals(boolean.class) || type.equals(Boolean.class);
		}
	},
	INTEGER {
		private Class<?>[] types = new Class<?>[] { byte.class, short.class, int.class, long.class, Byte.class,
				Short.class, Integer.class, Long.class };

		@Override
		public boolean isType(Class<?> type) {
			for (Class<?> accept : types) {
				if (accept.isAssignableFrom(type))
					return true;
			}
			return false;
		}
	},
	STRING {
		@Override
		public boolean isType(Class<?> type) {
			return String.class.isAssignableFrom(type);
		}
	},
	ARRAY {
		@Override
		public boolean isType(Class<?> type) {
			return type.isArray();
		}
	};

	public List<ProtocolField> extractFields(Class<?> clazz) {
		List<ProtocolField> ret = new ArrayList<>();
		for (Field f : clazz.getDeclaredFields()) {
			if ((f.getModifiers() & (Modifier.STATIC | Modifier.FINAL)) == 0 && this.isType(f.getType()))
				ret.add(new ProtocolField(this, f));
		}
		Collections.sort(ret);
		return ret;
	}

	public static List<ProtocolField> extractAllFields(Class<?> clazz) {
		return extractAllFields(clazz, true);
	}

	public static List<ProtocolField> extractAllFields(Class<?> clazz, boolean includeBoolean) {
		List<ProtocolField> ret = new ArrayList<>();
		for (ProtocolType type : values()) {
			if (includeBoolean || type != ProtocolType.BOOLEAN)
				ret.addAll(type.extractFields(clazz));
		}
		return ret;
	}

	public static Map<Class<?>, Method> EXTRACTORS = new HashMap<>();
	static {
		try {
			EXTRACTORS.put(byte.class, Number.class.getMethod("byteValue"));
			EXTRACTORS.put(short.class, Number.class.getMethod("shortValue"));
			EXTRACTORS.put(int.class, Number.class.getMethod("intValue"));
			EXTRACTORS.put(long.class, Number.class.getMethod("longValue"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public abstract boolean isType(Class<?> type);
}
