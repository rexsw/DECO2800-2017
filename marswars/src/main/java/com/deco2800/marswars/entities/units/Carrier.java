package com.deco2800.marswars.entities.units;

import com.badlogic.gdx.audio.Sound;
import com.deco2800.marswars.actions.*;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.EntityStats;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.SoundManager;
import com.deco2800.marswars.util.Point;
import com.deco2800.marswars.worlds.BaseWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * A carrier unit that is able to load up to 3 other units, extends Soldier
 * class
 * 
 * @author Han Wei @hwkhoo
 */

public class Carrier extends Soldier {
    private static final float MOVING_SPEED = 0.1f;

    private static final Logger LOGGER = LoggerFactory.getLogger(Carrier.class);

    private static final int CAPACITY = 4;

    private Optional<DecoAction> currentAction = Optional.empty();

    private String loadSound = "carrier-loading-sound.mp3";
    private String unableLoad = "cant unload while doing something else";
    private String noSound = "no sound";

    private Soldier[] loadedUnits = new Soldier[CAPACITY];
    private ActionType nextAction;
    private int totalLoaded = 0;

	public Carrier(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		setXRenderLength(2.2f);
		setYRenderLength(2.2f);
		this.name = "Carrier";
		this.setAttributes();
		this.addNewAction(ActionType.LOAD);
		this.addNewAction(ActionType.UNLOAD);
		this.addNewAction(ActionType.UNLOADINDIVIDUAL);
		this.removeActions(ActionType.DAMAGE);
		this.removeActions(ActionType.ATTACKMOVE);
		this.isCarrier();
		setStance(0); // Default stance for carrier is passive
    }
	
    /**
     * On rightclick method for carrier. If clicked on an allied unit that is
     * loadable, it will load the unit if theres space, else it will move to
     * target.
     * 
     * @param x
     * @param y
     */
    @Override
    public void onRightClick(float x, float y) {
	List<BaseEntity> entities;
	try {
	    entities = ((BaseWorld) GameManager.get().getWorld())
		    .getEntities((int) x, (int) y);

	} catch (IndexOutOfBoundsException e) {
	    // if the right click occurs outside of the game world, nothing will
	    // happen
	    LOGGER.info("Right click occurred outside game world." + e);
	    this.setTexture(defaultTextureName);
	    return;
	}
	if (nextAction != null) {
	    ActionSetter.setAction(this, x, y, nextAction);
	    nextAction = null;
	} else {
	    if (!entities.isEmpty() && entities.get(0) instanceof Soldier) {
		Soldier target = (Soldier) entities.get(0);
		load(target);
	    }

	    for (int i = 0; i < CAPACITY; i++) {
		if (!(loadedUnits[i] == null)) {
		    LOGGER.error("moving unit " + i);

		    loadedUnits[i].setCurrentAction(Optional.of(new MoveAction(
			    (int) x, (int) y, loadedUnits[i], MOVING_SPEED)));
		}
	    }

	    if (!(!entities.isEmpty() && entities.get(0) instanceof Soldier)) {
		currentAction = Optional.of(
			new MoveAction((int) x, (int) y, this, MOVING_SPEED));
		LOGGER.error("Assigned action move to" + x + " " + y);
	    }
	}
	this.setTexture(defaultTextureName);
	SoundManager sound = (SoundManager) GameManager.get()
		.getManager(SoundManager.class);
	Sound loadedSound = sound.loadSound(movementSound);
	sound.playSound(loadedSound);
    }

    /**
     * On tick method for the carrier that carries out actions that occur every
     * tick
     * 
     * @param tick
     */
    @Override
    public void onTick(int tick) {
	regeneration();
	checkOwnerChange();
	if (!currentAction.isPresent()) {
	    if (this.getOwner() == -1)
		modifyFogOfWarMap(true, getFogRange());
	    // make stances here.
	    int xPosition = (int) this.getPosX();
	    int yPosition = (int) this.getPosY();
	    List<BaseEntity> entities = GameManager.get().getWorld()
		    .getEntities(xPosition, yPosition);
	    int entitiesSize = entities.size();
	    for (BaseEntity e : entities) {
		if (e instanceof MissileEntity) {
		    entitiesSize--;
		}

	    }
	    boolean moveAway = entitiesSize > 2;
	    if (moveAway) {

		BaseWorld world = GameManager.get().getWorld();

		/*
		 * We are stuck on a tile with another entity therefore
		 * randomize a close by position and see if its a good place to
		 * move to
		 */
		Random r = new Random();
		Point p = new Point(xPosition + r.nextInt(2) - 1,
			yPosition + r.nextInt(2) - 1);

		/* Ensure new position is on the map */
		if (p.getX() < 0 || p.getY() < 0 || p.getX() > world.getWidth()
			|| p.getY() > world.getLength()) {
		    return;
		}
		/* Check that the new position is free */
		if (world.getEntities((int) p.getX(), (int) p.getY())
			.size() > 1) {
		    // No good
		    return;
		}

		/* Finally move to that position using a move action */
		currentAction = Optional.of(new MoveAction((int) p.getX(),
			(int) p.getY(), this, MOVING_SPEED));
	    }
	    return;
	}

	if (!currentAction.get().completed()) {
	    currentAction.get().doAction();
	} else {
	    currentAction = Optional.empty();
	}

    }

