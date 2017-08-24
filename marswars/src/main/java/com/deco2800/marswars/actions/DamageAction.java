package com.deco2800.marswars.actions;

import com.deco2800.marswars.entities.AttackableEntity;
import com.deco2800.marswars.managers.GameManager;

/**
 * Created by timhadwen on 30/7/17.
 * Edited by Tze Thong Khor on 22/8/17
 */
public class DamageAction implements DecoAction {
	
	enum State {
		SETUP_MOVE,
		MOVE_TOWARDS,
		ATTACK
	}
	MoveAction action = null;
	private State state = State.SETUP_MOVE;
	private AttackableEntity entity;
	private AttackableEntity enemy;
	boolean completed = false;
	
	public DamageAction(AttackableEntity entity, AttackableEntity goalEntity) {
		this.entity = goalEntity;
		this.enemy= entity;
	}

	@Override
	public void doAction() {
		switch (state) {
			case SETUP_MOVE:
				action = new MoveAction(enemy.getPosX(), enemy.getPosY(), entity);
				state = State.MOVE_TOWARDS;
				break;
			case MOVE_TOWARDS:
				// When close to the enemy's attack range, attack.
				if (!action.completed()) {
					
				}
				action.doAction();
				break;
			case ATTACK:
				if (GameManager.get().getWorld().getEntities().contains(enemy)) {
				}
				break;
		}

	}

	@Override
	public boolean completed() {
		return completed;
	}

	@Override
	public int actionProgress() {
		return 0;
	}
}
