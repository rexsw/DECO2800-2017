package com.deco2800.marswars.entities.units;

import java.util.ArrayList;
import java.util.List;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.terrainelements.ResourceType;
import com.deco2800.marswars.managers.GameManager;

public class Corn extends AmbientAnimal{

	public Corn(float posX, float posY, float posZ, int onwer) {
		super(posX, posY, posZ);		
	}
	
	@Override  
	public void setAttributes() {
		this.setMaxHealth(50);
		this.setHealth(50);
		this.setDamage(200);
		this.setMaxArmor(0);
		this.setArmor(0);
		this.setArmorDamage(0);
		this.setAttackRange(20);
		this.setAttackSpeed(1);
		this.setDrop(ResourceType.CRYSTAL);
		this.setSpeed(0.05f);
	}
	
	
	@Override 
	public void move(){
		List<BaseEntity> entityList = GameManager.get().getWorld().getEntities();
		List<AttackableEntity> enemy = new ArrayList<AttackableEntity>();
		//For each entity in the world
		for (BaseEntity e: entityList) {
			//If an attackable entity
			if (e instanceof AttackableEntity) {
				//Not owned by the same player
				AttackableEntity attackable = (AttackableEntity) e;
				if (!this.sameOwner(attackable)) {
					//Within attacking distance
					float diffX = attackable.getPosX() - this.getPosX();
					float diffY = attackable.getPosY() - this.getPosY();
					if (Math.abs(diffX) + Math.abs(diffY) <= this.getAttackRange()) {
						enemy.add((AttackableEntity) e);
					}
				}
			}
		}
		timidBehaviour(enemy);
	}
	
	@Override
	public String toString(){
		return "Unispac";
	}
}
