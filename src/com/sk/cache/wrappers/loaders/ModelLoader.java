package com.sk.cache.wrappers.loaders;

import com.sk.cache.fs.CacheSystem;
import com.sk.cache.fs.CacheType;
import com.sk.cache.wrappers.Model;
import com.sk.datastream.Stream;

public class ModelLoader extends WrapperLoader<Model> {

	private final CacheType cache;
	
	public ModelLoader(CacheSystem sys) {
		super(sys);
		this.cache = sys.getCacheSource().getCacheType(7);
	}
	
	@Override
	public Model load(int id) {
		Stream data = cache.getArchive(id).getFile(0).getDataAsStream();
		Model ret = new Model(this, id);
		ret.decode(data);
		return ret;
	}

	@Override
	public boolean canLoad(int id) {
		return cache.getArchive(id) != null;
	}

}
