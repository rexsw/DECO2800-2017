package com.deco2800.marswars;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.moos.util.Array2D;
import com.deco2800.moos.worlds.AbstractWorld;

import java.util.List;

/**
 * Created by timhadwen on 2/8/17.
 */
public class World extends AbstractWorld {
	/* Crappy way of storing collision */
	protected Array2D<List<BaseEntity>> collisionMap;

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

	public Array2D<List<BaseEntity>> getCollisionMap() {
		return collisionMap;
	}

	public boolean hasEntity(int x, int y) {
		return collisionMap.get(x, y).size() > 0;
	}

	public List<BaseEntity> getEntities(int x, int y) {
		return collisionMap.get(x, y);
	}


}
