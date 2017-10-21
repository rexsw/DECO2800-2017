package com.deco2800.marswars.actions;

public abstract class AbstractPauseAction implements DecoAction {
	
	protected boolean actionPaused = false;
	
	@Override
	public int actionProgress() {
		return 0;
	}

	/**
	 * Prevents the current action from progressing.
	 */
	@Override
	public void pauseAction() {
		actionPaused = true;
	}

	/**
	 * Resumes the current action
	 */
	@Override
	public void resumeAction() {
		actionPaused = false;
	}
}
