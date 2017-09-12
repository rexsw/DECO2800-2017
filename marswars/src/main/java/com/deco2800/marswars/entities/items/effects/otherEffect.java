package com.deco2800.marswars.entities.items.effects;

import com.deco2800.marswars.entities.units.AttackableEntity;

public class otherEffect implements Effect{
	private int loyalty;
	private int vision;
	
	// don't do anything to these two yet, need clarification
	public otherEffect(int loyalty, int vision) {
		this.loyalty = loyalty; 
		this.vision = vision;
	}
	@Override
	public void applyEffect(AttackableEntity entity) {
		return;
	}

	@Override
	public void removeEffect(AttackableEntity entity) {
		return;
	}

}
