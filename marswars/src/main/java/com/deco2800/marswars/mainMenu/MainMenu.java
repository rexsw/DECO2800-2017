package com.deco2800.marswars.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.InitiateGame.Game;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.worlds.MapSizeTypes;
import com.deco2800.marswars.worlds.map.tools.MapTypes;


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
	private static final int MENUHEIGHT = 700; 
	private static final int MENUWIDTH = 1300;
	private Skin skin;
	private Stage stage; 
	
	private Window mainmenu; 
	boolean gameStarted = false;
	private Game game;
	boolean status = true;
	
	/* Managers */
	private TextureManager textureManager; //for loading in resource images


	/**
	 * Creates the initial Main Menu instance before starting the game
	 * @param skin
	 * @param stage
	 * @param window
	 * @param marswars
	 * @param camera 
	 */
	public MainMenu(Skin skin, Stage stage) {
		this.skin = skin;
		this.stage = stage; 
		this.mainmenu = new Window("Its a start", skin); 
		this.textureManager = (TextureManager) GameManager.get().getManager(TextureManager.class);

		GameManager.get().setMainMenu(this);
		createMenu();
	}

	/**
	 * Set the main menu size and adds in the table
	 * Does all the grunt work for creating the main menu
	 */
	private void createMenu(){
		/*Creates the screens for the menu that walk the player 
		 * through setting up their customized game */
		//GameManager.get().getGui().disableHUD();
		new MenuScreen(this.skin, this.mainmenu, this.stage, this);
		this.mainmenu.setSize(MENUWIDTH, MENUHEIGHT);
		
		//add background image
	    Texture backgroundTex = textureManager.getTexture("menubackground"); //$NON-NLS-1$
	    TextureRegion backgroundRegion = new TextureRegion(backgroundTex);
	    TextureRegionDrawable backgroundRegionDraw = new TextureRegionDrawable(backgroundRegion);
	    mainmenu.setBackground(backgroundRegionDraw);
	    
		mainmenu.align(Align.left | Align.center).padLeft(50);
	    
		this.stage.addActor(mainmenu);
	}
		
	public void startGame(boolean start, MapTypes mapType, MapSizeTypes mapSize){
		gameStarted = start;
		if (gameStarted){
			game = new Game(mapType, mapSize); //Start up a new game
			game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
	}
	
	public void endGame(){
		gameStarted = false;
	}
	
	public boolean gameStarted(){
		boolean started = gameStarted;
		return started; 
	}
	
	public void resize(int width, int height) {
		this.mainmenu.setPosition(width/2-MENUWIDTH/2, height/2-MENUHEIGHT/2);
		this.mainmenu.setSize(width, height);
		if(gameStarted){
			game.resize(width, height);
		}
	}
	
	public void renderGame(SpriteBatch batch, OrthographicCamera camera){
		if(gameStarted){
			game.render(batch, camera);
		}
	}
}