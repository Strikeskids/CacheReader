package com.sk.dist;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum ProtocolType {
	BOOLEAN {
		@Override
		public boolean isType(Class<?> type) {
			return type.equals(boolean.class);
		}
	},
	INTEGER {
		@Override
		public boolean isType(Class<?> type) {
			return type.equals(byte.class) || type.equals(short.class) || type.equals(int.class)
					|| type.equals(long.class);
		}
	},
	STRING {
		@Override
		public boolean isType(Class<?> type) {
			return type.equals(String.class);
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
		for (Field f : clazz.getFields()) {
			if (this.isType(f.getType()))
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

	public abstract boolean isType(Class<?> type);
}
