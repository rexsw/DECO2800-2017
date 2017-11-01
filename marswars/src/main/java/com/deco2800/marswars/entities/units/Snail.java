package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.entities.terrainelements.ResourceType;
import com.deco2800.marswars.managers.AiManager;
import com.deco2800.marswars.managers.GameManager;

public class Snail extends AmbientAnimal{

	public Snail(float posX, float posY, float posZ, int onwer) {
		super(posX, posY, posZ);		
	}
	
	@Override 
	public void setAttributes() {
		this.setMaxHealth(100);
		this.setHealth(100);
		this.setDamage(10);
		this.setMaxArmor(500);
		this.setArmor(500);
		this.setArmorDamage(40);
		this.setAttackRange(5);
		this.setAttackSpeed(1);
		this.setDrop(ResourceType.ROCK);
		this.setSpeed(0.05f);
	}
	
	@Override
	public String toString(){
		return "Spacil";
	}
	
	@Override 
	public void move(){
		((AiManager) GameManager.get().getManager(AiManager.class)).soldierGroupAttack(this);
	}
}
