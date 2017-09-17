package com.deco2800.marswars.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.LoadAction.State;
import com.deco2800.marswars.entities.units.Carrier;
import com.deco2800.marswars.entities.units.Soldier;

public class UnloadAction implements DecoAction {

    enum State {
	START_STATE, UNLOAD_STATE
    }

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(LoadAction.class);
    private boolean completed = false;
    private State state = State.START_STATE;
    private Soldier carrier;
    private int ticksLoad = 50;
    private boolean actionPaused = false;

    public UnloadAction(Soldier carrier) {
	super();
	this.carrier = carrier;
    }

    @Override
    public void doAction() {
	switch (state) {
	case UNLOAD_STATE:
	    unloadAction();
	    break;
	default:
	    state = State.UNLOAD_STATE;
	    return;
	}
    }

    private void unloadAction() {
	LOGGER.info("unloaded units");
	ticksLoad--;
	if (ticksLoad == 0) {
	    if (((Carrier) carrier).unloadPassenger()) {
		completed = true;
	    } else {
		return;
	    }
	    ticksLoad = 50;
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
