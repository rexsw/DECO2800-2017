package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;

/*The Pause menu that will be run by the hud every time that the game is paused.
 * Includes buttons for resuming the game, for viewing the game statistics, for changing certain game settings
 * quitting to the main menu and exiting the game
 */
public class PauseMenu extends Dialog{
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	
	public PauseMenu(String title, Skin skin) {
		super(title, skin);
		
		{
			text("Game Paused");  //$NON-NLS-1$
			
			button("Resume", 0); //$NON-NLS-1$
			button("Game Statistics", 1); //$NON-NLS-1$
			button("Settings", 2); //$NON-NLS-1$
			button("Quit to Main Menu", 3); //$NON-NLS-1$
			button("Exit Game", 4); //$NON-NLS-1$
			timeManager.pause();
		}	
	}
	
		protected void result(final Object object) {
			if (object == (Object) 1) {
				
			} else if (object == (Object) 2) {
				
			} else if (object == (Object) 3) {
				
			} else if (object == (Object) 4) {
				System.exit(0);
			} else {
				timeManager.unPause();
			}
		}
}