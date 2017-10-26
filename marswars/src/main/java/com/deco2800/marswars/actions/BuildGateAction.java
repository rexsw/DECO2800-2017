package com.deco2800.marswars.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.buildings.GateHorizontal;
import com.deco2800.marswars.buildings.GateVertical;
import com.deco2800.marswars.buildings.WallHorizontal;
import com.deco2800.marswars.buildings.WallVertical;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.GameBlackBoard;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.worlds.BaseWorld;

public class BuildGateAction implements DecoAction {
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
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildGateAction.class);
	
	public BuildGateAction(BuildingEntity wall) {
		this.actor = wall;
		this.owner = wall.getOwner();
		if (actor instanceof WallHorizontal) {
			this.gateDirection = "gate1";
		}else if (actor instanceof WallVertical) {
			this.gateDirection = "gate2";
		}
	}
	@Override
	public void doAction() {

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
	 * @return percentage of completion 
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
