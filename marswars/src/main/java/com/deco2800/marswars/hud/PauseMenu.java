package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;

/**
 * Created by Toby Guinea on 16/09
 * 
 * The Pause menu that will be run by the hud every time that the game is paused.
 * 
 * Implements buttons for the following functionalities
 * 
 * resuming the game
 * Checking your statistics for the current game
 * Changing game settings (once the feature is implemented
 * Quitting to the main menu once the menu is fully implemented
 * Exiting from the program
 * 
 */
public class PauseMenu extends Dialog{
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	private Stage stage;
	private Skin skin;
	private GameStats stats;
	private HUDView hud;
	
	public PauseMenu(String title, Skin skin, GameStats stats, HUDView hud) {
		super(title, skin);
		this.skin = skin;
		this.stats = stats;
		this.hud = hud;
		
		{
			hud.setPauseCheck(1);
			text("Game Paused");  //$NON-NLS-1$
			
			button("Resume", 0); //$NON-NLS-1$
			button("Statistics", 1); //$NON-NLS-1$
			button("Settings", 2); //$NON-NLS-1$
			button("Quit to Main Menu", 3); //$NON-NLS-1$
			button("Exit Game", 4); //$NON-NLS-1$
			this.timeManager.pause();
		}	
	}
	
		protected void result(final Object object) {
			if (object == (Object) 1) {
				this.stats.showStats();
			} else if (object == (Object) 2) {
				this.timeManager.unPause();
				this.hud.setPauseCheck(0);
			} else if (object == (Object) 3) {
				this.timeManager.unPause();
				this.hud.setPauseCheck(0);
			} else if (object == (Object) 4) {
				System.exit(0);
			} else {
				this.timeManager.unPause();
				this.hud.setPauseCheck(0);
			}
		}
}