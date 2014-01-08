package com.sk.cache.wrappers.loaders;

import com.sk.cache.fs.CacheSystem;
import com.sk.cache.fs.FileData;
import com.sk.cache.wrappers.ObjectDefinition;

public class ObjectDefinitionLoader extends ProtocolWrapperLoader<ObjectDefinition> {

	public ObjectDefinitionLoader(CacheSystem cacheSystem) {
		super(cacheSystem, cacheSystem.getCacheSource().getCacheType(16));
	}

	@Override
	public ObjectDefinition load(int id) {
		FileData data = getValidFile(id);
		ObjectDefinition ret = new ObjectDefinition(this, id);
		ret.decode(data.getDataAsStream());
		fixObject(ret);
		return ret;
	}

	private void fixObject(ObjectDefinition ret) {
		if (ret.walkable2) {
			ret.walkable = false;
			ret.blockType = 0;
		}
	}
}
