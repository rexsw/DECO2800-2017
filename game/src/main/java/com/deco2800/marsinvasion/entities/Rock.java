package com.deco2800.marsinvasion.entities;

import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.WorldEntity;

/**
 * Created by timhadwen on 29/7/17.
 */
public class Rock extends WorldEntity implements HasHealth {

	private int health = 100;

	public Rock(AbstractWorld parent, float posX, float posY, float posZ, float height, float width) {
		super(parent, posX, posY, posZ, height, width);
		this.setTexture("rock");
	}


	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public void setHealth(int health) {
		if (health > 100 || health < 0) {
			return;
		}

		this.health = health;
	}
}
