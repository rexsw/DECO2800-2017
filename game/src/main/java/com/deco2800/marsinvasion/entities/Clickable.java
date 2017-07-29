package com.deco2800.marsinvasion.entities;

import com.deco2800.marsinvasion.handlers.MouseHandler;

/**
 * An interface to make an Entity clickable via the MouseHandler
 * Created by timhadwen on 23/7/17.
 */
public interface Clickable {
	void onClick(MouseHandler handler);
	void onRightClick(float x, float y);
}