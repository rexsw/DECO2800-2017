package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;

public class Hotkeys {
	
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	
	private Stage stage;
	private Skin skin;
	private HUDView hud;
	private GameStats stats;
	
	private int pauseCheck = 0;
	private int helpCheck = 0;
	private int techCheck = 0;
	private int chatActiveCheck = 0;
	private int exitCheck = 0;
	
	private boolean messageToggle = false;
	
	private Dialog pause;
	private Dialog help;
	private Dialog techTree;
	private Dialog quit;
	
	private Window messageWindow;//window for the chatbox
	
	public Hotkeys(Stage stage, Skin skin, HUDView hud, GameStats stats, Window messageWindow) {
		this.stage = stage;
		this.skin = skin;
		this.hud = hud;
		this.stats = stats;
		this.messageWindow = messageWindow;
	}
	
	public void checkKeys() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			if (hud.getExitCheck() == 0) {
				hud.setExitCheck(1);
				quit = new ExitGame("Quit Game", skin, hud).show(stage); //$NON-NLS-1$
			}
		}
		
		if (hud.getExitCheck() == 1) {
			if(Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
				System.exit(0);
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
				hud.setExitCheck(0);
				quit.hide();
				timeManager.unPause();
				
			}
		}

		//tech tree listener
		if(Gdx.input.isKeyJustPressed(Input.Keys.T)) {
			if(this.hud.getTechCheck() == 0) {
				this.hud.setTechCheck(1);
				this.techTree = new TechTreeView("TechTree", this.skin, this.hud).show(this.stage); //$NON-NLS-1$
			} else {
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
			if (hud.getHelpCheck() == 0) {
				hud.setHelpCheck(1);
				help = new WorkInProgress("Help  Menu", skin, hud).show(stage); //$NON-NLS-1$
			} else {
				hud.setHelpCheck(0);
				help.hide();
				timeManager.unPause();
			}
		}
	}
}