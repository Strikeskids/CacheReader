package com.sk.cache.wrappers.loaders;

import java.util.Arrays;

import com.sk.cache.fs.CacheSystem;
import com.sk.cache.fs.FileData;
import com.sk.cache.wrappers.ItemDefinition;

public class ItemDefinitionLoader extends ProtocolWrapperLoader<ItemDefinition> {

	private static final String[] antiEdible = { "Burnt", "Rotten", "Poison", "Fish-like thing",
			"Dwarven rock cake" };

	public ItemDefinitionLoader(CacheSystem cacheSystem) {
		super(cacheSystem, cacheSystem.getCacheSource().getCacheType(19));
	}

	@Override
	public ItemDefinition load(int id) {
		FileData data = getValidFile(id);
		ItemDefinition ret = new ItemDefinition(this, id);
		ret.decode(data.getDataAsStream());
		fixItem(ret);
		return ret;
	}

	private void fixItem(ItemDefinition item) {
		if (item.lentTemplateId != -1 && item.lentId != -1) {
			fixLentItem(item);
		}
		if (item.noteTemplateId != -1) {
			fixNotedItem(item);
		}
		if (item.cosmeticTemplateId != -1 ) {
			fixCosmeticItem(item);
		}
		item.edible = isEdible(item);
	}
	
	private void fixCosmeticItem(ItemDefinition item) {
		fixItem(item, item.cosmeticId);
		item.cosmetic = true;
	}

	private void fixLentItem(ItemDefinition item) {
		fixItem(item, item.lentId);
		item.lent = true;
	}
	
	private void fixItem(ItemDefinition item, int sourceId) {
		final ItemDefinition source = this.load(sourceId);
		item.groundActions = source.groundActions;
		item.actions = source.actions;
		item.name = source.name;
		item.members = source.members;
		item.value = 0;
		item.team = source.team;
		item.actions[4] = "Discard";
	}

	private void fixNotedItem(ItemDefinition item) {
		final ItemDefinition note = this.load(item.noteId);
		item.value = note.value;
		item.name = note.name;
		item.stackable = true;
		item.members = note.members;
		item.noted = true;
	}

	private boolean isEdible(ItemDefinition item) {
		if (Arrays.asList(item.actions).contains("Eat") && item.name != null) {
			for (String s : antiEdible) {
				if (item.name.contains(s)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
