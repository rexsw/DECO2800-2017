package com.deco2800.marswars.actions;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.deco2800.marswars.buildings.*;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.SoundManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.util.WorldUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A BuildSelectAction for selecting a valid area to build on
 * Created by grumpygandalf on 15/8/17.
 */

public class BuildAction implements DecoAction{
	
	enum State {
		SELECT_SPACE,
		BUILD_STRUCTURE,
		SETUP_MOVE,
		MOVE_ACTION,
		CANCEL_BUILD
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildAction.class);
	private boolean completed = false;
	OrthographicCamera camera;
	private float projX;
	private float projY;
	private int relocateSelect = 10;
	private CheckSelect temp;
	private float buildingDims;
	private boolean validBuild;
	private State state = State.SELECT_SPACE;
	private float speedMultiplier = 3.33f;
	private float buildingSpeed = 1;
	private BuildingEntity base;
	private BaseEntity actor;
	private int maxHealth;
	private int currentHealth = 10;
	private MoveAction moveAction = null;
	private BuildingType building;
	private float fixPos = 0f;
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	private boolean actionPaused = false;
	private long id;
	private Sound sound;

	/**
	 * Constructor for the BuildAction
	 * @param builder The unit assigned the construction
	 * @param building Type of building to be constructed
	 */
	public BuildAction(BaseEntity builder, BuildingType building) {
		this.actor = builder;
		this.building = building;
		this.buildingDims = (int)(building.getBuildSize());
	}
	
	public BuildAction(BaseEntity builder, BuildingType building, float x, float y) {
		this.actor = builder;
		this.building = building;
		this.buildingDims = (int)(building.getBuildSize());
		this.projX = x;
		this.projY = y;
		this.state = State.SETUP_MOVE;
		float[] parse = new float[]{projX, projY, fixPos};
		this.temp = WorldUtil.selectionStage(temp, buildingDims, parse, building);
		temp.setGreen();
		validBuild = true;
		finaliseBuild();
	}
	
	/**
	 * Keeps getting current position of mouse pointer and checks if it's a valid build area
	 * When called on, switches state to move builder and begin building
	 */
	public void doAction() {
		if (! timeManager.isPaused() && ! actionPaused && !completed) {
			if (state == State.CANCEL_BUILD) {
				if (temp != null) {
					GameManager.get().getWorld().removeEntity(temp);
					if (sound != null && id != 0) {
						SoundManager soundManager = (SoundManager) GameManager.get().getManager(SoundManager.class);
						soundManager.stopSound(sound, id);
					}
				}
				if (base != null){
					GameManager.get().getWorld().removeEntity(base);
					if (sound != null && id != 0) {
						SoundManager soundManager = (SoundManager) GameManager.get().getManager(SoundManager.class);
						soundManager.stopSound(sound, id);
					}
				}
				completed = true;
			}
			if (state == State.SELECT_SPACE) {
				relocateSelect--;
				if (relocateSelect == 0) {
					float[] parse = new float[]{projX, projY, fixPos};
					temp = WorldUtil.selectionStage(temp, buildingDims, parse, building);
					/*
					 * updating coordinates and parsed values into selectionStage because java passes by values unless 
					 * it's an object.
					 */
					projX = parse[0];
					projY = parse[1];
					fixPos = parse[2];
					//updating validBuild based on the texture of temp.
					validBuild = temp == null ? false : temp.getTexture().contains("green") || 
							temp.getTexture().contains("Green");
					relocateSelect = 10;

				}
			} else if (state == State.BUILD_STRUCTURE) {
				if (base != null) {
					if (currentHealth == 10) {
						SoundManager soundManager = (SoundManager) GameManager.get().getManager(SoundManager.class);
						sound = soundManager.loadSound("building.wav");
						id = soundManager.playSound(sound);
						soundManager.loopSound(sound, id);
					}
					if (currentHealth >= maxHealth) {
						currentHealth = maxHealth;
						base.animate3();
						base.setBuilt(true);
						SoundManager soundManager = (SoundManager) GameManager.get().getManager(SoundManager.class);
						soundManager.stopSound(sound, id);
						completed = true;
						LOGGER.error("FINALISED");
					} else if (maxHealth / 2 < currentHealth) {
						base.animate2();
						currentHealth = (int) (currentHealth + (buildingSpeed * speedMultiplier));
						base.setHealth(currentHealth);
					} else {
						base.animate1();
						currentHealth = (int) (currentHealth + (buildingSpeed * speedMultiplier));
						base.setHealth(currentHealth);
					}
					
				}
			} else if (state == State.SETUP_MOVE) {
				moveAction = new MoveAction(projX, projY, actor);
				state = State.MOVE_ACTION;
			} else if (state == State.MOVE_ACTION) {
				if (moveAction.completed()) {
					state = State.BUILD_STRUCTURE;
					return;
				}
				moveAction.doAction();
			}
		}
	}
	
