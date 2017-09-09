package com.deco2800.marswars.entities.items;

public enum ArmourTypes {
    ARMOUR1LEVEL1 (10, 100, 1),
    ARMOUR1LEVEL2 (20, 200, 2),
    ARMOUR1LEVEL3 (30, 300, 3),
    ARMOUR2LEVEL1 (15, 200, 1),
    ARMOUR2LEVEL2 (30, 400, 2),
    ARMOUR2LEVEL3 (45, 800, 3);


    private final int armourValue;
    private final int armourCost;
    private int armourLevel;

    ArmourTypes(int armourValue, int armourCost, int armourLevel) {
        this.armourValue = armourValue;
        this.armourCost = armourCost;
        this.armourLevel = armourLevel;
    }

    int getArmourValue() { return this.armourValue; }

    int getArmourCost() {return this.armourCost; }

    int getArmourLevel() { return this.armourLevel; }

}
