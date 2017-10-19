package com.deco2800.marswars.entities.items.effects;

import com.deco2800.marswars.entities.units.AttackableEntity;

/**
 * Class that defines an effect that increases or decreases health of a target instantly for purposes of healing or 
 * damage (for item effects that instantly heal or instantly do damage). Damage here bypasses armour.
 * 
 * health = amount of health to heal or damage to deal
 * isDamage = boolean indicating whether if the effect would be a heal or damage (true if it is damage)
 * target = Target enumerate value indicating the intended target of this effect (see Effect interface for more detail).
 * @author Mason
 *
 */
public class HealthEffect implements Effect{
	private int health;
	private boolean isDamage;
	private Target target;
	
	/**
	 * Constructor for this effect. Sets the class fields with the parameters.
	 * @param health  amount of health to heal or damage to deal
	 * @param isDamage  boolean indicating whether if the effect would be a heal or damage (true if it is damage)
	 * @param target  Target enumerate value indicating the intended target of this effect.
	 */
	public HealthEffect(int health, boolean isDamage, Target target) {
		this.health = health;
		this.isDamage = isDamage;
		this.target = target;
	}
	
	/**
	 * Method to apply the effect which is to instantly heal or instant damage. For healing case, the effect can not 
	 * cause attackable entities targeted by this effect to have more hp than their max hp.
	 * @param entity  the target of this effect to apply it on.
	 */
	@Override
	public void applyEffect(AttackableEntity entity) {
		if (isDamage) {
			entity.setHealth(entity.getHealth() - this.health);
		} else { //can't heal more than the max hp of the target
			entity.setHealth(entity.getHealth() + this.health > entity.getMaxHealth()?entity.getMaxHealth() :
				entity.getHealth() + this.health);
		}
	}

	/**
	 * There should be no remove action for any health effect hence this method should do nothing.
	 * @param entity  Target with the effect currently activated on them to remove. But should not be able to remove
	 * damage or heal effects.
	 */
	@Override
	public void removeEffect(AttackableEntity entity) {
		if (isDamage) {
			entity.setHealth(entity.getHealth() + this.health > entity.getMaxHealth()?entity.getMaxHealth() :
				entity.getHealth() + this.health);
		} else { //can't heal more than the max hp of the target
			entity.setHealth(entity.getHealth() - this.health);
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
	 * Generates description stating how much this effect heals or damages (dependent on isDamage)
	 * @return string that says how much the effect heals/damages
	 */
	@Override
	public String generateDescription() {
		StringBuilder string = new StringBuilder("");
		if (!isDamage) {
			string.append("Heal: " + health + "\n");
		} else {
			string.append("Damage: " + health + "\n");
		}
		return string.toString();
	}

}