	/**
	 * Checks completed state
	 * @return returns boolean stating if building was completed
	 */
	@Override
	public boolean completed() {
		return completed;
	}
	
	/**
	 * Returns number from 0 to 100 representing completion
	 * @return percentage of completion 
	 */
	@Override
	public int actionProgress() {
		return (int)(100 * (base.getMaxHealth() / base.getHealth()));
	}
	
	/**
     * checks if there are enough resources to pay for the selected entity
     * @param owner
     * @param c
     * @param resourceManager
     * @return
     */
    private boolean canAfford(int owner, ResourceManager resourceManager) {
    	if (resourceManager.getRocks(owner) >= building.getCost()
    			|| GameManager.get().areCostsFree()) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * checks if there are enough resources to pay for the selected entity
     * @param owner
     * @param c
     * @param resourceManager
     * @return
     */
    private void payForEntity(int owner, ResourceManager resourceManager) {
    	if (GameManager.get().areCostsFree()) {
    		// no payment
    	} else {
    		resourceManager.setRocks(resourceManager.getRocks(owner) - building.getCost(), owner);
    	}
    }
	
	/**
	 * Can be called on to force this action to begin building.
	 */
	public void finaliseBuild() {
		if (temp != null && validBuild) {
			GameManager.get().getWorld().removeEntity(temp);
			ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
			if (canAfford(actor.getOwner(), resourceManager)) {
				createBuilding();
				payForEntity(actor.getOwner(), resourceManager);
				GameManager.get().getWorld().addEntity(base);
				this.buildingSpeed = base.getBuildSpeed();
				maxHealth = base.getMaxHealth();
				base.setHealth(currentHealth);
				state = State.SETUP_MOVE;
				LOGGER.info("BUILDING NEW " + building.toString());
			}
			else {
				LOGGER.error("NEED MORE ROCKS TO CONSTRUCT BASE" + resourceManager.getRocks(actor.getOwner()));
				completed = true;
			}

		}
		else {
			LOGGER.error("CANNOT BUILD HERE");
			if (temp != null) {
				GameManager.get().getWorld().removeEntity(temp);
				completed = true;
			}
		}
	}
	
	/**
	 * Creates the building depending on the building type
	 */
	private void createBuilding() {
		switch(building) {
		case TURRET:
			base = new Turret(GameManager.get().getWorld(), 
					(int)projX+fixPos-((int)((buildingDims+1)/2)), (int)projY+fixPos, 0f, actor.getOwner());
			break;
		case BASE:
			base = new Base(GameManager.get().getWorld(), 
					(int)projX+fixPos-((int)((buildingDims+1)/2)), (int)projY+fixPos, 0f, actor.getOwner());
			break;
		case BARRACKS:
			base = new Barracks(GameManager.get().getWorld(), 
					(int)projX+fixPos-((int)((buildingDims+1)/2)), (int)projY+fixPos, 0f, actor.getOwner());
			break;
		case BUNKER:
			base = new Bunker(GameManager.get().getWorld(), 
					(int)projX+fixPos-((int)((buildingDims+1)/2)), (int)projY+fixPos, 0f, actor.getOwner());
			break;
		case HEROFACTORY:
			base = new HeroFactory(GameManager.get().getWorld(),
					(int)projX+fixPos-((int)((buildingDims+1)/2)), (int)projY+fixPos, 0f, actor.getOwner());
			break;
		case TECHBUILDING:
			base = new TechBuilding(GameManager.get().getWorld(), 
					(int)projX+fixPos-((int)((buildingDims+1)/2)), (int)projY+fixPos, 0f, actor.getOwner());
			break;
		default:
			break;
		}
		base.setFix(true);
		base.setBuilt(false);
		base.animate1();
	}

	/**
	 * Prevents the current action from progressing.
	 */
	@Override
	public void pauseAction() {
		actionPaused = true;
	}

	/**
	 * Resumes the current action
	 */
	@Override
	public void resumeAction() {
		actionPaused = false;
	}
	
	/**
	 * Forces building to cancel
	 */
	public void cancelBuild() {
		state = State.CANCEL_BUILD;
	}
	
	/**
	 * Checks if build action is in selection mode
	 * @return returns true if in select mode
	 */
	public boolean selectMode() {
		return state == State.SELECT_SPACE;
	}
}
