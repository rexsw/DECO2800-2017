package com.deco2800.marswars.entities;

/**
 * Damage handling interface for entities
 * @author Tze Thong Khor 22/8/17
 *
 */
public interface HasDamage {
	
	/**
	 * Returns the damage of an entity
	 * @return
	 */
	int getDamageDeal();
	
	/**
	 * Set the damage of an entity
	 * @param damage
	 */
	void setDamage(int damage);
}
