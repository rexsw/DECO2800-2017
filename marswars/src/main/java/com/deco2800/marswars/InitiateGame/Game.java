package com.deco2800.marswars.InitiateGame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.deco2800.marswars.MarsWars;
import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Tickable;
import com.deco2800.marswars.entities.units.*;
import com.deco2800.marswars.hud.HUDView;
import com.deco2800.marswars.managers.*;
import com.deco2800.marswars.renderers.Render3D;
import com.deco2800.marswars.renderers.Renderable;
import com.deco2800.marswars.renderers.Renderer;
import com.deco2800.marswars.worlds.CustomizedWorld;
import com.deco2800.marswars.worlds.FogWorld;
import com.deco2800.marswars.worlds.MapSizeTypes;
import com.deco2800.marswars.worlds.map.tools.MapContainer;
import com.deco2800.marswars.worlds.map.tools.MapTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Initialises the features for the game. An abstraction of the original marswars.java. 
 * @author Naziah Siddique
 */
public class Game{	
	long lastGameTick = 0;
	long lastMenuTick = 0;
	long pauseTime = 0;

	/**
	 * Set the renderer.
	 * 3D is for Isometric worlds
	 * 2D is for Side Scrolling worlds
	 * Check the documentation for each renderer to see how it handles AbstractEntity coordinates
	 */
	Renderer renderer = new Render3D();

	private OrthographicCamera camera;
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	// Please don't delete
	private WeatherManager weatherManager = (WeatherManager)
			GameManager.get().getManager(WeatherManager.class);

	private static final Logger LOGGER = LoggerFactory.getLogger(MarsWars.class);
	
	private HUDView view; 
	public static GameSave savedGame;
	
	/**
	 * Creates a Game instance and starts off the game
	 * @param playerTeams 
	 * @param aITeams 
	 */
	public Game(MapTypes mapType, MapSizeTypes mapSize, int aITeams, int playerTeams, boolean newGame) throws java.io.FileNotFoundException{
	    savedGame = new GameSave(mapType,mapSize,aITeams,playerTeams);
	    if(!newGame){
		loadGame();
	    } else {
	    	startGame(mapType, mapSize, aITeams, playerTeams);
	    }
	}

	
	/*Loads saved game*/
	private void loadGame() throws java.io.FileNotFoundException {
		GameSave loadedGame = new GameSave();
		loadedGame.readGame();

		this.createMap(loadedGame.data.mapType, loadedGame.data.mapSize);
		this.view = new HUDView(GameManager.get().getStage(),
				GameManager.get().getSkin(), GameManager.get());
		this.camera = GameManager.get().getCamera();
		this.timeManager.setGameStartTime();
		this.timeManager.unPause();

		//different
		this.addEntitiesFromLoadGame(loadedGame.data.aITeams,loadedGame.data.playerTeams,loadedGame);

		//same
		this.setThread();
		this.fogOfWar();
		FogManager.setFog(loadedGame.data.fogOfWar);
		FogManager.setBlackFog(loadedGame.data.blackFogOfWar);
	   //ADD UNITS & WALKABLES
	}

	private void addEntitiesFromLoadGame(int aiteams, int playerteams,GameSave loadedGame){
		LOGGER.info("Start loading game");

		int playerid;
		ColourManager cm = (ColourManager) GameManager.get().getManager(ColourManager.class);
		ResourceManager rm = (ResourceManager) GameManager.get()
				.getManager(ResourceManager.class);

		//add all entities
		for(AbstractEntity each : loadedGame.data.walkables){
			GameManager.get().getWorld().addEntity(((BaseEntity)each));
		}

		for (int teamid = 1; teamid < aiteams + 1; teamid++) {
			cm.setColour(teamid);
			AiManager aim = (AiManager) GameManager.get()
					.getManager(AiManager.class);
			aim.addTeam(teamid);
			rm.setBiomass(0, teamid);
			rm.setRocks(0, teamid);
			rm.setCrystal(0, teamid);
			rm.setWater(0, teamid);
			rm.setMaxPopulation(10, teamid);
		}
		for (int teamid = 1; teamid < playerteams + 1; teamid++) {
			playerid = teamid * (-1);
			cm.setColour(playerid);
			rm.setBiomass(0, teamid);
			rm.setRocks(0, teamid);
			rm.setCrystal(0, teamid);
			rm.setWater(0, teamid);
			rm.setMaxPopulation(10, teamid);
		}


		GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		black.set();
		GameManager.get().getManager(WinManager.class);
	}

	
	/* The method that really starts off the game after Game instantiation. 
	 * Loads in other game components and initialises Game private variables.
	 */
	private void startGame(MapTypes mapType, MapSizeTypes mapSize, int aITeams, int playerTeams){
		this.createMap(mapType, mapSize);
		this.view = new HUDView(GameManager.get().getStage(), 
				GameManager.get().getSkin(), GameManager.get());
		this.camera = GameManager.get().getCamera();
		this.timeManager.setGameStartTime();
		this.timeManager.unPause();
		this.addAIEntities(aITeams, playerTeams);
		this.setThread();
		this.fogOfWar();
		// Please don't delete
		//this.weatherManager.setWeatherEvent();
	}
	
