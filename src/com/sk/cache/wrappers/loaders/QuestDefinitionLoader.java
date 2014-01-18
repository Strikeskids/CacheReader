package com.sk.cache.wrappers.loaders;

import com.sk.cache.fs.Archive;
import com.sk.cache.fs.CacheSystem;
import com.sk.cache.fs.FileData;
import com.sk.cache.wrappers.QuestDefinition;

public class QuestDefinitionLoader extends WrapperLoader<QuestDefinition> {

	private final Archive source;

	public QuestDefinitionLoader(CacheSystem cacheSystem) {
		super(cacheSystem);
		source = cacheSystem.getCacheSource().getCacheType(2).getArchive(35);
	}

	@Override
	public QuestDefinition load(int id) {
		FileData data = source.getFile(id);
		if (data == null)
			throw new IllegalArgumentException("Bad quest id");
		QuestDefinition ret = new QuestDefinition(this, id);
		ret.decode(data.getDataAsStream());
		return ret;
	}

	@Override
	public boolean canLoad(int id) {
		return source.getFile(id) != null;
	}

}
