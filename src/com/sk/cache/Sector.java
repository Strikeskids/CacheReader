package com.sk.cache;

public class Sector {

	public static final int HEADER_SIZE = 8, DATA_SIZE = 512, SIZE = HEADER_SIZE + DATA_SIZE;

	private final int id, next;
	private final int type, fileId, fileChunk;
	private final byte[] data;
	private final int dataOffset;

	public Sector(int id, int type, int fileId, int fileChunk, int next, byte[] data, int dataOffset) {
		this.id = id;
		this.next = next;
		this.type = type;
		this.fileId = fileId;
		this.fileChunk = fileChunk;
		this.data = data;
		this.dataOffset = dataOffset;
	}

	public int getId() {
		return id;
	}

	public int getNextSector() {
		return next;
	}

	public int getCacheType() {
		return type;
	}

	public int getFileId() {
		return fileId;
	}

	public int getFileChunk() {
		return fileChunk;
	}

	public byte[] getData() {
		return data;
	}

	public int getDataOffset() {
		return dataOffset;
	}

}
