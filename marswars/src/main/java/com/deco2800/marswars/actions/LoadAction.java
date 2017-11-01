package com.deco2800.marswars.actions;

import com.deco2800.marswars.buildings.Turret;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Carrier;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A LoadAction for loading units into a carrier unit
 * 
 * @author Han Wei @hwkhoo
 */
public class LoadAction extends AbstractPauseAction {

	//this speeds up the carrier unit
	private static final float MOVING_SPEED=0.1f;

    enum State {
	START_STATE, MOVE_STATE, LOAD_STATE
    }

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(LoadAction.class);
    private boolean completed = false;
    private MoveAction action = null;
    private State state = State.START_STATE;
    private Soldier carrier;
    private AttackableEntity target;
    private int ticksLoad = 50;
	// Variables for pause
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);


    /**
     * Constructor for the LoadAction
     * 
     * @param carrier
     *            The carrier unit assigned to load
     * @param target
     *            The target to be loaded
     */
    public LoadAction(Soldier carrier, Soldier target) {
	super();
	this.carrier = carrier;
	this.target = target;
    }
    
    /**
     * Constructor for the LoadAction
     * 
     * @param Astronaut
     *            The unit to be loaded into a turret
     * @param target
     *            The turret to be loaded into
     */
    public LoadAction(Astronaut astro, Turret turret) {
	super();
	this.carrier = astro;
	this.target = turret;
    }
    

    /**
     * Sets the state to move until the carrier is close to the target then
     * switches state to load to load the target into the carrier
     */
    @Override
    public void doAction() {
		if (! timeManager.isPaused() && ! actionPaused) {
			switch (state) {
				case MOVE_STATE:
					moveTowardsAction();
					return;
				case LOAD_STATE:
					loadAction();
					break;
				default:
					action = new MoveAction(target.getPosX(), target.getPosY(),
							carrier, MOVING_SPEED);
					state = State.MOVE_STATE;
					return;
			}
		}
    }

    /**
     * Loads targetted unit after 50 ticks
     */
    private void loadAction() {
	LOGGER.info("loaded units");
	ticksLoad--;
	if (ticksLoad == 0) {
		if (target instanceof Turret) {
			if (((Turret) target).loadAstronaut((Astronaut) carrier)) {
			}
		}else if (((Carrier) carrier).loadPassengers((Soldier) target)) {
	    } 
		completed = true;
	    ticksLoad = 50;
	}

    }

    /**
     * Checks the distance between target and carrier
     * 
     * @return distance between target and carrier
     */
    private float getDistanceToTarget() {
	float diffX = target.getPosX() - carrier.getPosX();
	float diffY = target.getPosY() - carrier.getPosY();
	return Math.abs(diffX) + Math.abs(diffY);
    }

    /**
     * keeps moving towards target until close enough, then switches to load
     * state
     */
    private void moveTowardsAction() {
	float distance;
	// When close to the target, load.
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
