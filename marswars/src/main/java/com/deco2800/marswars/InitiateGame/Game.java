package com.deco2800.marswars.InitiateGame;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.entities.units.Carrier;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.entities.units.Tank;
import com.deco2800.marswars.hud.HUDView;
import com.deco2800.marswars.hud.MiniMap;
import com.deco2800.marswars.managers.AiManager;
import com.deco2800.marswars.managers.ColourManager;
import com.deco2800.marswars.managers.GameBlackBoard;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.managers.WinManager;

/**
 * Manages the features for the game 
 * @author cherr
 *
 */
public class Game{
	private Stage stage; 
	private HUDView view; 
	private Skin skin; 
	private TextureManager reg; 
	private InputProcessor inputP;
	private OrthographicCamera camera; 
	
	long lastGameTick = 0;
	long lastMenuTick = 0;
	long pauseTime = 0;

	private TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);	

	
	public Game(Skin skin, Stage stage, OrthographicCamera camera){
		this.stage = stage; 
		this.skin = skin; 
		this.camera = camera;
		this.reg = (TextureManager)(GameManager.get().getManager(TextureManager.class));
		startGame();

	}
	
	private void startGame(){
		timeManager.setGameStartTime();
		timeManager.unPause();
		this.addAIEntities();
		//inputP = new InputProcessor(this.camera, this.stage, this.skin);
		//inputP.setInputProcessor();
		//createMiniMap();
		//setGUI();	
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
	 * Creates the game minimap 
	 */
	private void createMiniMap() {
		MiniMap m = new MiniMap("minimap", 220, 220); //$NON-NLS-1$
		//m.render();
		//initialise the minimap and set the image
		GameManager.get().setMiniMap(m);
		GameManager.get().getMiniMap().updateMap(this.reg);
	}

	/**
	 * Can assume that since this class has been instantiated
	 * that the game is in full play
	 */
	public void render(){
		//GameManager.get().getMiniMap().render();
		//GameManager.get().getMiniMap().updateMap((TextureManager)(GameManager.get().getManager(TextureManager.class)));
		//this.view.updateMiniMapMenu();
		//this.view.enableHUD();
		//GameManager.get().toggleActiveView();
	}
	
	public void resize(int width, int height){
		//this.view.resize(width, height);
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
}