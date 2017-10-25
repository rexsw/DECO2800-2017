package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.UseSpecialAction;
import com.deco2800.marswars.entities.items.Armour;
import com.deco2800.marswars.entities.items.Item;
import com.deco2800.marswars.entities.items.Special;
import com.deco2800.marswars.entities.items.Weapon;
import com.deco2800.marswars.entities.items.effects.Effect;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.renderers.Renderable;
import com.deco2800.marswars.util.Box3D;
import com.deco2800.marswars.util.WorldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class that defines the Inventory for a Commander. Each Commander will have their own instance of Inventory and only 
 * 1. The Inventory can only have at most 1 Weapon item, at most 1 Armour item and at most 4 special items. When the
 * Inventory already has a Weapon item or  an Armour item, these instances get replaced with the new items being added.
 * Also for Weapon and Armour items, their effects are immediately applied once they have been added, as well as removed
 * immediately when the items are removed from the Inventory. Adding more than 4 Special items to the inventoey will not
 * be allowed.
 * 
 * owner = the Instance of the Commander class that this instance of the Inventory class belongs to
 * armour = the Armour item that is currently equipped.
 * weapon = the Weapon item that is currently equipped.
 * specials = List of instances of the Special items that are currently equipped.
 * @author Mason
 *
 */
public class Inventory extends AbstractEntity implements HasAction, Tickable, Renderable{
	
	private Commander owner;
    private Armour armour;
    private Weapon weapon;
    private List<Special> specials;
    private static final Logger LOGGER = LoggerFactory.getLogger(Inventory.class);
    private Optional<DecoAction> currentAction = Optional.empty();

    /**
     * Constructor of the Inventory class to make a new instance of Inventory for the provided Commander class that has
     * no items stored in it.
     * @param owner  The Commander that would be the owner of this instance of Inventory
     */
    public Inventory(Commander owner) {
    	super(new Box3D(1, 1, 1, 1, 1, 1));
    	this.owner = owner;
        this.armour = null;
        this.weapon = null;
        this.specials = new ArrayList<Special>();
    }

    /**
     * Adds an item to the Inventory. Depending on the item type, the methods would add different stats when applied. 
     * Would immediately activate Weapon and Armour items upon equipping. Returns a boolean being true when the addition
     * to the Inventory state is successful, false otherwise.
     * @param item  The item that is to be added to the inventory.
     * @return  Boolean indicating whether the basic items passed theough the first test.
     */
    public boolean addToInventory(Item item) {
        switch (item.getItemType()) {
            case WEAPON:
                if (this.weapon != null) {
                	// remove the effect of the current weapon first
                	this.removeEffect(this.weapon, owner);
                }
                // replace with the new weapon
                this.weapon = (Weapon) item;
                // apply effect
                this.applyEffect(this.weapon, owner);
                return true;
            case ARMOUR:
            	if (this.armour != null) {
                	// remove the effect of the current weapon first
            		this.removeEffect(this.armour, owner);
                }
                // replace with the new armour
                this.armour = (Armour) item;
                // apply effect
                this.applyEffect(this.armour, owner);
                return true;
            default: //which should be SPECIAL, just fix code smell
            	if (this.specials.size() < 4) {
            		this.specials.add((Special)item);
            		return true;
            	}
                return false;
        }
    }

    /**
     * Removes an existing item the the Commander's inventory. If items being remove are Weapon or Armour items, then 
     * their effects should be deactivated.
     * @param item  The item to be removed from the inventory.
     * @return  Boolean indicating whether the removal was successful, false otherwise (including when no such item 
     * exists in there)
     */
    public boolean removeFromInventory(Item item) {
        switch (item.getItemType()) {
            case WEAPON:
                return removeWeapon(item);
            case ARMOUR:
                return removeArmour(item);
            default: //which should be SPECIAL, just fix code smell
                return this.specials.remove(item);
        }
    }
    
    /**
     * Tries to remove the given item as if it was a weapon.
     * @return true if the item was removed, false otherwise.
     */
    private boolean removeWeapon(Item item) {
        if ((weapon != null) && (weapon.equals(item))) {
            this.removeEffect(this.weapon, owner);
            this.weapon = null;
            return true;
        }
        return false;
    }
    
    /**
     * Tries to remove the given item as if it was armour.
     * @return true if the item was removed, false otherwise.
     */
    private boolean removeArmour(Item item) {
        if ((armour != null) && (armour.equals(item))) {
            this.removeEffect(this.armour, owner);
            this.armour = null;
            return true;
        }
        return false;
    }
    
    /**
     * Method to apply all the effects defined the the Effect classes in the Item class provided, to a target provided.
     * @param item  The item to apply the effect of.
     * @param target  The target to apply the effects to.
     */
    private void applyEffect(Item item, AttackableEntity target) {
    	for (Effect e: item.getEffect()) {
        	e.applyEffect(target);
        }
    }
    
    /**
     * Method to remove all the effects defined the the Effect classes in the Item class provided, to a target provided.
     * Note: this should only be used when it is known that applyEffect has been used on that character to be able to 
     * cancel the appliedEffect.
     * @param item  The item to remove the effect of.
     * @param target  The target to apply the effects to.
     */
    private void removeEffect(Item item, AttackableEntity target) {
    	for (Effect e: item.getEffect()) {
        	e.removeEffect(target);
        }
    }
    
    /**
     * Gets the current Weapon item stored in the Inventory. 
     * 
     * @return Weapon class that is stored in the Inventory. Will return null if there are none.
     */
    public Weapon getWeapon() {
    	return this.weapon;
    }
    
    /**
     * Gets the current Armour item stored in the Inventory.
     * 
     * @return Armour class that is stored in the Inventory. Will return null if there are none.
     */
    public Armour getArmour() {
    	return this.armour;
    }
    
    /**
     * Gets the current Special items stored in the Inventory as a List.
     * 
     * @return List of Special classes that is stored in the Inventory. List is empty if there are none stored in the
     * Inventory.
     */
    public List<Special> getSpecials() {
    	return this.specials;
    }

    /**
     * Method to handle functionality of Special items when they are used via are clicked on in the inventory bar in HUD
     *  to Activate the item (i.e. actually use it).
     *  
     *  @param special to be used
     */
    public void useItem(Special special) {
    	if(this.specials.contains(special)) {
    		WorldUtil.removeOverlay();
    		currentAction = Optional.of(new UseSpecialAction(special, owner));
    	} else {
    		LOGGER.error("***** Unrecognized " + special.getName());
    	}
    }
    
	@Override
	public Optional<DecoAction> getCurrentAction() {
		return currentAction;
	}

	@Override
	public void onTick(int tick) {
		if (currentAction.isPresent() && !currentAction.get().completed()) {
			currentAction.get().doAction();
		} else {
			currentAction = Optional.empty();
		}
	}
}
