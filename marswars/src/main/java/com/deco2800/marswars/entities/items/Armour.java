package com.deco2800.marswars.entities.items;

import com.deco2800.marswars.entities.items.effects.DefenceEffect;
import com.deco2800.marswars.entities.items.effects.Effect;
import com.deco2800.marswars.entities.items.effects.Effect.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for Armour items. Armour items would be passive items that have defensive passive effects. These effect are not
 * limited to simple stat changes. Stats that Armour items can change are max health, max armour, current health,
 * current armour and movement speed.
 * 
 * type = the ArmourType enumerate value which stores the basic meta data needed for the armour item.
 * lvl = the item's level which would determine the costs and stat increases for it's current and further upgrades.
 * effects = the list of Effect classes which contain the item's effect(s) (i.e. their functionality).
 * 
 * @author Mason
 *
 */
public class Armour extends Item {

	private ArmourType type; //Enumerate of the specific Armour item that has its meta data
	private List<Effect> effects; //List of effects the Armour item has.
	private int level;

	/**
	 * Constructor for an Armour item. Takes in an Armourtype enumerate value which specifies the stat changes the
	 * armour item is to have as well as its name, costs and the ratio for increasing these when upgraded.
	 * @param type The ArmouType enumerate that has the stored meta data for the specific item to be created.
	 * @param level the level of the item to be created.
	 */
	public Armour(ArmourType type, int level) {
		this.effects = new ArrayList<>();
		this.type = type;
		this.level = level;
		this.effects.add(new DefenceEffect(getArmourValue(), getArmourHealth(), getMoveSpeed(), Target.SELF));
	}

	/**
	 * Gets the amount of Armour to change for the Armour item. This will
	 * change both the max Armour stat and the current Armour stats. The
	 * amount of armour is equal to the base armour value multiplied by by
	 * the  multiplier associated with an item of the  armour piece's level.
	 * 
	 * @return the amount of Armour the item will add on. Negative numbers will mean the Armour stats will be reduced.
	 */
	public int getArmourValue() {
		// returns armour value multiplied by the multiplier associated with
		// an item of the armor piece's level
		return Math.round(type.getArmourValue()*type.getItemLevelMultipliers
				()[level - 1]);
	}

	/**
	 * Gets the amount of health (i.e. the entity's health) the item will add on. This will change both the max Health
	 * and the current Health stats. The  amount of health is equal to the base health value multiplied by by
	 * the  multiplier associated with an item of the  armour piece's level.
	 * 
	 * @return the amount of Health the item will add on. Negative numbers will mean the Health stats will be reduced.
	 */
	public int getArmourHealth() {
		return Math.round(type.getArmourHealth()*type.getItemLevelMultipliers()[level - 1]);
	}
	
	/**
	 * Gets the amount of Movement Speed the item will add on. Higher
	 * movement speed means the entity will move faster. The  amount of
	 * movement speed is equal to the base movement value multiplied by by
	 * the multiplier associated with an item of the armour piece's level.
	 *  
	 * @return the amount of movement speed the item will add on. Negative numbers will mean Movement Speed stat will 
	 * decrease when applied.
	 */
	public float getMoveSpeed() {
		return type.getMoveSpeed()*type.getItemLevelMultipliers()[level - 1];
	}

	/**
	 * Gets a list of all the effects the Armour item has.
	 * 
	 * @return A new List of Effect objects that represent each effect of the item.
	 */
	public List<Effect> getEffect() {
		return new ArrayList<Effect>(effects);
	}

	/**
	 * Gets the item type of the Armour item.
	 * 
	 * @return The enumerate of Item Type that corresponds to Armour items.
	 */
	@Override
	public Type getItemType() {
		return Type.ARMOUR;
	}

	/**
	 * Gets the item level of the Armour item.
	 *
	 * @return int representing the level of the Armour item.
	 */
	public int getItemLevel() {	return level; }

	/**
	 * Sets the item level of the Armour item.
	 *
	 */
	public void setItemLevel(int level) { this.level = level; }

	/**
	 * Gets the name of the Armour item.
	 * 
	 * @return The name of the item as a string.
	 */
	@Override
	public String getName() {
		return type.getName();
	}

	/**
	 * Gets the description of the Armour item which specifies its effects and stat changes.
	 * 
	 *  @return The description of the item as a string.
	 */
	@Override
	public String getDescription() {
		return this.getName() + "\nArmour: " + this.getArmourValue() + "\nHealth: " + this.getArmourHealth()
				+ "\nMovementSpeed: " + this.getMoveSpeed();
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
	 * Override equals method so that equality is based on the ArmourType enumerate value that was used to make the
	 * armour item.
	 * @param object  The object to be compared.
	 * @return true if the ArmourType enumerate values are the same, false otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		if (this == object) {
			return true;
		}
		if (getClass() != object.getClass()) {
			return false;
		}
		return ((Armour) object).type == this.type;
	}

	/**
	 * The updated hashcode for this class.
	 *
	 * @return The updated hashcode for this class.
	 */
	@Override
	public int hashCode() {
		return type.hashCode();
	}
}