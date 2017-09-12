package com.deco2800.marswars.entities.items;

public enum WeaponType {
    // damage, range, speed(100 is standard speed, less is faster, more is
    // slower), cost(rcwb), level

    //assault blasters
    WEAPON1LEVEL1 (10, 3, 90, new int[]{20, 20, 0, 0}, 1),
    WEAPON1LEVEL2 (20, 4, 80, new int[]{30, 30, 10, 10}, 2),
    WEAPON1LEVEL3 (30, 5, 70, new int[]{50, 50, 30, 30}, 2),

    //sniper rifles
    WEAPON2LEVEL1 (20, 5, 110, new int[]{30, 30, 0, 0}, 1),
    WEAPON2LEVEL2 (40, 7, 120, new int[]{50, 50, 20, 20}, 2),
    WEAPON2LEVEL3 (60, 9, 130, new int[]{80, 80, 40, 40}, 3);

    private final int weaponDamage;
    private final int[] weaponCost;
    private final int weaponRange;
    private final int weaponSpeed;
    private int weaponLevel;

    WeaponType(int weaponDamage, int weaponRange, int weaponSpeed, int[]
            weaponCost, int
            weaponLevel) {
        this.weaponDamage = weaponDamage;
        this.weaponRange = weaponRange;
        this.weaponSpeed = weaponSpeed;
        this.weaponCost = weaponCost;
        this.weaponLevel = weaponLevel;
    }

    int getWeaponDamage() { return this.weaponDamage; }

    int getWeaponRange() {return this.weaponRange; }

    int getWeaponSpeed() {return this.weaponSpeed; }

    int[] getWeaponCost() {return this.weaponCost; }

    int getWeaponLevel() { return this.weaponLevel; }



}
