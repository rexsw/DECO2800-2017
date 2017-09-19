package com.deco2800.marswars.entities;

import java.util.ArrayList;
import java.util.List;

import com.deco2800.marswars.entities.items.Armour;
import com.deco2800.marswars.entities.items.Item;
import com.deco2800.marswars.entities.items.Special;
import com.deco2800.marswars.entities.items.Weapon;
import com.deco2800.marswars.entities.items.effects.Effect;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Commander;

public class Inventory {
	
	private Commander owner;
    private Armour armour;
    private Weapon weapon;
    private List<Special> specials;

    public Inventory(Commander owner) {
    	this.owner = owner;
        this.armour = null;
        this.weapon = null;
        this.specials = new ArrayList<>();
    }

    public boolean addToInventory(Item item) {
    	//  design choice need to make here. 
    	// can weapon and armour be replaced OR only success add on null object
    	// currently implementation is replaced straight way
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
            case SPECIAL:
            	if (this.specials.size() < 4) {
            		this.specials.add((Special)item);
            		return true;
            	}
                return false;
            default:
            	return false;
        }
    }

    public boolean removeFromInventory(Item item) {
        switch (item.getItemType()) {
            case WEAPON:
                if (weapon != null) {
                	this.removeEffect(this.weapon, owner);
                    this.weapon = null;
                    return true;
                }
                return false;
            case ARMOUR:
                if (armour != null) {
                	this.removeEffect(this.armour, owner);
                    this.armour = null;
                    return true;
                }
                return false;
            case SPECIAL:
                return this.specials.remove(item);
            default: 
            	return false;
        }
    }
    
    public void applyEffect(Item item, AttackableEntity target) {
    	for (Effect e: item.getEffect()) {
        	e.applyEffect(target);
        }
    }
    
    public void removeEffect(Item item, AttackableEntity target) {
    	for (Effect e: item.getEffect()) {
        	e.removeEffect(target);
        }
    }
    
    public Weapon getWeapon() {
    	return this.weapon;
    }
    
    public Armour getArmour() {
    	return this.armour;
    }
    
    public List<Special> getSpecials() {
    	return this.specials;
    }
    // I think when user click item on the HUD GUI, it should be handled here. not too sure, need discuss
    public void useItem() {
    }

}
