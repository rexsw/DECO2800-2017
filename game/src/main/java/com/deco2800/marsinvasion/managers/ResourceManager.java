package com.deco2800.marsinvasion.managers;

import com.deco2800.moos.managers.Manager;

/**
 * Resource Manager
 *
 */
public class ResourceManager extends Manager {
	private int rocks = 0;
	private int fuel = 0;
	private int rarepepes = 0;

	public int getRocks() {
		return rocks;
	}

	public void setRocks(int rocks) {
		this.rocks = rocks;
	}

	public int getFuel() {
		return fuel;
	}

	public void setFuel(int fuel) {
		this.fuel = fuel;
	}

	public int getRarepepes() {
		return rarepepes;
	}

	public void setRarepepes(int rarepepes) {
		this.rarepepes = rarepepes;
	}
}