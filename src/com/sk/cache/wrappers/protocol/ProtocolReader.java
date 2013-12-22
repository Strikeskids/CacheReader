package com.sk.cache.wrappers.protocol;

import com.sk.datastream.Stream;

public abstract class ProtocolReader {

	public abstract void read(Object destination, int type, Stream s);

	public abstract boolean validateType(int type);

	public abstract void addSelfToGroup(ProtocolGroup group);

}
