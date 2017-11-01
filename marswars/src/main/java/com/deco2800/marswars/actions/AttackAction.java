package com.deco2800.marswars.actions;

import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Medic;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;

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
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	
	/**
	 * Possible states of the AttackAction
	 */
	enum State {
		SETUP_MOVE,
		MOVE_TOWARDS,
		ATTACK,
		ATTACKING
	}

	/**
	 * Constructor - creates a new AttackAction object.
	 * @param entity
	 * @param enemy
	 */
	public AttackAction(AttackableEntity entity, AttackableEntity enemy) {
		this.entity = entity;
		this.enemy= enemy;
	}

	/**
	 * Perform the current action, only if the game is not currently paused.
	 */
	@Override
	public void doAction() {
		if (!timeManager.isPaused()) {
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
					action = new MoveAction((int)enemy.getPosX(), (int)enemy.getPosY(), entity);
					state = State.MOVE_TOWARDS;
					return;
			}

		}
	}

	/**
	 * Gets the absolute distance from the current entity to the current entity
	 * @return the absolute distance from the 
	 */
	public float getDistanceToEnemy() {
		float diffX = enemy.getPosX() - entity.getPosX();
		float diffY = enemy.getPosY() - entity.getPosY();
		return Math.abs(diffX) + Math.abs(diffY);
	}

	/**
	 * Returns a boolean describing whether or not the current AttackAction
	 * has been completed.
	 * @return
	 */
	@Override
	public boolean completed() {
		return completed;
	}

	/**
	 * Stub method
	 * @return int value
	 */
	@Override
	public int actionProgress() {
		return 0;
	}


	private void attackingAction() {
		//If the enemy is converted while being attacked
		if (entity.sameOwner(enemy) && !(entity instanceof Medic)) {
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

	/**
	 * Causes the unit possessing this action to attack an enemy, should it come
	 * within attacking distance of this unit.
	 */
	private void moveTowardsAction() {
	    float distance;
		// When close to the enemy's attack range, attack.
		if (action.completed()) {
			state = State.ATTACK;
			return;
		}
		distance = getDistanceToEnemy();
		if (distance <= entity.getAttackRange() && Math.abs(entity.getPosX()-(int)(entity.getPosX())) < 0.5f
				&& Math.abs(entity.getPosY()-(int)(entity.getPosY())) < 0.5f ) {
			state = State.ATTACK;
			return;
		}
		action.doAction();
	}

	/**
	 * Attack actions do not need to be paused. (Dummy method)
	 */
	@Override
	public void pauseAction() {
		return;
	}

	/**
	 * Attack actions do not need to be paused. (Dummy method)
	 */
	@Override
	public void resumeAction() {
		return;
	}
}
