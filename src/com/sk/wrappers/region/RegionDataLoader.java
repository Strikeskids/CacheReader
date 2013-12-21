package com.sk.wrappers.region;

import com.sk.cache.fs.Archive;
import com.sk.cache.fs.CacheSystem;
import com.sk.cache.fs.CacheType;
import com.sk.cache.fs.FileData;
import com.sk.datastream.Stream;
import com.sk.wrappers.Wrapper;
import com.sk.wrappers.WrapperLoader;


public abstract class RegionDataLoader<T extends Wrapper<?>> extends WrapperLoader {

	protected final CacheType cache;

	public RegionDataLoader(CacheSystem cacheSystem) {
		super(cacheSystem);
		this.cache = cacheSystem.getCacheType(5);
	}
	
	@Override
	public abstract T load(int regionHash);
	
	public T load(int regionX, int regionY) {
		if (regionX < 0 || regionY < 0 || regionX > 0x3f || regionY > 0x3f) {
			throw new IllegalArgumentException("Bad region coordinates");
		}
		return load(regionX | regionY << 7);
	}

	protected Stream getData(int regionHash, int file) {
		Archive archive = cache.getArchive(regionHash);
		if (archive == null)
			throw new IllegalArgumentException("Bad region hash");
		FileData data = archive.getFile(file);
		if (data == null)
			return null;
		return data.getDataAsStream();
	}

}
