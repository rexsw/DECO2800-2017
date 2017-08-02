package com.deco2800.marswars.entities;

import com.deco2800.marswars.worlds.AbstractWorld;

/**
 * Created by timhadwen on 29/7/17.
 */
public class Water extends BaseEntity implements HasHealth {
	public Water(AbstractWorld parent, float posX, float posY, float posZ, float height, float width) {
		super(posX, posY, posZ, height, width, 1f);
		this.setTexture("water");
		this.setCost(100);
	}

	@Override
	public int getHealth() {
		return 0;
	}

	@Override
	public void setHealth(int health) {

	}
}