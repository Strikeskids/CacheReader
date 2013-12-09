package com.sk.wrappers;

import java.util.Arrays;

import com.sk.cache.fs.Archive;
import com.sk.cache.fs.CacheSystem;
import com.sk.cache.fs.CacheType;
import com.sk.cache.fs.FileData;

public class ItemDefinitionLoader extends WrapperLoader {

	private static final String[] antiEdible = { "Burnt", "Rotten", "Poison", "Fish-like thing",
			"Dwarven rock cake" };

	private CacheType cache;

	public ItemDefinitionLoader(CacheSystem cacheSystem) {
		super(cacheSystem);
		this.cache = cacheSystem.getCacheType(19);
	}

	@Override
	public ItemDefinition load(int id) {
		FileData data = loadData(id);
		ItemDefinition ret = new ItemDefinition(this, id);
		ret.decode(data.getDataAsStream());
		fixItem(ret);
		return ret;
	}

	private FileData loadData(int id) {
		Archive archive = cache.getArchive(id >>> 8);
		if (archive == null)
			throw new IllegalArgumentException("Bad item id");
		FileData data = archive.getFile(id & 0xff);
		if (data == null)
			throw new IllegalArgumentException("Bad item id");
		return data;
	}

	private void fixItem(ItemDefinition item) {
		if (item.lentTemplateId != -1 && item.lentId != -1) {
			fixLentItem(item);
		}
		if (item.noteTemplateId != -1) {
			fixNotedItem(item);
		}
		item.edible = isEdible(item);
	}

	private void fixLentItem(ItemDefinition item) {
		final ItemDefinition lent = this.load(item.lentId);
		item.groundActions = lent.groundActions;
		item.actions = lent.actions;
		item.name = lent.name;
		item.members = lent.members;
		item.value = 0;
		item.team = lent.team;
		item.actions[4] = "Discard";
		item.lent = true;
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
