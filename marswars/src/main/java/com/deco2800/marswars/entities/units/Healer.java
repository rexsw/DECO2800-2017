package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.managers.AbstractPlayerManager;

/*
 * A medical combat unit, heal friend units. not engaged in fighting.
 * will heal enemy if loyalty changed.
 * 
 */
public class Healer extends Soldier {

	public Healer(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		//this.movementSound = "tankMovementSound";
		
		// set all the attack attributes
		this.setMaxHealth(500);
		this.setHealth(500);
		this.setDamage(-25);
		this.setArmor(200);
		//this.setArmorDamage(150);
		this.setAttackRange(10);
		this.setAttackSpeed(20);
	}
	/*
	 * Will override the default action of soldier on being attacked to attack the
	 * attacking unit instead (if possible), will run away if it can't reach it.
	 */
}
