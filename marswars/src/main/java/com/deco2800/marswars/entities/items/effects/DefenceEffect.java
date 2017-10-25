package com.deco2800.marswars.entities.items.effects;

import com.deco2800.marswars.entities.units.AttackableEntity;

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
			
			entity.setMaxArmor(entity.getMaxArmor() + this.armour);
			entity.setArmor(entity.getArmor() + this.armour);
			entity.setMaxHealth(entity.getMaxHealth() + this.health);
			entity.setHealth(entity.getHealth() + this.health);
			//only allows the resulting movement speed to be positive.
			entity.setSpeed(entity.getSpeed() + this.moveSpeed);
		
	}

	/**
	 * Method to remove this effect off of the provided entity. Should only be used on entities that have this effect 
	 * applied to them.
	 * @param entity  attackable entity with this effect applied to remove it from them.
	 */
	@Override
	public void removeEffect(AttackableEntity entity) {
			
		entity.setMaxArmor(entity.getMaxArmor() - this.armour > 0 ? entity.getMaxArmor() - this.armour : 1);
		entity.setArmor(entity.getArmor() - this.armour > 0? entity.getArmor() - this.armour : 1);
		entity.setMaxHealth(entity.getMaxHealth() - this.health > 0? entity.getMaxHealth() - this.health : 1);
		entity.setHealth(entity.getHealth() - this.health > 0? entity.getHealth() - this.health : 1);
		entity.setSpeed(entity.getSpeed() - this.moveSpeed > 0.01f ? entity.getSpeed() - this.moveSpeed : 0.01f);
			
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
		if (Math.abs(moveSpeed) > 0.000001) {
			string.append("Movement Speed: " + moveSpeed + "\n");
		}
		return string.toString();
	}

}
