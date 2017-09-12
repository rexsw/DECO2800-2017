package com.deco2800.marswars.entities.items.ml_ver;

public class Weapon extends PassiveItem {

    private WeaponType weaponType;
    private int itemXp;

    public Weapon(String name, WeaponType type) {
        super(name);
        super.setItemType("Weapon");
        this.itemXp = 0;
        this.weaponType = type;
        switch (type) {
            case WEAPON1LEVEL1:
                // decrement resources
                break;
            case WEAPON1LEVEL2:
                // decrement resources
                break;
            case WEAPON1LEVEL3:
                // decrement resources
                break;
            case WEAPON2LEVEL1:
                // decrement resources
                break;
            case WEAPON2LEVEL2:
                // decrement resources
                break;
            case WEAPON2LEVEL3:
                // decrement resources
                break;
            default:
                // anything here???
                break;
        }

    }

    public int getWeaponDamage() { return weaponType.getWeaponDamage(); }

    public int getWeaponSpeed() { return weaponType.getWeaponSpeed(); }

    public int getWeaponRange() { return weaponType.getWeaponSpeed(); }

    public int[] getWeaponCost() {return weaponType.getWeaponCost(); }

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
