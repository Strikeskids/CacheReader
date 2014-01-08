package com.sk.cache.dist.pack;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.sk.cache.DataSource;
import com.sk.cache.fs.CacheSource;
import com.sk.cache.wrappers.loaders.ItemDefinitionLoader;
import com.sk.cache.wrappers.loaders.ObjectDefinitionLoader;
import com.sk.cache.wrappers.loaders.RegionLoader;

public class PackDriver {

	private static ZipOutputStream zos;

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		CacheSource sys = new CacheSource(new DataSource(new File("/Users/Strikeskids/jagexcache/Runescape/LIVE")));
		File out = new File("packed" + System.currentTimeMillis() + ".zip").getAbsoluteFile();
		zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(out)));
		pack(new RegionPacker(new RegionLoader(sys)));
		pack(new ObjectPacker(new ObjectDefinitionLoader(sys)));
		pack(new ItemPacker(new ItemDefinitionLoader(sys)));
		zos.close();
		System.out.println("Finished all packing");
		System.out.println("Took " + (System.currentTimeMillis() - start) + " millis");
		Runtime.getRuntime().exec("cp " + out + " " + new File(out.getParent(), "packed.zip"));
	}

	private static void pack(Packer<?> pckr) throws IOException {
		zos.putNextEntry(new ZipEntry(pckr.getStorage().getSimpleName() + ".packed"));
		pckr.pack(zos);
		zos.closeEntry();
	}
}
