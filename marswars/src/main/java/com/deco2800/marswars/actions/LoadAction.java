package com.deco2800.marswars.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.DamageAction.State;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Carrier;

public class LoadAction implements DecoAction {

    enum State {
	START_STATE, MOVE_STATE, LOAD_STATE
    }

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(LoadAction.class);
    private boolean completed = false;
    private MoveAction action = null;
    private State state = State.START_STATE;
    private AttackableEntity carrier;
    private AttackableEntity target;
    private int ticksLoad = 50;
    private boolean actionPaused = false;

    public LoadAction(AttackableEntity carrier, AttackableEntity target) {
	super();
	this.carrier = carrier;
	this.target = target;
    }

    @Override
    public void doAction() {
	switch (state) {
	case MOVE_STATE:
	    moveTowardsAction();
	    return;
	case LOAD_STATE:
	    loadAction();
	    break;
	default:
	    action = new MoveAction(target.getPosX(), target.getPosY(),
		    carrier);
	    state = State.MOVE_STATE;
	    return;
	}
    }

    private void loadAction() {
	LOGGER.info("loaded units");
	ticksLoad--;
	if (ticksLoad == 0) {
	    if (((Carrier) carrier).loadPassengers(target)) {
		completed = true;
	    } else {
		return;
	    }
	    ticksLoad = 50;
	}

    }
	private float getDistanceToTarget() {
		float diffX = target.getPosX() - carrier.getPosX();
		float diffY = target.getPosY() - carrier.getPosY();
		return Math.abs(diffX) + Math.abs(diffY);
	}

    private void moveTowardsAction() {
	    float distance;
		// When close to the enemy's attack range, attack.
		if (action.completed()) {
			state = State.LOAD_STATE;
			return;
		}
		distance = getDistanceToTarget();
		if (distance <= 1) {
			state = State.LOAD_STATE;
			return;
		}
		action.doAction();
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
