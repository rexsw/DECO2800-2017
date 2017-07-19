package com.deco2800.marsinvasion.entities;

import com.deco2800.moos.worlds.WorldEntity;

/**
 * Created by timhadwen on 19/7/17.
 */
public class Peon extends WorldEntity {

	public Peon(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1, 1, 1);
		this.setTexture("tree");
	}
}
