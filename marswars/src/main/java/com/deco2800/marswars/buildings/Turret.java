package com.deco2800.marswars.buildings;

import java.util.List;
import java.util.Optional;

import com.deco2800.marswars.actions.AttackAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.EntityID;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.worlds.AbstractWorld;

/**
 * Created by judahbennett on 25/8/17.
 *
 * A turret that can be used for base defence
 */

public class Turret extends BuildingEntity {

	/**
	 * Constructor for the turret.
	 * @param world The world that will hold the turret.
	 * @param posX its x position on the world.
	 * @param posY its y position on the world.
	 * @param posZ its z position on the world.
	 */
	public Turret(AbstractWorld world, float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, BuildingType.TURRET, owner);
	}
	
	public void powerUpTurret(){
		this.setDamage(this.getDamageDeal()*2);
	}
	
	public void releaseTurret(){
		if(this.getNumOfSolider() > 0){
			this.setNumOfSolider(this.getNumOfSolider() - 1);
		}		
	}
	
	public void attack(AttackableEntity target){
		int x = (int) target.getPosX();
		int y = (int) target.getPosY();
		if (setTargetType(target)) {
			currentAction = Optional.of(new AttackAction(this, target));
		} 
		else {
			currentAction = Optional.of(new MoveAction((int) x, (int) y, this));
		}
	}
	public boolean setTargetType(AttackableEntity target) {
		if (!this.sameOwner(target) //(belongs to another player, currently always true)
				&& this!= target) { //prevent soldier suicide when owner is not set
			return true;
		}
		return false;
	}
	
	public void aggressiveBehaviour(List<AttackableEntity> enemy) {
		//Attack closest enemy
		for (int i=1; i<=getAttackRange(); i++) {
			for (AttackableEntity a: enemy) {
				float xDistance = a.getPosX() - this.getPosX();
				float yDistance = a.getPosY() - this.getPosY();
				boolean distanceEquality = (Math.abs(Math.abs(yDistance) + Math.abs(xDistance) - i) < 0.01);
				if (distanceEquality) {
					attack(a);
					return;
				}
			}
		}
	}
}
