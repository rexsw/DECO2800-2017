package com.deco2800.marswars.entities.units;
/*
 * A heavier combat unit, does longer range and more armour than a soldier 
 * intended to be slower, more expensive and and slower to fire
 * 
 */
public class Tank extends Soldier {
	
	public Tank(float posX, float posY, float posZ) {
		super(posX, posY, posZ);
		this.defaultTextureName = "tank";
		//this.movementSound = "tankMovementSound";
		this.selectedTextureName = "tankSelected";
		
		// set all the attack attributes
		this.setMaxHealth(1000);
		this.setHealth(1000);
		this.setDamage(75);
		this.setArmor(500);
		this.setArmorDamage(150);
		this.setAttackRange(10);
		this.setAttackSpeed(20);
	}
	/*
	 * Will override the default action of soldier on being attacked to attack the
	 * attacking unit instead (if possible), will run away if it can't reach it.
	 */

}
