package com.deco2800.marswars.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Bullet;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.managers.GameManager;

/**
 * Created by timhadwen on 30/7/17.
 * Edited by Tze Thong Khor on 22/8/17
 */
public class AttackAction implements DecoAction {
	private MoveAction action = null;
	private State state = State.SETUP_MOVE;
	private AttackableEntity entity;
	private AttackableEntity enemy;
	boolean completed = false;
	private int attackInterval = 1000;
	private int attackSpeed;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AttackAction.class);
	
	enum State {
		SETUP_MOVE,
		MOVE_TOWARDS,
		ATTACK
	}
	
	public AttackAction(AttackableEntity entity, AttackableEntity goalEntity) {
		this.entity = entity;
		this.enemy= goalEntity;
		attackSpeed = entity.getAttackSpeed();
	}

	@Override
	public void doAction() {
		switch (state) {
			case MOVE_TOWARDS:
				moveTowardsAction();
				return;
			case ATTACK:
				attackAction();
				break;
			default: //SETUP_MOVE case. should not be able to get any other state besides SETUP_MOVE here. 
				action = new MoveAction(enemy.getPosX(), enemy.getPosY(), entity);
				state = State.MOVE_TOWARDS;
				return;
		}
	}
	
	/**
	 * Gets the absolute distance from the current entity to the current entity
	 * @return the absolute distance from the 
	 */
	private float getDistanceToEnemy() {
		float diffX = enemy.getPosX() - entity.getPosX();
		float diffY = enemy.getPosY() - entity.getPosY();
		return Math.abs(diffX) + Math.abs(diffY);
	}

	private void setUpMissile() {
		GameManager.get().getWorld().addEntity(new Bullet(entity.getPosX(), entity.getPosY(), entity.getPosZ(),
				enemy, entity.getDamageDeal(), entity.getArmorDamage(), "bullet", 0)); //((Soldier) entity).getMissileTexture()
	}
	
	@Override
	public boolean completed() {
		return completed;
	}

	@Override
	public int actionProgress() {
		return 0;
	}
	
	private void moveTowardsAction() {
	    float distance;
		// When close to the enemy's attack range, attack.
		if (action.completed()) {
			state = State.ATTACK;
			return;
		}
		distance = getDistanceToEnemy();
		if (distance <= entity.getAttackRange()) {
			state = State.ATTACK;
			return;
		}
		action.doAction();
	}
	
	private void attackAction() {
	    float distance;
		if (GameManager.get().getWorld().getEntities().contains(enemy)) {
			attackInterval -= attackSpeed;
			distance = getDistanceToEnemy();
			if (attackInterval <= 0 && distance <= entity.getAttackRange()) {
				setUpMissile();
				LOGGER.info("Enemy health " + enemy.getHealth());
				attackInterval = 1000;
			} else if (distance > entity.getAttackRange()) {
				state = State.SETUP_MOVE;
			}
		} else {
			LOGGER.info("Set action empty");
			completed = true;
			return;
		}
	}

	@Override
	public void pauseAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resumeAction() {
		// TODO Auto-generated method stub
		
	}
}
