package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.managers.AbstractPlayerManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TechnologyManager;

/*
 * A medical combat unit, heal friend units. not engaged in fighting.
 * will heal enemy if loyalty changed.
 * 
 */
public class Healer extends Soldier {

	public Healer(float posX, float posY, float posZ, AbstractPlayerManager owner) {
		super(posX, posY, posZ, owner);

		//invoking the techManager
		TechnologyManager t = (TechnologyManager) GameManager.get().getManager(TechnologyManager.class);

		this.movementSound = "tankMovementSound";
		
		// set all the attack attributes
//		this.setMaxHealth(500);
//		this.setHealth(500);
//		this.setDamage(-25);
//		this.setArmor(200);
//
//		//this.setArmorDamage(150);
//		this.setAttackRange(10);
//		this.setAttackSpeed(20);

		//Calling attributes from the  techmanager unit attributes map
		this.setMaxHealth(t.unitAttributes.get("Healer")[1]);
		this.setHealth(t.unitAttributes.get("Healer")[1]);
		this.setDamage(t.unitAttributes.get("Healer")[2]);
		this.setArmor(t.unitAttributes.get("Healer")[3]);
		this.setArmorDamage(t.unitAttributes.get("Healer")[4]);
		this.setAttackRange(t.unitAttributes.get("Healer")[5]);
		this.setAttackSpeed(t.unitAttributes.get("Healer")[6]);
	}
	/*
	 * Will override the default action of soldier on being attacked to attack the
	 * attacking unit instead (if possible), will run away if it can't reach it.
	 */
}
