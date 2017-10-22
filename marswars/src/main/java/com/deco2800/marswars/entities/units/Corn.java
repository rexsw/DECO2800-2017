package com.deco2800.marswars.entities.units;


public class Corn extends AmbientAnimal{

	public Corn(float posX, float posY, float posZ, int onwer) {
		super(posX, posY, posZ);		
	}
	
	public void setDefaultAttributes() {
		this.setMaxHealth(50);
		this.setHealth(50);
		this.setDamage(200);
		this.setArmor(0);
		this.setMaxArmor(0);
		this.setArmorDamage(0);
		this.setAttackRange(1);
		this.setAttackSpeed(1);
		
		this.setSpeed(0.05f);
	}
}
