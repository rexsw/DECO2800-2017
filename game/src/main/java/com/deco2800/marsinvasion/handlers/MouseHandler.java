package com.deco2800.marsinvasion.handlers;

import com.deco2800.marsinvasion.InitialWorld;
import com.deco2800.marsinvasion.entities.Clickable;
import com.deco2800.marsinvasion.util.WorldUtil;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.WorldEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Really crappy mouse handler for the game
 * Created by timhadwen on 23/7/17.
 */
public class MouseHandler {
	private AbstractWorld world;

	private List<Clickable> listeners = new ArrayList<>();

	/**
	 * Constructor for the mouse handler
	 * @param world
	 */
	public MouseHandler(AbstractWorld world) {
		this.world = world;
	}

	/**
	 * Currently only handles objects on height 0
	 * @param x
	 * @param y
	 */
	public void handleMouseClick(float x, float y, int button) {
		switch(button) {
			case 0: // Left Click
				System.out.printf("Clicked at %f %f\n\r", x, y);

				// If we get another left click ignore the previous listeners
				listeners.clear();

				float proj_x = 0 , proj_y = 0;

				proj_x = x/55f;
				proj_y = -(y - 32f / 2f) / 32f + proj_x;
				proj_x -= proj_y - proj_x;

				Optional<WorldEntity> closest = WorldUtil.closestEntityToPosition(world, proj_x, proj_y, 2f);
				if (closest.isPresent() &&  closest.get() instanceof Clickable) {
					((Clickable) closest.get()).onClick(this);
				} else {
					if (world instanceof InitialWorld) {
						((InitialWorld)(world)).deSelectAll();
					}
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