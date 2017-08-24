package com.deco2800.marswars.entities;

/**
 * Armor handling interface for entities
 * @author Tze Thong Khor 21/8/17
 *
 */
public interface HasArmor {
	
	/**
	 * Returns the armor of an entity
	 * @return
	 */
	int getArmor();
	
	/**
	 * Sets the armor of an entity
	 * @param armor
	 */
	void setArmor(int armor);
}
