package com.sk.cache.wrappers.loaders;

import com.sk.cache.fs.Archive;
import com.sk.cache.fs.CacheSystem;
import com.sk.cache.fs.FileData;
import com.sk.cache.wrappers.NpcDefinition;

public class NpcDefinitionLoader extends ProtocolWrapperLoader<NpcDefinition> {

	public NpcDefinitionLoader(CacheSystem cacheSystem) {
		super(cacheSystem, cacheSystem.getCacheSource().getCacheType(18));
	}

	@Override
	public NpcDefinition load(int id) {
		FileData data = getValidFile(id);
		NpcDefinition ret = new NpcDefinition(this, id);
		ret.decode(data.getDataAsStream());
		return ret;
	}
	
	@Override
	protected FileData getFile(int id) {
		Archive archive = cache.getArchive(id >>> 7);
		if (archive == null)
			return null;
		return archive.getFile(id & 0x7f);
	}

}
