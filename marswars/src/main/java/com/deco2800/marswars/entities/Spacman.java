package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.ActionSetter;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.managers.AiManagerTest;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.Manager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.PlayerManager;
import com.deco2800.marswars.managers.SoundManager;
import com.deco2800.marswars.util.Point;
import com.deco2800.marswars.worlds.BaseWorld;
import com.deco2800.marswars.worlds.FogWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.Line;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * A generic player instance for the game
 * Created by timhadwen on 19/7/17.
 */
public class Spacman extends BaseEntity implements Tickable, Clickable, HasHealth, HasOwner {
	LineOfSight lineOfSight;

	private static final Logger LOGGER = LoggerFactory.getLogger(Spacman.class);

	private Optional<DecoAction> currentAction = Optional.empty();

	private int health = 100;
	
	private Manager owner = null;

	public static int cost = 10;
	
	// this is the resource gathered by this unit, it may shift to other unit in a later stage
	private GatheredResource gatheredResource = null;
//Hello wo
	private ActionType nextAction;
	private ActionSetter actionSetter;

	/**
	 * Constructor for the Spacman
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public Spacman(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1, 1, 1);
		this.setTexture("spacman_green");
		this.setCost(cost);
		this.setEntityType(EntityType.UNIT);
		this.initActions();
		this.addNewAction(ActionType.MOVE);
		this.addNewAction(ActionType.GATHER);
		this.nextAction = null;
		lineOfSight = new LineOfSight(posX,posY,posZ,1,1);
		FogWorld fogWorld = GameManager.get().getFogWorld();
		fogWorld.addEntity(lineOfSight);
	}

	/**
	 * Sets the position of this spacman
	 * @param x
	 * @param y
	 * @param z
	 */
	@Override
	public void setPosition(float x, float y, float z) {
		super.setPosition(x, y, z);
		lineOfSight.setPosition(x,y,z);


	}

    /**
     * function to change the cost of making a Spacman
     * @param c
     */
	public static void changeCost(int c){
	    cost = c;
    }

	/**
	 * Sets the position X
	 * @param x
	 */
	@Override
	public void setPosX(float x) {
		super.setPosX(x);
		lineOfSight.setPosX(x);
	}

	/**
	 * Sets the position Y
	 * @param y
	 */
	@Override
	public void setPosY(float y) {
		super.setPosY(y);
		lineOfSight.setPosY(y);
	}

	/**
	 * Sets the position Z
	 * @param z
	 */
	@Override
	public void setPosZ(float z) {
		super.setPosZ(z);
		lineOfSight.setPosZ(z);
	}
	/**
	 * On tick method for the spacman
	 * @param i
	 */
	@Override
	public void onTick(int i) {
		/* Don't let spacman stand on a tile with another entity,
			do really basic formation stuff instead
		 */
		if (!currentAction.isPresent()) {
			if (GameManager.get().getWorld().getEntities((int)this.getPosX(), (int)this.getPosY()).size() > 2) {
				BaseWorld world = GameManager.get().getWorld();

				/* We are stuck on a tile with another entity
				 * therefore randomize a close by position and see if its a good
				 * place to move to
				 */
				Random r = new Random();
				Point p = new Point(this.getPosX() + r.nextInt(2) - 1, this.getPosY() + r.nextInt(2) - 1);

				/* Ensure new position is on the map */
				if (p.getX() < 0 || p.getY() < 0 || p.getX() > world.getWidth() || p.getY() > world.getLength()) {
					return;
				}
				/* Check that the new position is free */
				if (world.getEntities((int)p.getX(), (int)p.getY()).size() > 1) {
					// No good
					return;
				}

				LOGGER.info("Spacman is on a tile with another entity, move out of the way");

				/* Finally move to that position using a move action */
				this.currentAction = Optional.of(new MoveAction((int)p.getX(), (int)p.getY(), this));

			}
			return;
		}

		/* If the action is completed, remove it otherwise keep doing that action */
		if (!currentAction.get().completed()) {
			currentAction.get().doAction();
		} else {
			LOGGER.info("Action is completed. Deleting");
			currentAction = Optional.empty();
		}
	}

	/**
	 * Get the line of sight of this spacman
	 * @return LineOfSight
	 */
	public LineOfSight getLineOfSight(){
		return lineOfSight;
	}

