package com.deco2800.marswars.buildings;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.deco2800.marswars.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.entities.Clickable;
import com.deco2800.marswars.entities.HasProgress;
import com.deco2800.marswars.entities.Tickable;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.managers.ColourManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
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
	private String colour;
	//Colour for this building
	protected int fogRange;
	//distance building can see in fog
	private boolean built = true;
	//building has functionality if built is true
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
		this.addNewAction(EntityID.ASTRONAUT);
		ColourManager cm = (ColourManager) GameManager.get()
				.getManager(ColourManager.class);
		//colour = cm.getColour(owner); TEXTURES NOT READY
		colour = "";
		switch(building) {
		case TURRET:
			graphics = Arrays.asList("turret1"+colour, "turret2"+colour, "turret3"+colour, "turret4"+colour);
			this.setTexture(graphics.get(graphics.size()-2));
			this.setBuildSpeed(1f);
			this.setMaxHealth(1850);
			this.setHealth(1850);
			this.building = "Turret";
			fogRange = 7;
			break;
		case BASE:
			graphics = Arrays.asList("base1"+colour, "base2"+colour, "base3"+colour, "base4"+colour);
			this.setTexture(graphics.get(graphics.size()-2));
			this.setBuildSpeed(.5f);
			this.setMaxHealth(2500);
			this.setHealth(2500);
			this.setFix(true);
			this.building = "Base";
			fogRange = 3;
			break;
		case BARRACKS:
			graphics = Arrays.asList("barracks1"+colour, "barracks2"+colour, "barracks3"+colour, "barracks4"+colour);
			this.setTexture(graphics.get(graphics.size()-2));
			this.setBuildSpeed(1.5f);
			this.setMaxHealth(2000);
			this.setHealth(2000);
			this.setFix(true);
			this.building = "Barracks";
			fogRange = 3;
			break;
		case BUNKER:
			graphics = Arrays.asList("bunker1"+colour, "bunker2"+colour, "bunker3"+colour, "bunker4"+colour);
			this.setTexture(graphics.get(graphics.size()-2));
			this.setBuildSpeed(.5f);
			this.setMaxHealth(800);
			this.setHealth(800);
			this.building = "Bunker";
			fogRange = 2;
			break;
		case HEROFACTORY:
			//Update this
			break;
		default:
			break;
		}
		//this.setCost(building.getCost());
		this.setCost(0);
		buildSize = building.getBuildSize();
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
		if(!this.isAi()) {
			if (!this.isSelected()) {
				this.makeSelected();
				handler.registerForRightClickNotification(this);
				LOGGER.info("clicked on base");
				sound.playSound("closed.wav");
				LOGGER.info("info"+ String.valueOf(super.getHealth()));
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
		if (this.getOwner() == -1 && built)  {
			modifyFogOfWarMap(true, fogRange);
		}
		if (currentAction.isPresent()) {
			currentAction.get().doAction();

			if (currentAction.get().completed()) {
				currentAction = Optional.empty();
			}
		}
	}
	
	
	/**
	 * @return Building Name
	 */
	public String getbuilding() {
		return building;
	}
	
	/**
	 * Set the 'built' state of building
	 * @param built building functions only accessible if build state is true
	 */
	public void setBuilt(boolean built) {
		this.built = built;
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

	/**
	 * Set the health of the entity
	 * @param health of the entity
	 */
	@Override
	public void setHealth(int health) {
		super.setHealth(health);
		if (this.getHealth() < this.getMaxHealth()/3 && built) {
			this.setTexture(graphics.get(3));
		}
	}
	
	/**
	 * Returns buildings name
	 * @return String name of building
	 */
	@Override
	public String toString(){
		return building;
	}
	
}
