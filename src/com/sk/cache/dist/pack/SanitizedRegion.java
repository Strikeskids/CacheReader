package com.sk.cache.dist.pack;

import java.util.ArrayList;
import java.util.List;

import com.sk.cache.dist.unpack.PackedRegion;
import com.sk.cache.wrappers.ObjectDefinition;
import com.sk.cache.wrappers.region.Flagger;
import com.sk.cache.wrappers.region.LocalObject;
import com.sk.cache.wrappers.region.Region;

public class SanitizedRegion extends PackedRegion {

	private Region source;
	private List<LocalObject> removed;

	public SanitizedRegion(Region source) {
		this.source = source;
		removed = new ArrayList<>();
		simplify();
		this.source = null;
		this.removed = null;
	}

	private void simplify() {
		for (LocalObject obj : source.objects.getObjects()) {
			if (isPassable(obj)) {
				addPassableObject(obj);
			}
		}
		initializeFlags();
		resetSourceFlags();
	}

	private void initializeFlags() {
		flags = new byte[source.flags.length][Region.width][Region.height];
		for (int plane = 0; plane < source.flags.length; plane++) {
			boolean different = false;
			for (int x = 0; x < source.flags[plane].length; ++x) {
				for (int y = 0; y < source.flags[plane][x].length; ++y) {
					flags[plane][x][y] = (byte) source.flags[plane][x][y];
					if ((source.flags[plane][x][y] & 0x200100) != 0)
						flags[plane][x][y] = -1;
					if (flags[plane][x][y] != 0)
						different = true;
				}
			}
			if (!different)
				flags[plane] = null;
		}
	}

	private void resetSourceFlags() {
		for (LocalObject o : removed) {
			flagSource(o);
		}
	}

	private boolean flagSource(LocalObject obj) {
		Flagger flagger = obj.createFlagger(source);
		if (flagger != null) {
			flagger.flag(source);
			return true;
		}
		return false;
	}

	private boolean unflagSource(LocalObject obj) {
		Flagger flagger = obj.createFlagger(source);
		if (flagger != null) {
			flagger.unflag(source);
			return true;
		}
		return false;
	}

	private void addPassableObject(LocalObject obj) {
		removed.add(obj);
		unflagSource(obj);
	}

	private boolean isPassable(LocalObject obj) {
		return isOfType(obj, PASS_NAMES, PASS_ACTIONS);
	}

	private boolean isOfType(LocalObject obj, String[] names, String... actions) {
		ObjectDefinition def = obj.getDefinition();
		return def != null && (names.length == 0 || checkName(def, names))
				&& (actions.length == 0 || checkActions(def, actions));
	}

	private boolean checkName(ObjectDefinition def, String... names) {
		if (def.name == null)
			return false;
		String name = def.name.toLowerCase();
		for (String s : names) {
			if (name.contains(s))
				return true;
		}
		return false;
	}

	private boolean checkActions(ObjectDefinition def, String... actions) {
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

	private static final String[] PASS_ACTIONS = { "open", "close", "climb", "squeeze" };
	private static final String[] PASS_NAMES = { "door", "gate", "stile", "vine" };

}
