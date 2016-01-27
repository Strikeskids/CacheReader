package com.sk.cache.wrappers;

import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sk.cache.wrappers.loaders.WrapperLoader;
import com.sk.datastream.Stream;

public abstract class ProtocolWrapper extends StreamedWrapper {

	public Map<Integer, Object> attributes = null;
	public static WeakReference<Map<Integer, Object>> decodedParams = new WeakReference<Map<Integer, Object>>(null);

	public ProtocolWrapper(WrapperLoader<?> loader, int id) {
		super(loader, id);
	}

	@Override
	public void decode(Stream stream) {
		int opcode;
		while ((opcode = stream.getUByte()) != 0) {
			decodeOpcode(stream, opcode);
		}
	}

	protected abstract void decodeOpcode(Stream stream, int opcode);

	protected Map<Integer, Object> decodeParams(Stream stream) {
		int h = stream.getUByte();
		Map<Integer, Object> params = new LinkedHashMap<Integer, Object>(h);
		for (int m = 0; m < h; m++) {
			boolean r = stream.getUByte() == 1;
			int key = stream.getUInt24();
			Object value = (r) ? stream.getString() : stream.getInt();
			params.put(key, value);
		}
		decodedParams = new WeakReference<Map<Integer, Object>>(params);
		return params;
	}

}
