package com.sk.dist;

import java.util.ArrayList;
import java.util.List;

import com.sk.wrappers.ObjectDefinition;
import com.sk.wrappers.region.Flagger;
import com.sk.wrappers.region.LocalObject;
import com.sk.wrappers.region.LocalObjects;
import com.sk.wrappers.region.Region;

public class SanitizedRegion {

	public byte[][][] flags;

	public SanitizedRegion(Region source) {
		initialize(source);
	}

	private void initialize(Region source) {
		LocalObjects objs = source.objects;
		List<LocalObject> removed = new ArrayList<>();
		for (LocalObject obj : objs.getObjects()) {
			ObjectDefinition def = source.getLoader().objectDefinitionLoader.load(obj.id);
			if (checkName(def) && checkActions(def)) {
				removed.add(obj);
				Flagger flagger = obj.createFlagger(source);
				if (flagger != null)
					flagger.unflag(source);
			}
		}
		flags = new byte[source.flags.length][Region.width][Region.height];
		for (int plane = 0; plane < source.flags.length; plane++) {
			boolean different = false;
			for (int x = 0; x < source.flags[plane].length; ++x) {
				for (int y = 0; y < source.flags[plane][x].length; ++y) {
					flags[plane][x][y] = (byte) source.flags[plane][x][y];
					if ((source.flags[plane][x][y] & 0x200100) != 0)
						flags[plane][x][y] = -1;
					if (flags[plane][x][y] != 0 && flags[plane][x][y] != -1)
						different = true;
				}
			}
			if (!different)
				flags[plane] = null;
		}
		for (LocalObject o : removed) {
			Flagger f = o.createFlagger(source);
			if (f != null)
				f.flag(source);
		}
	}

	private boolean checkName(ObjectDefinition def) {
		if (def.name == null)
			return false;
		String name = def.name.toLowerCase();
		for (String s : names) {
			if (name.contains(s))
				return true;
		}
		return false;
	}

	private boolean checkActions(ObjectDefinition def) {
		for (String action : def.actions) {
			if (action == null)
				continue;
			action = action.toLowerCase();
			for (String s : actions) {
				if (action.contains(s))
					return true;
			}
		}
		return false;
	}

	private static final String[] actions = { "open", "close", "climb", "squeeze" };
	private static final String[] names = { "door", "gate", "stile" };
}
