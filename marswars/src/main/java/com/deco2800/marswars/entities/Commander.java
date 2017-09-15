package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.*;
import com.deco2800.marswars.entities.items.*;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.managers.MouseHandler;

import java.util.Optional;

/**
 * A hero for the game
 * Created by timhadwen on 19/7/17.
 * Edited by Zeid Ismail on 8/09
 */
public class Commander extends Soldier {

	Inventory inventory;


	Optional<DecoAction> currentAction = Optional.empty();

	public Commander(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		this.setTexture("spacman_red");
		this.setEntityType(EntityType.HERO);
		
		this.addNewAction(ActionType.DAMAGE);
		this.addNewAction(ActionType.MOVE);
		//default values
		this.setMaxHealth(1000);
		this.setHealth(1000);
		this.setDamage(100);
		this.setArmor(500);
		this.setArmorDamage(100);
		this.setAttackRange(10);
		this.setAttackSpeed(30);
		this.setSpeed(0.05f);
		
		this.inventory = new Inventory(this);
	}

	@Override
	public void onTick(int i) {
		if (!currentAction.isPresent()) {
			return;
		}

		if (!currentAction.get().completed()) {
			currentAction.get().doAction();
		}
	}

	@Override
	public void onClick(MouseHandler handler) {
		this.makeSelected();
		handler.registerForRightClickNotification(this);
	}

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