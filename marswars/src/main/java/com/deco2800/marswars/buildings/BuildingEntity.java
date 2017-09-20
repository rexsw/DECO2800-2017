package com.deco2800.marswars.buildings;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.deco2800.marswars.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.managers.AbstractPlayerManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.Manager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.PlayerManager;
import com.deco2800.marswars.managers.SoundManager;

/**
 * Created by grumpygandalf on 27/8/17.
 *
 * 
 */
public class BuildingEntity extends AttackableEntity implements Clickable,
		Tickable, HasProgress, HasAction, Floodable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildingEntity.class);
	// list of available graphic for this building
	private List<String> graphics;
	// Size of this building (must be isometric, 2x2, 3x3 etc)
	private float buildSize;
	private String building;
	// Current action of this building
	private Optional<DecoAction> currentAction = Optional.empty();
	//owner of this building
	private MouseHandler currentHandler;
	//Current mousehandler manager

	// bool for weather event tracking
	boolean isFlooded = false;

	/**
	 * Constructor for the BuildingEntity
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public BuildingEntity(float posX, float posY, float posZ, BuildingType building, int owner) {
		super(posX, posY, posZ, building.getBuildSize(), building.getBuildSize(), 
				0f, building.getBuildSize(), building.getBuildSize(), false);
		this.setOwner(owner);
		this.setEntityType(EntityType.BUILDING);
		this.addNewAction(ActionType.GENERATE);
		switch(building) {
		case TURRET:
			this.setCost(200);
			graphics = Arrays.asList("turret1", "turret2", "turret3");
			this.setTexture(graphics.get(graphics.size()-1));
			this.setSpeed(1f);
			this.setHealth(1850);
			this.building = "Turret";
			break;
		case BASE:
			this.setCost(350);
			graphics = Arrays.asList("base1", "base2", "base3");
			this.setTexture(graphics.get(graphics.size()-1));
			this.setSpeed(.5f);
			this.setHealth(2500);
			this.setFix(true);
			this.building = "Base";
			break;
		case BARRACKS:
			this.setCost(300);
			graphics = Arrays.asList("barracks1", "barracks2", "barracks3");
			this.setTexture(graphics.get(graphics.size()-1));
			this.setSpeed(1.5f);
			this.setHealth(2000);
			this.setFix(true);
			this.building = "Barracks";
			break;
		case BUNKER:
			this.setCost(100);
			graphics = Arrays.asList("bunker1", "bunker2", "bunker3");
			this.setTexture(graphics.get(graphics.size()-1));
			this.setSpeed(.5f);
			this.setHealth(800);
			this.building = "Bunker";
			break;
		default:
			break;
		}
		buildSize = building.getBuildSize();
		this.setCost(0); //free cost for testing
	}
	

	/**
	 * Return build size of this entity
	 */
	public float getBuildSize() {
		return this.buildSize;
	}
	
	/**
	 * Load first building stage
	 */
	public void animate1() {
		this.setTexture(graphics.get(0));
	}
	
	/**
	 * Load second building stage
	 */
	public void animate2() {
		this.setTexture(graphics.get(1));
	}
	
	/**
	 * Load final building stage
	 */
	public void animate3() {
		this.setTexture(graphics.get(2));
	}
	
	@Override
	/**
	 * Get the progress of current action
	 * @return int
	 */
	public int getProgress() {
		if (currentAction.isPresent()) {
			return currentAction.get().actionProgress();
		}
		return 0;
	}

	@Override
	/**
	 * Returns true if there is a current action, false if not
	 * @return boolean
	 */
	public boolean showProgress() {
		return currentAction.isPresent();
	}
	
	@Override
	/**
	 * Set the action of this building
	 * @param action
	 */
	public void setAction(DecoAction action) {
		currentAction = Optional.of(action);
	}

	/**
	 * Returns the current action (used in WeatherManager)
	 * @return
	 */
	public Optional<DecoAction> getAction() {
		return currentAction;
	}
	
	/**
	 * Give action to the building
	 * @param action
	 */
	public void giveAction(DecoAction action) {
		if (!currentAction.isPresent()) {
			currentAction = Optional.of(action);
		}
	}
	
	/**
	 * On click handler
	 * 
	 * * @param handler
	 */
	public void onClick(MouseHandler handler) {
		SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
		currentHandler = handler;
		if(!this.isAi()) {
			if (!this.isSelected()) {
				this.makeSelected();
				handler.registerForRightClickNotification(this);
				LOGGER.info("clicked on base");
				sound.playSound("closed.wav");
			}
		} else {
			this.makeSelected();
			LOGGER.info("clicked on ai base");
			sound.playSound("endturn.wav");
		}
	}

	/**
	 * Perform some action when right clicked at x, y
	 * @param x
	 * @param y
	 */
	public void onRightClick(float x, float y) {
		//base has no action on right click for now
		SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
		this.deselect();
	}

	/**
	 * On Tick handler for all buildings
	 * @param i time since last tick
	 */
	public void onTick(int i) {
		if (currentAction.isPresent()) {
			currentAction.get().doAction();

			if (currentAction.get().completed()) {
				currentAction = Optional.empty();
			}
		}
	}
	
	/**
	 * @return The stats of the entity
	 */
	public EntityStats getStats() {
		return new EntityStats(building, this.getHealth(), null, currentAction, this);
	}

	/**
	 * Sets the boolean describing whether or not the BuildingEntity is
	 * currently under the flooding effect.
	 * @param state
	 */
	public void setFlooded(boolean state){
		this.isFlooded = state;
	}

	/**
	 * Returns the boolean describing whether or not the BuildingEntity is
	 * currently under the flooding effect.
	 * @return state
	 */
	public boolean isFlooded() {
		return this.isFlooded;
	}
}
