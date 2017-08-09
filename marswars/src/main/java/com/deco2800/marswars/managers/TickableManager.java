package com.deco2800.marswars.managers;

/**
 * A tickable manager.
 * Receives an onTick every game tick
 */
public interface TickableManager {
	/**
	 * On tick method for a manager
	 * @param i
	 */
	void onTick(long i);
}
