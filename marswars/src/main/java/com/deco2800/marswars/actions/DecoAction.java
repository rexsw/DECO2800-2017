package com.deco2800.marswars.actions;

/**
 * An action that a unit can complete
 * Created by timhadwen on 23/7/17.
 */
public interface DecoAction {
	void doAction();
	boolean completed();
	int actionProgress();
}
