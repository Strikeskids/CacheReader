package com.sk.datastream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ConcatenatedStream extends ByteStream {

	public ConcatenatedStream(Stream... inputs) {
		super(concat(inputs));
	}

	private static byte[] concat(Stream... streams) {
		ByteArrayOutputStream concatenator = new ByteArrayOutputStream(streams.length * 10);
		for (Stream s : streams) {
			try {
				concatenator.write(s.getAllBytes());
			} catch (IOException impossible) {
			}
		}
		return concatenator.toByteArray();
	}

}
