package com.sk.cache.dist.pack;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sk.cache.wrappers.ObjectDefinition;
import com.sk.cache.wrappers.region.Flagger;
import com.sk.cache.wrappers.region.LocalObject;
import com.sk.cache.wrappers.region.LocalObjects;
import com.sk.cache.wrappers.region.Region;

public class SanitizedRegion {

	public byte[][][] flags;
	public byte[][] stairs;

	public SanitizedRegion(Region source) {
		initialize(source);
	}

	private void initialize(Region source) {
		LocalObjects objs = source.objects;
		List<LocalObject> removed = new ArrayList<>();
		Set<LocalObject> upstairs = new HashSet<>();
		Set<LocalObject> downstairs = new HashSet<>();
		for (LocalObject obj : objs.getObjects()) {
			ObjectDefinition def = source.getLoader().objectDefinitionLoader.load(obj.id);
			if (checkName(def, names) && checkActions(def, actions)) {
				removed.add(obj);
				Flagger flagger = obj.createFlagger(source);
				if (flagger != null)
					flagger.unflag(source);
			}
			if (checkName(def, floorNames) && checkActions(def, "climb-up")) {
				Dimension size = obj.getSize();
				if (size == null)
					continue;
				LocalObject opposite = null;
				outer: for (int x = 0; x < size.width; ++x) {
					for (int y = 0; y < size.height; ++y) {
						for (LocalObject possible : objs.getObjectsAt(x + obj.x, y + obj.y, obj.plane + 1)) {
							ObjectDefinition pdef = source.getLoader().objectDefinitionLoader.load(possible.id);
							if (checkName(pdef, floorNames) && checkActions(pdef, "climb-down")
									&& possible.getSize() != null) {
								opposite = possible;
								break outer;
							}
						}
					}
				}
				if (opposite != null) {
					upstairs.add(obj);
					downstairs.add(opposite);
				}
			}
		}
		List<byte[]> stairs = new ArrayList<>();
		for (LocalObject o : upstairs) {
			byte flag = 1;
			if (downstairs.contains(o))
				flag |= 2;
			stairs.add(new byte[] { o.x, o.y, (byte) o.getSize().width, (byte) o.getSize().height, flag });
		}
		for (LocalObject o : downstairs) {
			if (upstairs.contains(o))
				continue;
			byte flag = 2;
			stairs.add(new byte[] { o.x, o.y, (byte) o.getSize().width, (byte) o.getSize().height, flag });
		}
		this.stairs = stairs.toArray(new byte[stairs.size()][]);
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

	private static final String[] actions = { "open", "close", "climb", "squeeze" };
	private static final String[] names = { "door", "gate", "stile", "vine" };

	private static final String[] floorNames = { "stair", "ladder" };
}
