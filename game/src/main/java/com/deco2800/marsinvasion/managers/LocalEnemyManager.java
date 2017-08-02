package com.deco2800.marsinvasion.managers;

import com.deco2800.marsinvasion.actions.MoveAction;
import com.deco2800.marsinvasion.entities.EnemySpacman;
import com.deco2800.marsinvasion.entities.Spacman;
import com.deco2800.marsinvasion.util.WorldUtil;
import com.deco2800.moos.managers.GameManager;
import com.deco2800.moos.managers.Manager;
import com.deco2800.moos.managers.TickableManager;
import com.deco2800.moos.entities.AbstractEntity;

import java.util.List;
import java.util.Optional;

/**
 * Created by timhadwen on 30/7/17.
 */
public class LocalEnemyManager extends Manager implements TickableManager {

	@Override
	public void onTick(long l) {
		List<AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		for (AbstractEntity e : entities) {
			if (e instanceof EnemySpacman) {
				Optional<AbstractEntity> spacman = WorldUtil.getClosestEntityOfClass(Spacman.class, e.getPosX(), e.getPosY());
				if (spacman.isPresent() && !((EnemySpacman) e).getCurrentAction().isPresent()) {
					if (e.distance(spacman.get()) < 2f) {
						((Spacman)spacman.get()).setHealth(((Spacman) spacman.get()).getHealth() - 2);
					}
					if (e.distance(spacman.get()) < 5f) {
						((EnemySpacman) e).setCurrentAction(new MoveAction(spacman.get().getPosX(), spacman.get().getPosY(), e));
					}
				}
			}
		}
	}
}
