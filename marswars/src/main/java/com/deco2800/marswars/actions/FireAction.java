package com.deco2800.marswars.actions;

import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.MissileEntity;
import com.deco2800.marswars.initiategame.Game;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;

/**
 * A FireAction for moving missiles around
 * Created by vinsonyeung on 12/10/17.
 */
public class FireAction extends AbstractPauseAction {

	/* Goal positions */
	private float goalX = 0;
	private float goalY = 0;

	/* Direction */
	private int directionX = 0;
	private int directionY = 0;
	
	/* Entity we are trying to move towards */
	private AbstractEntity entity;

	/* Speed factor */
	private float speed;

	/* Movement */
	private float movementX;
	private float movementY;
	
	/* Completed variable */
	private boolean completed = false;
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);

	/**
	 * Constructor for the move action
	 * @param goalX goal position X
	 * @param goalY goal position Y
	 * @param entity entity to move around
	 */
	public FireAction(float goalX, float goalY, AbstractEntity entity) {
		this.goalX = goalX;
		this.goalY = goalY;
		this.entity = entity;
		
		if (entity instanceof AttackableEntity) {
			speed = ((AttackableEntity) entity).getSpeed();
		} else if (entity instanceof MissileEntity) {
			speed = ((MissileEntity) entity).getSpeed();
		} else {
			speed = 0.05f;
		}
		
		/* Used to check if the entity has passed the goal */
		if (entity.getPosX() > goalX) {
			directionX = 1;
		} else if (entity.getPosX() < goalX) {
			directionX = -1;
		}
		if (entity.getPosY() > goalY) {
			directionY = 1;
		} else if (entity.getPosY() < goalY) {
			directionY = -1;
		}
		
		float deltaX = entity.getPosX() - goalX;
		float deltaY = entity.getPosY() - goalY;
		float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);
		movementX = (float) (speed * Math.cos(angle));
		movementY = (float) (speed * Math.sin(angle));

		if (this.goalX < 0)
			this.goalX = 0;
		if (this.goalY < 0)
			this.goalY = 0;
	}

	public FireAction(float goalX, float goalY, AbstractEntity entity, float speed) {
		this(goalX, goalY, entity);
		this.speed = speed;
	}

	/**
	 * Do action method
	 * Completes the action 1 tick at a time
	 */
	@Override
	public void doAction() {
		if (! timeManager.isPaused() && ! actionPaused) {
            /* The new position */
			float newX = entity.getPosX() + movementX;
			float newY = entity.getPosY() + movementY;
			/* Check if entity has reached the target square or surpassed it */
			boolean reachedTarget =(directionX == 1 && entity.getPosX() <= goalX) ||
					(directionX == -1 && entity.getPosX() >= goalX) ||
					(directionY == 1 && entity.getPosY() <= goalY) ||
					(directionY == -1 && entity.getPosY() >= goalY);
			boolean offMap = newX<0 || newY<0 || newX> GameManager.get().getWorld().getWidth()||
					newY>GameManager.get().getWorld().getLength();
			if (reachedTarget || offMap) {
				entity.setPosX(goalX);
				entity.setPosY(goalY);
				completed = true;
				return;
			}
			

			
			/* Apply these values to the entity */
			entity.setPosX(newX);
			entity.setPosY(newY);
		}
	}

	/**
	 * Returns true if action is completed and can therefore be removed
	 * @return
	 */
	@Override
	public boolean completed() {
		return completed;
	}
}