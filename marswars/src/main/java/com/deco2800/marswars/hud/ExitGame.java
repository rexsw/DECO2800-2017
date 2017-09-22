package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;

public class ExitGame extends Dialog{

	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	HUDView hud;
	boolean started;

	public ExitGame(String title, Skin skin, HUDView hud, boolean started) {
		super(title, skin);
		this.hud = hud;
		this.started = started;
		
			{
				text("Are you sure you want to quit? ");
				button("Yes", 1);
				button("No, keep playing", 2);
				if(started) {
					timeManager.pause();
				}
			}
	}
			@Override
			protected void result(final Object object){
				if(object == (Object) 1){
					System.exit(0);
				} else {
					this.hud.setExitCheck(0);
					if(started) {
						timeManager.unPause();
					}
				}
			}	
}