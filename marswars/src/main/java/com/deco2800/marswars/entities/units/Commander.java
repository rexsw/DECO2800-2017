package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.actions.*;
import com.deco2800.marswars.entities.EntityStats;
import com.deco2800.marswars.entities.Inventory;
import com.deco2800.marswars.entities.items.*;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A hero for the game previously called as HeroSpacman.
 * Class for the hero character that the player chooses at the start of the game. This unit will be different to other 
 * in that it will have skills and can equip items.
 * 
 * inventory = instance of the Inventory class to store items that the Commander has equipped.
 * currentAction = the Commander's action that it is currently taking.
 * 
 * Created by timhadwen on 19/7/17.
 * Edited by Zeid Ismail on 8/09
 */
public class Commander extends Soldier {

	private Inventory inventory;
	private static final Logger LOGGER = LoggerFactory.getLogger(Commander.class);
	Optional<DecoAction> currentAction = Optional.empty();

	/**
	 * Constructor for the Commander in the specified location and with the specified owner (i.e. who controls the 
	 * Commander or which team it belongs to).
	 * @param posX  the Commander's X position in the world when spawned
	 * @param posY the Commander's Y position in the world when spawned
	 * @param posZ  the Commander's Z position in the world when spawned
	 * @param owner  the team id indicating which team the Commander belongs to a.k.a who controls this Commander.
	 */
	public Commander(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		LOGGER.debug("Create a commander for team: " + owner);
		this.name = "Commander";
		setAttributes();
		this.inventory = new Inventory(this);
	}

	public boolean addItemToInventory(Item item) {
		return inventory.addToInventory(item);
	}

	public boolean removeItemFromInventory(Item item) {
		return inventory.removeFromInventory(item);
	}

	/** not a to string, returns the inventory object itself
	 *
	 * @return inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public boolean equals(Object other) { 
		if (other instanceof Commander) {
			return this.toString().equals(((Commander)other).toString()) && this.owner == ((Commander)other).owner;
		}
		return false;
	}
	
	@Override
	public int hashCode() { // need more hash later
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + getOwner();
	    return result;
	}
	
	/**
	 * @return The stats of the entity
	 */
	public EntityStats getStats() {
		return new EntityStats("Commander", this.getHealth(), this.getMaxHealth(), null, this.getCurrentAction(), this);
	}
	
	@Override
	public String toString(){
		return this.name;
	}
	
	@Override
	public void setLoyalty(int loyalty) {
		return;
	}
	
}