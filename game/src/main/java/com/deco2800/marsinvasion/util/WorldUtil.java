package com.deco2800.marsinvasion.util;

import com.deco2800.moos.renderers.Renderable;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.WorldEntity;

import java.util.Optional;

/**
 * Created by timhadwen on 23/7/17.
 */
public class WorldUtil {

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
}
