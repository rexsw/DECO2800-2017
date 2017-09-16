package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;

public class ExitGame extends Dialog{

	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);

	public ExitGame(String title, Skin skin) {
		super(title, skin);
		
			{
				text("Are you sure you want to quit? ");
				button("Yes", 1);
				button("No, keep playing", 2);
				timeManager.pause();
			}
	}
			@Override
			protected void result(final Object object){
				if(object == (Object) 1){
					System.exit(0);
				} else {
					timeManager.unPause();
				}
			}	
}