package com.deco2800.marsinvasion.handlers;

import com.deco2800.moos.entities.Tree;
import com.deco2800.moos.worlds.AbstractWorld;

/**
 * Really crappy mouse handler for the game
 * Created by timhadwen on 23/7/17.
 */
public class MouseHandler {
	private AbstractWorld world;

	public MouseHandler(AbstractWorld world) {
		this.world = world;
	}


	/**
	 * Currently only handles objects on height 0
	 * @param x
	 * @param y
	 */
	public void handleMouseClick(float x, float y) {
		System.out.printf("Clicked at %f %f\n\r", x, y);

		float proj_x = 0 , proj_y = 0;

		proj_x = x/64f;
		proj_y = -(y - 32f / 2f) / 32f + proj_x;
		proj_x -= proj_y - proj_x;

		System.out.printf("Object at %d %d\n\r", (int)proj_x, (int)proj_y);

		this.world.addEntity(new Tree(this.world, (int)proj_x, (int)proj_y, 0));
	}
}