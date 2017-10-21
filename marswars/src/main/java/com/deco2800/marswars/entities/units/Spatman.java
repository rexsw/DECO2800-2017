package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.entities.EntityStats;

public class Spatman extends Soldier {

	private float slowMovementSpeed;
	
	public Spatman(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		this.name = "Spatman";
		this.setAttributes();
		this.slowMovementSpeed = 0.01f;
		this.setAreaDamage(2);
	}
	
	public float getSlowMovementkSpeed() {
		return slowMovementSpeed;
	}

	/**
	 * @return The stats of the entity
	 */
	public EntityStats getStats() {
		return new EntityStats("Spatman", this.getHealth(),this.getMaxHealth(), null, this.getCurrentAction(), this);
	}
	
}
