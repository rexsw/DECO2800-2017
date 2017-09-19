package com.deco2800.marswars.entities.items.effects;

import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Commander;

/**
 * Class that defines an effect that increases or decreases health of a target instantly for purposes of healing or 
 * damage (for item effects that instantly heal or instantly do damage).
 * 
 * health = amount of health to heal or damage to deal
 * isDamage = boolean indicating whether if the effect would be a heal or damage (true if it is damage)
 * @author Mason
 *
 */
public class HealthEffect implements Effect{
	private int health;
	private boolean isDamage;
	
	/**
	 * Constructor for this effect. Sets the class fields with the parameters.
	 * @param health  amount of health to heal or damage to deal
	 * @param isDamage  boolean indicating whether if the effect would be a heal or damage (true if it is damage)
	 */
	public HealthEffect(int health, boolean isDamage) {
		this.health = health;
		this.isDamage = isDamage;
	}
	
	/**
	 * Method to apply the effect which is to instantly heal or instant damage. For healing case, the effect can not 
	 * cause attackable entities targeted by this effect to have more hp than their max hp.
	 * @param entity  the target of this effect to apply it on.
	 */
	@Override
	public void applyEffect(AttackableEntity entity) {
		if (entity instanceof Commander) { //only allowing changes on Commander for testing purposes at this stage.
			Commander hero = (Commander) entity;		
			if (isDamage) {
				hero.setHealth(hero.getHealth() - this.health);
			} else { //can't heal more than the max hp of the target
				hero.setHealth(hero.getHealth() + this.health > hero.getMaxHealth()?hero.getMaxHealth() :
					hero.getHealth() + this.health);
			}
		}
		
	}

	/**
	 * There should be no remove action for any health effect hence this method should do nothing.
	 * @param entity  Target with the effect currently activated on them to remove. But should not be able to remove
	 * damage or heal effects.
	 */
	@Override
	public void removeEffect(AttackableEntity entity) {
		return;
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
