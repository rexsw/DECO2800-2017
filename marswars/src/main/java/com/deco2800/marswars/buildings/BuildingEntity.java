package com.deco2800.marswars.buildings;

import com.badlogic.gdx.audio.Sound;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.AttackAction;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.managers.ColourManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.SoundManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.util.Box3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
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
	protected List<String> graphics;
	// Size of this building (must be isometric, 2x2, 3x3 etc)
	private float buildSize;
	private String building;
	//The number of solider in the building
	private int numOfSolider;
	// Current action of this building
	protected Optional<DecoAction> currentAction = Optional.empty();
	// bool for weather event tracking
	boolean isFlooded = false;
	//Colour for this building
	protected TextureManager tm;
	private String teamColour;
	private String buildingTexture;
	private BuildingType buildType;
	protected boolean canAttack = false;
	

	protected boolean built = true;
	//building has functionality if built is true (Jenkins is dumb)
	/**
	 * Constructor for the BuildingEntity
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param building
	 * @param owner
	 */
	public BuildingEntity(float posX, float posY, float posZ, BuildingType building, int owner) {
		super(new Box3D(posX, posY, posZ, building.getBuildSize(), building.getBuildSize(), 
				0f), building.getBuildSize(), building.getBuildSize(), false);
		super.setOwner(owner);
		tm = (TextureManager) GameManager.get().getManager(TextureManager.class);
		teamColour = ((ColourManager) GameManager.get().getManager(ColourManager.class)).getColour(owner);
		this.buildingTexture = building.getTextName();
		this.setCost(100); //Prefer avoid pathfinding through buildings but still allow it
		this.buildType = building;
		switch(building) {
		case WALL:
			String wall1 = "1"+teamColour+"WallHorizontal";
			String wall2 = "1"+teamColour+"WallVertical";
			graphics = Arrays.asList(wall1, wall2);
			setBuilding("Wall", graphics.get(0), 15f, 3550, 3);
			break;
		case GATE:
			graphics = Arrays.asList("gate1", "gate2");
			setBuilding("Gate", graphics.size() >1 ? graphics.get(0):"spacman_ded", 15f, 2550, 3);
			break;
		case TURRET:
			setAllTextures(5);
			setBuilding("Turret", graphics.size() >3 ? graphics.get(2):"spacman_ded", 1f, 2500, 20, 0);
			break;
		case BASE:
			setAllTextures(5);
	        setBuilding("Base", graphics.size() >3 ? graphics.get(2):"spacman_ded", 0.5f, 3500, 3);
			break;
		case BARRACKS:
			setAllTextures(5);
			setBuilding("Barracks", graphics.size() >3 ? graphics.get(2):"spacman_ded", 1f, 2000, 3);
			break;
		case BUNKER:
			setAllTextures(5);
			setBuilding("Bunker", graphics.size() >3 ? graphics.get(2):"spacman_ded", 0.5f, 800, 2);
			break;
		case HEROFACTORY:
			setAllTextures(5);
			setBuilding("HeroFactory", graphics.size() >3 ? graphics.get(2):"spacman_ded", 1f, 2800, 3);
			break;
		case SPACEX:
			setAllTextures(5);
			setBuilding("TechBuilding", graphics.size() >3 ? graphics.get(2):"spacman_ded", 1f, 1800, 2);
			break;
		default:
			break;
		}
		buildSize = building.getBuildSize();
		this.setEntityType(EntityType.BUILDING);
		this.numOfSolider = 0;
		
	}
	

	/**
	 * Return build size of this entity
	 * @return float build size
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
	 * @return int progress
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
				Sound loadedSound = sound.loadSound("baseSelect.mp3");
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
		if (built)  {
			if (this.getOwner() < 0) {
				modifyFogOfWarMap(true, getFogRange());
				addActions(buildType);
			}
			if (buildType == BuildingType.TURRET && !(currentAction.isPresent()) && canAttack) {
				//Enemies within attack range are found
				List<BaseEntity> entityList = GameManager.get().getWorld().getEntities();
				List<AttackableEntity> enemy = new ArrayList<AttackableEntity>();
				for (BaseEntity e: entityList) {
					//If an attackable entity
					if (e instanceof AttackableEntity) {
						//Not owned by the same player
						AttackableEntity attackable = (AttackableEntity) e;
						if (!this.sameOwner(attackable)) {
							//Within attacking distance
							float diffX = attackable.getPosX() - this.getPosX();
							float diffY = attackable.getPosY() - this.getPosY();
							if (Math.abs(diffX) + Math.abs(diffY) <= this.getAttackRange()) {
								LOGGER.debug("ENEMY IN SIGHTS");
								enemy.add((AttackableEntity) e);
							}
						}
					}
				}
				aggressiveBehaviour(enemy);
			}
		}
		if(getHealth()<=0 && built) {
			modifyFogOfWarMap(false,getFogRange());
		}
		if (currentAction.isPresent()) {
			currentAction.get().doAction();

			if (currentAction.get().completed()) {
				currentAction = Optional.empty();
			}
		}
	}
	
	/**
	 * Will attack any enemy in its attack range.
	 * @param enemy
	 */
	public void aggressiveBehaviour(List<AttackableEntity> enemy) {
		//Attack threats first
		for (AttackableEntity a: enemy) {
			if (a.getStance() == 2) {
				for (int i=1; i<=getAttackRange(); i++) {
					//Attack closest
					float xDistance = a.getPosX() - this.getPosX();
					float yDistance = a.getPosY() - this.getPosY();
					boolean distanceEquality = Math.abs(Math.abs(yDistance) + Math.abs(xDistance) - i) < 0.01;
					if (distanceEquality) {
						attack(a);
						return;
					}
				}
			}
		}
		for (AttackableEntity a: enemy) {
			for (int i=1; i<=getAttackRange(); i++) {
				//Attack closest
				float xDistance = a.getPosX() - this.getPosX();
				float yDistance = a.getPosY() - this.getPosY();
				boolean distanceEquality = Math.abs(Math.abs(yDistance) + Math.abs(xDistance) - i) < 0.01;
				if (distanceEquality) {
					attack(a);
					return;
				}
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
	 * @return boolean state
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
		if (this.getHealth() < this.getMaxHealth()*.8 && built && building != "Wall" && building != "Gate") {
			this.setTexture(graphics.get(3));
		}
		if (this.getHealth() < this.getMaxHealth()*.3 && built && building != "Wall" && building != "Gate") {
			this.setTexture(graphics.get(4));
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
    
	/**
	 * Sets building details
	 * 
	 *  @param buildingType
	 *  @param texture
	 *  @param buildSpeed
	 *  @param maxHealth
	 *  @param fogRange
	 */
    private void setBuilding(String buildingType,  String texture, float buildSpeed, 
    		int maxHealth, int fogRange) {
        this.setTexture(texture);
        this.setBuildSpeed(buildSpeed);
        this.setMaxHealth(maxHealth);
        this.setHealth(maxHealth);
        this.building = buildingType;
        setFogRange(fogRange);
    }
    
	/**
	 * Sets building details and gives it stats for attacking
	 * 
	 *  @param buildingType
	 *  @param texture
	 *  @param buildSpeed
	 *  @param maxHealth
	 *  @param fogRange
	 *  @param damage
	 */
    private void setBuilding(String buildingType, String texture, float buildSpeed, 
    		int maxHealth, int fogRange, int damage) {
        setBuilding(buildingType, texture, buildSpeed, maxHealth, fogRange);
        this.setDamage(damage);
		this.setArmorDamage(500);
		this.setAttackRange(fogRange-4);
		this.setMaxAttackSpeed(10);
		this.setAttackSpeed(10);
    }
    
    /**
     * Adds the appropriate actions to the building based off of its type.
     * @param type the type of the building to add the actions.
     */
    private void addActions(BuildingType type) {
        switch (type) {
        case WALL:  
        	this.addNewAction(ActionType.BUILDGATE);
            break;
        case GATE:  
            break;
        case TURRET:  
    		this.addNewAction(ActionType.UNLOAD);
            break;
        case BASE:
            this.addNewAction(EntityID.ASTRONAUT);
            this.addNewAction(EntityID.SOLDIER);
            break;
        case BARRACKS:
            this.addNewAction(EntityID.MEDIC);
            this.addNewAction(EntityID.HACKER);
            this.addNewAction(EntityID.CARRIER);
            this.addNewAction(EntityID.TANK);
            break;
        case BUNKER:
            break;
        case HEROFACTORY:
            this.addNewAction(EntityID.COMMANDER);
            this.addNewAction(EntityID.SNIPER);
            this.addNewAction(EntityID.TANKDESTROYER);
            this.addNewAction(EntityID.SPATMAN);
            break;
        case SPACEX:
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
	 * Gets entity stats
	 * @return The stats of the entity
	 */
	@Override
	public EntityStats getStats() {
		return new EntityStats(building,this.getHealth(),this.getMaxHealth(), null, this.getCurrentAction(), this);
	}
	
	/**
	 * Returns Sets all string textures and adds them to list
	 * @param loadnumber how many types of textures to load
	 */
	public void setAllTextures(int loadnumber) {
		try {
			graphics = new ArrayList<String>(loadnumber);
			for (int a = 0 ; a < loadnumber; a++) {
				String tex = tm.getBuildingSprite(buildingTexture, teamColour, String.valueOf(a+1));
				graphics.add(a, tex);
			}
		}
		catch(NullPointerException n){
			LOGGER.error("setAlltexture has error");
			return;
		}
	}
	
	/**
	 * Helper function for attack. Override if the entity has different types of target.
	 * Set the target type to be enemy or friend or etc.. and check if the target is valid
	 * @param target to be attacked
	 * @return boolean true if valid target
	 */
	public boolean setTargetType(AttackableEntity target) {
		if (!this.sameOwner(target) //(belongs to another player, currently always true)
				&& this!= target) { //prevent soldier suicide when owner is not set
			return true;
		}
		return false;
	}
	
	/**
	 * Make the building start attacking target
	 * @param target to be attacked
	 */
	public void attack(AttackableEntity target){
		if (setTargetType(target)) {
			currentAction = Optional.of(new AttackAction(this, target));
		} 
	}
}
