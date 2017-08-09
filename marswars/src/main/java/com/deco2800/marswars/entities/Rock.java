package com.deco2800.marswars.entities;

import com.deco2800.marswars.managers.GameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rock extends BaseEntity implements HasHealth {

	private static final Logger LOGGER = LoggerFactory.getLogger(Rock.class);

	private int health = 100;

	/**
	 * Constructor for the Rock class
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param height
	 * @param width
	 */
	public Rock(float posX, float posY, float posZ, float height, float width) {
		super(posX, posY, posZ, height, width, 1f);
		this.setTexture("rock");
		this.canWalkOver = true;
		this.setCost(100);
	}


	/**
	 * Returns the current "health" of the rock. yes thats a weird complex
	 * @return
	 */
	@Override
	public int getHealth() {
		return health;
	}

	/**
	 * Sets the health of the rock
	 * @param health
	 */
	@Override
	public void setHealth(int health) {
		System.err.println("Setting health to " + health);

		if (health <= 0) {
			GameManager.get().getWorld().removeEntity(this);
		}

		this.health = health;
	}
}