	/**
	 * Creates game map based off user input in the main menu. 
	 * A map type may be selected, or a map may be randomised 
	 * after clicking 'Customise'.
	 * @param mapSize 
	 * @param mapType 
	 */
	private void createMap(MapTypes mapType, MapSizeTypes mapSize) {
		/* this is only for the 'Customise' option in the main menu*/
		if (mapType == null || mapSize == null){
			LOGGER.debug("Customisation feature selected, loading random map");
			MapContainer map = new MapContainer();
			CustomizedWorld world = new CustomizedWorld(map);
			GameManager.get().setWorld(world);
			world.loadMapContainer(map);
		} else {
			/*for all other game cases*/ 
			LOGGER.debug(String.format("Picked a %s type of %s size map,loading map", mapType.toString(), mapSize.toString()));
			MapContainer map = new MapContainer(mapType, mapSize);
			CustomizedWorld world = new CustomizedWorld(map);
			GameManager.get().setWorld(world);
			world.loadMapContainer(map);
		}
		
		/* Move camera to the center of the world */
		GameManager.get().getCamera().translate(GameManager.get().getWorld().getWidth()/2, 0);
		GameManager.get().setCamera(GameManager.get().getCamera());
		LOGGER.debug("Game just started, map is now loaded, bring up active view");
	}

	/*
	 * Initializes fog of war
	 * Multiseleciton tiles are also initialized here
	 */
	private void fogOfWar() {
		LOGGER.debug("Creating fog of war");

		FogManager fogOfWar = (FogManager)(GameManager.get().getManager(FogManager.class));
		fogOfWar.initialFog(GameManager.get().getWorld().getWidth(), GameManager.get().getWorld().getLength());
		FogWorld.initializeFogWorld(GameManager.get().getWorld().getWidth(),GameManager.get().getWorld().getLength());

		//these are initialization for multiselection tiles
		MultiSelection multiSelection = (MultiSelection) (GameManager.get().getManager(MultiSelection.class));
		multiSelection.resetSelectedTiles();
		FogWorld.initializeSelectedTiles(GameManager.get().getWorld().getWidth(),GameManager.get().getWorld().getLength());

		LOGGER.debug("Creation of fog of war complete");

	}

	/**
	 * Can assume that since this class has been instantiated
	 * that the game is in full play
	 * @param batch 
	 * @param renderCamera 
	 */
	public void render(SpriteBatch batch, OrthographicCamera renderCamera){
		/* Render the tiles second */
		BatchTiledMapRenderer tileRenderer = this.renderer.getTileRenderer(batch);
		tileRenderer.setView(renderCamera);
		tileRenderer.render();
		this.renderer.render(batch, renderCamera);
		GameManager.get().getGui().render(this.lastMenuTick);
	}
	
	public void resize(int width, int height){
		view.resize(width, height);
		LOGGER.info("Resized the HUD contents");
	}
	
	/*
	 * adds entities for the ai and set then to be ai owned
	 */
	private void addAIEntities(int aITeams, int playerTeams) {
		LOGGER.info("Adding entitires for the AI");
		
		int length = GameManager.get().getWorld().getLength();
		int width = GameManager.get().getWorld().getWidth();
		setPlayer(length, width, aITeams, playerTeams);
		GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		black.set();
		GameManager.get().getManager(WinManager.class);
		
		LOGGER.info("Entities for the AI successfully added");
	}
	
