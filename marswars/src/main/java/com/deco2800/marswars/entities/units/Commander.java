package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.actions.*;
import com.deco2800.marswars.entities.Inventory;
import com.deco2800.marswars.entities.items.*;
import com.deco2800.marswars.managers.MouseHandler;

import java.util.Optional;

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

	Inventory inventory;


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
		//this.name = "Commander";
		//this.setAllTextture();
		//this.setTexture("spacman_red");
		//this.setEntityType(EntityType.HERO);
		
//		this.addNewAction(ActionType.DAMAGE);
//		this.addNewAction(ActionType.MOVE);
		//default values
//		setAttributes();
		
		this.name = "Commander";
		setAttributes();
		this.inventory = new Inventory(this);
	}

	/**
	 * Method to check, execute and update the Commander's actions for the Commander to actually do and appear to do 
	 * the assigned current action.
	 * @param i  The current game tick.
	 */
	@Override
	public void onTick(int i) {
		if (!currentAction.isPresent()) { //no need to update or do anything if there already is no assigned action
			return;
		}
		//Do the assigned action if it's not completed already.
		if (!currentAction.get().completed()) {
			currentAction.get().doAction();
		}
	}

	/**
	 * Method to make handle the changes signaling HUD listeners when clicked on Commander to allow for displaying 
	 * difference options to control the Commander with the mouse (also handles th backend side of this i.e. changing 
	 * the state of the Commander to be selected).
	 * @param handler  The MouseHandler instance for the game which provides the medium to obtain information about 
	 * player mouse movement and clicks.
	 */
	@Override
	public void onClick(MouseHandler handler) {
		this.makeSelected();
		handler.registerForRightClickNotification(this);
	}

	/**
	 * Method to provide functionality for the right mouse button click once the Commander has been selected.
	 * 
	 */
	@Override
	public void onRightClick(float x, float y) {
		currentAction = Optional.of(new MoveAction((int) x, (int) y, this));
	}

	@Override
	public boolean isSelected() {
		return false;
	}

	@Override
	public void deselect() {
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
	public boolean equals(Object other) { // need more compare later
		if (other instanceof Commander) {
			return this.toString().equals(((Commander)other).toString());
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
	
	@Override
	public String toString(){
		return this.name;
	}
	
//	public boolean getActivation (Item activeItem) {
//		return activeItem.getActivation();
//	}
//
//	public void activateItem(Item activeItem) {
//		activeItem.activateItem();
//	}
//
//	public void deactivateItem(ActiveItem activeItem) {
//		activeItem.deactivateItem();
//	}

//	// used when button in inventory clicked to wear armour
//	public void applyArmour(Armour armour) {
//		this.armour += armour.getArmourValue();
//	}
//
//	// used when button in inventory clicked to wear weapon
//	public void applyWeapon(Weapon weapon) {
//		this.weaponDamage += weapon.getWeaponDamage();
//		this.attackSpeed += weapon.getWeaponSpeed();
//		this.attackRange += weapon.getWeaponRange();
//	}

	// used when button in inventory clicked to assign special item to the hero
//    public void applySpecial(Special special) {
//		// need to finish this
//		String type = special.getSpecialType();
//		switch(type) {
//			case "Heal":
//				// do stuff
//				break;
//			case "Damage":
//				// do stuff
//				break;
//			case "Speed":
//				// do stuff
//				break;
//			default:
//				// anything here??
//				break;
//		}
//	}



}