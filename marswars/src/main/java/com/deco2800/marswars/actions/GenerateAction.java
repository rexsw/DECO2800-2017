package com.deco2800.marswars.actions;

import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.entities.AbstractEntity;

/**
 * A generator action for deploying units from buildings
 * Created by timhadwen on 23/7/17.
 */
public class GenerateAction implements DecoAction {

	/* The resultant entity to be created */
	private AbstractEntity actionResult;

	/* The progress of the entities creation */
	private int progress = 100;

	/* The world to spawn the entity into */
	private AbstractWorld world;

	/**
	 * Constructor for the Generator action
	 * @param actionResult
	 * @param world
	 */
	public GenerateAction(AbstractEntity actionResult, AbstractWorld world) {
		this.actionResult = actionResult;
		this.world = world;
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