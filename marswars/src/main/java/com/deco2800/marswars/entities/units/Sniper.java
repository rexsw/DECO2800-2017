package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.entities.EntityStats;
import com.deco2800.marswars.managers.AbstractPlayerManager;

/*
 * A Sniper unit, does longer range and more armour than a soldier 
 * has a slower attack speed instead
 * 
 */
public class Sniper extends Soldier {
	
	public Tank(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		//this.movementSound = "tankMovementSound";
		
		// set all the attack attributes
		this.setMaxHealth(1000);
		this.setHealth(1000);
		this.setDamage(200);
		this.setArmor(200);
		this.setArmorDamage(200);
		this.setAttackRange(20);
		this.setAttackSpeed(5);
		this.setAreaDamage(0);
	}
	/*
	 * Will override the default action of soldier on being attacked to attack the
	 * attacking unit instead (if possible), will run away if it can't reach it.
	 */
	
	
	/**
	 * @return The stats of the entity
	 */
	public EntityStats getStats() {
		return new EntityStats("Sniper", this.getHealth(), null, this.getCurrentAction(), this);
	}
}
