package com.deco2800.marswars.buildings;

import com.deco2800.marswars.worlds.AbstractWorld;

/**
 * Created by judahbennett on 25/8/17.
 *
 * A turret that can be used for base defence
 */

public class Turret extends BuildingEntity{

	/**
	 * Constructor for the turret.
	 * @param world The world that will hold the turret.
	 * @param posX its x position on the world.
	 * @param posY its y position on the world.
	 * @param posZ its z position on the world.
	 */
	public Turret(AbstractWorld world, float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, BuildingType.TURRET, owner);
	}
	
	@Override
	public void powerUpTurret(){
		this.setDamage(this.getDamageDeal()*2);
	}
	
	@Override
	public void releaseTurret(){
		if(this.numOfSolider > 0){
			this.numOfSolider = this.numOfSolider - 1;
		}
		
		
	}
}
