package com.deco2800.marswars.entities.items;

public enum WeaponType {
	// damage, range, speed(100 is standard speed, less is faster, more is
	// slower), cost(rcwb), level

	// assault blasters
	WEAPON1("W1", 10, 3, 90, new int[] { 20, 20, 0, 0 }, 1.3f),
	// WEAPON1LEVEL2 (20, 4, 80, new int[]{30, 30, 10, 10}, 2),
	// WEAPON1LEVEL3 (30, 5, 70, new int[]{50, 50, 30, 30}, 2),

	// sniper rifles
	WEAPON2("W2", 20, 5, 110, new int[] { 30, 30, 0, 0 }, 1.5f);
	// WEAPON2LEVEL2 (40, 7, 120, new int[]{50, 50, 20, 20}, 2),
	// WEAPON2LEVEL3 (60, 9, 130, new int[]{80, 80, 40, 40}, 3);

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
	
	private int upgrateRatio(int lvl) {
		return 1+Math.round((lvl-1)*ratio);
	}

	String getName() {
		return this.name;
	}

	int getWeaponDamage(int lvl) {
		return this.baseDamage * upgrateRatio(lvl);
	}

	int getWeaponRange(int lvl) {
		return this.baseRange* upgrateRatio(lvl);
	}

	int getWeaponSpeed(int lvl) {
		return this.baseSpeed* upgrateRatio(lvl);
	}

	int[] getWeaponCost(int lvl) {
		return this.baseCost;
	}

}
