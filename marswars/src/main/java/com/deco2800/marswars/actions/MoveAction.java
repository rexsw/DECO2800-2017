package com.deco2800.marswars.actions;

import com.deco2800.marswars.World;
import com.deco2800.marswars.util.Pathfinder;
import com.deco2800.marswars.util.Point;
import com.deco2800.moos.entities.AbstractEntity;
import com.deco2800.moos.managers.GameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A MoveAction for moving units around
 * Created by timhadwen on 23/7/17.
 */
public class MoveAction implements DecoAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(MoveAction.class);

	float goalX = 0;
	float goalY = 0;
	AbstractEntity entity;

	float speed = 0.05f;

	boolean completed = false;

	public MoveAction(float goalX, float goalY, AbstractEntity entity) {
		this.goalX = goalX;
		this.goalY = goalY;
		this.entity = entity;

		LOGGER.info("new x: " + goalX + " y: " + goalY);
        }

	@Override
	public void doAction() {
		List<Point> path = Pathfinder.aStar(new Point(entity.getPosX(), entity.getPosY()), new Point(goalX, goalY), (World) GameManager.get().getWorld());

		LOGGER.info("PATH SIZE: " + path.size());

		if (path.size() < 1) {
			completed = true;
			return;
		}

		if (entity != null) {

			float tmpgoalX = path.get(0).getX();
			float tmpgoalY = path.get(0).getY();

			if (Math.abs(entity.getPosX() - goalX) < speed && Math.abs(entity.getPosY() - goalY) < speed) {
				entity.setPosX(goalX);
				entity.setPosY(goalY);
				completed = true;
				return;
			}

			float deltaX = entity.getPosX() - tmpgoalX;
			float deltaY = entity.getPosY() - tmpgoalY;

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