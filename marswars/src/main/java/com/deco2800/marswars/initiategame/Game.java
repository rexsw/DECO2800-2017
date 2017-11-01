package com.deco2800.marswars.initiategame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.deco2800.marswars.MarsWars;
import com.deco2800.marswars.buildings.*;
import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.Tickable;
import com.deco2800.marswars.entities.terrainelements.Obstacle;
import com.deco2800.marswars.entities.units.*;
import com.deco2800.marswars.hud.HUDView;
import com.deco2800.marswars.managers.*;
import com.deco2800.marswars.managers.AiManager.Difficulty;
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

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Initialises the features for the game. An abstraction of the original marswars.java. 
 * @author Naziah Siddique
 */
public class Game{	
	long lastGameTick = 0;
	long lastMenuTick = 0;
	long pauseTime = 0;

	public static float ticktime = 20;

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

	private static final Logger LOGGER = LoggerFactory.getLogger(MarsWars.class);
	
	private HUDView view; 
	private static GameSave savedGame;
	private Difficulty aiDifficulty;

	/**
	 * start a loaded game
	 * @param playerTeams
	 * @param aITeams
	 */
	public Game(int aITeams, int playerTeams, Difficulty aiDifficulty) throws java.io.FileNotFoundException{
		this.aiDifficulty = aiDifficulty;
		savedGame = new GameSave(aITeams,playerTeams,aiDifficulty,true);
		loadGame(savedGame);
	}
	
	/**
	 * Creates a Game instance and starts off the game
	 * @param playerTeams 
	 * @param aITeams 
	 */
	public Game(MapTypes mapType, MapSizeTypes mapSize, int aITeams, int playerTeams, Difficulty aiDifficulty) {
		this.aiDifficulty = aiDifficulty;
		GameManager.get().setMapType(mapType);
		ColourManager colourManager = (ColourManager)GameManager.get()
				.getManager(ColourManager.class);
		int currentColorIndex = colourManager.getIndex();
		startGame(mapType, mapSize, aITeams, playerTeams);
		savedGame = new GameSave(aITeams,playerTeams,aiDifficulty,false);
		savedGame.data.setIndex(currentColorIndex);
	}


	/**
	 * this function load the game
	 * @throws java.io.FileNotFoundException
	 */
	private void loadGame(GameSave savedGame) throws java.io.FileNotFoundException {
		GameSave loadedGame = new GameSave();
		loadedGame.readGame();

		createMapForLoading(loadedGame);
		this.view = new HUDView(GameManager.get().getStage(),
				GameManager.get().getSkin(), GameManager.get());
		this.camera = GameManager.get().getCamera();
		this.timeManager.setGameStartTime();
		this.timeManager.unPause();

		//set win condition
		WinManager win = (WinManager) GameManager.get().getManager(WinManager.class);
		win.setwinconditions(loadedGame.data.getWinCondition());

		//set game time
		this.timeManager.setGameTime((int)loadedGame.data.getHour(),(int)loadedGame.data.getMin(),0);

		//set colour index
		ColourManager colourManager = (ColourManager)GameManager.get()
				.getManager(ColourManager.class);
		colourManager.setIndex(loadedGame.data.getIndex());

		savedGame.data.setIndex(loadedGame.data.getIndex());

		//different
		this.addEntitiesFromLoadGame(loadedGame.data.getaITeams(),loadedGame.data.getPlayerTeams(),loadedGame);



		setCameraInitialPosition();
		//same
		this.setThread();
		this.fogOfWar();
		FogManager.setFog(loadedGame.data.getFogOfWar());
		FogManager.setBlackFog(loadedGame.data.getBlackFogOfWar());

	}

