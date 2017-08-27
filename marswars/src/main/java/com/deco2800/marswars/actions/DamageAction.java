package com.deco2800.marswars.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.entities.AttackableEntity;
import com.deco2800.marswars.entities.Bullet;
import com.deco2800.marswars.entities.MissileEntity;
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
		this.entity = entity;
		this.enemy= goalEntity;
		attackSpeed = entity.getAttackSpeed();
	}

	@Override
	public void doAction() {
		float diffX;
		float diffY;
		float distance;
		switch (state) {
			case SETUP_MOVE:
				action = new MoveAction(enemy.getPosX(), enemy.getPosY(), entity);
				state = State.MOVE_TOWARDS;
				return;
			case MOVE_TOWARDS:
				// When close to the enemy's attack range, attack.
				if (action.completed()) {
					state = State.ATTACK;
					return;
				}
				diffX = enemy.getPosX() - entity.getPosX();
				diffY = enemy.getPosY() - entity.getPosY();
				distance = Math.abs(diffX) + Math.abs(diffY);
				if (distance <= entity.getAttackRange()) {
					state = State.ATTACK;
					return;
				}
				action.doAction();
				return;
			case ATTACK:
				if (GameManager.get().getWorld().getEntities().contains(enemy)) {
					attackInterval -= attackSpeed;
					diffX = enemy.getPosX() - entity.getPosX();
					diffY = enemy.getPosY() - entity.getPosY();
					distance = Math.abs(diffX) + Math.abs(diffY);
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
				break;
		}
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
}
