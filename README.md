CacheReader
===========

Reads the jagexcache

Look in wrappers for wrappers

Basically
```
DataSource ds = new DataSource(jagex_cache_dir);
CacheSystem cache = new CacheSystem(ds);
ItemDefinitionLoader loader = new ItemDefinitionLoader(cache);

loader.load(item_id);
```

Code should be easy to read and self-explanatory