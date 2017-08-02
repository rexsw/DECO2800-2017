package com.deco2800.marswars.managers;

import com.deco2800.marswars.worlds.InitialWorld;
import com.deco2800.marswars.worlds.BaseWorld;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Clickable;
import com.deco2800.marswars.worlds.AbstractWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Really crappy mouse handler for the game
 * Created by timhadwen on 23/7/17.
 */
public class MouseHandler extends Manager {

	private static final Logger LOGGER = LoggerFactory.getLogger(MouseHandler.class);

	private List<Clickable> listeners = new ArrayList<>();

	/**
	 * Constructor for the mouse handler
	 * @param world
	 */
	/**
	 * Currently only handles objects on height 0
	 * @param x
	 * @param y
	 */
	public void handleMouseClick(float x, float y, int button) {
		switch(button) {
			case 0: // Left Click
				AbstractWorld world = GameManager.get().getWorld();

				// If we get another left click ignore the previous listeners
				listeners.clear();

				float proj_x = 0 , proj_y = 0;

				proj_x = x/55f;
				proj_y = -(y - 32f / 2f) / 32f + proj_x;
				proj_x -= proj_y - proj_x;

				if (proj_x < 0 || proj_x > world.getWidth() || proj_y < 0 || proj_y > world.getLength()) {
					return;
				}

				LOGGER.info("Clicked on tile x:" + proj_x + " y:" + proj_y);

				List<BaseEntity> entities = ((BaseWorld)GameManager.get().getWorld()).getEntities((int)proj_x, (int)proj_y);


				if (entities.size() == 0) {
					return;
				}

				if (entities.get(entities.size() - 1) instanceof Clickable) {
					((Clickable) entities.get(entities.size() - 1)).onClick(this);
				} else {
					((InitialWorld)world).deSelectAll();
				}

				break;
			case 1: // Right click
				proj_x = x/55f;
				proj_y = -(y - 32f / 2f) / 32f + proj_x;
				proj_x -= proj_y - proj_x;

				for (Clickable c : listeners) {
					c.onRightClick(proj_x, proj_y);
				}
				listeners.clear();
				break;
		}
	}

	public void registerForRightClickNotification(Clickable thing) {
		listeners.add(thing);
	}
}