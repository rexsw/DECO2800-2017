package com.deco2800.marswars.entities.items;

/**
 * Enumerate to store the meta data for all the specific Armour type items.
 * 
 * Meta Data details: 
 * name = the item's name
 * baseArmour = amount of armour (max and current) it changes without any multipliers being applied
 * baseHealth = amount of health (max and current) it changes without any multipliers being applied
 * baseSpeed = amount of movement speed it changes without any multipliers being applied
 * baseCost = the resource costs for the item without any multipliers being applied where order of the resources in the
 * array is [rocks, crystals, water, biomass].
 * ratio = the multiplier/ratio for the increased stat changes and cost for upgrades
 * 
 * @author Mason
 *
 */
public enum ArmourType {
	// armour value, cost(rcwb), level
	// organic armour
	ARMOUR1("A1", 15, 30, 10, new int[] { 20, 20, 0, 0 }, 1.3f); //test dummy entry

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
	 * @return the current amount of Movement Speed that the item will add on.
	 */
	int getMoveSpeed(int lvl) {
		return applyUpgrateRatio(this.baseSpeed, lvl);
	}

	/**
	 * Gets the cost of the next upgrade based on the current level.
	 * 
	 * @param lvl current level of the item
	 * @return Array of integers which represent the cost of each resource to upgrade the item to the next level. Order
	 * of the resources in this array is [rocks, crystals, water, biomass].
	 */
	int[] getArmourCost(int lvl) {
		int[] cost = new int[baseCost.length];
		for (int i = 0; i < baseCost.length; i++) {
			cost[i] = applyUpgrateRatio(baseCost[i], lvl);
		}
		return cost;
	}

}
