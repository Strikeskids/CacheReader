package com.sk.cache.fs;
import java.io.IOException;

import com.sk.cache.DataSource;
import com.sk.cache.meta.ArchiveRequest;
import com.sk.cache.meta.ReferenceTable;

public class CacheType {

	private final ReferenceTable table;
	private final IndexFile index;
	private final DataSource source;
	private final int id;

	public CacheType(CacheSystem source, int id) {
		this.source = source.getSourceSystem();
		this.id = id;
		this.table = new ReferenceTable(source, id);
		this.index = new IndexFile(this.source, id);
	}

	public Archive getArchive(int archive) {
		byte[] data = getArchiveData(archive);
		if (data == null)
			return null;
		return new Archive(this, archive, data);
	}

	private byte[] getArchiveData(int archive) {
		ArchiveRequest query = index.getArchiveMeta(archive);
		if (query == null)
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
