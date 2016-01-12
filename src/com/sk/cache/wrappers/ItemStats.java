package com.sk.cache.wrappers;

import java.util.Map;

public class ItemStats {
	public int attackSpeed;

	public int meleeAccuracy;
	public int rangedAccuracy;
	public int magicAccuracy;

	public int meleeDamage;
	public int rangedDamage;
	public int magicDamage;

	public int meleeCritical;
	public int rangedCritical;
	public int magicCritical;

	public int armor;
	public int lifepoints;
	public int prayer;

	public CombatStyle style = CombatStyle.UNKNOWN;

	private Map<Integer, Object> params;

	private int getIntegerParam(int param) {
		Integer cur = (Integer) params.get(param);
		if (cur != null) {
			return cur;
		}
		return 0;
	}

	public void load(Map<Integer, Object> params) {
		this.params = params;
		magicAccuracy = getIntegerParam(3);
		rangedAccuracy = getIntegerParam(4);
		attackSpeed = getIntegerParam(14);
		meleeDamage = getIntegerParam(641);
		rangedDamage = getIntegerParam(643);
		magicDamage = getIntegerParam(965);
		lifepoints = getIntegerParam(1326);
		int melee = getIntegerParam(2821) | getIntegerParam(2825);
		int range = getIntegerParam(2822) | getIntegerParam(2826);
		int magic = getIntegerParam(2823) | getIntegerParam(2827);
		int neutral = getIntegerParam(2824);
		if (melee != 0 && (range | magic | neutral) == 0) {
			style = CombatStyle.MELEE;
		} else if (range != 0 && (melee | magic | neutral) == 0) {
			style = CombatStyle.RANGED;
		} else if (magic != 0 && (melee | range | neutral) == 0) {
			style = CombatStyle.MAGIC;
		} else if (neutral != 0 && (melee | range | magic) == 0) {
			style = CombatStyle.NEUTRAL;
		}
		meleeCritical = getIntegerParam(2833);
		rangedCritical = getIntegerParam(2834);
		magicCritical = getIntegerParam(2835);
		armor = getIntegerParam(2870);
		prayer = getIntegerParam(2946);
		meleeAccuracy = getIntegerParam(3267);
		this.params = null;
	}

	public boolean isDefault() {
		return (attackSpeed | meleeAccuracy | rangedAccuracy | magicAccuracy | meleeCritical | rangedCritical
				| magicCritical | meleeDamage | rangedDamage | magicDamage | armor | lifepoints | prayer) != 0
				|| style != CombatStyle.UNKNOWN;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ItemStats [attackSpeed=");
		builder.append(attackSpeed);
		builder.append(", meleeAccuracy=");
		builder.append(meleeAccuracy);
		builder.append(", rangedAccuracy=");
		builder.append(rangedAccuracy);
		builder.append(", magicAccuracy=");
		builder.append(magicAccuracy);
		builder.append(", meleeDamage=");
		builder.append(meleeDamage);
		builder.append(", rangedDamage=");
		builder.append(rangedDamage);
		builder.append(", magicDamage=");
		builder.append(magicDamage);
		builder.append(", meleeCritical=");
		builder.append(meleeCritical);
		builder.append(", rangedCritical=");
		builder.append(rangedCritical);
		builder.append(", magicCritical=");
		builder.append(magicCritical);
		builder.append(", armor=");
		builder.append(armor);
		builder.append(", lifepoints=");
		builder.append(lifepoints);
		builder.append(", prayer=");
		builder.append(prayer);
		builder.append(", style=");
		builder.append(style);
		builder.append("]");
		return builder.toString();
	}

	static enum CombatStyle {
		MELEE, RANGED, MAGIC, NEUTRAL, UNKNOWN;
	}
}
