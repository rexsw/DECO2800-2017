package com.deco2800.marswars.actions;

import com.deco2800.marswars.managers.TimeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.CheckSelect;
import com.deco2800.marswars.entities.Spacman;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.Manager;
import com.deco2800.marswars.managers.ResourceManager;


/**
 * A BuildSelectAction for selecting a valid area to build on
 * Created by grumpygandalf on 15/8/17.
 */

public class BuildAction implements DecoAction{
	
	enum State {
		SELECT_SPACE,
		BUILD_STRUCTURE,
		SETUP_MOVE,
		MOVE_ACTION
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildAction.class);
	private boolean completed = false;
	private float tileWidth = GameManager.get().getWorld().getMap().getProperties().get("tilewidth", Integer.class);
	private float tileHeight = GameManager.get().getWorld().getMap().getProperties().get("tileheight", Integer.class);
	OrthographicCamera camera;
	private float proj_x;
	private float proj_y;
	private int relocateSelect = 10;
	private CheckSelect temp;
	private float buildingDims;
	private boolean validBuild;
	private int buildProgress = 0;
	private State state = State.SELECT_SPACE;
	private float speedMultiplier = .33f;
	private float progress = 0;
	private float buildingSpeed = 1;
	private BuildingEntity base;
	private BaseEntity actor;
	private MoveAction moveAction = null;
	private BuildingType building;
	private float fixPos = 0f;

	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	private boolean actionPaused = false;
	
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
		if (! timeManager.isPaused() && ! actionPaused) {
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
					proj_x = worldCoords.x / tileWidth;
					proj_y = -(worldCoords.y - tileHeight / 2f)
							/ tileHeight + proj_x;
					proj_x -= proj_y - proj_x;
					proj_x = (int) proj_x;
					proj_y = (int) proj_y;
					if (buildingDims % 2 == 0) {
						fixPos = .5f;
					}
					if (!(proj_x < (((buildingDims + 1) / 2) - fixPos) || proj_x >
							(GameManager.get().getWorld().getWidth() - buildingDims - fixPos)
							|| proj_y < (((buildingDims + 1) / 2) - fixPos) || proj_y >
							GameManager.get().getWorld().getLength() - buildingDims - fixPos)) {
						temp = new CheckSelect(proj_x+fixPos-((int)((buildingDims+1)/2)), proj_y+fixPos, 0f,
								buildingDims, buildingDims, 0f);
						validBuild = GameManager.get().getWorld().checkValidPlace(temp.getPosX(), temp.getPosY(), buildingDims, fixPos);
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
					if (progress >= 100) {
						base.animate3();
						this.completed = true;
					} else if (progress > 50) {
						base.animate2();
						progress = progress + (buildingSpeed * speedMultiplier);
					} else {
						base.animate1();
						progress = progress + (buildingSpeed * speedMultiplier);
					}
				}
			} else if (state == State.SETUP_MOVE) {
				moveAction = new MoveAction(proj_x, proj_y, actor);
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
		return buildProgress;
	}
	
	/**
	 * Can be called on to force this action to begin building.
	 */
	public void finaliseBuild() {
		if (temp != null && validBuild == true) {
			GameManager.get().getWorld().removeEntity(temp);
			ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
			base = new BuildingEntity((int)proj_x+fixPos-((int)((buildingDims+1)/2)), (int)proj_y+fixPos, 0f, building, actor.getOwner());
			base.animate1();
			if (resourceManager.getRocks() >= base.getCost()) {
				resourceManager.setRocks(resourceManager.getRocks() - base.getCost());
				GameManager.get().getWorld().addEntity(base);
				base.fixPosition((int)(proj_x-((buildingDims-1)/2)), (int)(proj_y -((buildingDims-1)/2)), 0);
				this.buildingSpeed = base.getSpeed();
				state = State.SETUP_MOVE;
				LOGGER.info("BUILDING NEW STRUCTURE");
			}
			else {
				LOGGER.error("NEED MORE ROCKS TO CONSTRUCT BASE");
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
}
