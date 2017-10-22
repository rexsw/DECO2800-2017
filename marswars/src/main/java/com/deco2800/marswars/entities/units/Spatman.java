package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.entities.EntityStats;

public class Spatman extends Soldier {

	private float slowMovementSpeed;
	
	public Spatman(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		setXRenderLength(2.2f);
		setYRenderLength(2.2f);
		this.name = "Spatman";
		this.setAttributes();
		this.slowMovementSpeed = 0.02f;
		this.setAreaDamage(3);
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
