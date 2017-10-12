package com.deco2800.marswars.managers;

import com.deco2800.marswars.buildings.Base;

public class AiBuilder extends Manager {
	
	public boolean should(int team){
		ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		return rm.getBiomass(team) > 50 || rm.getCrystal(team) > 50 || rm.getRocks(team) > 50 || rm.getWater(team) > 50;
	}
	
	public void Baseuse(Base base) {
		return;
	}
	
	public void dobuild(int team) {
		return;
	}
	
	public void usetech(int team) {
	}
}
