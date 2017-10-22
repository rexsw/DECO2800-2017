package com.deco2800.marswars.entities.units;


public class Snail extends AmbientAnimal{

	public Snail(float posX, float posY, float posZ, int onwer) {
		super(posX, posY, posZ);		
	}
	
	@Override 
	public void setDefaultAttributes() {
		this.setMaxHealth(100);
		this.setHealth(100);
		this.setDamage(10);
		this.setArmor(500);
		this.setMaxArmor(500);
		this.setArmorDamage(40);
		this.setAttackRange(1);
		this.setAttackSpeed(1);
		
		this.setSpeed(0.05f);
	}
}
