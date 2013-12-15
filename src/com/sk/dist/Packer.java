package com.sk.dist;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import com.sk.wrappers.Wrapper;
import com.sk.wrappers.WrapperLoader;

public abstract class Packer<T extends Packed> {

	protected final WrapperLoader loader;
	protected final Class<? extends Wrapper<?>> source;
	protected final Class<T> storage;
	protected final int endId;
	private RandomAccessFile src;
	private int metaLength;

	public Packer(Class<T> storage, RandomAccessFile src) {
		this.storage = storage;
		this.loader = null;
		this.source = null;
		this.endId = -1;
		setPackedSource(src);
	}
	
	public <E extends WrapperLoader> Packer(E loader, Class<? extends Wrapper<E>> source, Class<T> storage) {
		this(loader, source, storage, -1);
	}

	public <E extends WrapperLoader> Packer(E loader, Class<? extends Wrapper<E>> source, Class<T> storage,
			int endId) {
		this.loader = loader;
		this.source = source;
		this.storage = storage;
		this.endId = endId;
	}

	public void setPackedSource(RandomAccessFile source) {
		this.src = source;
		try {
			this.metaLength = unpackIndex(source.length() - 4);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Packed unpack(int id) {
		if (this.src == null)
			throw new RuntimeException("Must set packed source first");
		int metaLoc = this.metaLength + id * 4;
		try (RACInputStream indexStream = new RACInputStream(this.src, metaLoc)) {
			if (metaLoc >= this.src.length() - 8)
				return null;
			int startIndex = readIndex(indexStream);
			if (startIndex == -1)
				return null;
			int endIndex;
			while ((endIndex = readIndex(indexStream)) == -1)
				;
			int size = endIndex - startIndex;
			byte[] bytes = new byte[size];
			this.src.seek(startIndex);
			this.src.readFully(bytes);
			Packed ret = unpack(bytes);
			ret.id = id;
			return ret;
		} catch (IOException ex) {
			return null;
		}
	}

	private int unpackIndex(long loc) {
		try {
			return readIndex(new RACInputStream(this.src, loc));
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public void pack(OutputStream output) throws IOException {
		ByteArrayOutputStream indices = new ByteArrayOutputStream();
		int index = 0;
		for (int id = 0; endId == -1 || id < endId; ++id) {
			Wrapper<?> wrap = getWrapper(id);
			if (wrap == null && endId == -1)
				break;
			writeIndex(indices, wrap == null ? -1 : index);
			int count = wrap == null ? 0 : pack(wrap, output);
			index += count;
		}
		writeIndex(indices, index);
		output.write(indices.toByteArray());
		writeIndex(output, indices.size());
	}

	protected int writeIndex(OutputStream out, int id) throws IOException {
		for (int i = 0, shift = 24; i < 4; ++i, shift -= 8) {
			out.write(id >> shift);
		}
		return 4;
	}

	protected int readIndex(InputStream in) throws IOException {
		int ret = 0;
		for (int i = 0; i < 4; ++i) {
			ret <<= 8;
			ret |= in.read();
		}
		return ret;
	}

	protected int writeValue(OutputStream out, int value) throws IOException {
		int size = 1;
		int type = 0;
		for (; (value >>> ((int) size * 8 - 2)) == 0; size *= 2, ++type)
			;
		out.write((value >>> (size - 1) * 8) & 0x3f | type << 6);
		for (int i = 1, shift = (size - 2) * 8; i < size; ++i, shift -= 8) {
			out.write(value >>> shift);
		}
		return size;
	}

	protected int readValue(InputStream in) throws IOException {
		int first = in.read();
		if (first == 0xff)
			return -1;
		int type = (first >> 6) & 0x3;
		int ret = first & ~0xc0;
		int count = (int) Math.pow(type, 2);
		for (int i = 1; i < count; ++i) {
			ret <<= 8;
			ret |= in.read();
		}
		return ret;
	}

	protected String readString(InputStream in) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int curByte;
		while ((curByte = in.read()) != 0 && curByte != -1) {
			baos.write(curByte);
		}
		return new String(baos.toByteArray(), "UTF-8");
	}

	protected int writeString(OutputStream out, String s) throws IOException {
		byte[] bytes = s.getBytes("UTF-8");
		out.write(bytes);
		out.write(0);
		return bytes.length;
	}

	private Wrapper<?> getWrapper(int id) {
		try {
			return loader.load(id);
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	public boolean checkWrapper(Wrapper<?> input) {
		return source.isInstance(input);
	}

	public abstract int pack(Wrapper<?> input, OutputStream output) throws IOException;

	public abstract Packed unpack(byte[] input) throws IOException;

}
