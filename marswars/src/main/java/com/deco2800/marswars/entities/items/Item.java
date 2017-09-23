package com.deco2800.marswars.entities.items;

import com.deco2800.marswars.entities.items.effects.Effect;

import java.util.List;

/**
 * Abstract class for items in the game. Items are to be only equippable/usable on Commander (i.e. the hero units).
 * Items will have 3 types, Weapon, Armour and Special (as indicated in enumerates). Attack and Armour items are passive
 * in nature and would affect at most 3 stats each (excluding effects that are simply stat changes e.g. igniting enemies
 * on physical contact). Special items will be similar to skills in that their effects could be anything but they are 
 * activated when the player chooses to. Special items are also single use i.e. once used, the item will not come back 
 * and the player will need to make another one if they want another one.
 * 
 * 1 Commander can have at most 1 Attack item and 1 Armour item. But Commander can have as many Special items as 
 * the total capacity of the Hero Spacman's inventory.
 * 
 * Items will also have levels i.e. they are upgradeable at the cost of resources. High levels will proportionally 
 * increase the stat changes of the items. Costs of upgrades will also proportionally increase. The max level an item 
 * can have is 3. Item levels start at 1.
 * 
 * @author Mason
 *
 */
public abstract class Item {
	/**
	 * Enumerate for the item type. See above for summary of their meanings and what each can do.
	 */
	public enum Type {
		WEAPON, ARMOUR, SPECIAL;
	}
	
	/**
	 * Method to get the item type i.e. the enumerates above.
	 * 
	 * @return WEAPON for Weapon items, ARMOUR for Armour items and SPECIAL for Special items.
	 */
	public abstract Type getItemType();

	/**
	 * Gets the name of the item.
	 * 
	 * @return String that is the name of the item.
	 */
	public abstract String getName();

	/**
	 * Gets the item's description which would describe the item's effect(s).
	 * 
	 * @return String that has the item's description.
	 */
	public abstract String getDescription();

	/**
	 * Gets all the effects of the item.
	 * 
	 * @return List of Effect classes which are all the item effects of the item.
	 */
	public abstract List<Effect> getEffect();
	
	/**
	 * Gets the texture of this item for display purpose
	 * 
	 * @return the texture of this item in String
	 */
	public abstract String getTexture();

}
