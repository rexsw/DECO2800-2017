package com.deco2800.marswars.util;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.renderers.Renderable;
import com.deco2800.marswars.worlds.BaseWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A utility class for the BaseWorld instances
 * Created by timhadwen on 23/7/17.
 */
public class WorldUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorldUtil.class);

	/**
	 * Finds the closest entity to a position within a delta
	 * @param world
	 * @param x
	 * @param y
	 * @param delta
	 * @return Optional of AbstractEntity
	 */
	public static Optional<BaseEntity> closestEntityToPosition(BaseWorld world, float x, float y, float delta) {
		BaseEntity ret = null;
		double distance = Double.MAX_VALUE;
		for (Renderable e : world.getEntities()) {
			double tmp_distance = Math.sqrt(Math.pow((e.getPosX() - x), 2) + Math.pow((e.getPosY() - y), 2));

			if (tmp_distance < distance) {
				// Closer than current closest
				distance = tmp_distance;
				ret = (BaseEntity) e;
			}
		}
		if (ret == null) {
			LOGGER.info("Found nothing");
			return Optional.empty();
		}
		if (distance < delta){
			LOGGER.info("Closest is " + ret.toString());
			return Optional.of(ret);
		} else {
			LOGGER.info("Nothing is that close");
			return Optional.empty();
		}
	}

	public static List getEntitiesOfClass(List<BaseEntity> entities, Class<?> c) {
		List<BaseEntity> classEntities = new ArrayList<>();
		for (BaseEntity w : entities) {
			if (w.getClass() == c) {
				classEntities.add(w);
			}
		}
		return classEntities;
	}

	public static Optional<BaseEntity> getClosestEntityOfClass(Class<?> c, float x, float y) {
		List<BaseEntity> entities = WorldUtil.getEntitiesOfClass(GameManager.get().getWorld().getEntities(), c);

		BaseEntity closest = null;
		float dist = Float.MAX_VALUE;
		for (BaseEntity e : entities) {
			float tmp_distance = (float)(Math.sqrt(Math.pow((e.getPosX() - x), 2) + Math.pow((e.getPosY() - y), 2)));
			if (closest == null || dist > tmp_distance) {
				dist = tmp_distance;
				closest = e;
			}
		}

		if (closest == null) {
			return Optional.empty();
		} else {
			return Optional.of(closest);
		}
	}

	public static Optional<BaseEntity> getEntityAtPosition(BaseWorld world, float x, float y) {
		for (Renderable e : world.getEntities()) {
			if (Math.abs(e.getPosX() - x) < 1f && Math.abs(e.getPosY() - y) < 1f) {
				return Optional.of((BaseEntity)e);
			}
		}
		return Optional.empty();
	}
}
