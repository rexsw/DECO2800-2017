package com.deco2800.marsinvasion.entities;

import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.WorldEntity;

/**
 * A hero for the game
 * Created by timhadwen on 19/7/17.
 */
public class HeroSpacman extends WorldEntity {

	/**
	 * Constructor for a hero
	 * @param world
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public HeroSpacman(AbstractWorld world, float posX, float posY, float posZ) {
		super(world, posX, posY, posZ, 1, 1.2f, 1.2f);
		this.setTexture("spacman_red");
	}
}
