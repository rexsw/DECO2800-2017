package com.deco2800.marswars.actions;

import com.deco2800.marswars.managers.TimeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Bullet;
import com.deco2800.marswars.entities.units.MissileEntity;
import com.deco2800.marswars.managers.GameManager;

/**
 * Created by timhadwen on 30/7/17.
 * Edited by Tze Thong Khor on 22/8/17
 */
public class DamageAction implements DecoAction {
	private MoveAction action = null;
	private State state = State.SETUP_MOVE;
	private AttackableEntity entity;
	private AttackableEntity enemy;
	boolean completed = false;
	private int attackInterval = 1000;
	private int attackSpeed;
	private boolean actionPaused;
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DamageAction.class);
	
	enum State {
		SETUP_MOVE,
		MOVE_TOWARDS,
		ATTACK
	}
	
	public DamageAction(AttackableEntity entity, AttackableEntity goalEntity) {
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
					attackAction();
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
				enemy, entity.getDamageDeal(), entity.getArmorDamage()));
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
				// should spawn missile, missile carry the damage info but now 
				// test attack without missile
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

	/**
	 * Prevents the current action from progressing.
	 */
	@Override
	public void pauseAction() {
		actionPaused = true;
	}

	/**
	 * Resumes the current action
	 */
	@Override
	public void resumeAction() {
		actionPaused = false;
	}
}
