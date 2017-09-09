package com.deco2800.marswars.entities.items;

public class Weapon extends PassiveItem {

    private WeaponType weaponType;
    private int itemXp;

    public Weapon(String name, WeaponType type) {
        super(name);
        super.setItemType("Weapon");
        this.weaponType = type;
        this.itemXp = 0;
    }

    public int getWeaponDamage() { return weaponType.getWeaponDamage(); }

    public int getWeaponCost() {return weaponType.getWeaponCost(); }

    public int getWeaponLevel() { return weaponType.getWeaponLevel(); }

    public int getItemXp() { return this.itemXp; }

    public void addItemXp(int xp) {
        this.itemXp += xp;
    }
    /** for use when buildings/tech allows a direct upgrade, ie without
     needing item xp
     */
    public void changeWeaponType(WeaponType type) {
        this.weaponType = type;
    }


}
