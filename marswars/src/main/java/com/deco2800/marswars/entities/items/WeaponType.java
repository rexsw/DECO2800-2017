package com.deco2800.marswars.entities.items;

/**
 * Enumerate to store the meta data for all the specific Weapon type items.
 * 
 * Meta Data details: 
 * name = the item's name 
 * baseDamage = amount of damage (normal and armour damage) to be added on without any multipliers being applied. 
 * baseRange = the amount of distance between the target and the hero spacmen. This is added on without any multipliers. 
 * baseSpeed = amount of attack speed it changes without any multipliers being applied 
 * baseCost = the resource costs for the item without any multipliers being applied where order of the resources in the 
 * array is [rocks, crystals, water, biomass].
 * 
 * @author Mason
 *
 */
public enum WeaponType implements ItemType {
	// assault blasters
	WEAPON1("W1", "power_gloves", 10, 3, 90, new int[] { 20, 20, 0, 0 }),

	// sniper rifles
	WEAPON2("W2", "power_gloves", 999, 999, 999, new int[] { 30, 30, 0, 0 });

	private String name;
	private int baseDamage;
	private int[] baseCost;
	private int baseRange;
	private int baseSpeed;
	private String texture;

	/**
	 * Constructor method of Weapon Type
	 * 
	 * @param name
	 *            of this weapon
	 * @param texture
	 *            of this weapon
	 * @param baseDamage
	 *            of this weapon, normal damage
	 * @param baseRange
	 *            of this weapon, attack range
	 * @param baseSpeed
	 *            of this weapon, attack speed
	 * @param baseCost
	 *            of this weapon
	 */
	WeaponType(String name, String texture, int baseDamage, int baseRange, int baseSpeed, int[] baseCost) {
		this.name = name;
		this.baseDamage = baseDamage;
		this.baseRange = baseRange;
		this.baseSpeed = baseSpeed;
		this.baseCost = baseCost;
		this.texture = texture;
	}

	/**
	 * Gets the item name
	 * 
	 * @return string that is the item name
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the damage the item will add on (only normal damage, armour damage will be handled in weapon class).
	 * 
	 * @return the amount of damage that the item will add on.
	 */
	public int getWeaponDamage() {
		return this.baseDamage;
	}

	/**
	 * Gets the attack range the item will add on.
	 * 
	 * @return the amount of attack range that the item will add on.
	 */
	public int getWeaponRange() {
		return this.baseRange;
	}

	/**
	 * Gets the amount of Attack Speed the item will add on.
	 * 
	 * @return the amount of Attack speed that the item will add on.
	 */
	public int getWeaponSpeed() {
		return this.baseSpeed;
	}

	/**
	 * Gets the build cost of this item.
	 * 
	 * @return Array of integers which represent the cost to build each resource. Order of the resources in this array
	 *         is [rocks, crystals, water, biomass].
	 */
	@Override
	public int[] getCost() {
		return this.baseCost.clone();
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
	 * Gets the description of the Weapon item which specifies its effects and stat changes.
	 * 
	 * @return The description of the item as a string.
	 */
	@Override
	public String getDescription() {
		return "Name: " + this.getName() + "\nDamage: " + this.getWeaponDamage() + "\nSpeed: " + this.getWeaponSpeed()
				+ "\nRange: " + this.getWeaponRange();
	}

}