	/**
	 * this function sets up the players of the loaded game
	 * @param aiteams
	 * @param playerteams
	 * @param loadedGame
	 */
	private void addEntitiesFromLoadGame(int aiteams, int playerteams,GameSave loadedGame){
		LOGGER.info("Start loading game");

		int playerid;
		ColourManager cm = (ColourManager) GameManager.get().getManager(ColourManager.class);
		ResourceManager rm = (ResourceManager) GameManager.get()
				.getManager(ResourceManager.class);

		aiDifficulty = loadedGame.data.getAiDifficulty();

		for (int teamid = 1; teamid < aiteams + 1; teamid++) {
			cm.setColour(teamid);
			AiManager aim = (AiManager) GameManager.get()
					.getManager(AiManager.class);
			aim.setDifficulty(aiDifficulty);
			aim.addTeam(teamid);

			ArrayList<Integer> aIStats = loadedGame.data.getaIStats().get(teamid-1);

			rm.setBiomass(aIStats.get(0), teamid);
			rm.setRocks(aIStats.get(1), teamid);
			rm.setCrystal(aIStats.get(2), teamid);
			rm.setMaxPopulation(10, teamid);
			rm.setPopulation(aIStats.get(3), teamid);
		}
		for (int teamid = 1; teamid < playerteams + 1; teamid++) {
			playerid = teamid * (-1);
			cm.setColour(playerid);

			ArrayList<Integer> playerStats = loadedGame.data.getPlayerStats().get(teamid-1);

			rm.setBiomass(playerStats.get(0), playerid);
			rm.setRocks(playerStats.get(1), playerid);
			rm.setCrystal(playerStats.get(2), playerid);
			rm.setMaxPopulation(10, playerid);
			rm.setPopulation(playerStats.get(3), playerid);
		}

		//load obatacles
		for(Obstacle each : loadedGame.data.getObstacles()){
			GameManager.get().getWorld().addEntity(each);
		}

		//add all entities
		loadEntities(loadedGame);
		loadBuildings(loadedGame);
		loadAnimals(loadedGame);

		GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		black.set();
		GameManager.get().getManager(WinManager.class);
	}

	/**
	 * this function loads all the animals
	 */
	private void loadAnimals(GameSave loadedGame){
		for(SavedAnimal e : loadedGame.data.getAnimals()){
			if("Snail".equals(e.getName())){
				AmbientAnimal animal = new Snail(e.getX(), e.getY(), 0, 0);
				animal.setHealth(e.getHealth());
				GameManager.get().getWorld().addEntity(animal);
			}
			else if("Corn".equals(e.getName())){
				AmbientAnimal animal = new Corn(e.getX(), e.getY(), 0, 0);
				animal.setHealth(e.getHealth());
				GameManager.get().getWorld().addEntity(animal);
			}
			else if("Dino".equals(e.getName())){
				AmbientAnimal animal = new Dino(e.getX(), e.getY(), 0, 0);
				animal.setHealth(e.getHealth());
				GameManager.get().getWorld().addEntity(animal);
			}
		}
	}

