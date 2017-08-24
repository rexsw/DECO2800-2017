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

	public WorkInProgress(String title, Skin skin) {
		super(title, skin);		
		{
			text("Sorry, this feature is currently a work in progress, we promise it'll be finished soon!");
			button("OK");
		}
	}
	
	@Override 
	protected void result(final Object object){
		
	}	
}
