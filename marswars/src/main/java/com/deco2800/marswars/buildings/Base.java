package com.deco2800.marswars.buildings;

import com.deco2800.marswars.worlds.AbstractWorld;

/**
 * Created by timhadwen on 19/7/17.
 *
 * A home base for the empire
 */
public class Base extends BuildingEntity {

	/**
	 * Constructor for the base.
	 * @param world The world that will hold the base.
	 * @param posX its x position on the world.
	 * @param posY its y position on the world.
	 * @param posZ its z position on the world.
	 */

	public Base(AbstractWorld world, float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, BuildingType.BASE, owner);
	}
}
