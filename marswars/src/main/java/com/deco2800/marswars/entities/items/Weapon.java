package com.deco2800.marswars.entities.items;

import java.util.ArrayList;
import java.util.List;

import com.deco2800.marswars.entities.items.effects.AttackEffect;
import com.deco2800.marswars.entities.items.effects.Effect;

/**
 * Class for Attack items. Attack items would be passive items that have offensive passive effects. These effect are not
 * limited to simple stat changes. Stats that Attack items can change are damage (normal and armour damage), the range
 * that the user can attack the enemy from and attack speed of the units..
 * 
 * type = the Weapon Type enumerate value which stores the basic meta data needed for the weapon item.
 * lvl = the item's level which would determine the costs and stat increases for it's current and further upgrades.
 * effects = the list of Effect classes which contain the item's effect(s) (i.e. their functionality).
 * 
 * @author Mason
 *
 */
public class Weapon extends Item {
    private WeaponType type;
    private List<Effect> effects;

    /**
	 * Constructor for an Weapon item. Takes in an Weapontype enumerate value which specifies the stat changes the
	 * Weapon item is to have as well as its name, costs and the ratio for increasing these when upgraded.
	 * @param type The ArmouType enumerate that has the stored meta data for the specific item to be created.
	 * @param lvl the level of the item to be created.
	 */
    public Weapon(WeaponType type) {
    	this.effects = new ArrayList<>();
        this.type = type;
        this.effects.add(new AttackEffect(getWeaponDamage(), getWeaponSpeed(), getWeaponRange()));
    }

    /**
	 * Gets the current damage (normal and armour damage) the item will add on. Takes into account the multiplier based 
	 * on the item's current level. 
	 * @return the current amount of damage (normal and armour damage) that the item will add on.
	 */
    public int getWeaponDamage() {
        return type.getWeaponDamage();
    }
    
    /**
	 * Gets the current attack range the item will add on. Takes into account the multiplier based on the item's current
	 *  level. 
	 * @return the current range of the damage change that the item will add on.
	 */
    public int getWeaponRange() {
    	return type.getWeaponRange();
    }
    
    /**
	 * Gets the current attack speed the item will add on. Takes into account the multiplier based on the item's current
	 *  level. 
	 * @return the current amount attack speed that the item will add on.
	 */
    public int getWeaponSpeed() {
    	return type.getWeaponSpeed();
    }
    
    /**
	 * Gets a list of all the effects the Weapon item has.
	 * 
	 * @return A new List of Effect objects that represent each effect of the item.
	 */
    @Override
    public List<Effect> getEffect() {
    	return new ArrayList<Effect>(effects);
    }
    
    /**
	 * Gets the item type of the Weapon item.
	 * 
	 * @return The enumerate of Item Type that corresponds to Weapon items.
	 */
    @Override
    public Type getItemType() {
        return Type.WEAPON;
    }

    /**
	 * Gets the item name
	 * 
	 * @return string that is the item name
	 */
    @Override
    public String getName() {
        return type.getName();
    }
    
    /**
	 * Gets the description of the Weapon item which specifies its effects and stat changes.
	 * 
	 *  @return The description of the item as a string.
	 */
    @Override
    public String getDescription() {
		return this.getName() + "\nDamage: " + this.getWeaponDamage() + "\nSpeed: " + this.getWeaponSpeed() + "\nRange: " + this.getWeaponRange();
    	
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
}

