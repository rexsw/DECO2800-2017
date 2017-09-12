package com.deco2800.marswars.entities.items;

import java.util.List;

import com.deco2800.marswars.entities.items.effects.Effect;

//public class Item {
//
//    protected String name;
////    protected String itemType;
//
//    public Item(String name) {
//        this.name = name;
//    }
//
//    public String getName(Item item) {
//        return this.name;
//    }
//
////    protected void setItemType(String type) { 
////    	this.itemType = type;
////    }
////
////    protected String getItemType() {
////    	return itemType; 
////    }
//}
public abstract class Item {
    public enum Type {
        WEAPON,
        ARMOUR,
        SPECIAL;
    }

    public abstract Type getItemType();

    public abstract String getName();

    public abstract String getDescription();

    public abstract List<Effect> getEffect();
//    @Override
//    public String toString() {
//        return getName() + ": " + getDescription();
//    }
}
