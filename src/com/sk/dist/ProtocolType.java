package com.sk.dist;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
	

	public static Map<ProtocolType, List<Field>> extractFields(Class<?> clazz) {
		EnumMap<ProtocolType, List<Field>> ret = new EnumMap<>(ProtocolType.class);
		for (ProtocolType type : ProtocolType.values()) {
			ret.put(type, new ArrayList<Field>());
		}
		for (Field f : clazz.getFields()) {
			for (ProtocolType type : ProtocolType.values()) {
				if (type.isType(f.getType())) {
					ret.get(type).add(f);
				}
			}
		}
		for (List<Field> fields : ret.values()) {
			Collections.sort(fields, new Comparator<Field>() {
				@Override
				public int compare(Field o1, Field o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
		}
		return ret;
	}

	public abstract boolean isType(Class<?> type);
}
