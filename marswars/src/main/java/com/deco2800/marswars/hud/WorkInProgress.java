package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Dialog box class notifying players that the selected feature/component
 * is currently in development 
 * @author naziah
 *
 */
public class WorkInProgress extends Dialog{
	
	/**
	 * Dialog notifying player that the feature selected 
	 * is still in progress
	 * @param title of the dialog box
	 * @param skin 
	 */
	public WorkInProgress(String title, Skin skin) {
		super(title, skin);		
		{
			text("Sorry, this feature is currently a work in progress, we promise it'll be finished soon!"); //$NON-NLS-1$
			button("OK"); //$NON-NLS-1$
		}
	}
}
