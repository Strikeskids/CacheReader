package com.sk.cache.wrappers.loaders;

import com.sk.cache.fs.Archive;
import com.sk.cache.fs.CacheSystem;
import com.sk.cache.fs.CacheType;
import com.sk.cache.fs.FileData;
import com.sk.cache.wrappers.CacheImage;

public class ImageLoader extends WrapperLoader<CacheImage> {

	private CacheType cache;

	public ImageLoader(CacheSystem cacheSystem) {
		super(cacheSystem);
		cache = cacheSystem.getCacheSource().getCacheType(8);
	}

	@Override
	public CacheImage load(int id) {
		Archive archive = cache.getArchive(id);
		if (archive == null)
			return null;
		FileData file = archive.getFile(0);
		if (file == null)
			return null;
		CacheImage ret = new CacheImage(this, id);
		ret.decode(file.getDataAsStream());
		return ret;
	}

	@Override
	public boolean canLoad(int id) {
		Archive arch = cache.getArchive(id);
		return arch != null && arch.getFile(0) != null;
	}

}
