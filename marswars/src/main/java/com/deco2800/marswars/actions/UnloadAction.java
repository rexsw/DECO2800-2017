package com.deco2800.marswars.actions;

import com.deco2800.marswars.buildings.Turret;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Carrier;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An UnloadAction for Unloading units from a carrier unit
 * 
 * @author Han Wei @hwkhoo
 */
public class UnloadAction extends AbstractPauseAction {

    enum State {
	START_STATE, UNLOAD_STATE
    }

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(LoadAction.class);
    private boolean completed = false;
    private State state = State.START_STATE;
    private AttackableEntity carrier;
    private int ticksLoad = 50;
    // Variables for pause
    private TimeManager timeManager = (TimeManager)
            GameManager.get().getManager(TimeManager.class);

    /**
     * Constructor for the UnloadAction
     * 
     * @param carrier
     *            The carrier unit assigned to load
     */
    public UnloadAction(AttackableEntity carrier) {
	super();
	this.carrier = carrier;
    }

    /**
     * Sets the state to unload
     */
    @Override
    public void doAction() {
        if (! timeManager.isPaused() && ! actionPaused) {
            if (state == State.UNLOAD_STATE) {
        	unloadAction();
            } else {
                state = State.UNLOAD_STATE;
                return;
            }
        }
    }

    /**
     * Unload units after 50 ticks
     */

    private void unloadAction() {
	LOGGER.info("unloaded units");
	ticksLoad--;
	if (ticksLoad == 0) {
		if (carrier instanceof Turret) {
			if (((Turret) carrier).unloadAstronauts()) {
			}
		}else if (((Carrier) carrier).unloadPassenger()) {
	    } 
		completed = true;
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
}
