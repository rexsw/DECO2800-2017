package com.deco2800.marswars.entities.items;

public class Special extends ActiveItem {

    private SpecialType specialType;

    public Special(String name, SpecialType type) {
        super(name);
        super.setItemType("Special");
        this.specialType = type;
        switch(type) {
            case AOEHEAL1:
                // decrement resources
                break;
            case AOEHEAL2:
                // decrement resources
                break;
            case AOEHEAL3:
                // decrement resources
                break;
            case AOEDAMAGE1:
                // decrement resources
                break;
            case AOEDAMAGE2:
                // decrement resources
                break;
            case AOEDAMAGE3:
                // decrement resources
                break;
            case AOESPEED1:
                // decrement resources
                break;
            case AOESPEED2:
                // decrement resources
                break;
            case AOESPEED3:
                // decrement resources
                break;
            default:
                // anything here??
                break;
        }

    }

    public int getAOERadius() { return specialType.getRadius(); }

    public int getAOEMagnitude() {return specialType.getMagnitude(); }

    public int getAOEDuration () { return specialType.getRadius(); }

    public int getAOELevel() { return specialType.getLevel(); }

    public int[] getAOECost() { return specialType.getCost(); }

    public String getSpecialType() { return specialType.getType(); }

}
