package com.deco2800.marswars.actions;

import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Bullet;
import com.deco2800.marswars.entities.units.Medic;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;


/**
 * A BuildSelectAction for selecting a valid area to build on
 * Created by vinsonyeung on 23/9/17.
 */

public class ShootAction extends AbstractPauseAction {
	private State state = State.COOLDOWN;
	private AttackableEntity entity;
	private AttackableEntity enemy;
	boolean completed = false;
	private int attackInterval = 1000;
	private int attackSpeed;

	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	
	
	enum State {
		COOLDOWN,
		SHOOT
	}
	
	public ShootAction(AttackableEntity entity, AttackableEntity goalEntity) {
		this.entity = entity;
		this.enemy= goalEntity;
		this.attackSpeed = entity.getAttackSpeed();
	}

	@Override
	public void doAction() {
		if (! timeManager.isPaused() && ! actionPaused) {
			switch (state) {
				case COOLDOWN:
					cooldown();
					return;
				case SHOOT:
					checkShoot();
					break;
				default:
					break;
			}
		}
	}
	
	private void checkShoot() {
		if (attackInterval <= 0) {
			shoot();
		} else {
			state = State.COOLDOWN; 
		}
	}
	
	private void cooldown() {
		float distance = getDistanceToEnemy();
		attackInterval -= attackSpeed;
		if (distance > entity.getAttackRange()) {
			completed = true;
		} else if (attackInterval <= 0) {
			state = State.SHOOT;
		}
	}
	
	private void setUpMissile() {
		GameManager.get().getWorld().addEntity(new Bullet(entity.getPosX(), entity.getPosY(), entity.getPosZ(),
				enemy, entity.getDamageDeal(), entity.getArmorDamage(), ((Soldier) entity).getMissileTexture(), entity.getAreaDamage(), entity.getOwner(), entity)); //((Soldier) entity).getMissileTexture()
	}
	
	private float getDistanceToEnemy() {
		float diffX = enemy.getPosX() - entity.getPosX();
		float diffY = enemy.getPosY() - entity.getPosY();
		return Math.abs(diffX) + Math.abs(diffY);
	}
	
	private void shoot() {
		//If the enemy is converted while being attacked
		if (entity.sameOwner(enemy) && !(entity instanceof Medic)) {
			completed = true;
			return;
		}
		if (GameManager.get().getWorld().getEntities().contains(enemy)) {
			if (attackInterval <= 0) {
				setUpMissile();
				attackInterval = 1000;
			} 
		} else {
			completed = true;
			return;
		}
	}

	@Override
	public boolean completed() {
		return completed;
	}
}