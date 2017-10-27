package com.deco2800.marswars.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.initiategame.Game;
import com.deco2800.marswars.initiategame.GameSave;
import com.deco2800.marswars.initiategame.SoundTrackPlayer;
import com.deco2800.marswars.managers.AiManager.Difficulty;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.worlds.MapSizeTypes;
import com.deco2800.marswars.worlds.map.tools.MapTypes;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;


/**
 * Creates a main menu window, which adds in a table depending on the 
 * state of the main menu progression.
 * 
 * E.g choosing between player and multiplayer will load up a table, 
 * then progressing to multiplayer and picking between starting a 
 * server and joining a server is another new table.
 * 
 * See MenuScreen.java for breakdowm of the main menu stages 
 *  
 * @author Toby Guinea
 *
 */
public class MainMenu {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MainMenu.class);
	private static final int MENUHEIGHT = 700; 
	private static final int MENUWIDTH = 1300;
	private Skin skin;
	private Stage stage;
	
	private Window mainmenu; 
	private static boolean gameStarted = false;
	private Game game;
	boolean status = true;
	boolean enabled = false; 
	private Difficulty aiDifficulty;
	
	/* Managers */
	private TextureManager textureManager; //for loading in resource images

	public Sound openMusic = null;


	public static final SoundTrackPlayer player = new SoundTrackPlayer();

	/**
	 * Creates the initial Main Menu instance before starting the game
	 * @param skin
	 * @param stage

	 */
	public MainMenu(Skin skin, Stage stage) {
		this.skin = skin;
		this.stage = stage; 
		this.mainmenu = new Window("", skin); 
		this.textureManager = (TextureManager)(GameManager.get().getManager(TextureManager.class));

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
		MenuScreen menu = new MenuScreen(this.skin, this.mainmenu, this.stage, this);
		menu.align(Align.left);
		this.mainmenu.setSize(MENUWIDTH, MENUHEIGHT);
		this.mainmenu.setDebug(enabled);
		
		//add background image
	    Texture backgroundTex = textureManager.getTexture("menubackground"); //$NON-NLS-1$
	    TextureRegion backgroundRegion = new TextureRegion(backgroundTex);
	    TextureRegionDrawable backgroundRegionDraw = new TextureRegionDrawable(backgroundRegion);
	    mainmenu.setBackground(backgroundRegionDraw);
	    
		mainmenu.align(Align.left | Align.top).pad(80);
		this.stage.addActor(mainmenu);

		openMusic = Gdx.audio.newSound(Gdx.files.internal("OriginalSoundTracks/OpeningTheme.mp3"));
		openMusic.loop();

	}
	
	/**
	 * Method called by MenuScreen to start off the game with the given parameters 
	 * @param start
	 * @param mapType
	 * @param mapSize
	 * @param aITeams
	 * @param playerTeams
	 */
	public void startGame(boolean start, MapTypes mapType, MapSizeTypes mapSize, int aITeams,
			int playerTeams, Difficulty aiDifficulty) {
		setGameStarted(start);
		if (isGameStarted()) {
			openMusic.stop();
			openMusic.dispose();
			game = new Game(mapType, mapSize, aITeams, playerTeams, aiDifficulty); //Start up a new game
			game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
	}
	
	
	/**
	 * Method called by MenuScreen to start off the game with saved game 
	 * @param start
	 */
	public void loadGame(boolean start) {
		GameSave loadedGame = new GameSave();
		try {
			loadedGame.readGame();
		} catch (FileNotFoundException e) {
			LOGGER.info("Error reading saved file");
			return;
		}
			setGameStarted(start);
			if (isGameStarted()) {
				openMusic.stop();
				openMusic.dispose();
				try {
					game = new Game(loadedGame.data.getaITeams(), loadedGame.data.getPlayerTeams(), loadedGame.data.getAiDifficulty()); //Start up a new game
				} catch (FileNotFoundException e2) {}//do nothing
					game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				}
	}


	
	/**
	 * Flags the game as ended
	 */
	public static void endGame(){
		gameStarted = false;
	}

	/**
	 * Return whether the game has started
	 *
	 * @return whether the game has started
	 */
	public static boolean isGameStarted() {
		return gameStarted;
	}

	/**
	 * set the game as started
	 *
	 * @param gameStarted yes or no
	 */
	public static void setGameStarted(boolean gameStarted) {
		MainMenu.gameStarted = gameStarted;
	}

	/**
	 * Flags the game as started
	 * @return
	 */
	public boolean gameStarted(){
		return gameStarted; 
	}
	
	/**
	 * Resizes the main menu according to the stage dimensions
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height) {
		this.mainmenu.setPosition(width/2-MENUWIDTH/2, height/2-MENUHEIGHT/2);
		this.mainmenu.setSize(width, height);
		
		if(gameStarted){
			game.resize(width, height);
		}
	}
	
	/**
	 * Renders the main menu. 
	 * If the game has started, it ensures the game is rendered too
	 * @param batch
	 * @param camera
	 */
	public void renderGame(SpriteBatch batch, OrthographicCamera camera){
		if(gameStarted){
			game.render(batch, camera);
		}
	}
}