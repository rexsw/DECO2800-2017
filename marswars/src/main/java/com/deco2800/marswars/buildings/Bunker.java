package com.deco2800.marswars.buildings;

import com.deco2800.marswars.entities.Clickable;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.entities.HasProgress;
import com.deco2800.marswars.entities.Tickable;
import com.deco2800.marswars.worlds.AbstractWorld;

/**
 * Created by judahbennett on 25/8/17.
 *
 * A bunker that can be used to increase population
 */

public class Bunker extends BuildingEntity {
	private int popAdd = 5;
	/**
	 * Constructor for the bunker.
	 * @param world The world that will hold the bunker.
	 * @param posX its x position on the world.
	 * @param posY its y position on the world.
	 * @param posZ its z position on the world.
	 */
	public Bunker(AbstractWorld world, float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, BuildingType.BUNKER, owner);
	}
	
	/**
	 * Get the population add value of this bunker
	 * @return int
	 */
	public int getPop() {
		return popAdd;
	}
	
}
