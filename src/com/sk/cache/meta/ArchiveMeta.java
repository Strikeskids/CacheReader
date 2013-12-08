package com.sk.cache.meta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArchiveMeta extends FileMeta {

	private static final int WHIRLPOOL_SIZE = 64;

	private int version = -1;
	private int crc;
	private int childCount;
	private byte[] whirlpool;

	private Map<Integer, FileMeta> children = new HashMap<>();
	private List<Integer> childIndexOrder = new ArrayList<>();
	private int maximumKey = -1;

	public ArchiveMeta(int id) {
		super(id);
	}

	public FileMeta getChild(int key) {
		return children.get(key);
	}

	public boolean validateChild(int index) {
		return getChild(index) != null;
	}

	public void putChild(int key, FileMeta entry) {
		children.put(key, entry);
		maximumKey = Math.max(key, maximumKey);
		childIndexOrder.add(key);
	}
	
	public void addChild(int key) {
		putChild(key, new FileMeta(key));
	}
	
	public int getChildIndex(int order) {
		return childIndexOrder.get(order);
	}

	public int getMaximum() {
		return maximumKey + 1;
	}

	public int getSize() {
		return children.size();
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getCrc() {
		return crc;
	}

	public void setCrc(int crc) {
		this.crc = crc;
	}

	public byte[] getAndInitializeWhirlpool() {
		if (whirlpool == null)
			whirlpool = new byte[WHIRLPOOL_SIZE];
		return getWhirlpool();
	}

	public byte[] getWhirlpool() {
		return whirlpool;
	}

	public void setWhirlpool(byte[] whirlpool) {
		if (!verifyWhirlpool(whirlpool))
			throw new IllegalArgumentException();
		this.whirlpool = whirlpool;
	}

	private boolean verifyWhirlpool(byte[] whirlpool) {
		return whirlpool != null && whirlpool.length == WHIRLPOOL_SIZE;
	}

	public int getChildCount() {
		return childCount;
	}

	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}

}
