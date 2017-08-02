package com.deco2800.marswars.entities;

import com.deco2800.moos.worlds.AbstractWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by timhadwen on 29/7/17.
 */
public class Rock extends BaseEntity implements HasHealth {

	private static final Logger LOGGER = LoggerFactory.getLogger(Rock.class);

	private int health = 100;

	public Rock(AbstractWorld parent, float posX, float posY, float posZ, float height, float width) {
		super(posX, posY, posZ, height, width, 1f);
		this.setTexture("rock");
	}


	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public void setHealth(int health) {
		System.err.println("Setting health to " + health);

		if (health <= 0) {
			this.getParent().removeEntity(this);
		}

		this.health = health;
	}
}
