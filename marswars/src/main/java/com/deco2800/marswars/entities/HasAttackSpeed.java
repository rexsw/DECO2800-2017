package com.deco2800.marswars.entities;

public interface HasAttackSpeed {
	
	/**
	 * Set the attack speed of the entity
	 * @param attackSpeed the attack speed of the entity
	 */
	public void setAttackSpeed(int attackSpeed);
	
	/**
	 * Return the attack speed of the entity
	 * @return attackspeed the attack speed of the entity
	 */
	public int getAttackSpeed();
}
