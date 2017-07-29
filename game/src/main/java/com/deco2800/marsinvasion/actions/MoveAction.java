package com.deco2800.marsinvasion.actions;

import com.deco2800.moos.worlds.WorldEntity;

/**
 * A MoveAction for moving units around
 * Created by timhadwen on 23/7/17.
 */
public class MoveAction implements DecoAction {

	int goalX = 0;
	int goalY = 0;
	WorldEntity entity;

	float speed = 0.01f;

	boolean completed = false;

	public MoveAction(int goalX, int goalY, WorldEntity entity) {
		this.goalX = goalX;
		this.goalY = goalY;
		this.entity = entity;
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

			if (entity.getPosX() == goalX && entity.getPosY() == goalY) {
				completed = true;
				return;
			}

			float deltaX = entity.getPosX() - goalX;
			float deltaY = entity.getPosY() - goalY;

			float angle = (float)(Math.atan2(deltaY, deltaX)) + (float)(Math.PI);

			float changeX = (float)(speed * Math.cos(angle));
			float changeY = (float)(speed * Math.sin(angle));

			entity.setPosX(entity.getPosX() + changeX);
			entity.setPosY(entity.getPosY() + changeY);
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