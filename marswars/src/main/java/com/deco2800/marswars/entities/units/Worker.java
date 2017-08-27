package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.managers.Manager;

public class Worker extends Soldier{

	public Worker(float posX, float posY, float posZ, Manager owner) {
		super(posX, posY, posZ, owner);
		
	}
	
	@Override
	public void setAllTextture() {
		this.defaultTextureName = "";
		//this.movementSound = "astronautMovementSound";
		this.selectedTextureName = "";
	}

}
