package com.deco2800.marswars.actions;

import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.MissileEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.util.PathfindingThread;
import com.deco2800.marswars.util.Point;

import java.util.List;

/**
 * A MoveAction for moving units around
 * Created by timhadwen on 23/7/17.
 */
public class MoveAction implements DecoAction {


	/* Goal positions */
	private float goalX = 0;
	private float goalY = 0;

	/* Entity we are trying to move towards */
	private AbstractEntity entity;

	/* Speed factor */
	private float speed;

	/* Completed variable */
	private boolean completed = false;

	/* A* Path we have found - Be careful, it might be null */
	private List<Point> path;

	/* Threads so that the pathfidning runs seperately and doesnt pause other entities */
	private PathfindingThread pathfinder;
	private Thread thread;

	/**
	 * Constructor for the move action
	 * @param goalX goal position X
	 * @param goalY goal position Y
	 * @param entity entity to move around
	 */
	public MoveAction(float goalX, float goalY, AbstractEntity entity) {
		this.goalX = goalX;
		this.goalY = goalY;
		this.entity = entity;
		
		if (entity instanceof MissileEntity) {
			speed = 0.4f;
		} else {
			speed = 0.05f;
		}

		if (this.goalX < 0)
			this.goalX = 0;
		if (this.goalY < 0)
			this.goalY = 0;


		pathfinder = new PathfindingThread(GameManager.get().getWorld(), new Point(entity.getPosX(), entity.getPosY()), new Point(goalX, goalY));
		thread = new Thread(pathfinder);
		thread.start();
	}

	/**
	 * Do action method
	 * Completes the action 1 tick at a time
	 */
	@Override
	public void doAction() {

		/* Ensure the thread has died and therefore the path has been found, otherwise return */
		if (thread.isAlive()) {
			return;
		} else {
			path = pathfinder.getPath();
		}

		/* If the path is null its probably completed */
		if (path == null || path.isEmpty()) {
			completed = true;
			return;
		}

		/* grab the next point on the path and attempt to move towards it */
		float tmpgoalX = path.get(0).getX();
		float tmpgoalY = path.get(0).getY();

		/* If we have arrived (or close enough to) then remove this point from the path and continue */
		if (Math.abs(entity.getPosX() - tmpgoalX) < speed && Math.abs(entity.getPosY() - tmpgoalY) < speed) {
			entity.setPosX(tmpgoalX);
			entity.setPosY(tmpgoalY);
			path.remove(0);
			return;
		}

		/* Calculate a deltaX and Y to move based on polar coordinates and speed to ensure
				speed is constant regardless of direction
		 */
		float deltaX = entity.getPosX() - tmpgoalX;
		float deltaY = entity.getPosY() - tmpgoalY;
		float angle = (float)(Math.atan2(deltaY, deltaX)) + (float)(Math.PI);
		float changeX = (float)(speed * Math.cos(angle));
		float changeY = (float)(speed * Math.sin(angle));
		float newX = entity.getPosX() + changeX;
		float newY = entity.getPosY() + changeY;

		/* Apply these values to the entity */
		entity.setPosX(newX);
		entity.setPosY(newY);
	}

	/**
	 * Returns true if action is completed and can therefore be removed
	 * @return
	 */
	@Override
	public boolean completed() {
		return completed;
	}

	/**
	 * Returns progress
	 * @return
	 */
	@Override
	public int actionProgress() {
		return 0;
	}
}