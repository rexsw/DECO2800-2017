
package com.deco2800.marswars.buildings;

import com.deco2800.marswars.worlds.AbstractWorld;

/**
 * Created by grumpygandalf on 23/10/17.
 *
 * A wall
 */
public class GateVertical extends BuildingEntity {

	/**
	 * Constructor for the base.
	 * @param world The world that will hold the base.
	 * @param posX its x position on the world.
	 * @param posY its y position on the world.
	 * @param posZ its z position on the world.
	 */

	public GateVertical(AbstractWorld world, float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, BuildingType.GATE, owner);
		this.setTexture("gate2");
	}
	
	@Override
	/**
	 * Load first building stage
	 */
	public void animate1() {
		//do nothing
	}
	
	@Override
	/**
	 * Load second building stage
	 */
	public void animate2() {
		//do nothing
	}
	
	@Override
	/**
	 * Load final building stage
	 */
	public void animate3() {
		//do nothing
	}
}