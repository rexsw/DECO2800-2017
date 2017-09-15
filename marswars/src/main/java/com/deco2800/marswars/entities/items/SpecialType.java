package com.deco2800.marswars.entities.items;

import java.util.ArrayList;
import java.util.List;

import com.deco2800.marswars.entities.items.effects.*;

public enum SpecialType implements ItemType {
	// name, duration(0 means instant), affect range (0 means self use), use
	// limit, cost, effects)
	AOEHEAL("Heal 1", "heal_needle", 0, 2, 1, new int[] { 0, 10, 30, 30 },
			new HealthEffect(100, false));
	// AOEDMG("Damage 1", 0, 2, 1, new int[] { 0, 10, 30, 30 },
	// new HealthEffect(100, true)),
	// SELFHEAL("Heal 2", 0, 0, 2, new int[] { 0, 10, 30, 30 },
	// new HealthEffect(1000, false)),
	//
	// BATTLEBUFF("War Cry", 10, 5, 1, new int[] { 100, 100, 50, 50 }, new
	// AttackEffect(100, 0, 0), new DefenceEffect(100, 500, 10));

	// radius, magnitude, duration (0 is instant), cost(rcwb), level, type
	// AOEHEAL1 (3, 25, 0, new int[]{0, 10, 30, 30}, 1, "Heal"),
	// AOEHEAL2 (4, 50, 0, new int[]{10, 20, 50, 50}, 2, "Heal"),
	// AOEHEAL3 (5, 75, 0, new int[]{20, 30, 100, 100}, 3, "Heal"),
	// AOEDAMAGE1 (3, -25, 0, new int[]{0, 10, 30, 30}, 1, "Damage"),
	// AOEDAMAGE2 (4, -50, 0, new int[]{10, 20, 50, 50}, 2, "Damage"),
	// AOEDAMAGE3 (5, -75, 0, new int[]{20, 30, 100, 100}, 3, "Damage"),
	// AOESPEED1 (3, 25, 0, new int[]{20, 20, 0, 0}, 1, "Speed"),
	// AOESPEED2 (4, 50, 0, new int[]{40, 40, 10, 10}, 2, "Speed"),
	// AOESPEED3 (5, 75, 0, new int[]{60, 60, 30, 30}, 3, "Speed");

	private String name;
	private String texture;
	private int duration;
	private int radius;
	private int useLimit;
	private int[] cost;
	private List<Effect> effects;

	/**
	 * Constructor method of the special items
	 * 
	 * @param name
	 *            of this item
	 * @param texture
	 *            of this item
	 * @param duration
	 *            of this item's effect
	 * @param radius
	 *            of this item's effect
	 * @param original
	 *            useLimit of this item
	 * @param cost
	 *            to build this item
	 * @param effects
	 *            of this item
	 */
	SpecialType(String name, String texture, int duration, int radius,
			int useLimit, int[] cost, Effect... effects) {
		this.effects = new ArrayList<>();
		this.name = name;
		this.texture = texture;
		this.radius = radius;
		this.duration = duration;
		this.useLimit = useLimit;
		this.cost = cost;
		for (Effect e : effects) {
			this.effects.add(e);
		}
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
	 * Gets the affect radius of this item
	 * 
	 * @return the affect radius of this item when used
	 */
	int getRadius() {
		return this.radius;
	}

	/**
	 * Gets the duration of this special item's effects
	 * 
	 * @return duration of the item effects
	 */
	int getDuration() {
		return this.duration;
	}

	/**
	 * Gets the original amount of use limit of this special item
	 * 
	 * @return the original amount of use limit of this item
	 */
	int getUseLimit() {
		return this.useLimit;
	}

	/**
	 * Gets the build cost of this item.
	 * 
	 * @return Array of integers which represent the cost to build each
	 *         resource. Order of the resources in this array is [rocks,
	 *         crystals, water, biomass].
	 */
	@Override
	public int[] getCost() {
		return this.cost;
	}

	/**
	 * Gets all the effects of this item in a list
	 * 
	 * @return a list of this item's effect
	 */
	List<Effect> getEffect() {
		return new ArrayList<Effect>(this.effects);
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
	 * Gets the description of the special item which specifies its effects and
	 * stat changes.
	 * 
	 * @return The description of the item as a string.
	 */
	@Override
	public String getDescription() {
		String result = "Name: " + this.getName() + "\n";
		for (Effect e : this.effects) {
			result += e.generateDescription();
		}
		return result;
	}

	/**
	 * Gets the build cost of this item in String format
	 * 
	 * @return the string presentation of item cost
	 */
	@Override
	public String getCostString() {
		String result = "";
		if (this.cost[0] > 0) {
			result += "Rock: " + this.cost[0] + "\n";
		}
		if (this.cost[1] > 0) {
			result += "Crystal: " + this.cost[1] + "\n";
		}
		if (this.cost[2] > 0) {
			result += "Water: " + this.cost[2] + "\n";
		}
		if (this.cost[3] > 0) {
			result += "Biomass: " + this.cost[3] + "\n";
		}
		return result;
	}

}
