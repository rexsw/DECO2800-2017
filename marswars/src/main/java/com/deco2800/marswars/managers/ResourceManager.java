package com.deco2800.marswars.managers;

/**
 * Resource Manager
 *
 */
public class ResourceManager extends Manager {
	private int rocks = 0;
	private int fuel = 0;
	private int water = 0;

	/**
	 * Gets the number of rocks
	 * @return
	 */
	public int getRocks() {
		return rocks;
	}

	/**
	 * Sets the number of rocks
	 * @param rocks
	 */
	public void setRocks(int rocks) {
		if (this.rocks < rocks) {
			SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
			sound.playSound("closed.wav");
		}
		this.rocks = rocks;
	}

	/**
	 * Gets the current fuel
	 * @return
	 */
	public int getFuel() {
		return fuel;
	}

	/**
	 * Sets the current fuel
	 * @param fuel
	 */
	public void setFuel(int fuel) {
		this.fuel = fuel;
	}

	/**
	 * Gets the current water
	 * @return
	 */
	public int getWater() {
		return water;
	}

	/**
	 * Sets the current water
	 * @param water
	 */
	public void setWater(int water) {
		this.water = water;
	}
}