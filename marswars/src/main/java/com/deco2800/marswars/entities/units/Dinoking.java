package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.entities.terrainelements.ResourceType;

public class Dinoking extends Dino{
	
	public Dinoking(float posX, float posY, float posZ, int onwer) {
		super(posX, posY, posZ, onwer);
		this.setXRenderLength(3.2f);
		this.setYRenderLength(3.2f);
	}
	
	@Override 
	public void setDefaultAttributes() {
		this.setMaxHealth(500);
		this.setHealth(500);
		this.setDamage(50);
		this.setArmor(0);
		this.setMaxArmor(0);
		this.setArmorDamage(20);
		this.setAttackRange(1);
		this.setAttackSpeed(1);
		this.setDrop(ResourceType.BIOMASS);
		this.setSpeed(0.01f);
	}
	
	@Override
	public String toString(){
		return "Da king";
	}
}
