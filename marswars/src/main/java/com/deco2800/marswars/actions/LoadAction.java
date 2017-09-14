package com.deco2800.marswars.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Carrier;

public class LoadAction implements DecoAction {

    enum State {
	MOVE_STATE, 
	LOAD_STATE
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadAction.class);
    private boolean completed = false;
    private MoveAction action = null;
    private State state = State.MOVE_STATE;
    private AttackableEntity carrier;
    private AttackableEntity target;
    private int ticksLoad = 200;

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
		break;
	}
    }

    private void loadAction() {
    LOGGER.info("loaded units");
	ticksLoad--;
	if(ticksLoad == 0) {
	    if (((Carrier) carrier).loadPassengers(target)) {
		completed = true;
	    } else {
		return;
	    }
	    ticksLoad = 200;
	}

    }

    private void moveTowardsAction() {
	if (action.completed()) {
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

    @Override
    public void pauseAction() {
	// TODO Auto-generated method stub

    }

    @Override
    public void resumeAction() {
	// TODO Auto-generated method stub

    }

}
