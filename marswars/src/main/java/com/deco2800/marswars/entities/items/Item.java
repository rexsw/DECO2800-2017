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

/**
 * Abstract class for items in the game. Items are to be only equippable/usable on Hero Spacmen (i.e. the hero units).
 * Items will have 3 types, Weapon, Armour and Special (as indicated in enumerates). Attack and Armour items are passive
 * in nature and would affect at most 3 stats each (excluding effects that are simply stat changes e.g. igniting enemies
 * on physical contact). Special items will be similar to skills in that their effects could be anything but they are 
 * activated when the player chooses to. Special items are also single use i.e. once used, the item will not come back 
 * and the player will need to make another one if they want another one.
 * 
 * 1 Hero Spacman can have at most 1 Attack item and 1 Armour item. But Hero Spacmen can have as many Special items as 
 * the total capacity of the Hero Spacman's inventory.
 * 
 * Items will also have levels i.e. they are upgradable at the cost of resources. High levels will proportionally 
 * increase the stat changes of the items. Costs of upgrades will also proportoinally increase. The max level an item 
 * can have is 3. Item levels start at 1.
 * 
 * @author Mason
 *
 */
public abstract class Item {
	/*
	 * Enumerate for the item type. See above for summary of their meanings and what each can do.
	 */
	public enum Type {
		WEAPON, ARMOUR, SPECIAL;
	}
	
	/**
	 * Method to get the item type i.e. the enumerates above.
	 * @return WEAPON for Weapon items, ARMOUR for Armour items and SPECIAL for Special items.
	 */
	public abstract Type getItemType();

	/**
	 * Gets the name of the item.
	 * @return String that is the name of the item.
	 */
	public abstract String getName();

	/**
	 * Gets the item's description which would describe the item's effect(s).
	 * @return String that has the item's description.
	 */
	public abstract String getDescription();

	/**
	 * Gets all the effects of the item.
	 * @return List of Effect classes which are all the item effects of the item.
	 */
	public abstract List<Effect> getEffect();
	// @Override
	// public String toString() {
	// return getName() + ": " + getDescription();
	// }
}
