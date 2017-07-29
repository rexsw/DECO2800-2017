package com.deco2800.marsinvasion.util;

import com.deco2800.moos.renderers.Renderable;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.WorldEntity;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A utility class for the World instances
 * Created by timhadwen on 23/7/17.
 */
public class WorldUtil {

	/**
	 * Finds the closest entity to a position within a delta
	 * @param world
	 * @param x
	 * @param y
	 * @param delta
	 * @return Optional of WorldEntity
	 */
	public static Optional<WorldEntity> closestEntityToPosition(AbstractWorld world, float x, float y, float delta) {
		WorldEntity ret = null;
		double distance = Double.MAX_VALUE;
		for (Renderable e : world.getEntities()) {
			double tmp_distance = Math.sqrt(Math.pow((e.getPosX() - x), 2) + Math.pow((e.getPosY() - y), 2));

			if (tmp_distance < distance) {
				// Closer than current closest
				distance = tmp_distance;
				ret = (WorldEntity) e;
			}
		}
		if (distance < delta){
			return Optional.of(ret);
		} else {
			return Optional.empty();
		}
	}

	public static ArrayList getEntitiesOfClass(ArrayList<Renderable> entities, Class<?> c) {
		return new ArrayList(entities.stream().filter(e -> e.getClass() == c).collect(Collectors.toList()));
	}

	public static WorldEntity getClosestEntityOfClass(AbstractWorld world, Class<?> c, float x, float y) {
		ArrayList<WorldEntity> entities = WorldUtil.getEntitiesOfClass(world.getEntities(), c);

		WorldEntity closest = null;
		float dist = Float.MAX_VALUE;
		for (WorldEntity e : entities) {
			double tmp_distance = Math.sqrt(Math.pow((e.getPosX() - x), 2) + Math.pow((e.getPosY() - y), 2));
//			if (closest == null || )
		}

		return null;

	}
}
