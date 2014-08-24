package com.sk.cache.fs;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.sk.cache.DataSource;
import com.sk.cache.wrappers.Wrapper;
import com.sk.cache.wrappers.loaders.ItemDefinitionLoader;
import com.sk.cache.wrappers.loaders.LocalObjectLoader;
import com.sk.cache.wrappers.loaders.ModelLoader;
import com.sk.cache.wrappers.loaders.NpcDefinitionLoader;
import com.sk.cache.wrappers.loaders.ObjectDefinitionLoader;
import com.sk.cache.wrappers.loaders.QuestDefinitionLoader;
import com.sk.cache.wrappers.loaders.RegionLoader;
import com.sk.cache.wrappers.loaders.ScriptLoader;
import com.sk.cache.wrappers.loaders.WrapperLoader;

public class CacheSystem {
	private final CacheSource cache;
	private final Map<Type, WrapperLoader<?>> loaderMap = new HashMap<Type, WrapperLoader<?>>();

	public final ItemDefinitionLoader itemLoader;
	public final ObjectDefinitionLoader objectLoader;
	public final ScriptLoader scriptLoader;
	public final RegionLoader regionLoader;
	public final LocalObjectLoader localObjectLoader;
	public final QuestDefinitionLoader questLoader;
	public final NpcDefinitionLoader npcLoader;
	public final ModelLoader modelLoader;

	public CacheSystem(CacheSource cache) {
		this.cache = cache;
		addLoader(itemLoader = new ItemDefinitionLoader(this));
		addLoader(objectLoader = new ObjectDefinitionLoader(this));
		addLoader(scriptLoader = new ScriptLoader(this));
		addLoader(regionLoader = new RegionLoader(this));
		addLoader(localObjectLoader = new LocalObjectLoader(this));
		addLoader(questLoader = new QuestDefinitionLoader(this));
		addLoader(npcLoader = new NpcDefinitionLoader(this));
		addLoader(modelLoader = new ModelLoader(this));
	}

	public CacheSystem(DataSource source) {
		this(new CacheSource(source));
	}

	public CacheSystem(File cacheFolder) throws FileNotFoundException {
		this(new DataSource(cacheFolder));
	}

	public CacheSource getCacheSource() {
		return cache;
	}

	@SuppressWarnings("unchecked")
	public <T extends Wrapper> WrapperLoader<T> getLoader(Class<T> wrapperClass) {
		return (WrapperLoader<T>) loaderMap.get(wrapperClass);
	}
	
	public Collection<WrapperLoader<?>> getLoaders() {
		return loaderMap.values();
	}

	public <T extends Wrapper> void addLoader(WrapperLoader<T> loader) {
		ParameterizedType type = (ParameterizedType) loader.getClass().getGenericSuperclass();
		loaderMap.put(type.getActualTypeArguments()[0], loader);
	}
}
