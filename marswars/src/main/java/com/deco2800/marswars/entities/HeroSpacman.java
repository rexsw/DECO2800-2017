package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.*;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.worlds.AbstractWorld;
import com.deco2800.marswars.entities.items.Effect;
import com.deco2800.marswars.entities.items.Item;
import com.deco2800.marswars.entities.items.Armour;
import com.deco2800.marswars.entities.items.Weapon;
import com.deco2800.marswars.entities.items.ActiveItem;
import com.deco2800.marswars.entities.items.PassiveItem;

import java.util.ArrayList;
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
		this.health = 100;
		this.armour = 100;
		this.attackRange = 5;
		this.attackSpeed = 10;
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

	public Effect getEffect(Item item) {
		return item.getEffect();
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
}