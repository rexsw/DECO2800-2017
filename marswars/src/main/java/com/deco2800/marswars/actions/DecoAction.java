package com.deco2800.marswars.actions;

/**
 * An action that a unit can complete
 * Created by timhadwen on 23/7/17.
 */
public interface DecoAction {
	/**
	 * Does the action
	 */
	void doAction();

	/**
	 * Returns true if the action is completed
	 * @return
	 */
	boolean completed();

	/**
	 * Returns the action progress if available
	 * @return
	 */
	int actionProgress();
}
