package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.entities.EntityStats;

/*
 * A heavier combat unit, does longer range and more armour than a soldier 
 * intended to be slower, more expensive and and slower to fire
 * 
 */
public class TankDestroyer extends Soldier {
	
	public TankDestroyer(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		this.name = "TankDestroyer";
		this.setAttributes();
		setXRenderLength(2.2f);
		setYRenderLength(2.2f);
		setStance(3); // Default stance for tank destroyer is skirmishing
		
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
