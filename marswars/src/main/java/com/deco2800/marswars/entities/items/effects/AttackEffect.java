package com.deco2800.marswars.entities.items.effects;

import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Commander;


/**
 * Class that defines an effect that increases the offensive stats of a unit i.e. damage, armour damage, attack speed
 * and attack range of the unit. It should be noted that armour damage is not directly increased in this class, but 
 * rather only increased by 25% of the damage (rounded to integer) to be added to unit by this effect class. Negative
 * values are allows to be parsed in, simply means that their stats are reduced rather than increased,
 * 
 * attackDamage = amount to add onto damage stat
 * attackSpeed = amount to add onto attack speed stat
 * attackRange = amount to add on to attack range stat
 * armourDamage = amount to add onto armour damage stat (25% of attackDamage rounded to integer)
 * target = Target enumerate value indicating the intended target of this effect (see Effect interface for more detail).
 * 
 * @author Mason
 *
 */
public class AttackEffect implements Effect{
	private int attackDamage;
	private int attackSpeed;
	private int attackRange;
	private int armourDamage;
	private Target target;
	
	/**
	 * Constructor for AttackEffect. armourDamage will be calculated here.
	 * @param damage  amount to add onto damage stat
	 * @param damageSpeed  amount to add onto attack speed stat 
	 * @param damageRange  amount to add on to attack range stat
	 */
	public AttackEffect(int damage, int damageSpeed, int damageRange, Target target) {
		this.attackDamage = damage;
		this.attackSpeed = damageSpeed;
		this.attackRange = damageRange;
		this.armourDamage = (int) (damage * 0.25);
		this.target = target;
	}
	
	/**
	 * Method to activate the item's effect which in this case is to add the stored stats to the provided attackable 
	 * entity. Cannot make the unit's stats drop to 0 or below.
	 * 
	 * @param entity  The entity to apply the effect to. 
	 */
	@Override
	public void applyEffect(AttackableEntity entity) {
		if (entity instanceof Commander) { //only allowing changes on Commander for testing purposes at this stage.
			Commander hero = (Commander) entity;
			//for below, only add stats if the resulting stat is positive, otherwise set to 1
			hero.setDamage(hero.getDamageDeal() + this.attackDamage > 0 ? hero.getDamageDeal() + this.attackDamage :
				1);
			hero.setAttackSpeed(setIncreaseAttackSpeed(hero));/*hero.getAttackSpeed() + this.attackSpeed > 0 ? 
					hero.getAttackSpeed() + this.attackSpeed : 1);*/
			hero.setAttackRange(hero.getAttackRange() + this.attackRange > 0 ? 
					hero.getAttackRange() + this.attackRange : 1);
			hero.setArmorDamage(hero.getArmorDamage() + this.armourDamage > 0 ?
					hero.getArmorDamage() + this.armourDamage : 1);
		}
	}
	
	/**
	 * Helper method for increasing the attack speed of hero
	 * @param hero
	 * 			The hero to apply the effect to
	 * @return
	 * 		The attackspeed
	 */
	private int setIncreaseAttackSpeed(Commander hero) {
		if (hero.getAttackSpeed() + this.attackSpeed >= hero.getMaxAttackSpeed()) {
			hero.setMaxAttackSpeed(hero.getAttackSpeed() + this.attackSpeed);
			return hero.getAttackSpeed() + this.attackSpeed;
		} else if (hero.getAttackSpeed() + this.attackSpeed < 1) {
			return 1;
		} else {
			return hero.getAttackSpeed() + this.attackSpeed;
		}
	}
	
	/**
	 * Method to remove this class's effect. Should only be called after applyEffect has been used before.
	 * 
	 * @param entity  The entity to apply the effect to. 
	 */
	@Override
	public void removeEffect(AttackableEntity entity) {
		if (entity instanceof Commander) { //only allowing changes on Commander for testing purposes at this stage.
			Commander hero = (Commander) entity;
			hero.setDamage(hero.getDamageDeal() - this.attackDamage > 0 ? hero.getDamageDeal() - this.attackDamage :
				1);
			hero.setAttackSpeed(setReduceAttackSpeed(hero));/*hero.getAttackSpeed() - this.attackSpeed > 0 ? 
					hero.getAttackSpeed() - this.attackSpeed : 1);*/
			hero.setAttackRange(hero.getAttackRange() - this.attackRange > 0 ? 
					hero.getAttackRange() - this.attackRange : 1);
			hero.setArmorDamage(hero.getArmorDamage() - this.armourDamage > 0 ?
					hero.getArmorDamage() - this.armourDamage : 1);
		}
	}
	
	/**
	 * Helper method for reducing the attack speed of hero
	 * @param hero
	 * 			The hero to apply the effect to
	 * @return
	 * 		The attackspeed
	 */
	private int setReduceAttackSpeed(Commander hero) {
		int diff = hero.getAttackSpeed() - this.attackSpeed;
		if (diff < 0) {
			return 1;
		} else {
			return hero.getAttackSpeed() - this.attackSpeed;
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
	 * Creates a string based on stat changes that would be the description of the item to be used in the shop. Outlines
	 * how much damage, armour damage, attack speed and attack range the effect adds.
	 * 
	 * @return string that the describes how much damage, armour damage, attack speed and attack range the effect adds.
	 */
	@Override
	public String generateDescription() {
		StringBuilder string = new StringBuilder("");
		if (attackDamage != 0) {
			string.append("WeaponDamage: " + attackDamage + "\n");
			string.append("ArmourDamage: " + armourDamage + "\n");
		}
		if (attackSpeed != 0) {
			string.append("AttackSpeed: " + attackSpeed + "\n");
		}
		if (attackRange != 0) {
			string.append("AttackRange: " + attackRange + "\n");
		}
		return string.toString();
	}
}