	/**
	 * this functions loads all the saved buildings
	 * @param loadedGame
	 */
	private void loadBuildings(GameSave loadedGame){
		for(SavedBuilding e : loadedGame.data.getBuilding()){
			switch(e.getBuildingType()){
				case WALL:
					WallHorizontal wall = new WallHorizontal(GameManager.get().getWorld(), e.getX(), e.getY(), 0, e.getTeamId());
					wall.setHealth(e.getHealth());
					GameManager.get().getWorld().addEntity(wall);
					break;
				case GATE:
					GateHorizontal gate = new GateHorizontal(GameManager.get().getWorld(), e.getX(), e.getY(), 0, e.getTeamId());
					gate.setHealth(e.getHealth());
					GameManager.get().getWorld().addEntity(gate);
					break;
				case TURRET:
					Turret turret = new Turret(GameManager.get().getWorld(), e.getX(), e.getY(), 0, e.getTeamId());
					turret.setHealth(e.getHealth());
					GameManager.get().getWorld().addEntity(turret);
					break;
				case BASE:
					Base base = new Base(GameManager.get().getWorld(), e.getX(), e.getY(), 0, e.getTeamId());
					base.setHealth(e.getHealth());
					GameManager.get().getWorld().addEntity(base);
					break;
				case BARRACKS:
					Barracks barracks = new Barracks(GameManager.get().getWorld(), e.getX(), e.getY(), 0, e.getTeamId());
					barracks.setHealth(e.getHealth());
					GameManager.get().getWorld().addEntity(barracks);
					break;
				case BUNKER:
					Bunker bunker = new Bunker(GameManager.get().getWorld(), e.getX(), e.getY(), 0, e.getTeamId());
					bunker.setHealth(e.getHealth());
					GameManager.get().getWorld().addEntity(bunker);
					break;
				case HEROFACTORY:
					HeroFactory heroFactory = new HeroFactory(GameManager.get().getWorld(), e.getX(), e.getY(), 0, e.getTeamId());
					heroFactory.setHealth(e.getHealth());
					GameManager.get().getWorld().addEntity(heroFactory);
					break;
				case SPACEX:
					TechBuilding techBuilding = new TechBuilding(GameManager.get().getWorld(), e.getX(), e.getY(), 0, e.getTeamId());
					techBuilding.setHealth(e.getHealth());
					GameManager.get().getWorld().addEntity(techBuilding);
					break;
				default:
					break;

			}
		}
	}

	/**
	 * this function loads all the saved entities
	 * @param loadedGame
	 */
	private void loadEntities(GameSave loadedGame){
		for(SavedEntity each : loadedGame.data.getEntities())
			if("Astronaut".equals(each.getName())){
				Astronaut astronaut = new Astronaut(each.getX(), each.getY(), 0, each.getTeamId());
				astronaut.setHealth(each.getHealth());
				GameManager.get().getWorld().addEntity(astronaut);
			}
			else if("Tank".equals(each.getName())){
				Tank tank = new Tank(each.getX(), each.getY(), 0, each.getTeamId());
				tank.setHealth(each.getHealth());
				GameManager.get().getWorld().addEntity(tank);
			}
			else if("Carrier".equals(each.getName())){
				Carrier carrier = new Carrier(each.getX(), each.getY(), 0, each.getTeamId());
				carrier.setHealth(each.getHealth());
				GameManager.get().getWorld().addEntity(carrier);
			}
			else if("Spacman".equals(each.getName())){
				Spacman spacman = new Spacman(each.getX(), each.getY(), 0, each.getTeamId());
				spacman.setHealth(each.getHealth());
				GameManager.get().getWorld().addEntity(spacman);
			}
			else if("Sniper".equals(each.getName())){
				Sniper sniper = new Sniper(each.getX(), each.getY(), 0, each.getTeamId());
				sniper.setHealth(each.getHealth());
				GameManager.get().getWorld().addEntity(sniper);
			}
			else if("TankDestroyer".equals(each.getName())){
				TankDestroyer tankDestroyer = new TankDestroyer(each.getX(), each.getY(), 0, each.getTeamId());
				tankDestroyer.setHealth(each.getHealth());
				GameManager.get().getWorld().addEntity(tankDestroyer);
			}
			else if("Spatman".equals(each.getName())){
				Spatman spatman = new Spatman(each.getX(), each.getY(), 0, each.getTeamId());
				spatman.setHealth(each.getHealth());
				GameManager.get().getWorld().addEntity(spatman);
			}
			else if("Commander".equals(each.getName())){
				Commander commander = new Commander(each.getX(), each.getY(), 0, each.getTeamId());
				commander.setHealth(each.getHealth());
				GameManager.get().getWorld().addEntity(commander);
			}
			else if("Medic".equals(each.getName())){
				Medic medic = new Medic(each.getX(), each.getY(), 0, each.getTeamId());
				medic.setHealth(each.getHealth());
				GameManager.get().getWorld().addEntity(medic);
			}
			else if("Hacker".equals(each.getName())){
				Hacker hacker = new Hacker(each.getX(), each.getY(), 0, each.getTeamId());
				hacker.setHealth(each.getHealth());
				GameManager.get().getWorld().addEntity(hacker);
			}else if("Soldier".equals(each.getName())) {
				Soldier soldier = new Soldier(each.getX(), each.getY(), 0, each.getTeamId());
				soldier.setHealth(each.getHealth());
				GameManager.get().getWorld().addEntity(soldier);
			}

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
		setCameraInitialPosition();
		this.setThread();
		this.fogOfWar();
		// Please don't delete
		//this.weatherManager.setWeatherEvent();
		GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		black.set();
		GameManager.get().getManager(WinManager.class);
	}

