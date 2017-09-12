package com.deco2800.marswars.entities.items.effects;

import com.deco2800.marswars.entities.units.AttackableEntity;

public interface Effect {
	
	void applyEffect(AttackableEntity entity);
	void removeEffect(AttackableEntity entity);
	String generateDescription();
}
