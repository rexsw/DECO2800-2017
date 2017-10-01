package com.deco2800.marswars.entities.items.effects;

import com.deco2800.marswars.entities.items.effects.Effect.Target;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Commander;

/**
 * Class defines effect that increases defensive stats of a unit i.e. armour, max armour, hp, max hp and movement speed.
 * 
 * armour = amount of to add to armour and max armour stats
 * health = amount to add to hp and max hp stats
 * movementSpeed = amount to add to movement speed stat
 * target = Target enumerate value indicating the intended target of this effect (see Effect interface for more detail).
 * 
 * @author Mason
 *
 */
public class DefenceEffect implements Effect{
	private int armour;
	private int health;
	private float moveSpeed;
	private Target target;
	
	/**
	 * Constructor for the defence effect. Class fields would be set here.
	 * @param armour  amount of to add to armour and max armour stats
	 * @param health  amount to add to hp and max hp stats
	 * @param moveSpeed  amount to add to movement speed stat
	 */
	public DefenceEffect(int armour, int health, float moveSpeed, Target target) {
		this.armour = armour;
		this.health = health;
		this.moveSpeed = moveSpeed;
		this.target = target;
	}
	
	/**
	 * Method to apply the additions to the defence stats to the provided attackable entity
	 * @param entity  The attackable entity to receive the effect
	 */
	@Override
	public void applyEffect(AttackableEntity entity) {
		if (entity instanceof Commander) {//only allowing changes on Commander for testing purposes at this stage.
			Commander hero = (Commander) entity;
			
			hero.setMaxArmor(hero.getMaxArmor() + this.armour);
			hero.setArmor(hero.getArmor() + this.armour);
			hero.setMaxHealth(hero.getMaxHealth() + this.health);
			hero.setHealth(hero.getHealth() + this.health);
			//only allows the resulting movement speed to be positive.
			hero.setSpeed(hero.getSpeed() + this.moveSpeed > 0 ? hero.getSpeed() + this.moveSpeed : 0.0001f);
		}
		
	}

	/**
	 * Method to remove this effect off of the provided entity. Should only be used on entities that have this effect 
	 * applied to them.
	 * @param entity  attackable entity with this effect applied to remove it from them.
	 */
	@Override
	public void removeEffect(AttackableEntity entity) {
		if (entity instanceof Commander) {//only allowing changes on Commander for testing purposes at this stage.
			Commander hero = (Commander) entity;
			
			hero.setMaxArmor(hero.getMaxArmor() > this.armour ? hero.getMaxArmor() - this.armour : 1);
			hero.setArmor(hero.getArmor() > this.armour ? hero.getArmor() - this.armour : 1);
			hero.setMaxHealth(hero.getMaxHealth() > this.health ? hero.getMaxHealth() - this.health : 1);
			hero.setHealth(hero.getHealth() > this.health ? hero.getHealth() - this.health : 1);
			hero.setSpeed(hero.getSpeed() > this.moveSpeed ? hero.getSpeed() - this.moveSpeed : 0.01f);
			
		}
	}
	
	/**
	 * Returns the intended target of this effect as a Target enumerate value.
	 * @return Target enumerate value corresponding to the intended target of this effect.
	 */
	@Override
	public Target getTarget() {
		return this.target;
	}
	
	/**
	 * Method to create a string that describes the effect i.e. says that this effect increases armour, health and 
	 * movement speed.
	 * @return string stating how much will be added onto armour, health and movement speed by this effect. 
	 */
	@Override
	public String generateDescription() {
		StringBuilder string = new StringBuilder("");
		if (armour != 0) {
			string.append("Armour: " + armour + "\n");
		}
		if (health != 0) {
			string.append("Max Health: " + health + "\n");
		}
		if (Math.abs(moveSpeed) < 0.000001) {
			string.append("Movement Speed: " + moveSpeed + "\n");
		}
		return string.toString();
	}

}
