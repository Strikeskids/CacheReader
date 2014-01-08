package com.sk.cache.wrappers.loaders;

import com.sk.cache.fs.Archive;
import com.sk.cache.fs.CacheSource;
import com.sk.cache.fs.CacheType;
import com.sk.cache.fs.FileData;
import com.sk.cache.wrappers.Wrapper;
import com.sk.datastream.Stream;

public abstract class RegionDataLoader<T extends Wrapper<?>> extends WrapperLoader {

	protected final CacheType cache;

	public RegionDataLoader(CacheSource cacheSystem) {
		super(cacheSystem);
		this.cache = cacheSystem.getCacheType(5);
	}

	@Override
	public abstract T load(int regionHash);

	public T load(int regionX, int regionY) {
		if (regionX < 0 || regionY < 0 || regionX > 0x7f || regionY > 0x7f) {
			throw new IllegalArgumentException("Bad region coordinates");
		}
		return load(regionX | regionY << 7);
	}

	protected Stream getData(int regionHash, int file) {
		Archive archive = cache.getArchive(regionHash);
		if (archive == null)
			return null;
		FileData data = archive.getFile(file);
		if (data == null)
			return null;
		return data.getDataAsStream();
	}

	@Override
	public boolean canLoad(int id) {
		return getData(id, 0) != null && getData(id, 3) != null;
	}

}
