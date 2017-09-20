package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.actions.AttackAction;

/**
 * A combat unit that deal loyalty damage to enemy and convert enemy
 * to its own side. 
 * @author User
 *
 */

public class Hacker extends Soldier {

	public Hacker(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		this.setMaxHealth(200);
		this.setHealth(200);
		this.setDamage(0);
		this.setLoyaltyDamage(20);
		this.setArmor(50);
		this.setArmorDamage(0);
		this.setAttackRange(5);
		this.setAttackSpeed(10);
	}

	
}