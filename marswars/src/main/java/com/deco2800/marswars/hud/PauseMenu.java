package com.deco2800.marswars.hud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.deco2800.marswars.mainMenu.MainMenu;
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
	private static final int WIDTH = 300;
	private static final int HEIGHT = 500;
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);

	
	private static final Logger LOGGER = LoggerFactory.getLogger(Hotkeys.class);
	
	private GameStats stats;
	private HUDView hud;
	public PauseMenu(String title, Skin skin, Stage stage, GameStats stats, HUDView hud) {
		super(title, skin);
		this.stats = stats;
		this.hud = hud;
		TextButtonStyle buttonStyle = skin.get("pausemenubutton", TextButtonStyle.class);			
		LabelStyle labelStyle = skin.get("subtitle", LabelStyle.class);
		LOGGER.info("Instantiating the Pause menu");
		this.align(Align.center);
		
		{
			hud.setPauseCheck(1);
			//this.text("Game Paused", labelStyle);
			button("Resume Game", 0, buttonStyle);
			this.getButtonTable().row();
			button("Show Stats", 1, buttonStyle);
			this.getButtonTable().row();
			button("Settings", 2, buttonStyle);
			this.getButtonTable().row();
			button("Quit to Main Menu", 3, buttonStyle);
			this.getButtonTable().row();
			button("Save Game", 4, buttonStyle);
			this.getButtonTable().row();
			button("Exit Game", 5, buttonStyle);
			
			this.timeManager.pause();
			}	
	}
		/**
		 * interprets the button press chosen by the player
		 */
		protected void result(final Object object) {
			if (object == (Object) 1) {
				LOGGER.info("Opening Stats");
				this.stats.showStats();
			} else if (object == (Object) 2) {
				LOGGER.info("Opening Settings");
				this.timeManager.unPause();
				this.hud.setPauseCheck(0);
			} else if (object == (Object) 3) {
				LOGGER.info("Quitting to main menu");
				this.hud.setPauseCheck(0);
				GameManager.get().resetGame();
			} else if (object == (Object) 5) {
				LOGGER.info("Quitting the application");
				System.exit(0);
			} else {
				this.timeManager.unPause();
				this.hud.setPauseCheck(0);
			}
		}
		
		@Override
		public float getPrefWidth() {
			return WIDTH;
		}
		
		
		@Override
		public int getAlign() {
			return Align.center;
		}
}