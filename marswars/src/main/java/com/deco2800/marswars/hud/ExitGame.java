package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;

/**
 * Creates a dialog menu which is used to check whether the player wishes to exit the game
 */
public class ExitGame extends Dialog{

	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	HUDView hud;
	boolean started;

	/**
	 * Creates an 'Exit Game' dialog
	 * @param title
	 * @param skin
	 * @param hud
	 * @param started
	 */
	public ExitGame(String title, Skin skin, HUDView hud, boolean started) {
		super(title, skin);
		this.hud = hud;
		this.started = started;
		
			{
				text("Are you sure you want to quit? ");
				button("Yes", 1);
				button("No, keep playing", 2);
				if(started) {
					this.timeManager.pause();
				}
			}
	}		
			/**
			 * interprets the button press chosen by the player
			 */
			@Override
			protected void result(final Object object){
				if(object == (Object) 1){
					System.exit(0);
				} else {
					if(this.hud != null) {
						this.hud.setExitCheck(0);
					}
					if(this.started) {
						this.timeManager.unPause();
					}
				}
			}	
}