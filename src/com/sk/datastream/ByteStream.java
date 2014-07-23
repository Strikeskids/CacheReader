package com.sk.datastream;


public class ByteStream extends Stream {

	private final byte[] payload;
	private int offset;

	public ByteStream(final byte[] payload) {
		if (payload == null || payload.length == 0)
			throw new IllegalArgumentException();
		this.payload = payload;
	}

	@Override
	public int getLocation() {
		return offset;
	}

	@Override
	public int getLength() {
		return payload.length;
	}

	@Override
	public byte getByte() {
		assertLeft(1);
		return payload[offset++];
	}

	@Override
	public void seek(int loc) {
		offset = loc;
	}

	@Override
	public byte[] getAllBytes() {
		return payload;
	}

}
