package com.deco2800.marswars.actions;

import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Bullet;
import com.deco2800.marswars.entities.units.Hacker;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	// Variables for pause
	private boolean actionPaused = false;
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	
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
		if (! timeManager.isPaused() && ! actionPaused) {
			switch (state) {
				case MOVE_TOWARDS:
					moveTowardsAction();
					return;
				case ATTACK:
					attack();
					break;
				default: //SETUP_MOVE case. should not be able to get any other state besides SETUP_MOVE here.
					action = new MoveAction(enemy.getPosX(), enemy.getPosY(), entity);
					state = State.MOVE_TOWARDS;
					return;
			}
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
				enemy, entity.getDamageDeal(), entity.getArmorDamage(), ((Soldier) entity).getMissileTexture(), entity.getAreaDamage(), entity.getOwner(), entity)); //((Soldier) entity).getMissileTexture()
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
	
	private void attack() {
		if (entity instanceof Hacker) {
			if (entity.sameOwner(enemy)) {
				completed = true;
				return;
			}
		}
	    float distance;
		if (GameManager.get().getWorld().getEntities().contains(enemy)) {
			attackInterval -= attackSpeed;
			distance = getDistanceToEnemy();
			if (attackInterval <= 0 && distance <= entity.getAttackRange()) {
				setUpMissile();
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