	/**
	 * Generates a number of player and ai teams with basic unit at a give x-y
	 * co-ord
	 * 
	 * @ensure the x,y pair are within the game map & playerteams+aiteams < 6
	 * @param length
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
		int x;
		int y;
		int playerid;
		ColourManager cm = (ColourManager) GameManager.get()
				.getManager(ColourManager.class);
		ResourceManager rm = (ResourceManager) GameManager.get()
				.getManager(ResourceManager.class);
		for (int teamid = 1; teamid < aiteams + 1; teamid++) {
			int avoidInfinite = 0;
			do {
				x = ThreadLocalRandom.current().nextInt(1, length - 1);
				y = ThreadLocalRandom.current().nextInt(1, width - 1);
				avoidInfinite ++;
			}  while(!GameManager.get().getWorld().checkValidPlace(null, x, y, 4, 0) && avoidInfinite < 20);
			cm.setColour(teamid);
			setUnit(teamid, x, y, rm);
			AiManager aim = (AiManager) GameManager.get()
					.getManager(AiManager.class);
			aim.addTeam(teamid);
		}
		for (int teamid = 1; teamid < playerteams + 1; teamid++) {
			playerid = teamid * (-1);
			int avoidInfinite = 0;
			do {
				x = ThreadLocalRandom.current().nextInt(1, length - 1);
				y = ThreadLocalRandom.current().nextInt(1, width - 1);
				avoidInfinite ++;
			}  while(!GameManager.get().getWorld().checkValidPlace(null, x, y, 4, 0) && avoidInfinite < 20);
			cm.setColour(playerid);
			setUnit(playerid, x, y, rm);
		}
		
		LOGGER.debug("Players successfully set to game map");
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
	private void setUnit(int teamid, int x, int y, ResourceManager rm) {
		rm.setBiomass(0, teamid);
		rm.setRocks(0, teamid);
		rm.setCrystal(0, teamid);
		rm.setWater(0, teamid);
		rm.setMaxPopulation(10, teamid);

		Astronaut ai = new Astronaut(x, y, 0, teamid);
		Astronaut ai1 = new Astronaut(x, y, 0, teamid);
		Base aibase = new Base(GameManager.get().getWorld(), x, y, 0, teamid);
		Soldier soldier = new Soldier(x, y, 0, teamid);
		GameManager.get().getWorld().addEntity(soldier);
		Tank tank = new Tank(x, y, 0, teamid);
		Carrier carrier = new Carrier(x, y, 0, teamid);
		Commander commander = new Commander(x,y,0,teamid);
		Medic medic = new Medic(x, y, 0, teamid);
		Hacker hacker = new Hacker(x, y, 0, teamid);
		GameManager.get().getWorld().addEntity(medic);
		GameManager.get().getWorld().addEntity(commander);
		GameManager.get().getWorld().addEntity(hacker);
		GameManager.get().getWorld().addEntity(carrier);
		GameManager.get().getWorld().addEntity(tank);
		GameManager.get().getWorld().addEntity(ai);
		GameManager.get().getWorld().addEntity(ai1);
		GameManager.get().getWorld().addEntity(aibase);
		
		LOGGER.info("Team units successfully set");
	}
	
	private void setThread() {
		// do something important here, asynchronously to the rendering thread

		new Thread(new Runnable() {
			@Override
			public void run() {
				// do something important here, asynchronously to the rendering thread
				while(true) {
					if (!timeManager.isPaused() && TimeUtils.nanoTime() - lastGameTick > 10000000) {
						if (TimeUtils.nanoTime() - lastGameTick > 10500000) {
							LOGGER.error("Tick was too slow: " + ((TimeUtils.nanoTime() - lastGameTick)/1000000.0) + "ms");
						}

						/*
						 * threshold here need to be tweaked to make things move better for different CPUs 
						 */
						//initial value 100000
						for (Renderable e : GameManager.get().getWorld().getEntities()) {
							if (e instanceof Tickable) {
								long startTime = TimeUtils.nanoTime();
								((Tickable) e).onTick(0);
								long endTime = TimeUtils.nanoTime();

								if (endTime - startTime > 100000) { //0.01ms
									LOGGER.error("Entity " + e + " took " + ((endTime - startTime)/1000000.0) + "ms");
								}
							}
						}
						GameManager.get().onTick(0);
						lastGameTick = TimeUtils.nanoTime();

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

}