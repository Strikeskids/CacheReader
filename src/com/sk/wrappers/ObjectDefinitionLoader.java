package com.sk.wrappers;

import com.sk.cache.fs.Archive;
import com.sk.cache.fs.CacheSystem;
import com.sk.cache.fs.CacheType;
import com.sk.cache.fs.FileData;

public class ObjectDefinitionLoader extends WrapperLoader {

	private final CacheType cache;

	public ObjectDefinitionLoader(CacheSystem cacheSystem) {
		super(cacheSystem);
		this.cache = cacheSystem.getCacheType(16);
	}

	@Override
	public ObjectDefinition load(int id) {
		FileData data = loadData(id);
		ObjectDefinition ret = new ObjectDefinition(this, id);
		ret.decode(data.getDataAsStream());
		fixObject(ret);
		return ret;
	}

	public FileData loadData(int id) {
		Archive archive = cache.getArchive(id >>> 8);
		if (archive == null)
			throw new IllegalArgumentException("Bad object id");
		FileData data = archive.getFile(id & 0xff);
		if (data == null)
			throw new IllegalArgumentException("Bad object id");
		return data;
	}

	private void fixObject(ObjectDefinition ret) {
		if (ret.walkable2) {
			ret.walkable = false;
			ret.blockType = 0;
		}
	}

}
