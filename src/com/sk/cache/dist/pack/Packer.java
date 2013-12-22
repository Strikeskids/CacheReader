package com.sk.cache.dist.pack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;

import com.sk.cache.dist.unpack.Packed;
import com.sk.cache.wrappers.WrapperLoader;

public abstract class Packer<T extends Packed> {

	private final WrapperLoader loader;
	private final Class<?> source;
	private final Class<T> storage;
	protected final int endId;

	public Packer(WrapperLoader loader, Class<?> source, Class<T> storage) {
		this(loader, source, storage, -1);
	}

	public Packer(WrapperLoader loader, Class<?> source, Class<T> storage, int endId) {
		this.loader = loader;
		this.source = source;
		this.storage = storage;
		this.endId = endId;
	}

	public void pack(OutputStream output) throws IOException {
		System.out.println("Packing " + getSource().getName());
		ByteArrayOutputStream indices = new ByteArrayOutputStream();
		ByteArrayOutputStream wrapperStream = new ByteArrayOutputStream();
		int index = 0;
		Deflater compressor = new Deflater();
		byte[] tempStore = new byte[8192];
		for (int id = 0; endId == -1 || id < endId; ++id) {
			if (id != 0 && id % 10 == 0)
				System.out.println();
			System.out.print(id + " ");
			Object wrap = getWrapper(id);
			if (wrap == null && endId == -1)
				break;
			wrap = sanitize(wrap);
			if (!checkInput(wrap))
				throw new RuntimeException("Bad input object at index " + id);
			wrapperStream.reset();
			int count = wrap == null ? 0 : pack(wrap, wrapperStream);
			if (count > 500) {
				compressor.reset();
				compressor.setInput(wrapperStream.toByteArray());
				compressor.finish();
				int compressed;
				int totalSize = 0;
				while ((compressed = compressor.deflate(tempStore)) != 0 || !compressor.needsInput()) {
					output.write(tempStore, 0, compressed);
					totalSize += compressed;
				}
				System.out.print("(* " + index + ") ");
				writeIndex(indices, index | 0x80000000);
				index += totalSize;
			} else {
				output.write(wrapperStream.toByteArray());
				writeIndex(indices, wrap == null ? -1 : index);
				index += count;
			}
		}
		System.out.println();
		System.out.println("Finished packing");
		writeIndex(indices, index);
		output.write(indices.toByteArray());
		writeIndex(output, indices.size());
	}

	protected int writeIndex(OutputStream out, int id) throws IOException {
		for (int i = 0, shift = 24; i < 4; ++i, shift -= 8) {
			int toWrite = (id >>> shift) & 0xff;
			out.write(toWrite);
		}
		return 4;
	}

	protected int writeValue(OutputStream out, long value) throws IOException {
		if (value == -1) {
			out.write(0xff);
			return 1;
		} else if (value <= 0x3f) {
			out.write((int) value);
			return 1;
		}
		int size = 1;
		int type = 0;
		for (; (value >>> (size * 8 - 2)) != 0; size *= 2, ++type) {
			int shift = size * 8 - 2;
			long shifted = value >>> shift;
			if (shifted == 0)
				break;
		}
		int keyByte = (int) ((value >>> (size - 1) * 8) & 0x3f) | type << 6;
		out.write(keyByte);
		for (int i = 1, shift = (size - 2) * 8; i < size; ++i, shift -= 8) {
			out.write((int) (value >>> shift));
		}
		return size;
	}

	protected int writeString(OutputStream out, String s) throws IOException {
		if (s == null)
			s = "";
		byte[] bytes = s.getBytes("UTF-8");
		out.write(bytes);
		out.write(0);
		return bytes.length + 1;
	}

	private Object getWrapper(int id) {
		if (!getLoader().canLoad(id))
			return null;
		try {
			return getLoader().load(id);
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	public boolean checkInput(Object wrap) {
		return wrap == null || getSource().isInstance(wrap);
	}

	public Object sanitize(Object wrap) {
		return wrap;
	}

	public abstract int pack(Object input, OutputStream output) throws IOException;

	public Class<T> getStorage() {
		return storage;
	}

	public Class<?> getSource() {
		return source;
	}

	public WrapperLoader getLoader() {
		return loader;
	}

}
