package com.deco2800.marswars;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.entities.units.Carrier;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.entities.units.Tank;
import com.deco2800.marswars.managers.*;
import com.deco2800.marswars.renderers.Render3D;
import com.deco2800.marswars.renderers.Renderable;
import com.deco2800.marswars.renderers.Renderer;
import com.deco2800.marswars.hud.*;
import com.deco2800.marswars.mainMenu.MainMenu;
import com.deco2800.marswars.worlds.CustomizedWorld;
import com.deco2800.marswars.worlds.FogWorld;
import com.deco2800.marswars.worlds.map.tools.MapContainer;
import com.deco2800.marswars.InitiateGame.InputProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Moos
 * The starting class for the UQ DECO2800 Game Engine.
 * This class handles the application rendering including selection of tileRenderer and creation of the world.
 * @Author Tim Hadwen
 */
public class MarsWars extends ApplicationAdapter implements ApplicationListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(MarsWars.class);

	/**
	 * Set the renderer.
	 * 3D is for Isometric worlds
	 * 2D is for Side Scrolling worlds
	 * Check the documentation for each renderer to see how it handles AbstractEntity coordinates
	 */
	Renderer renderer = new Render3D();

	/**
	 * Create a camera for panning and zooming.
	 * Camera must be updated every render cycle.
	 */
	OrthographicCamera camera;

	Stage stage;
	Window window;

	TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);
	BackgroundManager bgManager = (BackgroundManager) GameManager.get().getManager(BackgroundManager.class);

	long lastGameTick = 0;
	long lastMenuTick = 0;
	long pauseTime = 0;

	
	private boolean gameStarted = false;
	MainMenu menu;

	Skin skin;
	
	HUDView view;

	Set<Integer> downKeys = new HashSet<>();
	TextureManager reg;
	
	private InputProcessor inputP;
	
	/**
	 * Creates the required objects for the game to start.
	 * Called when the game first starts
	 */
	@Override
	public void create () {
		this.stage = new Stage(new ScreenViewport());
		this.skin = new Skin(Gdx.files.internal("uiskin.json")); //$NON-NLS-1$
		GameManager.get().setSkin(this.skin);
		GameManager.get().setStage(this.stage);
		/*All managers */
		this.reg = (TextureManager)(GameManager.get().getManager(TextureManager.class));
								
		// zero game length clock (i.e. Tell TimeManager new game has been launched)
		this.timeManager.setGameStartTime();

		this.camera = new OrthographicCamera(1920, 1080);
		this.inputP = new InputProcessor(this.camera, this.stage, this.skin);

		GameManager.get().setCamera(this.camera);
		playGame();
	}
	
	/**
	 * Constructs the rest of the game. 
	 * Note: the following methods will be removed from marswars soon to be abstracted 
	 * into their relevant classes
	 */
	public void playGame(){
		createMiniMap();
		createMap();
		this.inputP.setInputProcessor();

		fogOfWar();
		addAIEntities();
		setThread();
		setGUI();
		this.menu = new MainMenu(this.skin, this.stage, this, camera); //$NON-NLS-1$
		view.disableHUD();

		timeManager.pause();
	}
	
	/**
	 * Creates the game minimap 
	 */
	public void createMiniMap() {
		MiniMap m = new MiniMap("minimap", 220, 220);
		//initialise the minimap and set the image
		GameManager.get().setMiniMap(m);
		GameManager.get().getMiniMap().updateMap(this.reg);
	}
	
	/**
	 * Creates game map
	 */
	private void createMap() {
		MapContainer map = new MapContainer();
		CustomizedWorld world = new CustomizedWorld(map);
		GameManager.get().setWorld(world);
		world.loadMapContainer(map);
		
		/* Move camera to the center of the world */
		this.camera.translate(GameManager.get().getWorld().getWidth()*32, 0);
		GameManager.get().setCamera(this.camera);
	}
	
	/*
	 * Initializes fog of war
	 */
	private void fogOfWar() {
		FogManager fogOfWar = (FogManager)(GameManager.get().getManager(FogManager.class));
		fogOfWar.initialFog(GameManager.get().getWorld().getWidth(), GameManager.get().getWorld().getLength());
		FogWorld.initializeFogWorld(GameManager.get().getWorld().getWidth(),GameManager.get().getWorld().getLength());
	}
	
	/*
	 * adds entities for the ai and set then to be ai owned
	 */
	private void addAIEntities() {
		int length = GameManager.get().getWorld().getLength();
		int width = GameManager.get().getWorld().getWidth();
		setPlayer(length, width, 1, 1);
		GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		black.set();
		GameManager.get().getManager(WinManager.class);
	}
	
	private void setThread() {
		// do something important here, asynchronously to the rendering thread

		new Thread(new Runnable() {
			@Override
			public void run() {
				// do something important here, asynchronously to the rendering thread
				while(true) {
					if (!MarsWars.this.timeManager.isPaused()) {
						/*
						 * threshold here need to be tweaked to make things move better for different CPUs 
						 */
						if(TimeUtils.nanoTime() - MarsWars.this.lastGameTick > 100000) {
							for (Renderable e : GameManager.get().getWorld().getEntities()) {
								if (e instanceof Tickable) {
									((Tickable) e).onTick(0);

								}
							}
							GameManager.get().onTick(0);
							lastGameTick = TimeUtils.nanoTime();
						}
					}
						//MarsWars.this.lastGameTick = TimeUtils.nanoTime();
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						LOGGER.error(e.toString());
					}
				}
			}
		}).start();
	}
	
	/*
	 * Setup GUI > Refer to com.deco2800.marwars.hud for this now 
	 */
	private void setGUI() {
		/* Add another button to the menu */
		this.view = new com.deco2800.marswars.hud.HUDView(this.stage, this.skin, GameManager.get(), this.reg);
		this.view.disableHUD();
	}

	/**
	 * Renderer thread
	 * Must update all displayed elements using a Renderer
	 */
	@Override
	public void render () {
        /*
         * Create a new render batch.
         * At this stage we only want one but perhaps we need more for HUDs etc
         */
		SpriteBatch batch = new SpriteBatch();

		/*
         * Update the input managers
         */
		this.inputP.handleInput(this.pauseTime);
        /*
         * Update the camera
         */
		this.camera.update();
		batch.setProjectionMatrix(this.camera.combined);

        /*
         * Clear the entire display as we are using lazy rendering
         */
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render background first
		String backgroundString = this.bgManager.getBackground();
		TextureManager textureManager = (TextureManager) GameManager.get().getManager(TextureManager.class);
		Texture background = textureManager.getTexture(backgroundString);
		batch.begin();
		batch.draw(background, this.camera.position.x - this.camera.viewportWidth*this.camera.zoom/2 , this.camera.position.y -
				this.camera.viewportHeight*this.camera.zoom/2, this.camera.viewportWidth*this.camera.zoom,
				this.camera.viewportHeight*this.camera.zoom);
		batch.end();
		
        /* Render the tiles second */
		BatchTiledMapRenderer tileRenderer = this.renderer.getTileRenderer(batch);
		tileRenderer.setView(this.camera);
		tileRenderer.render();

		/*
         * Use the selected renderer to render objects onto the map
         */
		this.renderer.render(batch, this.camera);		

		this.view.render(this.lastMenuTick);

		/* Dispose of the spritebatch to not have memory leaks */
		Gdx.graphics.setTitle("DECO2800 " + this.getClass().getCanonicalName() +  " - FPS: "+ Gdx.graphics.getFramesPerSecond()); //$NON-NLS-1$ //$NON-NLS-2$
		this.stage.act();
		this.stage.draw();
		GameManager.get().setCamera(this.camera);
		batch.dispose();
		if(!this.gameStarted) {
			GameManager.get().getMiniMap().updateMap((TextureManager)(GameManager.get().getManager(TextureManager.class)));
			this.view.updateMiniMapMenu();
			GameManager.get().toggleActiveView();
			this.gameStarted = true;
		}
	}
	
	/**
	 * Resizes the viewport.
	 *
	 * @param width the new width of the viewport.
	 * @param height the new height of the viewport.
	 */
	@Override
	public void resize(int width, int height) {
		this.camera.viewportWidth = width;
		this.camera.viewportHeight = height;
		this.camera.update();
		GameManager.get().setCamera(this.camera);
		this.stage.getViewport().update(width, height, true);
		this.view.resize(width, height);
		this.menu.resize(width, height);
	}

	/**
	 * Disposes of assets etc when the rendering system is stopped.
	 */
	@Override
	public void dispose () {
		// Don't need this at the moment
	}
	
	/**
	 * generates a number of player and ai teams with basic unit at a give x-y
	 * co-ord
	 * 
	 * @ensure the x,y pair are within the game map & playerteams+aiteams < 6
	 * @param lenght
	 *            int length of the map
	 * @param width
	 *            int width of the map
	 * @param aiteams
	 *            int number of ai teams
	 * @param playerteams
	 *            int number of playerteams
	 */
	private void setPlayer(int length, int width, int aiteams,
			int playerteams) {
		int x, y, playerid;
		ColourManager cm = (ColourManager) GameManager.get()
				.getManager(ColourManager.class);
		ResourceManager rm = (ResourceManager) GameManager.get()
				.getManager(ResourceManager.class);
		for (int teamid = 1; teamid < aiteams + 1; teamid++) {
			x = ThreadLocalRandom.current().nextInt(1, length - 1);
			y = ThreadLocalRandom.current().nextInt(1, width - 1);
			cm.setColour(teamid);
			Setunit(teamid, x, y, rm);
			AiManager aim = (AiManager) GameManager.get()
					.getManager(AiManager.class);
			aim.addTeam(teamid);
		}
		for (int teamid = 1; teamid < playerteams + 1; teamid++) {
			playerid = teamid * (-1);
			x = ThreadLocalRandom.current().nextInt(1, length - 1);
			y = ThreadLocalRandom.current().nextInt(1, width - 1);
			cm.setColour(playerid);
			Setunit(playerid, x, y, rm);
		}
	}

	/**
	 * adds a teams units to the game and sets the resource manager
	 * 
	 * @param teamid
	 *            int the team's id
	 * @param x
	 *            int the x location of the team's spawn
	 * @param y
	 *            int the y location of the team's spawn
	 * @param rm
	 *            ResourceManager the ResourceManager of the game to set
	 */
	private void Setunit(int teamid, int x, int y, ResourceManager rm) {
		rm.setBiomass(0, teamid);
		rm.setRocks(0, teamid);
		rm.setCrystal(0, teamid);
		rm.setWater(0, teamid);
		Astronaut ai = new Astronaut(x, y, 0, teamid);
		Astronaut ai1 = new Astronaut(x, y, 0, teamid);
		Base aibase = new Base(GameManager.get().getWorld(), x, y, 0, teamid);
		Soldier soldier = new Soldier(x, y, 0, teamid);
		GameManager.get().getWorld().addEntity(soldier);
		Tank tank = new Tank(x, y, 0, teamid);
		Carrier carrier = new Carrier(x, y, 0, teamid);
		GameManager.get().getWorld().addEntity(carrier);
		GameManager.get().getWorld().addEntity(tank);
		GameManager.get().getWorld().addEntity(ai);
		GameManager.get().getWorld().addEntity(ai1);
		GameManager.get().getWorld().addEntity(aibase);
	}

	/**
	 * @return the Graphical User Interface
	 */
	public HUDView getGUI(){
		return this.view;
	}
}
