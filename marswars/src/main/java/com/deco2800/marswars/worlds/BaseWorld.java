package com.deco2800.marswars.worlds;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.util.Array2D;

import java.util.List;

/**
 * A base world for the game. Use this not AbstractWorld
 */
public class BaseWorld extends AbstractWorld {
	/* Crappy way of storing collision */
	protected Array2D<List<BaseEntity>> collisionMap;

	/**
	 * Adds an entity to this world
	 * @param entity
	 */
	public void addEntity(BaseEntity entity) {
		super.addEntity(entity);

		if (!entity.isCollidable())
			return;

		//Add to the collision map
		int left = (int)entity.getPosX();
		int right = (int)Math.ceil(entity.getPosX() + entity.getXLength());
		int bottom = (int)entity.getPosY();
		int top = (int)Math.ceil(entity.getPosY() + entity.getYLength());
		for (int x = left; x < right; x++) {
			for (int y = bottom; y < top; y++) {
				collisionMap.get(x, y).add(entity);
			}
		}
	}

	/**
	 * Gets the collision map of the world.
	 * yes this uses a lot of memory.
	 * @return
	 */
	public Array2D<List<BaseEntity>> getCollisionMap() {
		return collisionMap;
	}

	/**
	 * Returns true if there is an entity in here
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean hasEntity(int x, int y) {
		return collisionMap.get(x, y).size() > 0;
	}

	/**
	 * Gets the entity at an x y position
	 * @param x
	 * @param y
	 * @return
	 */
	public List<BaseEntity> getEntities(int x, int y) {
		return collisionMap.get(x, y);
	}
}
