package com.deco2800.marswars.entities.items;

public class Armour extends PassiveItem {

    private ArmourType armourType;
    private int itemXp;

    public Armour(String name, ArmourType type) {
        super(name);
        super.setItemType("Armour");
        this.armourType = type;
        this.itemXp = 0;
        switch(type) {
            case ARMOUR1LEVEL1:
                // decrement resources
                break;
            case ARMOUR1LEVEL2:
                // decrement resources
                break;
            case ARMOUR1LEVEL3:
                // decrement resources
                break;
            case ARMOUR2LEVEL1:
                // decrement resources
                break;
            case ARMOUR2LEVEL2:
                // decrement resources
                break;
            case ARMOUR2LEVEL3:
                // decrement resources
                break;
            default:
                // anything here???
                break;
        }
    }

    public int getArmourValue() {
        return armourType.getArmourValue();
    }

    public int[] getArmourCost() {
        return armourType.getArmourCost();
    }

    public int getArmourLevel() {
        return armourType.getArmourLevel();
    }

    public int getItemXp() {
        return this.itemXp;
    }

    public void addItemXp(int xp) {
        this.itemXp += xp;
    }

    /**
     * for use when a building/tech unlock allows a direct upgrade, ie
     * without needing item xp
     *
     * @param type
     */
    public void changeArmourType(ArmourType type) {
        this.armourType = type;
    }
}