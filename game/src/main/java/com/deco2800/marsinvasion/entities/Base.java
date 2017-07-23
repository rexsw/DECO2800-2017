package com.deco2800.marsinvasion.entities;

import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.WorldEntity;

/**
 * Created by timhadwen on 19/7/17.
 */
public class Base extends WorldEntity {

	public Base(AbstractWorld world, float posX, float posY, float posZ) {
		super(world, posX, posY, posZ, 1, 2, 2);
		this.setTexture("tree");
	}
}
