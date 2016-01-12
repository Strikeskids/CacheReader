package com.sk.cache.wrappers;

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
