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

	public Healer(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		this.name = "Healer";
		this.movementSound = "tankMovementSound";

		setAttributes();
	}
	/*
	 * Will override the default action of soldier on being attacked to attack the
	 * attacking unit instead (if possible), will run away if it can't reach it.
	 */
}
