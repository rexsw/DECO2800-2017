package com.deco2800.marswars.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.entities.units.Carrier;
import com.deco2800.marswars.entities.units.Soldier;

/**
 * An UnloadAction for UNloading units from a carrier unit
 * 
 * @author Han Wei
 */
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

    /**
     * Constructor for the UnloadAction
     * 
     * @param carrier
     *            The carrier unit assigned to load
     */
    public UnloadAction(Soldier carrier) {
	super();
	this.carrier = carrier;
    }

    /**
     * Sets the state to unload
     */
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

    /**
     * Unload units after 50 ticks
     */

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

    /**
     * Checks completed state
     * 
     * @return returns boolean stating if loading was completed
     */
    @Override
    public boolean completed() {
	return completed;
    }

    /**
     * Returns number from 0 to 100 representing completion
     * 
     * @return percentage of completion
     */
    @Override
    public int actionProgress() {
	return 100 - (ticksLoad * 2);
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