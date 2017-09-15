package com.deco2800.marswars.entities.items.effects;

import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Commander;;

public class AttackEffect implements Effect{
	private int attackDamage;
	private int attackSpeed;
	private int attackRange;
	private int armourDamage;
	
	public AttackEffect(int damage, int damageSpeed, int damageRange) {
		this.attackDamage = damage;
		this.attackSpeed = damageSpeed;
		this.attackRange = damageRange;
		this.armourDamage = (int) (damage * 0.25);
	}
	
	@Override
	public void applyEffect(AttackableEntity entity) {
		if (entity instanceof Commander) {
			Commander hero = (Commander) entity;
			
			hero.setDamage(hero.getDamageDeal() + this.attackDamage);
			hero.setAttackSpeed(hero.getAttackSpeed() + this.attackSpeed);
			hero.setAttackRange(hero.getAttackRange() + this.attackRange);
			hero.setArmorDamage(hero.getArmorDamage() + this.armourDamage);
		}
	}
	
	@Override
	public void removeEffect(AttackableEntity entity) {
		if (entity instanceof Commander) {
			Commander hero = (Commander) entity;
			
			hero.setDamage(hero.getDamageDeal() - this.attackDamage);
			hero.setAttackSpeed(hero.getAttackSpeed() - this.attackSpeed);
			hero.setAttackRange(hero.getAttackRange() - this.attackRange);
			hero.setArmorDamage(hero.getArmorDamage() - this.armourDamage);
		}
	}

	@Override
	public String generateDescription() {
		StringBuilder string = new StringBuilder("");
		if (attackDamage != 0) {
			string.append("WeaponDamage: " + attackDamage + "\n");
			string.append("ArmourDamage: " + armourDamage + "\n");
		}
		if (attackSpeed != 0) {
			string.append("AttackSpeed: " + attackSpeed + "\n");
		}
		if (attackRange != 0) {
			string.append("AttackRange: " + attackRange + "\n");
		}
		return string.toString();
	}
}
