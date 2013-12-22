package com.sk.dist.pack;

import com.sk.dist.unpack.PackedObject;
import com.sk.wrappers.ObjectDefinition;
import com.sk.wrappers.WrapperLoader;

public class ObjectPacker extends ProtocolPacker<PackedObject> {

	public ObjectPacker(WrapperLoader loader) {
		super(loader, SanitizedObject.class, PackedObject.class);
	}

	@Override
	public SanitizedObject sanitize(Object o) {
		if (o == null || !(o instanceof ObjectDefinition))
			return null;
		return new SanitizedObject((ObjectDefinition) o);
	}

}
