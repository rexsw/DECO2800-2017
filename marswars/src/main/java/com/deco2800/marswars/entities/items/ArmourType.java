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
 * texture = string of the image texture file
 * 
 * @author Mason
 *
 */
public enum ArmourType implements ItemType {
	// armour value, cost(rcwb), level
	// organic armour
	ARMOUR1("A1", "defence_helmet", 15, 30, 10, new int[] { 20, 20, 0, 0 }); 

	private String name;
	private int baseArmour;
	private int[] baseCost;
	private int baseHealth;
	private int baseSpeed;
	// private float ratio;
	private String texture;

	/**
	 * Constructor method of armour type
	 * 
	 * @param name
	 *            of this armour
	 * @param texture
	 *            of this armour
	 * @param baseArmour
	 *            of this armour
	 * @param baseHealth
	 *            of this armour, maxHealth and currentHealth
	 * @param baseSpeed,
	 *            movement speed
	 * @param baseCost,
	 *            cost for building it
	 */
	ArmourType(String name, String texture, int baseArmour, int baseHealth,
			int baseSpeed, int[] baseCost) {
		this.name = name;
		this.baseArmour = baseArmour;
		this.baseHealth = baseHealth;
		this.baseSpeed = baseSpeed;
		this.baseCost = baseCost;
		// this.ratio = ratio;
		this.texture = texture;
	}

	// /**
	// * Helper method to calculate the rounded integer stat changes or upgrade
	// costs based on the item's current level
	// * and the item's ratio field. Result is calculated by multiplying the
	// stat change or cost by the ratio
	// * level - 1 times.
	// *
	// * @param change The stat change or the cost to be
	// * @param lvl the current level of the item.
	// * @return rounded integer of the stat change or cost after applying the
	// multiplier
	// */
	// private int applyUpgrateRatio(int change, int lvl) {
	// return (int) Math.round(change * Math.pow(ratio, (lvl - 1)));
	// }

	/**
	 * Gets the item name
	 * 
	 * @return string that is the item name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the amount of Armour (max and current) the item will add on.
	 * 
	 * @return the amount of Armour (max and current) that the item will add on.
	 */
	int getArmourValue() {
		return this.baseArmour;
	}

	/**
	 * Gets the amount of Health (max and current) the item will add on.
	 * 
	 * @return the amount of Health (max and current) that the item will add on.
	 */
	int getArmourHealth() {
		return this.baseHealth;
	}

	/**
	 * Gets the amount of Movement Speed the item will add on.
	 * 
	 * @return the amount of Movement Speed that the item will add on.
	 */
	int getMoveSpeed() {
		return this.baseSpeed;
	}

	/**
	 * Gets the cost of the this armour
	 * 
	 * @return Array of integers which represent the cost of each resource to
	 *         build the item. Order of the resources in this array is [rocks,
	 *         crystals, water, biomass].
	 */
	@Override
	public int[] getCost() {
		return this.baseCost.clone();
	}

	/**
	 * Gets the item texture String
	 * 
	 * @return texture string of this item
	 */
	@Override
	public String getTextureString() {
		return this.texture;
	}

	/**
	 * Gets the description of the Weapon item which specifies its effects and
	 * stat changes.
	 * 
	 * @return The description of the item as a string.
	 */
	@Override
	public String getDescription() {
		return "Name: " + this.getName() + "\nArmour: " + this.getArmourValue()
				+ "\nMaxHealth: " + this.getArmourHealth() + "\nMove Speed: "
				+ this.getMoveSpeed();
	}

	/**
	 * Gets the build cost of this item in String format
	 * 
	 * @return the string presentation of item cost
	 */
	@Override
	public String getCostString() {
		String result = "";
		if (this.baseCost[0] > 0) {
			result += "Rock: " + this.baseCost[0] + "\n";
		}
		if (this.baseCost[1] > 0) {
			result += "Crystal: " + this.baseCost[1] + "\n";
		}
		if (this.baseCost[2] > 0) {
			result += "Water: " + this.baseCost[2] + "\n";
		}
		if (this.baseCost[3] > 0) {
			result += "Biomass: " + this.baseCost[3] + "\n";
		}
		return result;
	}

}
