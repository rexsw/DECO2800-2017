package com.deco2800.marswars.entities.items;

import java.util.ArrayList;
import java.util.List;

import com.deco2800.marswars.entities.items.effects.Effect;

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
	 * Constructor 
	 * @param type
	 */
	public Special(SpecialType type) {
		this.type = type;
		this.effects = type.getEffect();
		this.useLimit = type.getUseLimit();
	}
	
	public int getDuration() {
		return type.getDuration();
	}
	
	public int getRadius() {
		return type.getRadius();
	}
	
	/**
	 * This method should get called when an item is used
	 * @return true if the item still have more than one usage, false if this is the last usage
	 */
	public boolean useItem() {
		if (--useLimit > 1) {
			return true;
		}
		return false;
	}
	@Override
	public Type getItemType() {
		return Type.SPECIAL;
	}

	@Override
	public String getName() {
		return type.getName();
	}

	@Override
	public String getDescription() {
		StringBuilder string = new StringBuilder(this.getName()+"\n");
		for (Effect e : this.getEffect()) {
			string.append(e.generateDescription());
		}
		return string.toString();
	}

	@Override
	public List<Effect> getEffect() {
		return new ArrayList<>(effects);
	}
	
//  private SpecialType specialType;
//
//  public Special(String name, SpecialType type) {
//      super(name);
//      super.setItemType("Special");
//      this.specialType = type;
//      switch(type) {
//          case AOEHEAL1:
//              // decrement resources
//              break;
//          case AOEHEAL2:
//              // decrement resources
//              break;
//          case AOEHEAL3:
//              // decrement resources
//              break;
//          case AOEDAMAGE1:
//              // decrement resources
//              break;
//          case AOEDAMAGE2:
//              // decrement resources
//              break;
//          case AOEDAMAGE3:
//              // decrement resources
//              break;
//          case AOESPEED1:
//              // decrement resources
//              break;
//          case AOESPEED2:
//              // decrement resources
//              break;
//          case AOESPEED3:
//              // decrement resources
//              break;
//          default:
//              // anything here??
//              break;
//      }
//
//  }
//
//  public int getAOERadius() { return specialType.getRadius(); }
//
//  public int getAOEMagnitude() {return specialType.getMagnitude(); }
//
//  public int getAOEDuration () { return specialType.getRadius(); }
//
//  public int getAOELevel() { return specialType.getLevel(); }
//
//  public int[] getAOECost() { return specialType.getCost(); }
//
//  public String getSpecialType() { return specialType.getType(); }
}
