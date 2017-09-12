package com.deco2800.marswars.entities.items.effects;

import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.HeroSpacman;;

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
	
	public void applyEffect(AttackableEntity entity) {
		if (entity instanceof HeroSpacman) {
			HeroSpacman hero = (HeroSpacman) entity;
			
			hero.setDamage(hero.getDamageDeal() + this.attackDamage);
			hero.setAttackSpeed(hero.getAttackSpeed() + this.attackSpeed);
			hero.setAttackRange(hero.getAttackRange() + this.attackRange);
			hero.setArmorDamage(hero.getArmorDamage() + this.armourDamage);
		}
	}
	
	public void removeEffect(AttackableEntity entity) {
		if (entity instanceof HeroSpacman) {
			HeroSpacman hero = (HeroSpacman) entity;
			
			hero.setDamage(hero.getDamageDeal() - this.attackDamage);
			hero.setAttackSpeed(hero.getAttackSpeed() - this.attackSpeed);
			hero.setAttackRange(hero.getAttackRange() - this.attackRange);
			hero.setArmorDamage(hero.getArmorDamage() - this.armourDamage);
		}
	}
}
