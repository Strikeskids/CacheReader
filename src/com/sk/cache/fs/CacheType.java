package com.sk.cache.fs;

import java.io.IOException;
import java.util.Map;
import java.util.WeakHashMap;

import com.sk.cache.DataSource;
import com.sk.cache.meta.ArchiveMeta;
import com.sk.cache.meta.ArchiveRequest;
import com.sk.cache.meta.ReferenceTable;

public class CacheType {

	private Map<Integer, Archive> archiveCache = new WeakHashMap<Integer, Archive>();

	private final ReferenceTable table;
	private final IndexFile index;
	private final DataSource source;
	private final int id;

	public CacheType(CacheSource source, int id) {
		this.source = source.getSourceSystem();
		this.id = id;
		this.table = new ReferenceTable(source, id);
		this.index = new IndexFile(this.source, id);
	}

	public Archive getArchive(int archive) {
		Archive ret = archiveCache.get(archive);
		if (ret == null) {
			byte[] data = getArchiveData(archive);
			if (data == null)
				return null;
			ret = new Archive(this, archive, data);
		}
		archiveCache.put(archive, ret);
		return ret;
	}

	private byte[] getArchiveData(int archive) {
		ArchiveRequest query = index.getArchiveMeta(archive);
		ArchiveMeta meta = table.getEntry(archive);
		if (query == null || meta == null)
			return null;
		else
			return source.readArchive(query);
	}

	public ReferenceTable getTable() {
		return table;
	}

	public IndexFile getIndex() {
		return index;
	}

	public int getId() {
		return id;
	}

	public boolean init() {
		try {
			this.getTable().init();
			this.getIndex().init();
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
