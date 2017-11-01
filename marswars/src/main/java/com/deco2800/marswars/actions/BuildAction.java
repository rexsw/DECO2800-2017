package com.deco2800.marswars.actions;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.deco2800.marswars.buildings.*;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.AiManager.Difficulty;
import com.deco2800.marswars.managers.*;
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
	private float speedMultiplier = 6.66f;
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
	private double difficultyMultiplier = 1.0;
	private String wallDirection;
	private int startMove;

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
	
	/**
	 * Constructor for the BuildAction
	 * @param builder The unit assigned the construction
	 * @param building Type of building to be constructed
	 * @param x build pos
	 * @param y build pos
	 */
	public BuildAction(BaseEntity builder, BuildingType building, float x, float y) {
		this.actor = builder;
		this.building = building;
		this.buildingDims = (int)(building.getBuildSize());
		this.projX = x;
		this.projY = y;
		validBuild = true;
		finaliseBuild();
	}
	
	/**
	 * Constructor for the BuildAction
	 * @param builder The unit assigned the construction
	 * @param building Type of building to be constructed
	 * @param x build pos
	 * @param y build pos
	 * @param wallDirection load horizontal or vertical wall
	 */
	public BuildAction(BaseEntity builder, BuildingType building, float x, float y, String wallDirection) {
		this.wallDirection = wallDirection;
		this.actor = builder;
		this.building = building;
		this.buildingDims = (int)(building.getBuildSize());
		this.projX = x;
		this.projY = y;
		validBuild = true;
		finaliseBuild();
	}
	
	public BuildAction(BaseEntity builder, BuildingType building, boolean vaild) {
		this.actor = builder;
		this.building = building;
		this.buildingDims = (int)(building.getBuildSize());
		this.projX = 1;
		this.projY = 1;
		this.state = State.SETUP_MOVE;
		validBuild = vaild;
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
					if (sound != null && id != 0 && !actor.isAi()) {
						SoundManager soundManager = (SoundManager) GameManager.get().getManager(SoundManager.class);
						soundManager.stopSound(sound, id);
					}
				}
				if (base != null){
					GameManager.get().getWorld().removeEntity(base);
					if (sound != null && id != 0 && !actor.isAi()) {
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
					if(base.getHealth() < 0) {
						state = State.CANCEL_BUILD;
					}
					if (currentHealth == 10 && !actor.isAi()) {
						SoundManager soundManager = (SoundManager) GameManager.get().getManager(SoundManager.class);
						sound = soundManager.loadSound("building.wav");
						id = soundManager.playSound(sound);
						soundManager.loopSound(sound, id);
					}
					if (currentHealth >= maxHealth) {
						currentHealth = maxHealth;
						base.animate3();
						base.setBuilt(true);
						if (!actor.isAi()) {
							SoundManager soundManager = (SoundManager) GameManager.get().getManager(SoundManager.class);
							soundManager.stopSound(sound, id);
						}
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
				startMove = -1;
				moveAction = new MoveAction(projX + startMove, projY + startMove, actor);
				state = State.MOVE_ACTION;
			} else if (state == State.MOVE_ACTION) {
				if (moveAction.completed()) {
					if (!(actor.getPosX() == (projX + startMove) && actor.getPosY() == (projY + startMove))) {
						startMove ++;
						moveAction = new MoveAction(projX + startMove, projY + startMove, actor);
					}
					else {
						state = State.BUILD_STRUCTURE;
					}
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
	
	public void setDifficultyMultiplier(Difficulty difficulty) {
		switch(difficulty) {
		case EASY:
			difficultyMultiplier = 2.0;
			break;
		case NORMAL:
			difficultyMultiplier = 1.0;
			break;
		case HARD:
			difficultyMultiplier = 0.8;
			break;
		}
	}
	
	/**
     * checks if there are enough resources to pay for the selected entity
     * @param owner
     * @param resourceManager
     * @return boolean true if can afford
     */
    public boolean canAfford(int owner, ResourceManager resourceManager) {
    	if (resourceManager.getRocks(owner) >= (int)(building.getCost()*difficultyMultiplier)
    			|| (GameManager.get().areCostsFree() && !actor.isAi())) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * checks if there are enough resources to pay for the selected entity
     * @param owner
     * @param resourceManager
     */
    private void payForEntity(int owner, ResourceManager resourceManager) {
    	if (GameManager.get().areCostsFree() && !actor.isAi()) {
    		// no payment
    	} else {
    		resourceManager.setRocks(resourceManager.getRocks(owner)
    				- (int)(building.getCost()*difficultyMultiplier), owner);
    	}
    }
    
	/**
	 * Can be called on to force this action to begin building.
	 */
	public void finaliseBuild() {
		if (validBuild) {
			if (temp != null){
				GameManager.get().getWorld().removeEntity(temp);
			}
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
				GameBlackBoard gbb = (GameBlackBoard)GameManager.get().getManager(GameBlackBoard.class);
				gbb.updateunit(base);
			}
			else {
				LOGGER.error("NEED MORE ROCKS TO CONSTRUCT BUILDING" + resourceManager.getRocks(actor.getOwner()));
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
		case WALL:
			if (wallDirection != null) {
				if (wallDirection.equals("wall1")) {
					base = new WallHorizontal(GameManager.get().getWorld(), 
							(int)projX, (int)projY, 0f, actor.getOwner());
				} else if (wallDirection.equals("wall2")) {
					base = new WallVertical(GameManager.get().getWorld(), 
							(int)projX, (int)projY, 0f, actor.getOwner());
				}
			}
			break;
		case TURRET:
			base = new Turret(GameManager.get().getWorld(), 
					(int)projX +fixPos-((int)((buildingDims+1)/2)), (int)projY + fixPos, 0f, actor.getOwner());
			break;
		case BASE:
			base = new Base(GameManager.get().getWorld(), 
					(int)projX +fixPos-((int)((buildingDims+1)/2)), (int)projY + fixPos, 0f, actor.getOwner());
			break;
		case BARRACKS:
			base = new Barracks(GameManager.get().getWorld(), 
					(int)projX +fixPos-((int)((buildingDims+1)/2)), (int)projY + fixPos, 0f, actor.getOwner());
			break;
		case BUNKER:
			base = new Bunker(GameManager.get().getWorld(), 
					(int)projX +fixPos-((int)((buildingDims+1)/2)), (int)projY + fixPos, 0f, actor.getOwner());
			break;
		case HEROFACTORY:
			
			base = new HeroFactory(GameManager.get().getWorld(),
					(int)projX +fixPos-((int)((buildingDims+1)/2)), (int)projY + fixPos, 0f, actor.getOwner());
			break;
		case SPACEX:
			base = new TechBuilding(GameManager.get().getWorld(), 
					(int)projX +fixPos-((int)((buildingDims+1)/2)), (int)projY + fixPos, 0f, actor.getOwner());
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
