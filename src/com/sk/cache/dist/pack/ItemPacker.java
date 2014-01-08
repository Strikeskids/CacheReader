package com.sk.cache.dist.pack;

import com.sk.cache.dist.unpack.PackedItem;
import com.sk.cache.wrappers.ItemDefinition;
import com.sk.cache.wrappers.loaders.ItemDefinitionLoader;


public class ItemPacker extends ProtocolPacker<PackedItem> {

	public ItemPacker(ItemDefinitionLoader loader) {
		super(loader, ItemDefinition.class, PackedItem.class);
	}

}