    /**
     * Loads the target into the carrier using a LoadAction
     * 
     * @param target
     */
    public void load(Soldier target) {
	int x = (int) target.getPosX();
	int y = (int) target.getPosY();
	if (this.sameOwner(target) && this != target
		&& target.getLoadStatus() == 0) {
	    // prevent carrier from loading itself or other carriers
	    currentAction = Optional.of(new LoadAction(this, target));
	    LOGGER.info("Assigned action load target at " + x + " " + y);
	} else {
	    currentAction = Optional
		    .of(new MoveAction((int) x, (int) y, this, MOVING_SPEED));
	    LOGGER.error("Impossible to load target");
	}
    }

    /**
     * Unloads units in carrier using an UnloadAction if it is not doing
     * anything else
     * 
     */
    public void unload() {
	if (!currentAction.isPresent()) {
	    unloadPassenger();
	} else {
	    LOGGER.error(unableLoad);
	}
    }
    
    public void unloadIndividual() {
	if (!currentAction.isPresent()) {
	    unloadPassengerIndividual();
	} else {
	    LOGGER.error(unableLoad);
	}
    }

    /**
     * Adds loaded target into list of loaded units
     * 
     * @param target
     * @return true if able to load the target, false otherwise
     */
    public boolean loadPassengers(Soldier target) {
	try {
	    SoundManager sound = (SoundManager) GameManager.get()
			.getManager(SoundManager.class);
		Sound loadedSound = sound.loadSound(loadSound);
		sound.playSound(loadedSound);
	} catch (NullPointerException e) {
	    LOGGER.error(noSound);
	}
	for (int i = 0; i < CAPACITY; i++) {
	    if (loadedUnits[i] == null) {
		loadedUnits[i] = target;
		LOGGER.info("target loaded");
		if (target.getLoadStatus() != 2) {
		    target.setLoaded();
		    totalLoaded++;
		}
		return true;
	    }
	}
	return false;
    }

    /**
     * @return list of loaded units
     */
    public Soldier[] getPassengers() {
	return loadedUnits;
    }

    /**
     * unloads all Passengers in the carrier
     * 
     * @return true if units unloaded, false otherwise
     */
    public boolean unloadPassenger() {
	try {
	    SoundManager sound = (SoundManager) GameManager.get()
		    .getManager(SoundManager.class);
		Sound loadedSound = sound.loadSound(loadSound);
		sound.playSound(loadedSound);
	} catch (NullPointerException e) {
	    LOGGER.error(noSound);
	}
	LOGGER.info("Everyone off!");
	int empty = 0;
	boolean flag;
	for (int i = 0; i < CAPACITY; i++) {
	    if (!(loadedUnits[i] == null)) {
		loadedUnits[i].setUnloaded();
		totalLoaded--;
		LOGGER.error("Unit unloaded.");
		loadedUnits[i] = null;
		empty++;
	    }
	}
	if (empty == 0) {
	    flag = false;
	} else {
	    flag = true;
	}
	return flag;
    }
    
    /**
     * unloads the last loaded Passengers in the carrier
     * 
     * @return true if units unloaded, false otherwise
     */
    public boolean unloadPassengerIndividual() {
	try {
	    SoundManager sound = (SoundManager) GameManager.get()
		    .getManager(SoundManager.class);
	    Sound loadedSound = sound.loadSound(loadSound);
	    sound.playSound(loadedSound);
	} catch (NullPointerException e) {
	    LOGGER.error(noSound);
	}
	LOGGER.info("Last in first out!");
	int empty = 0;
	boolean flag;
	if(totalLoaded > 0) {
	    if (!(loadedUnits[totalLoaded - 1] == null)) {
		loadedUnits[totalLoaded - 1].setUnloaded();
		LOGGER.error("Unit unloaded.");
		loadedUnits[totalLoaded - 1] = null;
		totalLoaded--;
		empty++;
	   }
	}
	if (empty == 0) {
	    flag = false;
	} else {
	    flag = true;
	}
	return flag;
    }

    /**
     * @return The stats of the entity
     */
    public EntityStats getStats() {
	return new EntityStats("Carrier", this.getHealth(), this.getMaxHealth(),
		null, this.getCurrentAction(), this);
    }

    /**
     * Sets the next action of the carrier
     */
    @Override
    public void setNextAction(ActionType a) {
	if (a == ActionType.UNLOAD) {
	    if (!currentAction.isPresent()) {
		LOGGER.info("Starting to unload");
		unloadPassenger();
	    } else {
		LOGGER.error(unableLoad);
	    }
	} else if (a == ActionType.UNLOADINDIVIDUAL) {
	    if (!currentAction.isPresent()) {
		LOGGER.info("Starting to unload last unit");
		unloadPassengerIndividual();
	    } else {
		LOGGER.error(unableLoad);
	    }
	} else {
	    LOGGER.info("Assigned action " + ActionSetter.getActionName(a));
	    this.nextAction = a;
	}
    }
    
    /**
     * Returns the current action of the entity
     * 
     * @return current action
     */
    @Override
    public Optional<DecoAction> getCurrentAction() {
	return this.currentAction;
    }
}
