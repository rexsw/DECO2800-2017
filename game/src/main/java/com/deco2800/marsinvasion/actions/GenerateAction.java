package com.deco2800.marsinvasion.actions;

import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.WorldEntity;

/**
 * Created by timhadwen on 23/7/17.
 */
public class GenerateAction implements DecoAction {

	private WorldEntity actionResult;
	private int progress = 10;
	private AbstractWorld world;

	public GenerateAction(WorldEntity actionResult, AbstractWorld world) {
		this.actionResult = actionResult;
		this.world = world;
	}

	@Override
	public void doAction() {
		if (progress > 0) {
			progress--;
			System.out.printf("Progress: %d", progress);
		} else {
			System.out.println("Done");
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
		return progress;
	}
}
