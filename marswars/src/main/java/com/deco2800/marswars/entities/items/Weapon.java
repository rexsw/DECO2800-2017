package com.deco2800.marswars.entities.items;

import com.deco2800.marswars.entities.items.effects.AttackEffect;
import com.deco2800.marswars.entities.items.effects.Effect;
import com.deco2800.marswars.entities.items.effects.Effect.Target;

import java.util.ArrayList;
import java.util.List;

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
 * @author Z
 *
 */
public class Weapon extends Item {
    private WeaponType type;
    private List<Effect> effects;
    private int level;

    /**
	 * Constructor for an Weapon item. Takes in an Weapontype enumerate value which specifies the stat changes the
	 * Weapon item is to have as well as its name, costs and the ratio for increasing these when upgraded.
	 * @param type The ArmouType enumerate that has the stored meta data for the specific item to be created.
	 * @param level the level of the item to be created.
	 */
    public Weapon(WeaponType type, int level) {
    	this.effects = new ArrayList<>();
        this.type = type;
        this.level = level;
        this.effects.add(new AttackEffect(getWeaponDamage(), getWeaponSpeed(), getWeaponRange(), Target.SELF));
    }

    /**
	 * Gets the current damage (normal and armour damage) the item will add on. Takes into account the multiplier based 
	 * on the item's current level. 
	 * @return the current amount of damage (normal and armour damage) that the item will add on.
	 */
    public int getWeaponDamage() {
        return Math.round(type.getWeaponDamage()*type.getItemLevelMultipliers()
				[level - 1]);
    }
    
    /**
	 * Gets the current attack range the item will add on. Takes into account the multiplier based on the item's current
	 *  level. 
	 * @return the current range of the damage change that the item will add on.
	 */
    public int getWeaponRange() {
		return Math.round(type.getWeaponRange() * type.getItemLevelMultipliers()
				[level - 1]);
	}

    /**
	 * Gets the current attack speed the item will add on. Takes into account the multiplier based on the item's current
	 *  level. 
	 * @return the current amount attack speed that the item will add on.
	 */
    public int getWeaponSpeed() {

		return Math.round(type.getWeaponSpeed()*type.getItemLevelMultipliers()
				[level - 1]);
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
	 * Gets the item level of the Weapon item.
	 *
	 * @return int representing the level of the Weapon item.
	 */
	public int getItemLevel() {	return level; }

	/**
	 * Sets the item level of the Weapon item.
	 *
	 */
	public void setItemLevel(int level) { this.level = level; }

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
	
	/**
	 * Override equals method so that equality is based on the WeaponType enumerate value that was used to make the
	 * weapon item. Class cast exceptions not caught so that it would be easier to debug.
	 * @param object  The object to be compared.
	 * @return true if the WeaponType enumerate values are the same, false otherwise
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
		return ((Weapon) object).type == this.type;
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

