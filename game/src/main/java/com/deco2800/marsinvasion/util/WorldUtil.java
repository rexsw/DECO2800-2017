package com.deco2800.marsinvasion.util;

import com.deco2800.moos.DesktopLauncher;
import com.deco2800.moos.managers.GameManager;
import com.deco2800.moos.renderers.Renderable;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.entities.AbstractEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A utility class for the World instances
 * Created by timhadwen on 23/7/17.
 */
public class WorldUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DesktopLauncher.class);

	/**
	 * Finds the closest entity to a position within a delta
	 * @param world
	 * @param x
	 * @param y
	 * @param delta
	 * @return Optional of AbstractEntity
	 */
	public static Optional<AbstractEntity> closestEntityToPosition(AbstractWorld world, float x, float y, float delta) {
		AbstractEntity ret = null;
		double distance = Double.MAX_VALUE;
		for (Renderable e : world.getEntities()) {
			double tmp_distance = Math.sqrt(Math.pow((e.getPosX() - x), 2) + Math.pow((e.getPosY() - y), 2));

			if (tmp_distance < distance) {
				// Closer than current closest
				distance = tmp_distance;
				ret = (AbstractEntity) e;
			}
		}
		if (distance < delta){
			LOGGER.info("Closest is " + ret.toString());
			return Optional.of(ret);
		} else {
			LOGGER.info("Nothing is that close");
			return Optional.empty();
		}
	}

	public static List getEntitiesOfClass(List<AbstractEntity> entities, Class<?> c) {
		List<AbstractEntity> classEntities = new ArrayList<>();
		for (AbstractEntity w : entities) {
			if (w.getClass() == c) {
				classEntities.add(w);
			}
		}
		return classEntities;
	}

	public static Optional<AbstractEntity> getClosestEntityOfClass(Class<?> c, float x, float y) {
		List<AbstractEntity> entities = WorldUtil.getEntitiesOfClass(GameManager.get().getWorld().getEntities(), c);

		AbstractEntity closest = null;
		float dist = Float.MAX_VALUE;
		for (AbstractEntity e : entities) {
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

	public static Optional<AbstractEntity> getEntityAtPosition(AbstractWorld world, float x, float y) {
		for (Renderable e : world.getEntities()) {
			if (Math.abs(e.getPosX() - x) < 1f && Math.abs(e.getPosY() - y) < 1f) {
				return Optional.of((AbstractEntity)e);
			}
		}
		return Optional.empty();
	}
}
