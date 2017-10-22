package com.deco2800.marswars.entities.units;

/*
 * A medical combat unit, heal friend units. not engaged in fighting.
 * will heal enemy if loyalty changed.
 * 
 */
public class Medic extends Soldier {

	public Medic(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		this.name = "Medic";
		this.setAttributes();
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
