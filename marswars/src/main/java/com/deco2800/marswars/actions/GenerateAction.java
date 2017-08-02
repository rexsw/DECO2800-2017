package com.deco2800.marswars.actions;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.GameManager;
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

	/**
	 * Constructor for the Generator action
	 * @param actionResult
	 */
	public GenerateAction(BaseEntity actionResult) {
		this.actionResult = actionResult;
		this.world = GameManager.get().getWorld();
	}

	@Override
	public void doAction() {
		if (progress > 0) {
			progress--;
		} else {
			this.world.addEntity(actionResult);
			actionResult = null;
		}
	}

	@Override
	public boolean completed() {
		return actionResult == null;
	}

	@Override
	public int actionProgress() {
		return 100-progress;
	}
}