package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.entities.EntityStats;

/*
 * A heavier combat unit, does longer range and more armour than a soldier 
 * intended to be slower, more expensive and and slower to fire
 * 
 */
public class Tank extends Soldier {
	
	public Tank(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		//this.movementSound = "tankMovementSound";
		
		// set all the attack attributes
	    
		this.setMaxHealth(2500);
		this.setHealth(2500);
		this.setDamage(100);
		this.setMaxArmor(1500);
		this.setArmor(1500);
		this.setArmorDamage(150);
		this.setAttackRange(10);
		this.setAttackSpeed(20);
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
		return new EntityStats("Tank", this.getHealth(),this.getMaxHealth(), null, this.getCurrentAction(), this);
	}
}