	/**
	 * On click method for the spacman
	 * Should change to Blue
	 * @param handler
	 */
	@Override
	public void onClick(MouseHandler handler) {
		if(owner instanceof PlayerManager) {
			handler.registerForRightClickNotification(this);
			SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
			this.setTexture("spacman_blue");
			LOGGER.error("Clicked on spacman");
			this.makeSelected();
		} else {
			this.makeSelected();
			this.setEntityType(EntityType.AISPACMAN);
			LOGGER.error("Clicked on ai spacman");
			
		}
	}

	/**
	 * On rightclick method for spacman
	 * @param x
	 * @param y
	 */
	@Override
	public void onRightClick(float x, float y) {
		LOGGER.info("Spacman given instruction");
		if (nextAction != null) {
			LOGGER.info("Spacman given specific instruction");
			ActionSetter.setAction(this,x,y,nextAction);
		} else if (ActionSetter.setAction(this,x,y,ActionType.GATHER)) {
			LOGGER.info("Spacman try to gather");
		} else if (ActionSetter.setAction(this,x,y,ActionType.MOVE)) {
			LOGGER.info("Spacman try to move");
		}
		this.setTexture("spacman_green");
		SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
		sound.playSound("endturn.wav");
		this.deselect();
		this.nextAction = null;
	}

	/**
	 * Gets the health for this spacman
	 * @return
	 */
	@Override
	public int getHealth() {
		return health;
	}

	/**
	 *  Sets the health for this spacman 
	 * 	@param health 
	 */
	@Override
	public void setHealth(int health) {
		LOGGER.info("Set health to " + health);
		this.health = health;

		if (health < 0) {
			GameManager.get().getWorld().removeEntity(this);
			if(owner instanceof AiManagerTest) {
				((AiManagerTest) owner).isKill();
			}
			LOGGER.info("I am kill");
		}
	}
	
	/**
	 * Add gathered resource to unit's backpack
	 * @param resource
	 */
	public void addGatheredResource(GatheredResource resource) {
		this.gatheredResource = resource;
		LOGGER.error("Gathered "+ resource.getAmount() + " units of "+ resource.getType());
	}
	
	/**
	 * check if the unit has resource in it's backpack
	 * @return true if this unit carries something
	 */
	public boolean checkBackpack() {
		return gatheredResource != null;
	}
	
	/**
	 * remove the resource from unit's backpack
	 * @return gatheredResource
	 */
	public GatheredResource removeGatheredResource() {
		GatheredResource resource = new GatheredResource (gatheredResource.getType(), gatheredResource.getAmount());
		LOGGER.error("Removed "+ resource.getAmount() + " units of "+ resource.getType());
		gatheredResource = null;
		return resource;
	}

	/**
	 * Set the owner of this spacman
	 * @param owner
	 */
	@Override
	public void setOwner(Manager owner) {
		this.owner = owner;
	}

	/**
	 * Get the owner of this spacman
	 * @return owner
	 */
	@Override
	public Manager getOwner() {
		return this.owner;
	}

	/**
	 * Check if this spacman has the same owner as the other Abstract enitity
	 * @param entity
	 * @return true if they do have the same owner, false if not
	 */
	@Override
	public boolean sameOwner(AbstractEntity entity) {
		boolean isInstance = entity instanceof HasOwner;
		return isInstance && this.owner == ((HasOwner) entity).getOwner();
	}
	
	/**
	 * Check if this spacman currently has an action
	 * @return true if an action is present
	 */
	@Override
	public boolean isWorking() {
		return currentAction.isPresent();
	}
	
	/**
	 * Set an current action for this spac man
	 * @param action
	 */
	@Override
	public void setAction(DecoAction action) {
		currentAction = Optional.of(action);
	}

	/**
	 * Forces the spacman to only try the chosen action on the next rightclick
	 * @param nextAction the action to be forced
	 */
	@Override
	public void setNextAction(ActionType nextAction) {
		this.nextAction = nextAction;
		LOGGER.info("Next action set as " + ActionSetter.getActionName(nextAction));
	}

	public EntityStats getStats() {
		return new EntityStats("Spacman",this.health,this.getPosX(),this.getPosY(),this.getPosZ(), this.gatheredResource, this.currentAction, this);
	}

}