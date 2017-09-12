package com.deco2800.marswars.entities.items;

import java.util.ArrayList;
import java.util.List;

import com.deco2800.marswars.entities.items.effects.*;

public enum SpecialType {
	// name, duration(0 means instant), affect range (0 means self use), use limit, cost, effects)
	AOEHEAL("Heal 1", 0, 2, 1, new int[] { 0, 10, 30, 30 },
			new HealthEffect(100, false)),
	AOEDMG("Damage 1", 0, 2, 1, new int[] { 0, 10, 30, 30 },
			new HealthEffect(100, true)),
	SELFHEAL("Heal 2", 0, 0, 2, new int[] { 0, 10, 30, 30 },
			new HealthEffect(1000, false)),
	
	BATTLEBUFF("War Cry", 10, 5, 1, new int[] { 100, 100, 50, 50 }, new AttackEffect(100, 0, 0), new DefenceEffect(100, 500, 10));
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
	private int duration;
	private int radius;
	private int useLimit;
	private int[] cost;
	private List<Effect> effects;

	SpecialType(String name, int duration, int radius, int useLimit, int[] cost,
			Effect... effects) {
		this.effects = new ArrayList<>();
		this.name = name;
		this.radius = radius;
		this.duration = duration;
		this.useLimit = useLimit;
		this.cost = cost;
		for (Effect e : effects) {
			this.effects.add(e);
		}
	}

	String getName() {
		return this.name;
	}

	int getRadius() {
		return this.radius;
	}

	int getDuration() {
		return this.duration;
	}
	
	int getUseLimit() {
		return this.useLimit;
	}

	int[] getCost() {
		return this.cost;
	}
	
	List<Effect> getEffect() {
		return new ArrayList<Effect> (this.effects);
	}

}
