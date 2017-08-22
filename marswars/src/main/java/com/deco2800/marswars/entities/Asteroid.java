package com.deco2800.marswars.entities;

import java.util.Optional;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.managers.MouseHandler;

/**
 * A generic player instance for the game
 * @author Tze Thong Khor on 22/8/18
 *
 */
public class Asteroid extends BaseEntity implements Tickable, Clickable, HasHealth, HasArmor, 
	HasAttackRange, HasDamage{
	
	private Optional<DecoAction> currentAction = Optional.empty();
	
	private int health;
	private int armor;
	private int attackRange;
	private int damage;

	public Asteroid(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 2, 2, 2);
		this.setTexture("spatman_yellow"); // just for testing
	}

	@Override
	public void onClick(MouseHandler handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRightClick(float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTick(int tick) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getAttackRange() {
		return attackRange;
	}

	@Override
	public void setAttackRange(int attackRange) {
		this.attackRange = attackRange;
	}

	@Override
	public int getArmor() {
		return armor;
	}

	@Override
	public void setArmor(int armor) {
		this.armor = armor;
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public void setHealth(int health) {
		this.health = health;
	}

	@Override
	public int getDamage() {
		return damage;
	}

	@Override
	public void setDamage(int damage) {
		this.damage = damage;
	}

}
