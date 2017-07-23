package com.deco2800.marsinvasion.entities;

import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.WorldEntity;

/**
 * A hero for the game
 * Created by timhadwen on 19/7/17.
 */
public class Hero extends WorldEntity {

	/**
	 * Constructor for a hero
	 * @param world
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public Hero(AbstractWorld world, float posX, float posY, float posZ) {
		super(world, posX, posY, posZ, 1, 1, 1);
		this.setTexture("tree");
	}
}
