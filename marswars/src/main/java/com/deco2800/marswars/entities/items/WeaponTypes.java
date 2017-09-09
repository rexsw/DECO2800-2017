package com.deco2800.marswars.entities.items;

public enum WeaponTypes {
    WEAPON1LEVEL1 (10, 100, 1),
    WEAPON1LEVEL2 (20, 200, 2),
    WEAPON1LEVEL3 (30, 300, 3),
    WEAPON2LEVEL1 (15, 200, 1),
    WEAPON2LEVEL2 (30, 400, 2),
    WEAPON2LEVEL3 (45, 800, 3);

    private final int weaponDamage;
    private final int weaponCost;
    private int weaponLevel;

    WeaponTypes(int weaponDamage, int weaponCost, int weaponLevel) {
        this.weaponDamage = weaponDamage;
        this.weaponCost = weaponCost;
        this.weaponLevel = weaponLevel;

    }

    int getWeaponDamage() { return this.weaponDamage; }

    int getWeaponCost() {return this.weaponCost; }

    int getWeaponLevel() { return this.weaponLevel; }

}
