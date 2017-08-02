package com.deco2800.marsinvasion.actions;

import com.deco2800.moos.util.Box3D;
import com.deco2800.moos.entities.AbstractEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A MoveAction for moving units around
 * Created by timhadwen on 23/7/17.
 */
public class MoveAction implements DecoAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(MoveAction.class);

	float goalX = 0;
	float goalY = 0;
	AbstractEntity entity;

	float speed = 0.2f;

	boolean completed = false;

	public MoveAction(float goalX, float goalY, AbstractEntity entity) {
		this.goalX = goalX;
		this.goalY = goalY;
		this.entity = entity;

		LOGGER.info("new x: " + goalX + " y: " + goalY);
        }

	@Override
	public void doAction() {
		if (entity != null) {

			if (Math.abs(entity.getPosX() - goalX) < speed) {
				entity.setPosX(goalX);
			}

			if (Math.abs(entity.getPosY() - goalY) < speed) {
				entity.setPosY(goalY);
			}

			if (entity.getBox3D().distance(new Box3D(goalX, goalY, 0f, 0f, 0f, 0f)) < 1) {
				completed = true;
				return;
			}

			float deltaX = entity.getPosX() - goalX;
			float deltaY = entity.getPosY() - goalY;

			float angle = (float)(Math.atan2(deltaY, deltaX)) + (float)(Math.PI);

			float changeX = (float)(speed * Math.cos(angle));
			float changeY = (float)(speed * Math.sin(angle));

			float newX = entity.getPosX() + changeX;
			float newY = entity.getPosY() + changeY;

			LOGGER.info("Moving to X:" + newX + " Y:" + newY);

			entity.setPosX(newX);
			entity.setPosY(newY);
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