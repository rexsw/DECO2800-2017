package com.deco2800.marswars.entities.items;

public enum ArmourType {
    // armour value, cost(rcwb), level
    // organic armour
    ARMOUR1LEVEL1 (15, new int[]{20, 20, 0, 0}, 1),
    ARMOUR1LEVEL2 (30, new int[]{40, 40, 10, 10}, 2),
    ARMOUR1LEVEL3 (45, new int[]{60, 60, 20, 20}, 3),

    // synthetic armour
    ARMOUR2LEVEL1 (10, new int[]{0, 0, 30, 30}, 1),
    ARMOUR2LEVEL2 (20, new int[]{10, 10, 50, 50}, 2),
    ARMOUR2LEVEL3 (30, new int[]{20, 20, 80, 80}, 3);

    private int armourValue;
    private int[] armourCost;
    private int armourLevel;

    ArmourType(int armourValue, int[] armourCost, int armourLevel) {
        this.armourValue = armourValue;
        this.armourCost = armourCost;
        this.armourLevel = armourLevel;
    }

    int getArmourValue() { return this.armourValue; }

    int[] getArmourCost() {return this.armourCost; }

    int getArmourLevel() { return this.armourLevel; }

}
