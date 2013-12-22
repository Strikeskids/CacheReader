package com.sk.dist.unpack;

import java.lang.reflect.Field;

public class ProtocolField implements Comparable<ProtocolField> {

	private final ProtocolType type;
	private final Field field;

	public ProtocolField(ProtocolType type, Field field) {
		this.type = type;
		this.field = field;
	}

	public Field getField() {
		return field;
	}

	public ProtocolType getType() {
		return type;
	}

	@Override
	public int compareTo(ProtocolField o) {
		int dif = 0;
		if (dif == 0)
			dif = this.type.compareTo(o.type);
		if (dif == 0)
			dif = this.field.getName().compareTo(o.field.getName());
		return dif;
	}

	@Override
	public String toString() {
		return type.name() + " " + field.toString();
	}
}
