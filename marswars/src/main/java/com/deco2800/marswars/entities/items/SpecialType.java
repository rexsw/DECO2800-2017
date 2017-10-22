package com.deco2800.marswars.entities.items;

import com.deco2800.marswars.entities.items.effects.AttackEffect;
import com.deco2800.marswars.entities.items.effects.DefenceEffect;
import com.deco2800.marswars.entities.items.effects.Effect;
import com.deco2800.marswars.entities.items.effects.Effect.Target;
import com.deco2800.marswars.entities.items.effects.HealthEffect;

import java.util.ArrayList;
import java.util.List;

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
	// name, duration(0 means instant), effect range (0 means self use), use
	// limit, cost, effects)
	HEALTHSHOT("Health Shot", "health_shot", 0, 0, 5, new int[]{30, 50, 30}, 
			new HealthEffect(200, false, Target.SELF)),
	HEALTHSTATION("Health Station", "health_station", 0, 4, 2, new int[]{60, 100, 60}, 
			new HealthEffect(500, false, Target.SELF)),
	HEALTHBLESS("Health Bless", "healing_bless", 0, 0, 1, new int[]{100, 100, 100}, 
			new HealthEffect(1000, false, Target.SELF_TEAM)),
	NUKE("Nuke", "nuke", 0, 0, 1, new int[]{200, 100, 200}, 
			new HealthEffect(700, true, Target.ENEMY_TEAM)),
	MISSILE("Air Strike", "air_strike", 0, 5, 1, new int[]{200, 100, 200}, 
			new HealthEffect(1500, true, Target.ENEMY)),
	SNIPERSHOT("Sniper shot", "snipper_shot", 0, 1, 3, new int[]{30, 50, 20}, 
			new HealthEffect(500, true, Target.ENEMY)),
	
	FLOATINGBOOTS("Flooting boots", "floating_boots", 5, 3, 3, new int[]{60, 50, 60}, 
			new DefenceEffect(200, 0, 0.4f, Target.SELF_TEAM)),
	TELEBOOTS("Teleboots", "teleboots", 10, 0, 1, new int[]{60, 50, 60}, 
			new DefenceEffect(200, 0, 1.5f, Target.SELF)),
	PENETRATION("Penetration", "penetration", 10, 0, 1, new int[]{100, 150, 90}, 
			new DefenceEffect(-500, 0, -0.03f, Target.ENEMY_TEAM)),
	BARRIERGLOVES("Barrier Gloves", "barrier_gloves", 10, 0, 1, new int[]{100, 150, 100}, 
			new DefenceEffect(1500, 200, 0.03f, Target.SELF_TEAM)),
	COMMAND("Military Command", "military_command", 5, 0, 5, new int[]{50, 20, 50}, 
			new DefenceEffect(100, 100, 0.01f, Target.SELF_TEAM), new AttackEffect(50, 20, 5, Target.SELF_TEAM));
	
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
	 * @param useLimit
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
		result += "Type: Special\n";
		for (Effect e : this.effects) {
			result += e.generateDescription();
			String mark = e.getTarget().toString();
			mark = mark.contains("_") ? mark.replace("_", " ") : mark;
			result += "Target: " + mark;
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
			result += "Biomass: " + this.cost[2] + "\n";
		}
		return result;
	}

}
