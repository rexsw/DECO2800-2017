package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;

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
	
	private Stage stage;
	private Skin skin;
	private HUDView hud;
	private GameStats stats;
	
	private boolean messageToggle = false;
	
	private Dialog pause;
	private Dialog help;
	private Dialog techTree;
	private Dialog quit;
	
	private Window messageWindow;//window for the chatbox
	/**
	 * Calls a new instance of the hotkeys class
	 * 
	 * @param stage
	 * @param skin
	 * @param hud
	 * @param stats
	 * @param messageWindow
	 */
	public Hotkeys(Stage stage, Skin skin, HUDView hud, GameStats stats, Window messageWindow) {
		this.stage = stage;
		this.skin = skin;
		this.hud = hud;
		this.stats = stats;
		this.messageWindow = messageWindow;
	}
	
	/** 
	 * Runs all of the checks against the keys assigned to the various functions of the HUD
	 * 
	 * When called in the render method of HUDView will effectively poll the keyboard for the appropriate input
	 */
	public void checkKeys() {
		
		if (hud.getExitCheck() == 1) {
			if(Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
				System.exit(0);
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
				hud.setExitCheck(0);
				quit.hide();
				timeManager.unPause();
				
			}
		}
		
		//chat listener
		if (Gdx.input.isKeyJustPressed(Input.Keys.C) && this.noActive()) {
			this.messageWindow.setVisible(true);
			this.messageToggle = true;
			this.hud.setChatActiveCheck(1);
		} else if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.C)) {
			this.messageWindow.setVisible(false);
			this.messageToggle = false; 
			this.hud.setChatActiveCheck(0);
		}
		
		//pause menu listener
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			if (this.noActive()){
				pause = new PauseMenu("Pause Menu", skin, stage, stats, hud).show(stage);
			} else if (hud.getPauseCheck() != 0){
				timeManager.unPause();
				hud.setPauseCheck(0);
				pause.hide();
			}
		}
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			if (this.noActive()) {
				hud.setExitCheck(1);
				quit = new ExitGame("Quit Game", skin, hud, true).show(stage); //$NON-NLS-1$
			}
		}

		//tech tree listener
		if(Gdx.input.isKeyJustPressed(Input.Keys.T)) {
			if(this.noActive()) {
				this.hud.setTechCheck(1);
				this.techTree = new TechTreeView("TechTree", this.skin, this.hud).show(this.stage); //$NON-NLS-1$
			} else if (hud.getTechCheck() != 0){
				this.hud.setTechCheck(0);
				this.techTree.hide();
				this.timeManager.unPause();
			}
		}
		
		//HUD toggle listener
		if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {

			if (hud.isInventoryToggle()) {
				hud.actionsWindow.setVisible(true);
				hud.minimap.setVisible(true);
				hud.resourceTable.setVisible(true);
				//show (-) button to make resources invisible
				hud.dispActions.remove();
				hud.HUDManip.add(hud.removeActions);
				hud.setInventoryToggle(false);
			} else {
				hud.actionsWindow.setVisible(false);
				hud.minimap.setVisible(false);
				hud.resourceTable.setVisible(false);
				//show (+) to show resources again
				hud.removeActions.remove();
				hud.HUDManip.add(hud.dispActions);
				hud.setInventoryToggle(true);
			}
		}
		
		//help button listener
		if(Gdx.input.isKeyJustPressed(Input.Keys.H)) {
			if (this.noActive()) {
				hud.setHelpCheck(1);
				help = new WorkInProgress("Help  Menu", skin, hud).show(stage); //$NON-NLS-1$
			} else if (hud.getHelpCheck() != 0) {
				hud.setHelpCheck(0);
				help.hide();
				timeManager.unPause();
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
		
		if(hud.getHelpCheck() != 0) {
			return retBool;
		} else if (hud.getExitCheck() != 0) {
			return retBool;
		} else if (hud.getPauseCheck() != 0) {
			return retBool;
		} else if (hud.getChatActiveCheck() != 0) {
			return retBool;
		} else if (hud.getTechCheck() != 0) {
			return retBool;
		}
		
		return !retBool;
	}
}