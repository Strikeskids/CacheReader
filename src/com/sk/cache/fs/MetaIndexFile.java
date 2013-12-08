package com.sk.cache.fs;

import com.sk.cache.DataSource;

public class MetaIndexFile extends IndexFile {

	public MetaIndexFile(DataSource system) {
		super(system, DataSource.META_INDEX_FILE_NUM);
	}
}
