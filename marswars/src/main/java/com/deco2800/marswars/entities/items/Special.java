package com.deco2800.marswars.entities.items;

import com.deco2800.marswars.entities.items.effects.Effect;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for Special items. These items would be activated by the player at any time and are limited in use (i.e. how 
 * many times they can be activated). These items' effects can be ANYTHING e.g. ranging from permanent or temporary 
 * buffs to blowing up the world. A Commander can carry as many different kinds of these special items as their 
 * inventory allows (at most 4) but can carry multiple of the same item in 1 "stack".
 * 
 * type = the SpecialType enumerate value which stores the basic meta data for the Special item.
 * effects = List of Effect objects that contains the item's effect(s) (i.e. their functionality).
 * useLimit = Amount of uses the item has left.
 * 
 * @author Mason
 *
 */
public class Special extends Item {

	private SpecialType type;
	private List<Effect> effects;
	private int useLimit;
	

	/**
	 * Constructor for Special items taking in enumerate that contains the meta data for specific items.
	 * @param type  The SpecialType enumerate value containing the meta data for the specific Special item.
	 * @param target  
	 */
	public Special(SpecialType type) {
		this.type = type;
		this.effects = type.getEffect();
		this.useLimit = type.getUseLimit();
	}
	
	/**
	 * Gets the duration of the item's effect. If the duration is 0, then it is instantly cast effect rather than one
	 * that has a duration.
	 * @return the number of ticks the item effects last for.
	 */
	public int getDuration() {
		return type.getDuration();
	}
	
	/**
	 * Gets the aoe radius of the item's effect
	 * @return aoe radius in terms of tiles
	 */
	public int getRadius() {
		return type.getRadius();
	}
	
	/**
	 * This method should get called when an item is used
	 * @return true if the item still have more than one usage, false if this is the last usage
	 */
	public boolean useItem() {
		if (--useLimit >= 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * This method should get called when an item is displayed in the inventory
	 * @return the remaining usage of this item
	 */
	public int getUsage() {
		return this.useLimit;
	}
	
	/**
	 * Gets the type of the item which should be SPECIAL.
	 * @reutrn SPECIAL of the Item.Type enumerate values.
	 */
	@Override
	public Type getItemType() {
		return Type.SPECIAL;
	}

	/**
	 * Gets the name of the specific item.
	 * @return string that is the name of the specific item defined in the meta data enumerate tuple.
	 */
	@Override
	public String getName() {
		return type.getName();
	}

	/**
	 * Gets the description of the item based on its name and its effects.
	 * @return string generated from the item name and the descriptions of the effects.
	 */
	@Override
	public String getDescription() {
		StringBuilder string = new StringBuilder(this.getName()+"\n");
		for (Effect e : this.getEffect()) {
			string.append(e.generateDescription());
		}
		return string.toString();
	}

	/**
	 * Gets the list of Effect objects.
	 * @return a copy of the list of Effect objects for the item.
	 */
	@Override
	public List<Effect> getEffect() {
		return new ArrayList<Effect>(effects);
	}

	/**
	 * Method to get the saved texture string of the image file to be used as the item's icon defined in the enumerate 
	 * tuple.
	 * 
	 * @return saved texture string of the item icon in string format
	 */
	@Override
	public String getTexture() {
		return type.getTextureString();
	}
	
	/**
	 * Gets the enumerate value used to create the the Special item.
	 * @return
	 */
	public SpecialType getEnum() {
		return this.type;
	}
	
}
