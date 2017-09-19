package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;

/**
 * Dialog box class notifying players that the selected feature/component
 * is currently in development 
 * @author naziah
 *
 */
public class WorkInProgress extends Dialog{
	HUDView hud;
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	/**
	 * Dialog notifying player that the feature selected 
	 * is still in progress
	 * @param title of the dialog box
	 * @param skin 
	 */
	public WorkInProgress(String title, Skin skin, HUDView hud) {
		super(title, skin);		
		this.hud = hud;
		{
			text("Sorry, this feature is currently a work in progress, we promise it'll be finished soon!"); //$NON-NLS-1$
			button("OK", 0); //$NON-NLS-1$
			timeManager.pause();
		}
	}
	
	protected void result(final Object object) {
		this.hud.setHelpCheck(0);
		timeManager.unPause();
	}
}
