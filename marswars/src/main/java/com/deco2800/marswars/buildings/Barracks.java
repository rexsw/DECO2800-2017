package com.deco2800.marswars.buildings;

import com.deco2800.marswars.worlds.AbstractWorld;

/**
 * Created by JudahBennett on 25/8/17.
 *
 * A barracks to build an army
 */
public class Barracks extends BuildingEntity {

	/**
	 * Constructor for the barracks.
	 * @param world The world that will hold the base.
	 * @param posX its x position on the world.
	 * @param posY its y position on the world.
	 * @param posZ its z position on the world.
	 */
	public Barracks(AbstractWorld world, float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, BuildingType.BARRACKS, owner);
	}
}
