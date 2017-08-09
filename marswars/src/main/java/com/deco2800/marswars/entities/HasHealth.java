package com.deco2800.marswars.entities;

/**
 * Health handling interface for entties
 */
public interface HasHealth {
	/**
	 * Returns the health of an entity
	 * @return
	 */
	int getHealth();

	/**
	 * Sets the health of an entity
	 * @param health
	 */
	void setHealth(int health);
}
