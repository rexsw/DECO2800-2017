package com.deco2800.marswars.entities.items;

public enum ArmourType {
    // armour value, cost(rcwb), level
    // organic armour
	ARMOUR1("A1", 15, 30, 10, new int[] { 20, 20, 0, 0 }, 1.3f);
//    ARMOUR1LEVEL1 (15, new int[]{20, 20, 0, 0}, 1),
//    ARMOUR1LEVEL2 (30, new int[]{40, 40, 10, 10}, 2),
//    ARMOUR1LEVEL3 (45, new int[]{60, 60, 20, 20}, 3),

//    // synthetic armour
//    ARMOUR2LEVEL1 (10, new int[]{0, 0, 30, 30}, 1),
//    ARMOUR2LEVEL2 (20, new int[]{10, 10, 50, 50}, 2),
//    ARMOUR2LEVEL3 (30, new int[]{20, 20, 80, 80}, 3);

	private String name;
	private int baseArmour;
	private int[] baseCost;
	private int baseHealth;
	private int baseSpeed;
	private float ratio;

	ArmourType(String name, int baseArmour, int baseHealth, int baseSpeed,
			int[] baseCost, float ratio) {
		this.name = name;
		this.baseArmour = baseArmour;
		this.baseHealth = baseHealth;
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

	int getArmourValue(int lvl) {
		return this.baseArmour * upgrateRatio(lvl);
	}

	int getArmourHealth(int lvl) {
		return this.baseHealth* upgrateRatio(lvl);
	}

	int getMoveSpeed(int lvl) {
		return this.baseSpeed* upgrateRatio(lvl);
	}

	int[] getWeaponCost(int lvl) {
		int[] cost = new int[baseCost.length];
		for (int i = 0; i < baseCost.length; i++) {
			cost[i] = baseCost[i] * upgrateRatio(lvl);
		}
		return cost;
	}

}
