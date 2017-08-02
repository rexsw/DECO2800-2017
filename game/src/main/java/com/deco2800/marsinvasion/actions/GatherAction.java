package com.deco2800.marsinvasion.actions;

import com.deco2800.marsinvasion.entities.Base;
import com.deco2800.marsinvasion.entities.HasHealth;
import com.deco2800.marsinvasion.util.WorldUtil;
import com.deco2800.moos.entities.AbstractEntity;

import java.util.Optional;

/**
 * Created by timhadwen on 29/7/17.
 */
public class GatherAction implements DecoAction {

	enum State {
		SETUP_MOVE,
		MOVE_TOWARDS,
		COLLECT,
		SETUP_RETURN,
		RETURN_TO_BASE
	}

	MoveAction action = null;
	private State state = State.SETUP_MOVE;
	private AbstractEntity entity;
	private Class type;
	boolean completed = false;

	Optional<AbstractEntity> closest;

	private int ticksCollect = 10;

	public GatherAction(AbstractEntity entity, Class<?> type) {
		this.entity = entity;
		this.type = type;
	}

	@Override
	public void doAction() {
		switch(state) {
			case SETUP_MOVE:
				// Find the closest rock and move towards it
				closest = WorldUtil.getClosestEntityOfClass(type, entity.getPosX(), entity.getPosY());

				if (closest.isPresent()) {
					action = new MoveAction(closest.get().getPosX(), closest.get().getPosY(), entity);
				} else {
					this.completed = true;
				}

				state = State.MOVE_TOWARDS;
				break;
			case MOVE_TOWARDS:
				if (action.completed()) {
					state = State.COLLECT;
					return;
				}

				action.doAction();
				break;
			case COLLECT:
				ticksCollect--;
				if (ticksCollect == 0) {
					state = State.SETUP_RETURN;
					if (closest.isPresent() && closest.get() instanceof HasHealth) {
						((HasHealth) closest.get()).setHealth(((HasHealth) closest.get()).getHealth() - 10);
					}
					ticksCollect = 100;
				}
				break;
			case SETUP_RETURN:
				// Find the closest rock and move towards it
				closest = WorldUtil.getClosestEntityOfClass(Base.class, entity.getPosX(), entity.getPosY());

				if (closest.isPresent()) {
					action = new MoveAction(closest.get().getPosX(), closest.get().getPosY(), entity);
				}

				state = State.RETURN_TO_BASE;
				break;
			case RETURN_TO_BASE:
				if (action.completed()) {
					state = State.SETUP_MOVE;
					return;
				}

				action.doAction();
				break;
		}
	}

	@Override
	public boolean completed() {
		return completed;
	}

	@Override
	public int actionProgress() {
		return 0;
	}
}
