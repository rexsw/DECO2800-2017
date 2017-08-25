package com.deco2800.marswars.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.GatherAction.State;
import com.deco2800.marswars.entities.AttackableEntity;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.MissileEntity;
import com.deco2800.marswars.managers.GameManager;

/**
 * Created by timhadwen on 30/7/17.
 * Edited by Tze Thong Khor on 22/8/17
 */
public class ImpactAction implements DecoAction {
	
	private MoveAction action = null;
	private boolean completed = false;
	private State state = State.SETUP_MOVE;
	
	private MissileEntity missile;
	private AttackableEntity target;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImpactAction.class);
	
	enum State {
		SETUP_MOVE,
		MOVE_TOWARDS,
		IMPACT
	}
	
	public ImpactAction (MissileEntity missile, AttackableEntity target) {
		this.missile = missile;
		this.target = target;
	}
	
	@Override
	public void doAction() {
		switch (state) {
			case SETUP_MOVE:
				action = new MoveAction(target.getPosX(), target.getPosY(), missile);
				state = State.MOVE_TOWARDS;
				break;
			case MOVE_TOWARDS:
				if (GameManager.get().getWorld().getEntities().contains(target)) {
					if (action.completed()) {
						if (target.getPosX() == missile.getPosX() && target.getPosY() == missile.getPosY()) {
							state = State.IMPACT;
						} else {
							state = State.SETUP_MOVE;
						}
					}
					action.doAction();
					//LOGGER.info("MOVING TO TARGET");
				} else {
					GameManager.get().getWorld().removeEntity(missile);
				}
				break;
			case IMPACT:
				if (target.getArmor() > 0) {
					target.setHealth(target.getHealth() - missile.getDamageDeal()/2);
					target.setArmor(target.getArmor() - missile.getArmorDamage());
				} else {
					target.setHealth(target.getHealth() - missile.getDamageDeal());
				}
				LOGGER.info("target health " + target.getHealth());
				GameManager.get().getWorld().removeEntity(missile);
				if (target.getHealth() <= 0) {
					GameManager.get().getWorld().removeEntity(target);
					LOGGER.info("TARGET unit removed");
				}
				LOGGER.info("missile removed");
				completed = true;
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