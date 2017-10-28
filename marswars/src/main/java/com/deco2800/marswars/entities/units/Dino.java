package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.entities.terrainelements.ResourceType;
import com.deco2800.marswars.managers.AiManager;
import com.deco2800.marswars.managers.GameManager;

public class Dino extends AmbientAnimal{

	public Dino(float posX, float posY, float posZ, int onwer) {
		super(posX, posY, posZ);
		this.setXRenderLength(2.5f);
		this.setYRenderLength(2.5f);
	}
	
	@Override 
	public void setDefaultAttributes() {
		this.setMaxHealth(300);
		this.setHealth(300);
		this.setDamage(20);
		this.setArmor(0);
		this.setMaxArmor(0);
		this.setArmorDamage(20);
		this.setAttackRange(5);
		this.setAttackSpeed(1);
		this.setDrop(ResourceType.BIOMASS);
		this.setSpeed(0.01f);
	}
	
	@Override 
	public void move(){
		((AiManager) GameManager.get().getManager(AiManager.class)).soldierGroupAttack(this);
	}
	
	@Override
	public String toString(){
		return "Spacasaur";
	}
}
