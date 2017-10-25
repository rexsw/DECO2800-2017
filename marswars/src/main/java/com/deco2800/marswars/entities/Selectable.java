package com.deco2800.marswars.entities;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.deco2800.marswars.actions.ActionList;

/**
 * An interface for selectable entities
 */

public interface Selectable {

	enum EntityType {
		NOT_SET, BUILDING, UNIT, HERO, RESOURCE , AISPACMAN, TECHTREE;
	}

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
	 * @return
	 */
	Button getButton();

	/**
	 * A handler for the button press
	 */
	void buttonWasPressed();

	/**
	 * Gets a help text for this item.
	 * @return
	 */

	Label getHelpText();

	/**
	 * Returns a list of all of the actions the selected entity can make
	 * @return A list containing the actions available to the entity
	 */
	ActionList getValidActions();

	/**
	 *Adds a new valid action to the entity
	 * @param newAction The new action that is valid for the unit to perform
	 * @return True if successful, false if the action was not added or if it was already in the list
	 */
	boolean addNewAction(Object newAction);

	/**
	 *Removes a valid action from the entity
	 * @param actionToRemove The new action that is valid for the unit to perform
	 * @return True if successful, false if the action failed to remove or did not exist in the list
	 */
	boolean removeActions(Object actionToRemove);

	/**
	 * This method returns a value denoting the type of entity it is
	 * 0 = Unset
	 * 1 = Resource
	 * 2 = Building
	 * 3 = Unit
	 * 4 = Hero
	 * @return the entity type
	 */
	EntityType getEntityType();

	/**
	 * This sets a value denoting the type of entity it is
	 * 0 = Unset
	 * 1 = Resource
	 * 2 = Building
	 * 3 = Unit
	 * 4 = Hero
	 * @return the new entity type
	 */
	EntityType setEntityType(EntityType newType);

}
