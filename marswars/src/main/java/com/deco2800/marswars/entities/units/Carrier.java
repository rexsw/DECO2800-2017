package com.deco2800.marswars.entities.units;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.deco2800.marswars.actions.ActionSetter;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.BuildAction;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.LoadAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.actions.UnloadAction;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.EntityStats;
import com.deco2800.marswars.managers.AbstractPlayerManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.SoundManager;
import com.deco2800.marswars.util.Point;
import com.deco2800.marswars.worlds.BaseWorld;

/**
 * A carrier unit that is able to load up to 3 other units, extends Soldier
 * class
 * 
 * @author Han Wei
 */

public class Carrier extends Soldier {

    private static final Logger LOGGER = LoggerFactory.getLogger(Carrier.class);

    private static final int capacity = 3;

    private Optional<DecoAction> currentAction = Optional.empty();

    private Soldier[] loadedUnits = new Soldier[capacity];
    private ActionType nextAction;

    public Carrier(float posX, float posY, float posZ, int owner) {
	super(posX, posY, posZ, owner);

	// set all the attack attributes
	this.setMaxHealth(1000);
	this.setHealth(1000);
	this.setDamage(0);
	this.setArmor(500);
	this.setArmorDamage(0);
	this.setAttackRange(0);
	this.setAttackSpeed(0);
	this.isCarrier();
	this.addNewAction(ActionType.LOAD);
	this.addNewAction(ActionType.UNLOAD);
	this.removeActions(ActionType.DAMAGE);
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
	    LOGGER.info("Right click occurred outside game world.");
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

	    } else {
		BaseWorld world = GameManager.get().getWorld();
//		float newPosX = x + 3;
//		float newPosY = y + 3;
//		if (newPosX > world.getWidth()) {
//		    newPosX = x - 3;
//		}
//		if (newPosY > world.getLength()) {
//		    newPosY = y - 3;
//		}
		for (int i = 0; i < capacity; i++) {
		    if (!(loadedUnits[i] == null)) {
			LOGGER.error("moving unit " + i);

			loadedUnits[i].setCurrentAction(
				Optional.of(new MoveAction((int) x,
					(int) y, loadedUnits[i])));
		    }
		}
		currentAction = Optional
			.of(new MoveAction((int) x, (int) y, this));
		LOGGER.error("Assigned action move to" + x + " " + y);
	    }
	}
	this.setTexture(defaultTextureName);
	SoundManager sound = (SoundManager) GameManager.get()
		.getManager(SoundManager.class);
	sound.playSound(movementSound);
    }

    /**
     * On tick method for the carrier that carries out actions that occur every
     * tick
     * 
     * @param tick
     */
    @Override
    public void onTick(int tick) {
	if (!currentAction.isPresent()) {
	    if (this.getOwner() == -1)
		modifyFogOfWarMap(true, 3);
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

		// LOGGER.info("Spacman is on a tile with another entity, move
		// out of the way");

		// List<BaseEntity> entities =
		// GameManager.get().getWorld().getEntities(xPosition,
		// yPosition);
		/* Finally move to that position using a move action */
		currentAction = Optional.of(
			new MoveAction((int) p.getX(), (int) p.getY(), this));
	    }
	    return;
	}

	if (!currentAction.get().completed()) {
	    currentAction.get().doAction();
	} else {
	    // LOGGER.info("Action is completed. Deleting");
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
	    LOGGER.error("Assigned action load target at " + x + " " + y);
	} else {
	    currentAction = Optional.of(new MoveAction((int) x, (int) y, this));
	    LOGGER.error("Unloadable target");
	}
    }

    /**
     * Adds loaded target into list of loaded units
     * 
     * @param target
     * @return true if able to load the target, false otherwise
     */
    public boolean loadPassengers(Soldier target) {
	for (int i = 0; i < capacity; i++) {
	    if (loadedUnits[i] == null) {
		loadedUnits[i] = target;
		LOGGER.error("target loaded");
		if (target.getLoadStatus() != 2) {
		    target.setLoaded();
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
	LOGGER.info("Everyone off!");
	int empty = 0;
	for (int i = 0; i < capacity; i++) {
	    if (!(loadedUnits[i] == null)) {
		loadedUnits[i].setUnloaded();
		LOGGER.error("Unit unloaded.");
		loadedUnits[i] = null;
		empty++;
	    }
	}
	if (empty == 0) {
	    return false;
	} else {
	    return true;
	}
    }

    /**
     * @return The stats of the entity
     */
    public EntityStats getStats() {
	return new EntityStats("Carrier", this.getHealth(), null,
		this.getCurrentAction(), this);
    }

    /**
     * Sets the next action of the carrier
     */
    @Override
    public void setNextAction(ActionType a) {
	if (a == ActionType.UNLOAD) {
	    unloadPassenger();
	} else {
	    LOGGER.info("Assigned action " + ActionSetter.getActionName(a));
	    this.nextAction = a;
	}
    }

}
