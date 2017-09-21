package com.deco2800.marswars.entities.units;

/*
 * A medical combat unit, heal friend units. not engaged in fighting.
 * will heal enemy if loyalty changed.
 * 
 */
public class Medic extends Soldier {

	public Medic(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		this.setMaxHealth(20000);
		this.setHealth(20000);
		this.setDamage(-25);
		this.setArmor(50);
		this.setArmorDamage(0);
		this.setAttackRange(5);
		this.setAttackSpeed(10);
	}
	
	@Override
	/**
	 * Set the target type to friedly unit
	 */
	public boolean setTargetType(AttackableEntity target) {
		if (this.sameOwner(target)) {//(belongs to another player, currently always true) 
			return true;
		}
		return false;
	}
}
