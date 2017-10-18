package com.deco2800.marswars.actions;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	@Override
	public void doAction() {
		if (!timeManager.isPaused() && !actionPaused) {
			switch (state) {
				case SETUP_MOVE:
					action = new MoveAction(goalX, goalY, entity);
					state = State.MOVE_TOWARDS;
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
			}

		}
	}
	
	private void moveTowardsAction() {
		//Enemies within attack range are found
		List<BaseEntity> entityList = GameManager.get().getWorld().getEntities();
		List<AttackableEntity> enemy = new ArrayList<AttackableEntity>();
		//For each entity in the world
		for (BaseEntity e: entityList) {
			//If an attackable entity
			if (e instanceof AttackableEntity) {
				//Not owned by the same player
				AttackableEntity attackable = (AttackableEntity) e;
				if (!entity.sameOwner(attackable)) {
					//Within attacking distance
					float diffX = attackable.getPosX() - entity.getPosX();
					float diffY = attackable.getPosY() - entity.getPosY();
					if (Math.abs(diffX) + Math.abs(diffY) <= entity.getAttackRange()) {
						enemy.add((AttackableEntity) e);
					}
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
	
	@Override
	public boolean completed() {
		return completed;
	}

	@Override
	public int actionProgress() {
		return 0;
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
