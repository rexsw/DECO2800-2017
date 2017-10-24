package com.deco2800.marswars.buildings;

import com.badlogic.gdx.audio.Sound;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.SoundManager;
import com.deco2800.marswars.util.Box3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
	//The number of solider in the building
	private int numOfSolider;
	// Current action of this building
	protected Optional<DecoAction> currentAction = Optional.empty();
	// bool for weather event tracking
	boolean isFlooded = false;
	private String colour;
	//Colour for this building

	protected boolean built = true;
	//building has functionality if built is true
	/**
	 * Constructor for the BuildingEntity
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public BuildingEntity(float posX, float posY, float posZ, BuildingType building, int owner) {
		super(new Box3D(posX, posY, posZ, building.getBuildSize(), building.getBuildSize(), 
				0f), building.getBuildSize(), building.getBuildSize(), false);
		this.setOwner(owner);
		this.setEntityType(EntityType.BUILDING);
		colour = "";
		this.numOfSolider = 0;
		
		switch(building) {
		case WALL:
			graphics = Arrays.asList("wall1"+colour, "wall1"+colour, "wall1"+colour, "wall1"+colour);
			setBuilding("Wall", graphics.get(graphics.size()-2), 5f, 3550, 5, 0);
			addActions(building);
			break;
		case TURRET:
			graphics = Arrays.asList("turret1"+colour, "turret2"+colour, "turret3"+colour, "turret4"+colour);
			setBuilding("Turret", graphics.get(graphics.size()-2), 1f, 1850, 12, 10);
			addActions(building);
			break;
		case BASE:
			graphics = Arrays.asList("base1"+colour, "base2"+colour, "base3"+colour, "base4"+colour);
	        setBuilding("Base", graphics.get(graphics.size()-2), 0.5f, 2500, 3);
	        addActions(building);
	        this.setFix(true);
			break;
		case BARRACKS:
			graphics = Arrays.asList("barracks1"+colour, "barracks2"+colour, "barracks3"+colour, "barracks4"+colour);
			setBuilding("Barracks", graphics.get(graphics.size()-2), 1.5f, 2000, 3);
			addActions(building);
			break;
		case BUNKER:
			graphics = Arrays.asList("bunker1"+colour, "bunker2"+colour, "bunker3"+colour, "bunker4"+colour);
			setBuilding("Bunker", graphics.get(graphics.size()-2), 0.5f, 800, 2);
			addActions(building);
			break;
		case HEROFACTORY:
			graphics = Arrays.asList("herofactory1"+colour, "herofactory2"+colour, "herofactory3"+colour,
					"herofactory4"+colour);
			setBuilding("Hero Factory", graphics.get(graphics.size()-2), 0.5f, 3000, 3);
			addActions(building);
			break;
		case TECHBUILDING:
			graphics = Arrays.asList("tech1"+colour, "tech2"+colour, "tech3"+colour, "tech4"+colour);
			setBuilding("TechBuilding", graphics.get(graphics.size()-2), 0.5f, 800, 2);
			addActions(building);
			break;
		default:
			break;
		}
		this.setCost(building.getCost());
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
		if (! isFlooded) {
			currentAction = Optional.of(action);
		}
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
		if (!currentAction.isPresent() && ! isFlooded) {
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
				Sound loadedSound = sound.loadSound("closed.wav");
				sound.playSound(loadedSound);
			}
		} else {
			this.makeSelected();
			LOGGER.info("clicked on ai base");
			
		}
	}

	/**
	 * Perform some action when right clicked at x, y
	 * @param x
	 * @param y
	 */
	public void onRightClick(float x, float y) {
		//base has no action on right click for now
		this.deselect();
	}

	/**
	 * On Tick handler for all buildings
	 * @param i time since last tick
	 */
	public void onTick(int i) {
		if (this.getOwner() == -1 && built)  {
			modifyFogOfWarMap(true, getFogRange());
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
	 * Returns the 'built' state of building
	 * @return returns true if building is completed
	 */
	public boolean getBuilt() {
		return built;
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
	 * 
	 * @return the number of soldiers in the building
	 */
	public int getNumOfSolider() {
        return numOfSolider;
    }


	/**
	 * Sets the number of soldiers stored in the building.
	 * 
	 * @param numOfSolider The number of soldiers to set to be in the building.
	 */
    public void setNumOfSolider(int numOfSolider) {
        this.numOfSolider = numOfSolider;
    }
    
    private void setBuilding(String buildingType,  String texture, float buildSpeed, int maxHealth, int fogRange) {
        this.setTexture(texture);
        this.setBuildSpeed(buildSpeed);
        this.setMaxHealth(maxHealth);
        this.setHealth(maxHealth);
        this.building = buildingType;
        setFogRange(fogRange);
    }
    
    private void setBuilding(String buildingType, String texture, float buildSpeed, int maxHealth, int fogRange, int damage) {
        setBuilding(buildingType, texture, buildSpeed, maxHealth, fogRange);
        this.setDamage(damage);
    }
    
    /**
     * Adds the appropriate actions to the building based off of its type.
     * @param type the type of the building to add the actions.
     */
    private void addActions(BuildingType type) {
        switch (type) {
        case WALL:  
            break;
        case TURRET:  
            break;
        case BASE:
            this.addNewAction(EntityID.ASTRONAUT);
            this.addNewAction(EntityID.TANK);
            this.addNewAction(EntityID.SOLDIER);
            break;
        case BARRACKS:
            this.addNewAction(EntityID.MEDIC);
            this.addNewAction(EntityID.HACKER);
            this.addNewAction(EntityID.CARRIER);
            break;
        case BUNKER:
            this.addNewAction(EntityID.SNIPER);
            this.addNewAction(EntityID.TANKDESTROYER);
            this.addNewAction(EntityID.SPATMAN);
            break;
        case HEROFACTORY:
            this.addNewAction(EntityID.COMMANDER);
            break;
        case TECHBUILDING:
            break;
        default:
            break;
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
	
	/**
	 * @return The stats of the entity
	 */
	@Override
	public EntityStats getStats() {
		return new EntityStats(building,this.getHealth(),this.getMaxHealth(), null, this.getCurrentAction(), this);
	}
}
