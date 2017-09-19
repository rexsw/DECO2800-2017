package com.deco2800.marswars.InitiateGame;

import java.util.concurrent.ThreadLocalRandom;

import com.deco2800.marswars.managers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.TimeUtils;
import com.deco2800.marswars.MarsWars;
import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.entities.Tickable;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.entities.units.Carrier;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.entities.units.Medic;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.entities.units.Tank;
import com.deco2800.marswars.hud.HUDView;
import com.deco2800.marswars.hud.MiniMap;
import com.deco2800.marswars.renderers.Render3D;
import com.deco2800.marswars.renderers.Renderable;
import com.deco2800.marswars.renderers.Renderer;
import com.deco2800.marswars.worlds.FogWorld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Manages the features for the game 
 * @author cherr
 *
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

	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	private BackgroundManager bgManager = (BackgroundManager)
			GameManager.get().getManager(BackgroundManager.class);
	private WeatherManager weatherManager = (WeatherManager)
			GameManager.get().getManager(WeatherManager.class);

	private static final Logger LOGGER = LoggerFactory.getLogger(MarsWars.class);

	public Game(){
		startGame();
	}
	
	private void startGame(){
		this.timeManager.setGameStartTime();
		this.timeManager.unPause();
		this.addAIEntities();
		this.setThread();
		this.fogOfWar();
		//this.weatherManager.setWeatherEvent();
	}
		
	/*
	 * Initializes fog of war
	 */
	private void fogOfWar() {
		FogManager fogOfWar = (FogManager)(GameManager.get().getManager(FogManager.class));
		fogOfWar.initialFog(GameManager.get().getWorld().getWidth(), GameManager.get().getWorld().getLength());
		FogWorld.initializeFogWorld(GameManager.get().getWorld().getWidth(),GameManager.get().getWorld().getLength());
	}	


	/**
	 * Can assume that since this class has been instantiated
	 * that the game is in full play
	 * @param batch 
	 */
	public void render(OrthographicCamera camera, SpriteBatch batch){
		this.renderer.render(batch, camera);			
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
	
	/**
	 * generates a number of player and ai teams with basic unit at a give x-y
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
		Commander commander = new Commander(x,y,0,teamid);
		Medic medic = new Medic(x, y, 0, teamid);
		GameManager.get().getWorld().addEntity(medic);
		GameManager.get().getWorld().addEntity(commander);
		
		GameManager.get().getWorld().addEntity(carrier);
		GameManager.get().getWorld().addEntity(tank);
		GameManager.get().getWorld().addEntity(ai);
		GameManager.get().getWorld().addEntity(ai1);
		GameManager.get().getWorld().addEntity(aibase);
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

}