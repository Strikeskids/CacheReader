package com.sk.wrappers.protocol;

import com.sk.Debug;
import com.sk.datastream.Stream;

public class ExtraAttributeReader extends ProtocolGroup {

	private static final int TYPE = 249;

	public ExtraAttributeReader(ProtocolReader... reader) {
		for (ProtocolReader r : reader) {
			r.addSelfToGroup(this);
		}
	}

	@Override
	public void read(Object destination, int type, Stream s) {
		int count = s.getUByte();
		StringBuilder output = new StringBuilder();
		for (int attr = 0; attr < count; ++attr) {
			boolean flag = s.getUByte() == 1;
			int index = s.getUInt24();
			if (!super.validateType(index)) {
				formatAttribute(output, index, flag ? s.getString() : s.getInt());
			} else {
				getReader(index).read(destination, index, s);
			}
		}
		if (output.length() > 0) {
			System.out.printf("Skip 249 %d [%s]%n", count, output);
		}
	}

	private void formatAttribute(StringBuilder builder, int index, Object attr) {
		if (Debug.on) {
			if (builder.length() > 0) {
				builder.append(", ");
			}
			builder.append(Integer.toHexString(index));
			builder.append(" ");
			if (attr instanceof Integer) {
				builder.append(Long.toString(((Integer) attr) & Stream.INT_MASK));
			} else {
				builder.append(attr);
			}
		}
	}

	@Override
	public boolean validateType(int type) {
		return type == TYPE;
	}

	@Override
	public void addSelfToGroup(ProtocolGroup group) {
		group.addReader(this, TYPE);
	}

}
