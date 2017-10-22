package com.deco2800.marswars.hud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.hud.HUDView;

/**
 * 
 * @author Toby Guinea
 *
 * Allows for the HUDView class to check if any of the assigned hotkeys have been pressed recently.  Does this
 * by running the checkKeys() function which runs the checks against each of the hotkeys.  By calling this in the HUD's 
 * render method the method is always polling the keyboard for input
 * 
 */
public class Hotkeys {
	
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Hotkeys.class);
	
	private Stage stage;
	private Skin skin;
	private HUDView hud;
	private GameStats stats;
	private ChatBox chatBox;
	
	private boolean messageToggle = false;
	
	private Dialog pause;
	private Dialog techTree;
	private Dialog quit;
	
	private Table messageWindow;//window for the chatbox
	/**
	 * Calls a new instance of the hotkeys class
	 * 
	 * @param stage
	 * @param skin
	 * @param hud
	 * @param stats
	 * @param messageWindow
	 */
	public Hotkeys(Stage stage, Skin skin, HUDView hud, GameStats stats, Table messageWindow) {
		LOGGER.info("Instantiating the Hotkey check");
		this.stage = stage;
		this.skin = skin;
		this.hud = hud;
		this.stats = stats;
		this.messageWindow = messageWindow;
		
		this.chatBox = this.hud.getChatWindow();
	}
	
	/** 
	 * Runs all of the checks against the keys assigned to the various functions of the HUD
	 * 
	 * When called in the render method of HUDView will effectively poll the keyboard for the appropriate input
	 */
	public void checkKeys() {
		
		if (this.hud.getExitCheck() == 1) {
			if(Gdx.input.isKeyJustPressed(Input.Keys.Y) && !messageToggle) {
				LOGGER.info("Exits the game when the quit menu is active");
				System.exit(0);
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.N) && !messageToggle) {
				LOGGER.info("Resumes the game");
				this.hud.setExitCheck(0);
				this.quit.hide();
				this.timeManager.unPause();
				
			}
		}
		
		//chat listener
		if (Gdx.input.isKeyJustPressed(Input.Keys.C) && this.noActive() && !messageToggle) {
			LOGGER.info("Opening the Chat Window if no other window is active");
			this.chatBox.enableTextField();
			hud.showChatBox();
			messageToggle = true;
		} 

		if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.C)) {
			LOGGER.info("Closing the Chat Window");
			this.chatBox.disableTextField();
			hud.hideChatBox();
			messageToggle = false;
		}
		
		//pause menu listener
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !messageToggle) {
			if (this.noActive()){
				LOGGER.info("Opens the pause menu when there is no currently active menu");
				pause = new PauseMenu("Pause Menu", skin, stage, stats, hud).show(stage);
				this.hud.setPause(pause);
			} else if (this.hud.getPauseCheck() != 0){
				LOGGER.info("Closes the menu and resumes the game");
				timeManager.unPause();
				this.hud.setPauseCheck(0);
				this.hud.hidePause();
			}
		}
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.Q) && !messageToggle && this.noActive()) {
				LOGGER.info("Open the quit menu");
				this.hud.setExitCheck(1);
				this.quit = new ExitGame("Quit Game", this.skin, this.hud, true).show(this.stage);
			}

		//tech tree listener
		if(Gdx.input.isKeyJustPressed(Input.Keys.T) && !messageToggle) {
			if(this.noActive()) {
				LOGGER.info("Activate the tech tree menu");
				this.hud.setTechCheck(1);
				this.timeManager.pause();
				this.techTree = new TechTreeView("TechTree", this.skin, this.hud).show(this.stage);
				this.hud.setTechTree((TechTreeView) this.techTree);
			} else if (this.hud.getTechCheck() != 0){
				LOGGER.info("Hides the tech tree and continues the game");
				this.hud.setTechCheck(0);
				this.hud.hideTechTree();
				this.timeManager.unPause();
			}
		}
		
		//HUD toggle listener
		if(Gdx.input.isKeyJustPressed(Input.Keys.E) && noActive() && !messageToggle) {
			LOGGER.info("Toggles the HUD on and off each time the button is pressed");
			if (this.hud.isInventoryToggle()) {
				this.hud.actionsWindow.setVisible(true);
				this.hud.minimap.setVisible(true);
				this.hud.resourceTable.setVisible(true);
				//show (-) button to make resources invisible
				this.hud.setInventoryToggle(false);
			} else {
				this.hud.actionsWindow.setVisible(false);
				this.hud.minimap.setVisible(false);
				this.hud.resourceTable.setVisible(false);
				//show (+) to show resources again
				this.hud.setInventoryToggle(true);
			}
		}
		
		//help button listener
		if(Gdx.input.isKeyJustPressed(Input.Keys.H) && !messageToggle) {
			if (this.noActive()) {
				LOGGER.info("Activated the help menu");
				this.hud.setHelpCheck(1);
				this.hud.help.setVisible(true);
				this.timeManager.pause();
			} else if (this.hud.getHelpCheck() != 0) {
				LOGGER.info("Closed the help Menu");
				this.hud.setHelpCheck(0);
				this.hud.help.setVisible(false);;
				this.timeManager.unPause();
			}
		}
	}

	/**
	 * will check all of the truth values instantiated in HUDView and use them to determine
	 * whether any of the menus that the hud can create are active.  
	 * If noActive() == true: checkKeys() will allow the hotkey to perform its desired function
	 * If noActive() == false: will supress the action 
	 * 
	 * @return boolean
	 */
	private boolean noActive() {
		boolean retBool = false;
		
		if(this.hud.getHelpCheck() != 0) {
			return retBool;
		} else if (this.hud.getExitCheck() != 0) {
			return retBool;
		} else if (this.hud.getPauseCheck() != 0) {
			return retBool;
		} else if (this.hud.getTechCheck() != 0) {
			return retBool;
		}
		
		return !retBool;
	}
}