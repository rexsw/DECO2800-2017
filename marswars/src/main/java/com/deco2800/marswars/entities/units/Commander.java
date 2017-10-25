package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.UseSpecialAction;
import com.deco2800.marswars.entities.EntityStats;
import com.deco2800.marswars.entities.Inventory;
import com.deco2800.marswars.entities.items.Item;
import com.deco2800.marswars.util.WorldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

// A reminder for coder, the connectHero function should called when hero has been spawned! like bind to hero factory

/**
 * A hero for the game previously called as HeroSpacman.
 * Class for the hero character that the player chooses at the start of the game. This unit will be different to other 
 * in that it will have skills and can equip items.
 * 
 * inventory = instance of the Inventory class to store items that the Commander has equipped.
 * currentAction = the Commander's action that it is currently taking.
 * 
 * 
 */
public class Commander extends Soldier {

	private Inventory inventory;
	private static final Logger LOGGER = LoggerFactory.getLogger(Commander.class);
	Optional<DecoAction> currentAction = Optional.empty();
	private boolean statsChange;
	private boolean itemInUse = false;

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
		this.setEntityType(EntityType.HERO);
		setAttributes();
		this.inventory = new Inventory(this);
		this.statsChange = true;
		this.setArmor(this.getMaxArmor());
	}

	/**
	 * Add an item to the commander's inventory
	 * 
	 * @param item to be added
	 * @return true if added successful, else false
	 */
	public boolean addItemToInventory(Item item) {
		return inventory.addToInventory(item);
	}

	/**
	 * Remove an item from the commander's inventory
	 * 
	 * @param item to be removed
	 * @return true if removed successful, else false
	 */
	public boolean removeItemFromInventory(Item item) {
		return inventory.removeFromInventory(item);
	}

	/** 
	 *	This method returns the inventory object or the items bag of this commander
	 * @return inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * Equals method of this class, at the moment, it only checks 3 things
	 * 1. is other object an instance of Commander, if no return false
	 * 2. is other object has the same toString result as this commander, if no return false
	 * 3. are these two object has the same owner, if no return false
	 * @return boolean whether they are the same commander
	 */
	@Override
	public boolean equals(Object other) { 
		if (other instanceof Commander) {
			return this.toString().equals(((Commander)other).toString()) && this.owner == ((Commander)other).owner;
		}
		return false;
	}
	
	/**
	 * Hashcode method of commander class
	 * Currently, it only checks if two commander got the same owner.
	 * and because there should be only one commander for each player
	 * so this hash code should works fine
	 * @return int hash code of this commander object
	 */
	@Override
	public int hashCode() { // need more hash later
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + getOwner();
	    return result;
	}
	
	/**
	 * Gets the stats of this commander
	 * @return The stats of the entity follow the parent class soldier
	 */
	public EntityStats getStats() {
		return new EntityStats("Commander", this.getHealth(), this.getMaxHealth(), null, this.getCurrentAction(), this);
	}
	
	/**
	 * Gets the name of this commander, should be 'Commander'
	 * @return The name of commander
	 */
	@Override
	public String toString(){
		return this.name;
	}
	
	/**
	 * The purpose of this method is to avoid hero character gets hacked by hacker
	 */
	@Override
	public void setLoyalty(int loyalty) {
		return;
	}
	
	/**
	 * The purpose of this method is to track if hero has bought a new item
	 * 
	 * @return true if there is item change, else no
	 */
	public boolean getStatsChange() {
		return statsChange;
	}
	
	/**
	 * The purpose of this method is update if hero has bought a new item
	 * 
	 * @param true if there is item change, else no
	 */
	public void setStatsChange(boolean bought) {
		this.statsChange = bought;
	}
	
	/**
	 * Override to fit in inventory's on tick method into Commander's. This is so that Special items can have the overlay
	 * and to allow the constraints that special items can only be used with a Commander alive.
	 * @param tick  the current game tick
	 */
	@Override
	public void onTick(int tick) {
		super.onTick(tick);
		this.inventory.onTick(tick);
	}
	
	
	/**
	 * Boolean to indicate whether an item is being used. Only really used for activated special items that need the 
	 * user to select an area.
	 * @return  true if an item (as specified above) is being used. False otherwise.
	 */
	public boolean isItemInUse() {
		return this.itemInUse;
	}
	
	/**
	 * Set method to set the boolean field to indicate whether an item is being used.
	 * @param set  the boolean to be set to.
	 */
	public void setItemInUse(boolean set) {
		this.itemInUse = set;
	}
	
	@Override
	public void onRightClick(float x, float y) {
		if (itemInUse && inventory.getCurrentAction().isPresent()) {
			UseSpecialAction action = (UseSpecialAction) inventory.getCurrentAction().get();
			action.execute();
			itemInUse = false;
			return;
		} else {
			super.onRightClick(x, y);
		}
	}
	
	@Override
	public void deselect() {
		super.deselect();
		if (itemInUse && inventory.getCurrentAction().isPresent()) {
			UseSpecialAction action = (UseSpecialAction) inventory.getCurrentAction().get();
			action.cancel();
			itemInUse = false;
			WorldUtil.removeOverlay();
		}
	}
}