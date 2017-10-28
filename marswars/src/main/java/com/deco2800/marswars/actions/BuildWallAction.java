package com.deco2800.marswars.actions;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.marswars.actions.BuildAction.State;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.buildings.CheckSelect;
import com.deco2800.marswars.buildings.WallHorizontal;
import com.deco2800.marswars.buildings.WallVertical;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.util.Point;
import com.deco2800.marswars.util.WorldUtil;

/**
 * Allows actor to make a straight horizontal or vertical selection 
 * and then assigns build actions for each wall
 * Created by grumpygandalf on 20/10/2017
 */
public class BuildWallAction implements DecoAction{
	
	enum State {
		SELECT_START,
		SELECT_END,
		BUILD_WALL,
		CANCEL_BUILD
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildWallAction.class);
	private ArrayList<Point> wallProject;
	private ArrayList<CheckSelect> selectionCheck;
	private ArrayList<BuildAction> queue;
	private boolean completed = false;
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	private boolean actionPaused = false;
	private State state = State.SELECT_START;
	private int relocateSelect = 10;
	private boolean validBuild;
	private String wallTex;
	private int stage = 0;
	private float startX;
	private float startY;
	private float endX;
	private float endY;
	private BuildAction currentAction;
	BaseEntity actor;
	
	/**
	 * Constructor for the BuildAction
	 * @param builder The unit assigned the construction
	 */
	public BuildWallAction(BaseEntity builder) {
		this.actor = builder;
	}
	
