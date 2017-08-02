package com.deco2800.marsinvasion.entities;

import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.entities.AbstractEntity;

/**
 * Created by timhadwen on 29/7/17.
 */
public class Water extends AbstractEntity implements HasHealth {
	public Water(AbstractWorld parent, float posX, float posY, float posZ, float height, float width) {
		super(posX, posY, posZ, height, width, 1f);
		this.setTexture("water");
	}

	@Override
	public int getHealth() {
		return 0;
	}

	@Override
	public void setHealth(int health) {

	}
}