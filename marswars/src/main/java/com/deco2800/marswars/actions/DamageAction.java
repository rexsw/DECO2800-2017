package com.deco2800.marswars.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.entities.AttackableEntity;
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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DamageAction.class);
	
	enum State {
		SETUP_MOVE,
		MOVE_TOWARDS,
		ATTACK
	}
	
	public DamageAction(AttackableEntity entity, AttackableEntity goalEntity) {
		this.entity = goalEntity;
		this.enemy= entity;
		attackSpeed = entity.getAttackSpeed();
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
				if (action.completed()) {
					state = state.ATTACK;
					return;
				}
				action.doAction();
				break;
			case ATTACK:
				if (GameManager.get().getWorld().getEntities().contains(enemy)) {
					attackInterval -= attackSpeed;
					if (attackInterval <= 0) {
						// should spawn missle, missle carry the damage info but now 
						// test attack without missle
						if (enemy.getArmor() > 0) {
							enemy.setHealth(enemy.getHealth() - entity.getDamageDeal()/2);
							enemy.setArmor(enemy.getArmor() - entity.getArmorDamage());
						} else {
							enemy.setHealth(enemy.getHealth() - entity.getDamageDeal());
						}
						attackInterval = 1000;
					}
				} else {
					entity.setEmptyAction();
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