	/**
	 * Moves the camera to the player's base.
	 */
	private void setCameraInitialPosition() {
		for (int i = 0; i < GameManager.get().getWorld().getEntities().size(); i++) {
			AbstractEntity e = GameManager.get().getWorld().getEntities().get(i);
			if (e instanceof Base && ((Base) e).getOwner() < 0) {
				float x = e.getPosX();
				float y = e.getPosY();
				Vector2 basePosition = new Vector2();
				// 55 and 32 come from the width and height of the tiles, 0.5 is sin(30)
				basePosition.x = (float) (1 * (y * 55 * .5 + x * 55 * .5));
				basePosition.y = (float) (1 * (x * 32 * .5 - y * 32 * .5));
				this.camera.position.set(basePosition.x, basePosition.y, 0);
				break;
			}
		}
	}

	private void createMapForLoading(GameSave loadedGame){
		MapContainer map = new MapContainer("./resources/mapAssets/loadmap.tmx");
		CustomizedWorld world = new CustomizedWorld(map);
		GameManager.get().setWorld(world);
		world.loadAlreadyMapContainer(map,loadedGame);
		GameManager.get().getCamera().translate(GameManager.get().getWorld().getWidth()/2, 0);
		GameManager.get().setCamera(GameManager.get().getCamera());
		LOGGER.debug("Game just started, map is now loaded, bring up active view");
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
		FogWorld.clearSelectedTiles();


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
		for (int teamid = 1; teamid < aiteams+1; teamid++) {
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
			aim.setDifficulty(aiDifficulty);
			aim.addTeam(teamid);
		}
		LOGGER.info("Entities for the AI successfully added");
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
		int initResource = 0;
		if(teamid<10){
			initResource = 100;
		}

		rm.setBiomass(initResource, teamid);
		rm.setRocks(initResource, teamid);
		rm.setCrystal(initResource, teamid);
		rm.setMaxPopulation(10, teamid);
		Astronaut ai = new Astronaut(x, y, 0, teamid);
		Astronaut ai1 = new Astronaut(x, y, 0, teamid);
		Soldier ai2 = new Soldier(x, y, 0, teamid);
		Base base = new Base(GameManager.get().getWorld(), x, y, 0, teamid);
		base.setFix(true);
		base.setBuilt(true);
		GameManager.get().getWorld().addEntity(ai);
		GameManager.get().getWorld().addEntity(ai1);
		GameManager.get().getWorld().addEntity(ai2);
		GameManager.get().getWorld().addEntity(base);
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
						ticktime = (int)(((TimeUtils.nanoTime() - lastGameTick) / 1000000.0f));
						lastGameTick = TimeUtils.nanoTime();

						/*
						 * threshold here need to be tweaked to make things move better for different CPUs 
						 */
						for (Renderable e : GameManager.get().getWorld().getEntities()) {
							if (e instanceof Tickable) {
									((Tickable) e).onTick(0);
							}
						}
						GameManager.get().onTick(0);
					}
				}
			}
		}).start();
	}

	/**
	 * Returns the game GameSave object.
	 *
	 * @return the game GameSave object.
	 */
	public static GameSave getSavedGame() {
		return savedGame;
	}
}