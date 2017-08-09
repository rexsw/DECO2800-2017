package com.deco2800.marswars.entities;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * An interface for selectable entities
 */
public interface Selectable {
	/**
	 * Returns true if currently selected
	 * @return
	 */
	boolean isSelected();

	/**
	 * Deselects this object
	 */
	void deselect();

	/**
	 * Gets a button to show on the bottom contex menu
	 * TODO Make this a list of things or a menu
	 * @return
	 */
	Button getButton();

	/**
	 * A handler for the button press
	 * TODO again this should probably change
	 */
	void buttonWasPressed();

	/**
	 * Gets a help text for this item.
	 * TODO again this should probably be refactored slightly
	 * @return
	 */
	Label getHelpText();
}
