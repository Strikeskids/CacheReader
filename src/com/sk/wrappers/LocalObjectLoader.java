package com.sk.wrappers;

import java.util.ArrayList;
import java.util.List;

import com.sk.cache.fs.Archive;
import com.sk.cache.fs.CacheSystem;
import com.sk.cache.fs.CacheType;
import com.sk.cache.fs.FileData;
import com.sk.datastream.ConcatenatedStream;
import com.sk.datastream.Stream;

public class LocalObjectLoader extends WrapperLoader {

	private final CacheType cache;

	public LocalObjectLoader(CacheSystem cacheSystem) {
		super(cacheSystem);
		this.cache = cacheSystem.getCacheType(5);
	}

	@Override
	public LocalObjects load(int regionHash) {
		Stream data = getData(regionHash);
		LocalObjects ret = new LocalObjects(this, regionHash);
		ret.decode(data);
		return ret;
	}

	public Stream getData(int regionHash) {
		Archive archive = cache.getArchive(regionHash);
		if (archive == null)
			throw new IllegalArgumentException("Bad region hash");
		List<Stream> streams = new ArrayList<>();
		for (int i = 0; i < 3; ++i) {
			FileData file = archive.getFile(i);
			if (file == null)
				break;
			streams.add(file.getDataAsStream());
		}
		return new ConcatenatedStream(streams.toArray(new Stream[streams.size()]));
	}

	public LocalObjects load(int regionX, int regionY) {
		return load((regionX & 0x3f) << 7 | (regionY & 0x3f));
	}

}
