package com.sk.cache.meta;

public class FileMeta {

	private int identifier = -1;
	
	private final int id;
	
	public FileMeta(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public int getIdentifier() {
		return identifier;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

}
