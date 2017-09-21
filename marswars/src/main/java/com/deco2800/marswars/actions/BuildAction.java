package com.deco2800.marswars.actions;

import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.worlds.AbstractWorld;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.marswars.buildings.Barracks;
import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.buildings.Bunker;
import com.deco2800.marswars.buildings.CheckSelect;
import com.deco2800.marswars.buildings.Turret;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.SoundManager;


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
	private int tileWidth = GameManager.get().getWorld().getMap().getProperties().get("tilewidth", Integer.class);
	private int tileHeight = GameManager.get().getWorld().getMap().getProperties().get("tileheight", Integer.class);
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
	
	/**
	 * Keeps getting current position of mouse pointer and checks if it's a valid build area
	 * When called on, switches state to move builder and begin building
	 */
	public void doAction() {
		if (! timeManager.isPaused() && ! actionPaused && completed == false) {
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
					if (temp != null) {
						GameManager.get().getWorld().removeEntity(temp);
						validBuild = true;
					}
					camera = GameManager.get().getCamera();
					Vector3 worldCoords = camera.unproject(new
							Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
					projX = worldCoords.x / tileWidth;
					projY = -(worldCoords.y - tileHeight / 2f)
							/ tileHeight + projX;
					projX -= projY - projX;
					projX = (int) projX;
					projY = (int) projY;
					if (buildingDims % 2 == 0) {
						fixPos = .5f;
					}
					if (!(projX < (((buildingDims + 1) / 2) - fixPos) || projX >
							(GameManager.get().getWorld().getWidth() - buildingDims - fixPos)
							|| projY < (((buildingDims + 1) / 2) - fixPos) || projY >
							GameManager.get().getWorld().getLength() - buildingDims - fixPos)) {
						temp = new CheckSelect(projX+fixPos-((int)((buildingDims+1)/2)), projY+fixPos, 0f,
								buildingDims, buildingDims, 0f, building);
						validBuild = GameManager.get().getWorld().checkValidPlace(building, temp.getPosX(), temp.getPosY(), buildingDims, fixPos);
						if (validBuild) {
							temp.setGreen();
							GameManager.get().getWorld().addEntity(temp);

						} else {
							temp.setRed();
							GameManager.get().getWorld().addEntity(temp);
						}
					}
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
	 * Can be called on to force this action to begin building.
	 */
	public void finaliseBuild() {
		if (temp != null && validBuild) {
			GameManager.get().getWorld().removeEntity(temp);
			ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
			if (resourceManager.getRocks(actor.getOwner()) >= building.getCost()) {
				createBuilding();
				resourceManager.setRocks(resourceManager.getRocks(actor.getOwner()) - base.getCost(), actor.getOwner());
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
			//Update this
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
