package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.entities.EntityStats;
import com.deco2800.marswars.managers.AbstractPlayerManager;

/*
 * A heavier combat unit, does longer range and more armour than a soldier 
 * intended to be slower, more expensive and and slower to fire
 * 
 */
public class TankDestroyer extends Soldier {
	
	public TankDestroyer(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		//this.movementSound = "tankMovementSound";
		
		// set all the attack attributes
		this.setMaxHealth(1000);
		this.setHealth(1000);
		this.setDamage(100);
		this.setArmor(500);
		this.setArmorDamage(200);
		this.setAttackRange(10);
		this.setAttackSpeed(10);
		//setAttributes();
		this.setAreaDamage(1);
		
	}
	/*
	 * Will override the default action of soldier on being attacked to attack the
	 * attacking unit instead (if possible), will run away if it can't reach it.
	 */
	
	
	/**
	 * @return The stats of the entity
	 */
	public EntityStats getStats() {
		return new EntityStats("TankDestroyer", this.getHealth(),this.getMaxHealth(), null, this.getCurrentAction(), this);
	}
}
