package com.sk.dist;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class RACInputStream extends InputStream {

	private long location;
	private final RandomAccessFile rac;

	public RACInputStream(RandomAccessFile rac, long loc) {
		this.rac = rac;
		this.location = loc;
	}

	@Override
	public int read() throws IOException {
		if (rac.getFilePointer() != location) {
			rac.seek(location);
		}
		location++;
		return rac.read();
	}

}
