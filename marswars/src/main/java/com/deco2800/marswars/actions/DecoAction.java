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
	 * @return Returns true if the action is completed
	 */
	boolean completed();

	/**
	 * @return Returns the action progress if available
	 */
	int actionProgress();

	/**
	 * Prevents the current action from progressing.
	 */
	void pauseAction();

	/**
	 * Resumes the current action
	 */
	void resumeAction();

}
