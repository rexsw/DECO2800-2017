package com.deco2800.marswars.mainMenu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.deco2800.marswars.MarsWars;
import com.deco2800.marswars.InitiateGame.Game;

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
 * @author Toby Guinea
 *
 */
public class MainMenu {
	private static final int MENUHEIGHT = 300; 
	private static final int MENUWIDTH = 400;
	private Skin skin;
	private Stage stage; 
	
	private Window mainmenu; 
	private Label title;
	boolean gameStarted = false;
	private OrthographicCamera camera; 
	private Game game; 
	boolean status = true;
	private MarsWars marsWars;

	/**
	 * Creates the initial Main Menu instance before starting the game
	 * @param skin
	 * @param stage
	 * @param window
	 * @param marswars
	 * @param camera 
	 */
	public MainMenu(Skin skin, Stage stage, MarsWars marswars, OrthographicCamera camera) {
		this.skin = skin;
		this.stage = stage; 
		this.mainmenu = new Window("Its a start", skin); 
		this.marsWars = marswars;
		createMenu();
	}

	/**
	 * Set the main menu size and adds in the table
	 * Does all the grunt work for creating the main menu
	 */
	private void createMenu(){
		/*Creates the screens for the menu that walk the player 
		 * through setting up their customized game */
		marsWars.getGUI().disableHUD();
		new MenuScreen(this.skin, this.mainmenu, this.stage, this, this.marsWars.getGUI());
		this.mainmenu.setSize(MENUWIDTH, MENUHEIGHT);
		this.stage.addActor(mainmenu);
	}
		
	public void startGame(boolean start){
		gameStarted = start;
		marsWars.getGUI().enableHUD();
		game = new Game(skin, stage, camera);

	}
	
	public boolean gameStarted(){
		boolean started = gameStarted;
		return started; 
	}
	
	public void resize(int width, int height) {
		this.mainmenu.setPosition(width/2-MENUWIDTH/2, height/2-MENUHEIGHT/2);
		if(gameStarted){
			game.resize(width, height);
		}
	}
	
	public void renderGame(){
		if(gameStarted){
			game.render();
		}
	}
}