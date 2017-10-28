package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.entities.EntityStats;

/*
 * A Sniper unit, does longer range and more armour than a soldier 
 * has a slower attack speed instead
 * 
 */
public class Sniper extends Soldier {
	
	public Sniper(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		this.name = "Sniper";
		this.setFogRange(16);
		this.setAttributes();		
		setXRenderLength(2.2f);
		setYRenderLength(2.2f);
		setStance(3); // Default stance for sniper is skirmishing
	}
	/*
	 * Will override the default action of soldier on being attacked to attack the
	 * attacking unit instead (if possible), will run away if it can't reach it.
	 */
	
	
	/**
	 * @return The stats of the entity
	 */
	public EntityStats getStats() {
		return new EntityStats("Sniper", this.getHealth(),this.getMaxHealth(), null, this.getCurrentAction(), this);
	}
}
