package com.deco2800.marswars.actions;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timhadwen on 30/7/17.
 * Edited by Tze Thong Khor on 22/8/17
 */
public class AttackMoveAction implements DecoAction {
	private float goalX;
	private float goalY;
	private MoveAction action = null;
	private AttackAction attack = null;
	private State state = State.SETUP_MOVE;
	private AttackableEntity entity;
	private AttackableEntity target;
	boolean completed = false;

	// Variables for pause
	private boolean actionPaused = false;
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
	
	public AttackMoveAction(float goalX, float goalY, AttackableEntity entity) {
		this.goalX = goalX;
		this.goalY = goalY;
		this.entity = entity;
	}

	/**
	 * Perform the current action, only if the game is not currently paused.
	 */
	@Override
	public void doAction() {
		if (!timeManager.isPaused() && !actionPaused) {
			switch (state) {
				case SETUP_MOVE:
					action = new MoveAction(goalX, goalY, entity);
					state = State.MOVE_TOWARDS;
					return;
				case MOVE_TOWARDS:
					moveTowardsAction();
					return;
				case ATTACK:
					attack = new AttackAction(entity, target);
					state = State.ATTACKING;
					return;
				case ATTACKING:
					attackingAction();
					return;
				default:
					return;
			}

		}
	}

	/**
	 * Causes the unit possessing this action to attack an enemy, should it come
	 * within attacking distance of this unit.
	 */
	private void moveTowardsAction() {
		//Enemies within attack range are found
		List<BaseEntity> entityList = GameManager.get().getWorld().getEntities();
		List<AttackableEntity> enemy = new ArrayList<AttackableEntity>();
		//For each entity in the world
		for (BaseEntity e: entityList) {
			//If an attackable entity
			if (e instanceof AttackableEntity && ! entity.sameOwner(e)) {
				//Not owned by the same player
				//Within attacking distance
				float diffX = e.getPosX() - entity.getPosX();
				float diffY = e.getPosY() - entity.getPosY();
				if (Math.abs(diffX) + Math.abs(diffY) <=
						entity.getAttackRange()) {
					enemy.add((AttackableEntity) e);
				}
			}
		}
		// If an enemy unit is seen
		if (!enemy.isEmpty()) {
			// Attack enemy
			target = enemy.get(0);
			state = State.ATTACK;
			return;
		}
		// If destination is reached finish action
		if (action.completed()) {
			completed = true;
			return;
		};
		action.doAction();
	}
	
	private void attackingAction() {
		if (attack.completed()) {
			//If enemy is dead continue moving
			state = State.SETUP_MOVE;
			return;
		}
		attack.doAction();
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
