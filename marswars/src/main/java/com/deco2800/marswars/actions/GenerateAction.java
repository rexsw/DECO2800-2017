package com.deco2800.marswars.actions;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.GameBlackBoard;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.worlds.BaseWorld;

/**
 * A generator action for deploying units from buildings
 * Created by timhadwen on 23/7/17.
 */
public class GenerateAction implements DecoAction {

	/* The resultant entity to be created */
	private BaseEntity actionResult;

	/* The progress of the entities creation */
	private int progress = 100;

	/* The world to spawn the entity into */
	private BaseWorld world;

	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	private boolean actionPaused = false;

	/**
	 * Constructor for the Generator action
	 * @param actionResult the new action result
	 */
	public GenerateAction(BaseEntity actionResult) {
		this.actionResult = actionResult;
		this.world = GameManager.get().getWorld();
	}

	/**
	 * Completes the GenerateAction
	 * 
	 */
	@Override
	public void doAction() {
		if (! timeManager.isPaused() && ! actionPaused) {
			if (progress > 0) {
				progress--;
			} else {
				ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
				if (resourceManager.getPopulation(actionResult.getOwner()) < resourceManager.getMaxPopulation(actionResult.getOwner())) {
					this.world.addEntity(actionResult);
					GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
					black.updateunit(actionResult);
					actionResult = null;
				}
			}
		}
	}

	/**
	 * Returns true if completed, otherwise false
	 * @return
	 */
	@Override
	public boolean completed() {
		return actionResult == null;
	}

	/**
	 * Returns a progress percentage (where available)
	 * @return
	 */
	@Override
	public int actionProgress() {
		return 100-progress;
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