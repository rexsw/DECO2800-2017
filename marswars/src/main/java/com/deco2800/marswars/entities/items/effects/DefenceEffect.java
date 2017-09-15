package com.deco2800.marswars.entities.items.effects;

import com.deco2800.marswars.entities.Commander;
import com.deco2800.marswars.entities.units.AttackableEntity;

public class DefenceEffect implements Effect{
	private int armour;
	private int health;
	private float moveSpeed;
	
	public DefenceEffect(int armour, int health, float moveSpeed) {
		this.armour = armour;
		this.health = health;
		this.moveSpeed = moveSpeed;
	}
	
	@Override
	public void applyEffect(AttackableEntity entity) {
		if (entity instanceof Commander) {
			Commander hero = (Commander) entity;
			
			hero.setMaxArmor(hero.getMaxArmor() + this.armour);
			hero.setArmor(hero.getArmor() + this.armour);
			hero.setMaxHealth(hero.getMaxHealth() + this.health);
			hero.setHealth(hero.getHealth() + this.health);
			//only allows the resulting movement speed to be positive.
			hero.setSpeed(hero.getSpeed() + this.moveSpeed > 0 ? hero.getSpeed() + this.moveSpeed : 1); 
		}
		
	}

	@Override
	public void removeEffect(AttackableEntity entity) {
		if (entity instanceof Commander) {
			Commander hero = (Commander) entity;
			
			hero.setMaxArmor(hero.getMaxArmor() - this.armour);
			hero.setArmor(hero.getArmor() > this.armour ? hero.getArmor() - this.armour : 1);
			hero.setMaxHealth(hero.getMaxHealth() - this.health);
			hero.setHealth(hero.getHealth() > this.health ? hero.getHealth() - this.health : 1);
			hero.setSpeed(hero.getMoveSpeed() > this.moveSpeed ? hero.getSpeed() - this.moveSpeed : 1);
		}
	}
	
	@Override
	public String generateDescription() {
		StringBuilder string = new StringBuilder("");
		if (armour != 0) {
			string.append("Armour: " + armour + "\n");
		}
		if (health != 0) {
			string.append("Max Health: " + health + "\n");
		}
		if (moveSpeed != 0) {
			string.append("Movement Speed: " + moveSpeed + "\n");
		}
		return string.toString();
	}

}
