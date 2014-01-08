package com.sk.cache.dist.pack;

import com.sk.cache.dist.unpack.PackedObject;
import com.sk.cache.wrappers.ObjectDefinition;
import com.sk.cache.wrappers.loaders.ObjectDefinitionLoader;

public class ObjectPacker extends ProtocolPacker<PackedObject> {

	public ObjectPacker(ObjectDefinitionLoader loader) {
		super(loader, SanitizedObject.class, PackedObject.class);
	}

	@Override
	public SanitizedObject sanitize(Object o) {
		if (o == null || !(o instanceof ObjectDefinition))
			return null;
		return new SanitizedObject((ObjectDefinition) o);
	}

}
