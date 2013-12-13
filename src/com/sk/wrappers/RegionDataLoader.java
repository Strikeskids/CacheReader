package com.sk.wrappers;

import java.util.ArrayList;
import java.util.List;

import com.sk.cache.fs.Archive;
import com.sk.cache.fs.CacheSystem;
import com.sk.cache.fs.CacheType;
import com.sk.cache.fs.FileData;
import com.sk.datastream.ConcatenatedStream;
import com.sk.datastream.Stream;


public abstract class RegionDataLoader<T extends Wrapper<?>> extends WrapperLoader {

	protected final CacheType cache;

	public RegionDataLoader(CacheSystem cacheSystem) {
		super(cacheSystem);
		this.cache = cacheSystem.getCacheType(5);
	}
	
	@Override
	public abstract T load(int regionHash);
	
	public T load(int regionX, int regionY) {
		if (regionX < 0 || regionY < 0 || regionX >= 0x3f || regionY >= 0x3f) {
			throw new IllegalArgumentException("Bad region coordinates");
		}
		return load(regionX | regionY << 7);
	}

	protected Stream getData(int regionHash, int... files) {
		Archive archive = cache.getArchive(regionHash);
		if (archive == null)
			throw new IllegalArgumentException("Bad region hash");
		List<Stream> streams = new ArrayList<>();
		for (int file : files) {
			FileData data = archive.getFile(file);
			if (data == null)
				continue;
			streams.add(data.getDataAsStream());
		}
		return new ConcatenatedStream(streams.toArray(new Stream[streams.size()]));
	}

}
