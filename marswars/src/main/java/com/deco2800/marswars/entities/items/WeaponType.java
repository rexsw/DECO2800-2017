package com.deco2800.marswars.entities.items;

/**
 * Enumerate to store the meta data for all the specific Weapon type items.
 * 
 * Meta Data details: 
 * name = the item's name
 * baseDamage = amount of damage (normal and armour damage) to be added on without any multipliers being applied.
 * baseRange = the amount of distance between the target and the hero spacmen. This is addedn o without any multipliers. 
 * baseSpeed = amount of ms attack speed it changes without any multipliers being applied
 * baseCost = the resource costs for the item without any multipliers being applied where order of the resources in the
 * array is [rocks, crystals, water, biomass].
 * ratio = the multiplier/ratio for the increased stat changes and cost for upgrades
 * 
 * @author Michael
 *
 */
public enum WeaponType {
	// damage, range, speed(100 is standard speed, less is faster, more is
	// slower), cost(rcwb), level

	// assault blasters
	WEAPON1("W1", 10, 3, 90, new int[] { 20, 20, 0, 0 }, 1.3f),

	// sniper rifles
	WEAPON2("W2", 20, 5, 110, new int[] { 30, 30, 0, 0 }, 1.5f);


	private String name;
	private int baseDamage;
	private int[] baseCost;
	private int baseRange;
	private int baseSpeed;
	private float ratio;

	WeaponType(String name, int baseDamage, int baseRange, int baseSpeed,
			int[] baseCost, float ratio) {
		this.name = name;
		this.baseDamage = baseDamage;
		this.baseRange = baseRange;
		this.baseSpeed = baseSpeed;
		this.baseCost = baseCost;
		this.ratio = ratio;
	}
	
	/**
	 * Helper method to calculate the rounded integer stat changes or upgrade costs based on the item's current level 
	 * and the item's ratio field. Result is calculated by multiplying the stat change or cost by the ratio 
	 * level - 1 times. 
	 * 
	 * @param change The stat change or the cost to be 
	 * @param lvl the current level of the item.
	 * @return rounded integer of the stat change or cost after applying the multiplier
	 */
	private int applyUpgrateRatio(int change, int lvl) {
		return (int) Math.round(change * Math.pow(ratio, (lvl - 1)));
	}

	/**
	 * Gets the item name
	 * 
	 * @return string that is the item name
	 */
	String getName() {
		return this.name;
	}

	/**
	 * Gets the current damage (normal and armour damage) the item will add on. Takes into account the multiplier based 
	 * on the item's current level. 
	 * @param lvl the current level of the item.
	 * @return the current amount of damage (normal and armour damage) that the item will add on.
	 */
	int getWeaponDamage(int lvl) {
		return applyUpgrateRatio(this.baseDamage, lvl);
	}

	/**
	 * Gets the current attack range the item will add on. Takes into account the multiplier based on the item's current
	 * level. 
	 * @param lvl the current level of the item.
	 * @return the current amount of attack range that the item will add on.
	 */
	int getWeaponRange(int lvl) {
		return applyUpgrateRatio(this.baseRange, lvl);
	}

	/**
	 * Gets the current amount of Attack Speed the item will add on. Takes into account the multiplier based on the 
	 * item's current level. 
	 * @param lvl the current level of the item.
	 * @return the current amount of Attack speed that the item will add on.
	 */
	int getWeaponSpeed(int lvl) {
		return applyUpgrateRatio(this.baseSpeed, lvl);
	}

	/**
	 * Gets the cost of the next upgrade based on the current level.
	 * 
	 * @param lvl current level of the item
	 * @return Array of integers which represent the cost of each resource to upgrade the item to the next level. Order
	 * of the resources in this array is [rocks, crystals, water, biomass].
	 */
	int[] getWeaponCost(int lvl) {
		int[] cost = new int[baseCost.length];
		for (int i = 0; i < baseCost.length; i++) {
			cost[i] = applyUpgrateRatio(baseCost[i], lvl);
		}
		return cost;
	}

}
