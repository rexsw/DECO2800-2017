package com.deco2800.marswars.actions;

import com.deco2800.marswars.entities.units.AttackableEntity;
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
	private ShootAction shoot = null;
	private State state = State.SETUP_MOVE;
	private AttackableEntity entity;
	private AttackableEntity enemy;
	boolean completed = false;

	// Variables for pause
	private boolean actionPaused = false;
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AttackAction.class);

	
	enum State {
		SETUP_MOVE,
		MOVE_TOWARDS,
		ATTACK,
		ATTACKING
	}
	
	public AttackAction(AttackableEntity entity, AttackableEntity enemy) {
		this.entity = entity;
		this.enemy= enemy;
	}

	@Override
	public void doAction() {
		if (!timeManager.isPaused() && !actionPaused) {
			switch (state) {
				case MOVE_TOWARDS:
					moveTowardsAction();
					return;
				case ATTACK:
					shoot = new ShootAction(entity, enemy);
					state = State.ATTACKING;
					return;
				case ATTACKING:
					attackingAction();
					return;
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
	
	@Override
	public boolean completed() {
		return completed;
	}

	@Override
	public int actionProgress() {
		return 0;
	}
	
	private void attackingAction() {
		//If the enemy is converted while being attacked
		if (entity.sameOwner(enemy)) {
			completed = true;
			return;
		}
		if (shoot.completed()) {
			//If enemy is dead
			if (!GameManager.get().getWorld().getEntities().contains(enemy)) {
				completed = true;
				return;
			} else {
				state = State.SETUP_MOVE;
				return;
			}
		}
		shoot.doAction();
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

	@Override
	public void pauseAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resumeAction() {
		// TODO Auto-generated method stub
		
	}
}
