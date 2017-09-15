package com.deco2800.marswars.entities.items.effects;

import com.deco2800.marswars.entities.HeroSpacman;
import com.deco2800.marswars.entities.units.AttackableEntity;

public class HealthEffect implements Effect{
	private int health;
	private boolean isDamage;
	
	public HealthEffect(int health, boolean isDamage) {
		this.health = health;
		this.isDamage = isDamage;
	}
	@Override
	public void applyEffect(AttackableEntity entity) {
		if (entity instanceof HeroSpacman) {
			HeroSpacman hero = (HeroSpacman) entity;
			
			if (isDamage) {
				hero.setHealth(hero.getHealth() - this.health);
			} else {
				hero.setHealth(hero.getHealth() + this.health > hero.getMaxHealth()?hero.getMaxHealth() : hero.getHealth() + this.health);
			}
		}
		
	}

	@Override
	public void removeEffect(AttackableEntity entity) {
		return; //there should be no remove action for any health effect
	}
	
	@Override
	public String generateDescription() {
		StringBuilder string = new StringBuilder("");
		if (isDamage) {
			string.append("Heal: " + health + "\n");
		} else {
			string.append("Damage: " + health + "\n");
		}
		return string.toString();
	}

}
