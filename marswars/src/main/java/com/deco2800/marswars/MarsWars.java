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
import com.deco2800.marswars.entities.buildings.Base;
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
		stage = new Stage(new ScreenViewport());
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		/*All managers */
		reg = (TextureManager)(GameManager.get().getManager(TextureManager.class));

		// zero game length clock (i.e. Tell TimeManager new game has been launched)
		timeManager.setGameStartTime();
		
		//not sure why i have to create a window here and pass it into the menu
		//but creating a window in menu crashes the game
		menu = new MainMenu(skin, stage, new Window("its a start", skin), this);
		stage.addActor(menu.buildMenu());
		camera = new OrthographicCamera(1920, 1080);
		inputP = new InputProcessor(camera, stage, skin);

		GameManager.get().setCamera(camera);

		playGame();
	}
	
	/**
	 * Constructs the rest of the game. 
	 * Note: the following methods will be removed from marswars soon to be abstracted 
	 * into their relevant classes
	 */
	public void playGame(){
		createMiniMap();
		//inputP.setInputProcessor();
		createMap();
		inputP.setInputProcessor();

		fogOfWar();
		addAIEntities();
		setThread();
		setGUI();
	}
	
	/**
	 * Creates the game minimap 
	 */
	public void createMiniMap() {
		MiniMap m = new MiniMap("minimap", 220, 220);
		m.render();
		//initialise the minimap and set the image
		GameManager.get().setMiniMap(m);
		GameManager.get().getMiniMap().updateMap(reg);
	}
	
	/**
	 * Creates game map
	 */
	private void createMap() {
		MapContainer map = new MapContainer();
		CustomizedWorld world = new CustomizedWorld(map);
		world.loadMapContainer(map);
		GameManager.get().setWorld(world);
		
		/* Move camera to the center of the world */
		camera.translate(GameManager.get().getWorld().getWidth()*32, 0);
		GameManager.get().setCamera(camera);
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
		setAI(length, width, 1);
		setPlayer(length/2, width/2, Colours.PURPLE, -1);
		GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		black.set();
	}
	
	private void setThread() {
		// do something important here, asynchronously to the rendering thread

		new Thread(new Runnable() {
			@Override
			public void run() {
				// do something important here, asynchronously to the rendering thread
				while(true) {
					if (!timeManager.isPaused()) {
						/*
						 * threshold here need to be tweaked to make things move better for different CPUs 
						 */
						if(TimeUtils.nanoTime() - lastGameTick > 10000000) {
							for (Renderable e : GameManager.get().getWorld().getEntities()) {
								if (e instanceof Tickable) {
									((Tickable) e).onTick(0);

								}
							}
							GameManager.get().onTick(0);
							lastGameTick = TimeUtils.nanoTime();
						}
					}
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
		view = new com.deco2800.marswars.hud.HUDView(stage, skin, GameManager.get(), reg);
		view.disableHUD();
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
		inputP.handleInput(pauseTime);
        /*
         * Update the camera
         */
		camera.update();
		batch.setProjectionMatrix(camera.combined);

        /*
         * Clear the entire display as we are using lazy rendering
         */
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render background first
		String backgroundString = bgManager.getBackground();
		TextureManager textureManager = (TextureManager) GameManager.get().getManager(TextureManager.class);
		Texture background = textureManager.getTexture(backgroundString);
		batch.begin();
		batch.draw(background, camera.position.x - camera.viewportWidth*camera.zoom/2 , camera.position.y -
				camera.viewportHeight*camera.zoom/2, camera.viewportWidth*camera.zoom,
				camera.viewportHeight*camera.zoom);
		batch.end();
		
        /* Render the tiles second */
		BatchTiledMapRenderer tileRenderer = renderer.getTileRenderer(batch);
		tileRenderer.setView(camera);
		tileRenderer.render();

		/*
         * Use the selected renderer to render objects onto the map
         */
		renderer.render(batch, camera);		

		view.render(lastMenuTick);

		/* Dispose of the spritebatch to not have memory leaks */
		Gdx.graphics.setTitle("DECO2800 " + this.getClass().getCanonicalName() +  " - FPS: "+ Gdx.graphics.getFramesPerSecond());
		stage.act();
		stage.draw();
		GameManager.get().setCamera(camera);
		batch.dispose();
		if(!gameStarted) {
			GameManager.get().getMiniMap().render();
			GameManager.get().getMiniMap().updateMap((TextureManager)(GameManager.get().getManager(TextureManager.class)));
			view.updateMiniMapMenu();
			view.enableHUD();
			GameManager.get().toggleActiveView();
			gameStarted = true;
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
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
		GameManager.get().setCamera(camera);
		stage.getViewport().update(width, height, true);
		view.resize(width, height);
		menu.resize(width, height);
	}

	/**
	 * Disposes of assets etc when the rendering system is stopped.
	 */
	@Override
	public void dispose () {
		// Don't need this at the moment
	}
	
	/**
	 * generates a new AI team with basic unit at a give x-y co-ord
	 * @ensure the x,y pair are within the game map
	 */
	public void setAI(int length, int width, int teams) {
		ArrayList<Colours> colour = new ArrayList<Colours>();
		int x,y;
		colour.add(Colours.BLUE);
		colour.add(Colours.YELLOW);
		colour.add(Colours.PINK);
		colour.add(Colours.GREEN);
		ColourManager cm = (ColourManager) GameManager.get().getManager(ColourManager.class);
		ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		for(int teamid = 1; teamid < teams+1;teamid++) {
			x = ThreadLocalRandom.current().nextInt(1, length);
			y = ThreadLocalRandom.current().nextInt(1, width);
			rm.setBiomass(0, teamid);
			rm.setRocks(0, teamid);
			rm.setCrystal(0, teamid);
			rm.setWater(0, teamid);
			cm.setColour(teamid, colour.get(teamid-1));
			AiManager aim = (AiManager) GameManager.get().getManager(AiManager.class);
			aim.addTeam(teamid);
			Astronaut ai = new Astronaut(x, y, 0, teamid);
			Astronaut ai1 = new Astronaut(x, y, 0, teamid);
			Base aibase = new Base(GameManager.get().getWorld(), x, y, 0);
			Soldier soldier = new Soldier(x, y,0,teamid);
			GameManager.get().getWorld().addEntity(soldier);
			Tank tank = new Tank(x,y,0,teamid);
			GameManager.get().getWorld().addEntity(tank);
			GameManager.get().getWorld().addEntity(ai);
			GameManager.get().getWorld().addEntity(ai1);
			aibase.setOwner(teamid);
			GameManager.get().getWorld().addEntity(aibase);
		}
	}

	public void setPlayer(int x, int y, Colours colour, int teamid) {
		ColourManager cm = (ColourManager) GameManager.get().getManager(ColourManager.class);
		cm.setColour(teamid, colour);
		ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		rm.setBiomass(0, teamid);
		rm.setRocks(0, teamid);
		rm.setCrystal(0, teamid);
		rm.setWater(0, teamid);
		Spacman p = new Spacman(x, y, 0);
		Astronaut p1 = new Astronaut(x, y, 0, teamid);
		Base p2 = new Base(GameManager.get().getWorld(), x, y, 0);
		Soldier soldier = new Soldier(x, y,0,teamid);
		GameManager.get().getWorld().addEntity(soldier);
		Tank tank = new Tank(x,y,0,teamid);
		GameManager.get().getWorld().addEntity(tank);
		Carrier carrier = new Carrier(x, y, 0, teamid);
		GameManager.get().getWorld().addEntity(carrier);
		p.setOwner(teamid);
		GameManager.get().getWorld().addEntity(p);
		p1.setOwner(teamid);
		GameManager.get().getWorld().addEntity(p1);
		p2.setOwner(teamid);
		GameManager.get().getWorld().addEntity(p2);
	}
}
