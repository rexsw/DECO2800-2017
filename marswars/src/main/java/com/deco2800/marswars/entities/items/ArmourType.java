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
 * array is [rocks, crystals, biomass].
 * texture = string of the saved texture for the image file that would be used for the item's icon
 * 
 * @author Mason
 * @author Z
 *
 */
public enum ArmourType implements ItemType {

	HELMET("Combat Helmet", "helmet_1", 1000, 500, 0.05f, new int[] { 80, 30, 100 },
			new float[] { 1.0f, 1.2f, 1.4f }),

	GOGGLE("Tactical Goggle", "goggle_1", 3000, 1500, 0.1f, new int[] { 200, 500,
			300}, new float[] {1.0f, 1.2f, 1.4f });
	
	private String name;
	private int baseArmour;
	private int[] baseCost;
	private int baseHealth;
	private float baseSpeed;
	private String texture;
	private float[] itemLevelMultipliers;

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
			float baseSpeed, int[] baseCost, float[] levelMultipliers) {
		this.name = name;
		this.baseArmour = baseArmour;
		this.baseHealth = baseHealth;
		this.baseSpeed = baseSpeed;
		this.baseCost = baseCost;
		this.texture = texture;
		this.itemLevelMultipliers = levelMultipliers;
	}

	/**
	 * Gets the item name
	 * 
	 * @return string that is the item name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the item level stat multipliers
	 *
	 * @return float array that contains the stat multipliers for
	 * corresponding item levels
	 */
	public float[] getItemLevelMultipliers() {
		return this.itemLevelMultipliers;
	}

	/**
	 * Gets the amount of Armour (max and current) the item will add on.
	 * 
	 * @return the amount of Armour (max and current) that the item will add on.
	 */
	public int getArmourValue() {
		return this.baseArmour;
	}

	/**
	 * Gets the amount of Health (max and current) the item will add on.
	 * 
	 * @return the amount of Health (max and current) that the item will add on.
	 */
	public int getArmourHealth() {
		return this.baseHealth;
	}

	/**
	 * Gets the amount of Movement Speed the item will add on.
	 * 
	 * @return the amount of Movement Speed that the item will add on.
	 */
	public float getMoveSpeed() {
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
		return "Name: " + this.getName() + "\nType: Armour\nArmour: " + 
				this.getArmourValue() + "\nMaxHealth: " + this.getArmourHealth() 
				+ "\nMove Speed: " + this.getMoveSpeed();
	}

	/**
	 * Gets the build cost of this item in String format
	 * 
	 * @return the string presentation of item cost
	 */
	public String getCostString() {
		String result = "";
		if (this.baseCost[0] > 0) {
			result += "Rock: " + this.baseCost[0] + "\n";
		}
		if (this.baseCost[1] > 0) {
			result += "Crystal: " + this.baseCost[1] + "\n";
		}
		if (this.baseCost[2] > 0) {
			result += "Biomass: " + this.baseCost[2] + "\n";
		}
		return result;
	}
}
