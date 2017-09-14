package com.deco2800.marswars.entities.items;

/**
 * Enumerate to store the meta data for all the specific Armour type items.
 * 
 * Meta Data details: 
 * name = the item's name
 * baseArmour = amount of armour (max and current) it changes without any multipliers being applied
 * baseHealth = amount of health (max and current) it changes without any multipliers being applied
 * baseSpeed = amount of movement speed it changes without any multipliers being applied
 * baseCost = the resource costs for the item without any multipliers being applied
 * ratio = the multiplier/ratio for the increased stat changes and cost for upgrades
 * 
 * @author Mason
 *
 */
public enum ArmourType {
	// armour value, cost(rcwb), level
	// organic armour
	ARMOUR1("A1", 15, 30, 10, new int[] { 20, 20, 0, 0 }, 1.3f); //test dummy entry
	// ARMOUR1LEVEL1 (15, new int[]{20, 20, 0, 0}, 1),
	// ARMOUR1LEVEL2 (30, new int[]{40, 40, 10, 10}, 2),
	// ARMOUR1LEVEL3 (45, new int[]{60, 60, 20, 20}, 3),

	// // synthetic armour
	// ARMOUR2LEVEL1 (10, new int[]{0, 0, 30, 30}, 1),
	// ARMOUR2LEVEL2 (20, new int[]{10, 10, 50, 50}, 2),
	// ARMOUR2LEVEL3 (30, new int[]{20, 20, 80, 80}, 3);

	private String name;
	private int baseArmour;
	private int[] baseCost;
	private int baseHealth;
	private int baseSpeed;
	private float ratio;

	ArmourType(String name, int baseArmour, int baseHealth, int baseSpeed, int[] baseCost, float ratio) {
		this.name = name;
		this.baseArmour = baseArmour;
		this.baseHealth = baseHealth;
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
	 * Gets the current amount of Armour (max and current) the item will add on. Takes into account the multiplier based
	 * on the item's current level. 
	 * @param lvl the current level of the item.
	 * @return the current amount of Armour (max and current) that the item will add on.
	 */
	int getArmourValue(int lvl) {
		return applyUpgrateRatio(this.baseArmour, lvl);
	}

	/**
	 * Gets the current amount of Health (max and current) the item will add on. Takes into account the multiplier based
	 * on the item's current level. 
	 * @param lvl the current level of the item.
	 * @return the current amount of Health (max and current) that the item will add on.
	 */
	int getArmourHealth(int lvl) {
		return applyUpgrateRatio(this.baseHealth, lvl);
	}

	/**
	 * Gets the current amount of Movement Speed the item will add on. Takes into account the multiplier based on the 
	 * item's current level. 
	 * @param lvl the current level of the item.
	 * @return the current amount of Armour (max and current) that the item will add on.
	 */
	int getMoveSpeed(int lvl) {
		return applyUpgrateRatio(this.baseSpeed, lvl);
	}

	int[] getArmourCost(int lvl) {
		int[] cost = new int[baseCost.length];
		for (int i = 0; i < baseCost.length; i++) {
			cost[i] = applyUpgrateRatio(baseCost[i], lvl);
		}
		return cost;
	}

}
