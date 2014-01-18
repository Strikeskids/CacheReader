package com.sk.cache.fs;
import java.util.HashMap;
import java.util.Map;

import com.sk.cache.meta.ArchiveMeta;
import com.sk.datastream.ByteStream;
import com.sk.datastream.Stream;

public class Archive {
	private final int id;
	private final ArchiveMeta metaData;
	private final CacheType cache;

	private final byte[] wrapped;

	private Stream data;
	private int partitions;
	private int sizeMetaDataStart;

	private Map<Integer, FileData> children;

	public Archive(CacheType cache, int id, byte[] wrapped) {
		this.id = id;
		this.cache = cache;

		this.metaData = cache.getTable().getEntry(id);
		if (this.metaData == null)
			throw new IllegalArgumentException("Bad archive id");

		this.wrapped = wrapped;
		if (wrapped == null || wrapped.length == 0)
			throw new IllegalArgumentException("Bad archive data");
	}

	public int getId() {
		return id;
	}

	public CacheType getCache() {
		return cache;
	}

	public boolean validateFileIndex(int index) {
		return metaData.validateChild(index);
	}

	public FileData getFile(int index) {
		if (!validateFileIndex(index))
			return null;
		if (children == null) {
			unpack();
		}
		return children.get(index);
	}

	private void addFile(FileData data) {
		this.children.put(data.getId(), data);
	}

	private synchronized void unpack() {
		if (children != null)
			return;
		children = new HashMap<>();
		
		createDecompressedStream();
		
		if (metaData.getChildCount() > 1) {
			unpackMultipleChildren();
		} else {
			unpackSingleChild();
		}
		
		data = null;
	}
	
	private void unpackSingleChild() {
		addFile(data.getAllBytes(), 0);
	}
	
	private void unpackMultipleChildren() {
		getPartitions();
		byte[][] childBuffers = initializeTemporaryChildBuffers();
		transferDataToBuffers(childBuffers);
		createFiles(childBuffers);
	}

	private void createDecompressedStream() {
		byte[] decompressed = new ByteStream(wrapped).decompress();
		data = new ByteStream(decompressed);
	}

	private void getPartitions() {
		data.seek(data.getLength() - 1);
		partitions = data.getUByte();
		sizeMetaDataStart = data.getLength() - 1 - Stream.INT_SIZE / 8 * partitions * metaData.getChildCount();
	}

	private byte[][] initializeTemporaryChildBuffers() {
		byte[][] bufs = new byte[metaData.getChildCount()][];
		int[] sizes = getSizes();
		for (int i = 0; i < bufs.length; ++i) {
			bufs[i] = new byte[sizes[i]];
		}
		return bufs;
	}

	private int[] getSizes() {
		data.seek(sizeMetaDataStart);
		int[] sizes = new int[metaData.getChildCount()];
		for (int partition = 0; partition < partitions; ++partition) {
			int accum = 0;
			for (int i = 0; i < sizes.length; ++i) {
				accum += data.getInt();
				sizes[i] += accum;
			}
		}
		return sizes;
	}

	private void transferDataToBuffers(byte[][] buffers) {
		data.seek(sizeMetaDataStart);
		int[] offsets = new int[metaData.getChildCount()];
		for (int partition = 0; partition < partitions; ++partition) {
			int blockSize = 0;
			int sourceOffset = 0;
			for (int i = 0; i < offsets.length; ++i) {
				blockSize += data.getInt();
				System.arraycopy(data.getAllBytes(), sourceOffset, buffers[i], offsets[i], blockSize);
				offsets[i] += blockSize;
				sourceOffset += blockSize;
			}
		}
	}
	
	private void createFiles(byte[][] buffers) {
		for (int i = 0; i < buffers.length; ++i) {
			addFile(buffers[i], i);
		}
	}
	
	private void addFile(byte[] data, int i) {
		int index = metaData.getChildIndex(i);
		addFile(new FileData(this, index, metaData.getChild(index).getIdentifier(), data));
	}
}
