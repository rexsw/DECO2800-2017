package com.deco2800.marswars.managers;

/**
 * Resource Manager
 *
 */
public class ResourceManager extends Manager {
	private int rocks = 0;
	private int fuel = 0;
	private int water = 0;

	public int getRocks() {
		return rocks;
	}

	public void setRocks(int rocks) {
		if (this.rocks < rocks) {
			SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
			sound.playSound("closed.wav");
		}
		this.rocks = rocks;
	}

	public int getFuel() {
		return fuel;
	}

	public void setFuel(int fuel) {
		this.fuel = fuel;
	}

	public int getWater() {
		return water;
	}

	public void setWater(int water) {
		this.water = water;
	}
}