package com.deco2800.marswars.mainMenu;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.deco2800.marswars.MarsWars;

/**
 * Creates a main menu window, which adds in a table depending on the 
 * state of the main menu progression.
 * 
 * E.g choosing between player and multiplayer will load up a table, 
 * then progressing to multiplayer and picking between starting a 
 * server and joining a server is another new table.
 * 
 * FLOW DIAGRAM: 
 * TODO add in flow diagram of the main menu
 *  
 * @author Toby 
 *
 */
public class MainMenu {
	private static final int MENUHEIGHT = 300; 
	private static final int MENUWIDTH = 400;
	private Skin skin;
	private Stage stage; 
	
	Window mainmenu; 
	Label title;
	boolean status = true;

	/**
	 * Creates the initial Main Menu instance before starting the game
	 * @param skin
	 * @param stage
	 * @param window
	 * @param marswars
	 */
	public MainMenu(Skin skin, Stage stage, Window window, MarsWars marswars) {
		this.skin = skin;
		this.stage = stage; 
		this.mainmenu = window; 
		this.mainmenu.setDebug(true);
		createMenu();
    }

	/**
	 * Set the main menu size and adds in the table
	 * Does all the grunt work for creating the main menu
	 */
	private void createMenu(){
		new MenuScreen(this.skin, this.mainmenu, this.stage);
		this.mainmenu.setSize(MENUWIDTH, MENUHEIGHT);
	}
	
	public Window buildMenu(){
		return this.mainmenu;
	}
	
	public void resize(int width, int height) {
		this.mainmenu.setPosition(width/2-MENUWIDTH/2, height/2-MENUHEIGHT/2);
	}
}