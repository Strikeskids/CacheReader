package com.sk.cache.dist.pack;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.sk.cache.DataSource;
import com.sk.cache.fs.CacheSystem;
import com.sk.cache.wrappers.loaders.ItemDefinitionLoader;
import com.sk.cache.wrappers.loaders.ObjectDefinitionLoader;
import com.sk.cache.wrappers.loaders.RegionLoader;

public class PackDriver {

	private static ZipOutputStream zos;

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		CacheSystem sys = new CacheSystem(DataSource.getDefaultCacheDirectory());
		File out = new File("packed" + System.currentTimeMillis() + ".zip").getAbsoluteFile();
		zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(out)));
		pack(new RegionPacker(new RegionLoader(sys)));
		pack(new ObjectPacker(new ObjectDefinitionLoader(sys)));
		pack(new ItemPacker(new ItemDefinitionLoader(sys)));
		zos.close();
		System.out.println("Finished all packing");
		System.out.println("Took " + (System.currentTimeMillis() - start) + " millis");
		Files.copy(out.toPath(), new File(out.getParent(), "packed.zip").toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	private static void pack(Packer<?> pckr) throws IOException {
		zos.putNextEntry(new ZipEntry(pckr.getStorage().getSimpleName() + ".packed"));
		pckr.pack(zos);
		zos.closeEntry();
	}
}
