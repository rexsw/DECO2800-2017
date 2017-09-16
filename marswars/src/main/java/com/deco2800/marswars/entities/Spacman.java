package com.deco2800.marswars.entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.deco2800.marswars.actions.BuildAction;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.ActionSetter;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.buildings.BuildingType;
import com.deco2800.marswars.managers.*;
import com.deco2800.marswars.technology.Technology;
import com.deco2800.marswars.util.Array2D;

import com.deco2800.marswars.util.Point;
import com.deco2800.marswars.worlds.BaseWorld;
import com.deco2800.marswars.worlds.FogWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sound.sampled.Line;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import com.deco2800.marswars.managers.TechnologyManager;

/**
 * A generic player instance for the game
 * Created by timhadwen on 19/7/17.
 */

public class Spacman extends BaseEntity implements Tickable, Clickable,
		HasHealth, HasOwner, HasAction {
	//LineOfSight lineOfSight;

	private static final Logger LOGGER = LoggerFactory.getLogger(Spacman.class);

	/* Note from Building Team, I see we have a new way of processing actions, 
	 * however they cannot take the building type as a parameter.
	 * I will continue to use the simple implementation, but should merge 
	 * functionality later. Don't want to modify the actiontype class too much.
	 */
	private Optional<DecoAction> currentAction = Optional.empty();

	private int health = 100;
	
	private int owner;

	private int spacManCost = 10;
	
	private BuildAction build = null;
	
	// this is the resource gathered by this unit, it may shift to other unit in a later stage
	private GatheredResource gatheredResource = null;
	private ActionType nextAction = null;
	private BaseEntity nextBuild = null;

	/**
	 * Constructor for the Spacman
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public Spacman(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1, 1, 2);
		LOGGER.info("I LIVE");
		this.setTexture("spacman_green");
		this.setCost(spacManCost);
		this.setEntityType(EntityType.UNIT);
		this.addNewAction(ActionType.GATHER);
		this.addNewAction(ActionType.MOVE);
		this.addNewAction(EntityID.SPACMAN);
		//TechnologyManager t = (TechnologyManager) GameManager.get().getManager(TechnologyManager.class);
		this.setMoveSpeed(0.025f);
		int fogScaleSize=5;//this number should always be odd (the size of the line of sight edge
//
//		lineOfSight = new LineOfSight(posX,posY,posZ,fogScaleSize,fogScaleSize);
//		FogWorld fogWorld = GameManager.get().getFogWorld();
//		fogWorld.addEntity(lineOfSight,fogScaleSize);
	}



	/**
	 * Sets the position X
	 * @param x
	 */
	@Override
	public void setPosX(float x) {
		if(!this.isAi()) {
			//modifyFogOfWarMap(false,5);
		}
		super.setPosX(x);
		//lineOfSight.setPosX(x);
		if(!this.isAi()) {
			//modifyFogOfWarMap(true,5);

		}

	}

	/**
	 * Sets the position Y
	 * @param y
	 */
	@Override
	public void setPosY(float y) {

		if(!this.isAi()) {
			//modifyFogOfWarMap(false,5);
		}
		super.setPosY(y);
		//lineOfSight.setPosY(y);
		if(!this.isAi()) {
			//modifyFogOfWarMap(true,5);

		}

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
			if (GameManager.get().getWorld().getEntities((int)this.getPosX(), (int)this.getPosY()).size() > 1) {
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
	 * On click method for the spacman
	 * Should change to Blue
	 * @param handler
	 */
	@Override
	public void onClick(MouseHandler handler) {
		if(!this.isAi()) {
			// If Spacman is building, cannot interrupt with left click
			if (currentAction.isPresent()) {
				if(currentAction.get() instanceof BuildAction) {
					return;
				}
			}
			handler.registerForRightClickNotification(this);
			this.setTexture("spacman_blue");
			LOGGER.error("Clicked on spacman");
			build = ActionSetter.setAction(this, BuildingType.BASE);
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
		List<BaseEntity> entities;
		try {
			entities = ((BaseWorld) GameManager.get().getWorld()).getEntities((int) x, (int) y);

		} catch (IndexOutOfBoundsException e) {
			// if the right click occurs outside of the game world, nothing will happen
			this.setTexture("spacman_green");
			return;
		}
		if (currentAction.isPresent()) {
			if(currentAction.get() instanceof BuildAction) {
				build.finaliseBuild();
				this.setTexture("spacman_green");
				this.deselect();
				return;
			}
		}
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

		if (health <= 0) {
			GameManager.get().getWorld().removeEntity(this);
			LOGGER.info("I am kill");
		}
	}
	
	/**
	 * Add gathered resource to unit's backpack
	 * @param resource
	 */
	public void addGatheredResource(GatheredResource resource) {
		this.gatheredResource = resource;
		//LOGGER.error("Gathered "+ resource.getAmount() + " units of "+ resource.getType());
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
		//LOGGER.error("Removed "+ resource.getAmount() + " units of "+ resource.getType());
		gatheredResource = null;
		return resource;
	}
	
	/**
	 * Interactive button for building structures
	 */
	public Button getButton() {
		Button button = new TextButton("Please integrate listener so build fnc can be run", new Skin((Gdx.files.internal("uiskin.json"))));
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				buttonWasPressed();
			}
		});
		return button;
	}

	/**
	 * Allows build functionality on press
	 */
	public void buttonWasPressed() {
		build = ActionSetter.setAction(this, BuildingType.BASE);
		LOGGER.info("Begin Building!");
	}
	
	/**
	 * Set the owner of this spacman
	 * @param owner
	 */
	@Override
	public void setOwner(int owner) {
		this.owner = owner;
	}

	/**
	 * Get the owner of this spacman
	 * @return owner
	 */
	@Override
	public int getOwner() {
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
	 * Set an current action for this spacman
	 * @param action
	 */
	@Override
	public void setAction(DecoAction action) {
		currentAction = Optional.of(action);
	}

	/**
	 * Returns the current action of the entity
	 * @return current action
	 */
	@Override
	public Optional<DecoAction> getCurrentAction() {
		return currentAction;
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

	/**
	 * Forces the spacman to only try the chosen action on the next rightclick
	 * @param toBuild unit to build
	 */
	@Override
	public void setNextAction(BaseEntity toBuild, ActionType nextAction) {
		this.nextAction = nextAction;
		this.nextBuild = toBuild;
		LOGGER.info("Next action set as  building unit " + toBuild.getStats().getName());
	}

	public EntityStats getStats() {
		return new EntityStats("Spacman",this.health, this.gatheredResource, this.currentAction, this);
	}
	
	@Override
	public boolean isAi() {
		return owner >= 0;
	}

}
