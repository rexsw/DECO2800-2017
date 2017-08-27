package com.deco2800.marswars.entities;

import com.deco2800.marswars.managers.MouseHandler;

/**
 * An interface to make an Entity clickable via the MouseHandler
 * Created by timhadwen on 23/7/17.
 */
public interface Clickable {
	/**
	 * Called on entity click
	 * @param handler a mouse handler.
	 */
	void onClick(MouseHandler handler);

	/**
	 * Called on entity right click only when registered for right click notifications
	 * See MouseHandler
	 * @param x
	 * @param y
	 */
	void onRightClick(float x, float y);
}