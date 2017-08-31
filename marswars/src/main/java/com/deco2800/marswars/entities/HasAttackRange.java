package com.deco2800.marswars.entities;

/**
 * Range handling interface for entities
 * 
 * @author Tze Thong Khor on 21/8/17
 *
 */
public interface HasAttackRange {
	
	/**
	 * Returns the attack range of an entity.
	 * @return
	 */
	int getAttackRange();
	
	/**
	 * Sets the attack range of an entity.
	 * @param attackRange
	 */
	void setAttackRange(int attackRange);
}
