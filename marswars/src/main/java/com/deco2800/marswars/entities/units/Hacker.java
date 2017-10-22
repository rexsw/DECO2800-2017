package com.deco2800.marswars.entities.units;

/**
 * A combat unit that deal loyalty damage to enemy and convert enemy
 * to its own side. 
 * @author User
 *
 */

public class Hacker extends Soldier {

	public Hacker(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		this.name ="Hacker";
		this.setAttributes();
		setStance(1); // Default stance for hacker is defensive
	}

	
}