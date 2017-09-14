package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.*;
import com.deco2800.marswars.entities.items.*;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.worlds.AbstractWorld;

import java.util.Optional;

/**
 * A hero for the game
 * Created by timhadwen on 19/7/17.
 * Edited by Zeid Ismail on 8/09
 */
public class HeroSpacman extends BaseEntity implements Tickable, Clickable,
		HasAttackRange, HasAttackSpeed, HasHealth {
	int xp;
	int armour;
	int health;
	// additive, ie weapon damage adds on to base value
	int weaponDamage;
	int attackRange;
	int attackSpeed;
	Inventory inventory;


	Optional<DecoAction> currentAction = Optional.empty();

	/**
	 * Constructor for a hero
	 *
	 * @param world
	 * @param posX
	 * @param posY
	 * @param posZ
	 */

	public HeroSpacman(AbstractWorld world, float posX, float posY,
					   float posZ) {
		super(posX, posY, posZ, 1, 1f, 1f);
		this.setTexture("spacman_red");
		this.setEntityType(EntityType.HERO);
		//default values
		this.health = 100;
		this.armour = 100;
		this.weaponDamage = 100;
		this.attackRange = 2;
		this.attackSpeed = 100;
		this.xp = 0;
		this.inventory = new Inventory();
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

	public int getHealth() {
		return this.health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getArmor() {
		return this.armour;
	}

	public void setArmor(int armour) {
		this.armour = armour;
	}

	public int getAttackRange() {
		return attackRange;
	}

	public void setAttackRange(int attackRange) {
		this.attackRange = attackRange;
	}

	public int getAttackSpeed() {
		return attackSpeed;
	}

	public void setAttackSpeed(int attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	public void addItemToInventory(Item item) {
		inventory.addToInventory(item);
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

	public boolean getActivation (ActiveItem activeItem) {
		return activeItem.getActivation();
	}

	public void activateItem(ActiveItem activeItem) {
		activeItem.activateItem();
	}

	public void deactivateItem(ActiveItem activeItem) {
		activeItem.deactivateItem();
	}

	// used when button in inventory clicked to wear armour
	public void applyArmour(Armour armour) {
		this.armour += armour.getArmourValue();
	}

	// used when button in inventory clicked to wear weapon
	public void applyWeapon(Weapon weapon) {
		this.weaponDamage += weapon.getWeaponDamage();
		this.attackSpeed += weapon.getWeaponSpeed();
		this.attackRange += weapon.getWeaponRange();
	}

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