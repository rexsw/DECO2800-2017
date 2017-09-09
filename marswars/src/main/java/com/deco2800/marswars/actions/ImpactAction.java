package com.deco2800.marswars.actions;

import com.deco2800.marswars.managers.TimeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.MissileEntity;
import com.deco2800.marswars.managers.GameManager;

/**
 * Created by Vinson Yeung on 21/8/17.
 * Edited by Tze Thong Khor on 22/8/17
 */
public class ImpactAction implements DecoAction {
	
	private MoveAction action = null;
	private boolean completed = false;
	private boolean actionPaused = false;
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
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
		if (! timeManager.isPaused() && ! actionPaused) {
			switch (state) {
				case SETUP_MOVE:
					action = new MoveAction(target.getPosX(), target.getPosY(), missile);
					state = State.MOVE_TOWARDS;
					break;
				case MOVE_TOWARDS:
					boolean find = GameManager.get().getWorld().getEntities().contains(target);
					if (find && action.completed()) {
						state = target.getPosX() == missile.getPosX() && target.getPosY() == missile.getPosY() ?
									State.IMPACT : State.SETUP_MOVE;
						break;
					} else if (find) { 
						action.doAction();
						return;
					} else {
						completed = true;
						GameManager.get().getWorld().removeEntity(missile);
						return;
					}

				case IMPACT:
					if (target.getArmor() > 0) {
						target.setHealth(target.getHealth() - missile.getDamageDeal() / 2);
						target.setArmor(target.getArmor() - missile.getArmorDamage());
					} else {
						target.setHealth(target.getHealth() - missile.getDamageDeal());
					}
					GameManager.get().getWorld().removeEntity(missile);
					LOGGER.info("target health " + target.getHealth());
					completed = true;
					return;
				default: //should not reach here.
					break;
			}
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