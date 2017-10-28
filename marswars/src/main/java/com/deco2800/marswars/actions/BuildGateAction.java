package com.deco2800.marswars.actions;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.buildings.GateHorizontal;
import com.deco2800.marswars.buildings.GateVertical;
import com.deco2800.marswars.buildings.WallHorizontal;
import com.deco2800.marswars.buildings.WallVertical;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.GameBlackBoard;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.worlds.BaseWorld;

/**
 * A BuildGateAction for replacing 1x1 wall entity with gate
 * Created by grumpygandalf on 22/10/17.
 */
public class BuildGateAction implements DecoAction {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildGateAction.class);
	private BuildingEntity actor;
	private String gateDirection;
	private int currentXTile;
	private int currentYTile;
	private int owner;
	private BaseWorld thisWorld;
	private boolean completed = false;
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	private boolean actionPaused = false;
	private boolean nodeBefore = false;
	private boolean nodeAfter = false;
	
	/**
	 * Constructor for the BuildAction
	 * @param wall The wall to be replaced with gate
	 */
	public BuildGateAction(BuildingEntity wall) {
		this.actor = wall;
		this.owner = wall.getOwner();
		if (actor instanceof WallHorizontal) {
			this.gateDirection = "gate1";
		}else if (actor instanceof WallVertical) {
			this.gateDirection = "gate2";
		}
	}
	
	/**
	 * Replaces wall entity with gate entity
	 */
	@Override
	public void doAction() {
		if (! timeManager.isPaused() && ! actionPaused && !completed) {
			currentXTile = (int)actor.getPosX();
			currentYTile = (int)actor.getPosY();
			thisWorld = (BaseWorld) GameManager.get().getWorld();
			if (gateDirection.equals("gate1")) {
				checkLocalNode(currentXTile-1, currentYTile, false, 0);
				checkLocalNode(currentXTile+1, currentYTile, true, 0);
				if (nodeAfter && nodeBefore) {
					thisWorld.removeEntity(actor);
					GameBlackBoard gbb = (GameBlackBoard)GameManager.get().getManager(GameBlackBoard.class);
					gbb.updateDead(actor);
					GateHorizontal replace = new GateHorizontal(thisWorld,currentXTile, currentYTile, 0f, owner);
					replace.setFix(true);
					thisWorld.addEntity(replace);
					gbb.updateunit(replace);
				}
			} else if (gateDirection.equals("gate2")) {
				checkLocalNode(currentXTile, currentYTile-1, false, 1);
				checkLocalNode(currentXTile, currentYTile+1, true, 1);
				if (nodeAfter && nodeBefore) {
					thisWorld.removeEntity(actor);
					GameBlackBoard gbb = (GameBlackBoard)GameManager.get().getManager(GameBlackBoard.class);
					gbb.updateDead(actor);
					GateVertical replace = new GateVertical(thisWorld,currentXTile, currentYTile, 0f, owner);
					replace.setFix(true);
					thisWorld.addEntity(replace);
					gbb.updateunit(replace);
				}
			}
			completed = true;
		}
	}
	
	/**
	 * Checks local nodes for wall or gate
	 * @param xLoc x location to be checked
	 * @param yLoc y location to be checked
	 * @param direction horizontal or vertical wall
	 */
	private void checkLocalNode(int xLoc, int yLoc, boolean after, int direction) {
		
		if (xLoc >= 0 && yLoc >= 0  && xLoc < thisWorld.getWidth() && yLoc < thisWorld.getLength()) {
			for (BaseEntity check: thisWorld.getEntities(xLoc, yLoc)) {
				LOGGER.debug("GOT INSIDE");
				if (direction == 0) {
					if (check instanceof GateHorizontal || check instanceof WallHorizontal) {
						if (after) {
							nodeAfter = true;
						}else {
							nodeBefore = true;
						}
					}
				}else {
					if (check instanceof GateVertical || check instanceof WallVertical) {
						if (after) {
							nodeAfter = true;
						}else {
							nodeBefore = true;
						}
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

}
