package com.deco2800.marsinvasion.actions;

import com.deco2800.moos.worlds.WorldEntity;

/**
 * Created by timhadwen on 29/7/17.
 */
public class GatherAction implements DecoAction {

	enum State {
		MOVE_TOWARDS,
		COLLECT,
		RETURN_TO_BASE
	}

	MoveAction action = null;
	private State state = State.MOVE_TOWARDS;
	private WorldEntity entity;
	private Class type;

	public GatherAction(WorldEntity entity, Class<?> type) {
		this.entity = entity;
		this.type = type;
	}

	@Override
	public void doAction() {
		switch(state) {
			case MOVE_TOWARDS:
				// Find the closest rock and move towards it
//				WorldUtil.closestEntityToPosition(entity.getParent(), type)
//				action = new MoveAction()
				break;
			case COLLECT:

				break;
			case RETURN_TO_BASE:

				break;
		}
	}

	@Override
	public boolean completed() {
		return false;
	}

	@Override
	public int actionProgress() {
		return 0;
	}
}