	@Override
	public void doAction() {
		if (! timeManager.isPaused() && ! actionPaused && !completed) {
			int tileWidth = GameManager.get().getWorld().getMap().getProperties().get("tilewidth", Integer.class);
			int tileHeight = GameManager.get().getWorld().getMap().getProperties().get("tileheight", Integer.class);
			if (! timeManager.isPaused() && ! actionPaused && !completed) {
				if (state == State.CANCEL_BUILD) {
					if (selectionCheck != null) {
						for (CheckSelect wallSelect: selectionCheck) {
							GameManager.get().getWorld().removeEntity(wallSelect);
						}	
					}
					completed = true;
				}
				if (state == State.SELECT_START) {
					relocateSelect--;
					if (relocateSelect == 0) {
						relocateSelect = 10;
					}
				}
				if (state == State.SELECT_END) {
					relocateSelect--;
					if (relocateSelect == 0) {
						if (selectionCheck != null) {
							for (CheckSelect wallSelect: selectionCheck) {
								GameManager.get().getWorld().removeEntity(wallSelect);
							}	
						}
							LOGGER.debug("X start "+(int)startX + " Y start " + (int)startY);
							OrthographicCamera camera = GameManager.get().getCamera();
							Vector3 worldCoords = camera.unproject(new
									Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
							endX = worldCoords.x / tileWidth;
							endY = -(worldCoords.y - tileHeight / 2f) / tileHeight + endX;
							endX -= endY - endX;
							endX = (int) endX;
							endY = (int) endY;
							LOGGER.debug("X "+endX +" Y " + endY);
							Point startPoint = new Point(startX, startY);
							Point endPoint = new Point(endX, endY);
							if ((int)startPoint.getX() == (int)endPoint.getX()) {
								//Vertical wall build
								wallTex = "wall2";
								wallProject = new ArrayList<Point>(Math.abs((int)endPoint.getY()+1-(int)startPoint.getY())+1);
								if (startPoint.getY() >= endPoint.getY()) {
									for (int start = (int)endPoint.getY(); start <= (int)startPoint.getY(); start ++) {
										wallProject.add(new Point((int)startPoint.getX(), start));
									}
								}else {
									for (int start = (int)startPoint.getY(); start <= (int)endPoint.getY(); start ++) {
										wallProject.add(new Point((int)startPoint.getX(), start));
									}
								}
							}else if ((int)startPoint.getY() == (int)endPoint.getY()) {
								//Horizontal wall build
								wallTex = "wall1";
								wallProject = new ArrayList<Point>(Math.abs((int)endPoint.getX()-(int)startPoint.getX())+1);
								if (startPoint.getX() >= endPoint.getX()) {
									for (int start = (int)endPoint.getX(); start <= (int)startPoint.getX(); start ++) {
										wallProject.add(new Point(start, (int)startPoint.getY()));
									}
								} else {
									for (int start = (int)startPoint.getX(); start <= (int)endPoint.getX(); start ++) {
										wallProject.add(new Point(start, (int)startPoint.getY()));
									}
								}
							}else {
								relocateSelect = 10;
								return;
							}
							validBuild = true;
							selectionCheck = new ArrayList<CheckSelect>(wallProject.size());
							for (Point point: wallProject) {
								float[] parse = new float[]{point.getX(), point.getY()};
								ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
	
								if (!GameManager.get().getWorld().checkValidPlace(BuildingType.WALL, (int)parse[0], (int)parse[1], 1, 0)) {
									validBuild = false;
									break;
								}
						    	if (resourceManager.getRocks(actor.getOwner()) >= (int)(BuildingType.WALL.getCost()*wallProject.size())
						    			|| (GameManager.get().areCostsFree() && !actor.isAi())) {
									CheckSelect select = new CheckSelect(parse[0], parse[1], 0, 1, 1, 0);
									selectionCheck.add(select);
						    	}
							}
							if (validBuild == false) {
								for (CheckSelect wallSelect : selectionCheck) {
									wallSelect.setRed();
									GameManager.get().getWorld().addEntity(wallSelect);
								}
							}else {
								for (CheckSelect wallSelect: selectionCheck) {
									wallSelect.setGreen();
									GameManager.get().getWorld().addEntity(wallSelect);
								}
							}
						relocateSelect = 10;
					}
				}
				if (state == State.BUILD_WALL) {
					if (validBuild == false) {
						state = State.CANCEL_BUILD;
						return;
					}
					if (wallProject != null && wallProject.size() >= 1) {
						if (selectionCheck != null) {
							for (CheckSelect wallSelect: selectionCheck) {
								GameManager.get().getWorld().removeEntity(wallSelect);
							}	
							selectionCheck = null;
						}
						if (currentAction != null) {
							if (currentAction.completed()) {
								if (wallProject.size()==1) {
									completed = true;
									return;
								}
								wallProject.remove(0);
							} else if (currentAction.actionProgress() > 0) {
								currentAction.doAction();
								return;
							}
						}
						currentAction = new BuildAction(actor, BuildingType.WALL, (int)wallProject.get(0).getX(), (int)wallProject.get(0).getY(), wallTex);
					}
				}
			}
		}
	}
	
	/**
	 * Returns completed status
	 * @return true if completed
	 */
	@Override
	public boolean completed() {
		return completed;
	}
	
	/**
	 * 
	 * @return int percentage of completion 
	 */
	@Override
	public int actionProgress() {
		return 0;
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
	 * Returns the stage
	 * @return returns stage
	 */
	public int getStage() {
		return stage;
	}
	
	/**
	 * Locks in initial point and waits for end point
	 * @param x position
	 * @param y position
	 */
	public void beginWall(float x, float y) {
		startX = x;
		startY = y;
		relocateSelect = 30;
		state = State.SELECT_END;
	}
	
	/**
	 * Locks in end point and starts wall construction stage
	 */
	public void projectWall() {
		state = State.BUILD_WALL;
	}

	/**
	 * Forces building to cancel
	 */
	public void cancelBuild() {
		state = State.CANCEL_BUILD;
	}
	
	/**
	 * Checks if build action is in selection mode
	 * @return boolean returns true if in select mode
	 */
	public boolean selectMode() {
		return state == State.SELECT_END;
	}
	

}