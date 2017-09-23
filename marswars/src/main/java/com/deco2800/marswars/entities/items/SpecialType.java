package com.deco2800.marswars.entities.items;

import java.util.ArrayList;
import java.util.List;

import com.deco2800.marswars.entities.items.effects.*;

/**
 * Enumerate class to store the meta data for all the Special items. Since Special items can have any sort of effect, 
 * the meta data stored will have stats that are likely common to be used such as area of effect (aoe) radius or 
 * duration for effects that are temporary.
 * 
 * name = the name of the item
 * texture = the string of the saved texture for the image file that would be used for the item's icon
 * duration = the amount of ticks the effect will last if it is temporary (note 0 will mean it is an instant effect 
 * rather than something that lasts over a duration).
 * radius = aoe range radius in terms of tiles
 * useLimit = the starting amount of uses the item has
 * cost = array of amount of resources it takes to buy the item.
 * effects = list of Effect objects the item will have (will implement and define the item's effect and mechanics).
 * 
 * @author Mason
 *
 */
public enum SpecialType implements ItemType {
	// name, duration(0 means instant), affect range (0 means self use), use
	// limit, cost, effects)
	AOEHEAL1("Heal 1", "heal_needle", 0, 2, 2, new int[] { 0, 10, 30, 30 },
			new HealthEffect(100, false)),
	BOMB("Bomb", "boot", 0, 5, 1, new int[] { 200, 50, 50, 0 },
			new HealthEffect(100, true)),
	AOEHEAL2("Heal Bomb", "scope", 0, 5, 1, new int[] { 50, 100, 100, 100 },
			new HealthEffect(100, false)),
	NUKE("Nuke", "bullets", 0, 15, 1, new int[] { 500, 900, 50, 50 },
			new HealthEffect(1000, true)),
	MASS1HEAL("Mass Heal1", "health_boost", 0, 1, 1, new int[] { 200, 500, 300, 500 },
			new HealthEffect(9000, false));
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
		this.effects = new ArrayList<Effect>();
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
	public int getRadius() {
		return this.radius;
	}

	/**
	 * Gets the duration of this special item's effects
	 * 
	 * @return duration of the item effects
	 */
	public int getDuration() {
		return this.duration;
	}

	/**
	 * Gets the original amount of use limit of this special item
	 * 
	 * @return the original amount of use limit of this item
	 */
	public int getUseLimit() {
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
