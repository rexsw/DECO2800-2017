package com.deco2800.marswars.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.hud.HUDView;
import com.deco2800.marswars.hud.MiniMap;
import com.deco2800.marswars.mainmenu.MainMenu;
import com.deco2800.marswars.worlds.BaseWorld;
import com.deco2800.marswars.worlds.map.tools.MapTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Game manager manages all the components of the game.
 * Throughout we call GameManager GM
 * Created by timhadwen on 30/7/17.
 */
public class GameManager implements TickableManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(GameManager.class);

	private static GameManager instance = null;

	private List<Manager> managers = new ArrayList<>();

	private BaseWorld gameWorld;

	private Skin gameskin;
	
	private Skin altskin; 
	
	private Stage gamestage;

	private BaseWorld mapWorld;
	
	private OrthographicCamera camera;
	
	private Vector3 cameraPos;

	private int activeView = 0; // 0 is gameWorld, 1 is mapWorld

	private MiniMap miniMap;

	private HUDView gui;
	
	private MainMenu menu;
	
	private TextureManager gameTexture;
	
	private boolean gameStarted = false;
	
	private boolean costsFree = false;
	
	private MapTypes map;

	/**
	 * Returns an instance of the GM
	 * @return GameManager
	 */
	public static GameManager get() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;
	}

	/**
	 * Private constructor to inforce use of get()
	 */
	private GameManager() {
		
	}

	/**
	 * Adds a manager component to the GM
	 * @param manager the manager to be added.
	 */
	public void addManager(Manager manager) {
		managers.add(manager);
	}

	/**
	 * Retrives a manager from the list.
	 * If the manager does not exist one will be created, added to the list and returned
	 * @param type The class type (ie SoundManager.class)
	 * @return A Manager component of the requested type
	 */
	public synchronized Manager getManager(Class<?> type) {
		/* Check if the manager exists */
		for (Manager m : managers) {
			if (m.getClass() == type) {
				return m;
			}
		}

		/* Otherwise create one */
		try {
			Constructor<?> ctor = type.getConstructor();
			this.addManager((Manager) ctor.newInstance());
		} catch (Exception e) {
			// Gotta catch 'em all
			LOGGER.error(e.toString());
		}

		/* And then return it */
		for (Manager m : managers) {
			if (m.getClass() == type) {
				return m;
			}
		}
		LOGGER.warn("GameManager.get returned null! It shouldn't have!");
		return null;
	}
	
	public List<Manager> getManagerList(){
		return managers;
	}

	/**
	 * Sets the current game world
	 * @param world the world to be set
	 */
	public void setWorld(BaseWorld world) {
		this.gameWorld = world;
	}

	/**
	 * Sets the current map world.
	 * @param world
	 */
	public void setMapWorld(BaseWorld world) {
		this.mapWorld = world;
	}
	
	/**
	 * Sets the TextureManager for world
	 * @param texture
	 */
	public void setTextureManager(TextureManager texture) {
		this.gameTexture = texture;
		managers.add(gameTexture);
	}
	
	/**
	 * Sets the current main menu display
	 * @param menu
	 */
	public void setMainMenu(MainMenu menu){
		this.menu = menu;
	}
	
	/**
	 * Returns the current main menu
	 * @return menu
	 */
	public MainMenu getMainMenu(){
		return menu;
	}

	/**
	 * Gets the current game world
	 * @return the currently loaded world
	 */
	public BaseWorld getWorld() {
		return gameWorld;
	}



	/**
	 * Gets the minimap
	 * @return The minimap
	 */
	public MiniMap getMiniMap() {
		return miniMap;
	}

	/**
	 * Sets the minimap to be displayed
	 * @param map
	 */
	public void setMiniMap(MiniMap map) {
		miniMap = map;
	}

	/**
	 * Sets the used GUI to easy access
	 * @param gui the gui the game is using
	 */
	public void setGui(HUDView gui){
		this.gui = gui;
	}

	/**
	 * @return returns the gui the game is using
	 */
	public HUDView getGui(){
		return this.gui;
	}

	/**
	 * Gets the current map world.
	 * @return
	 */
	public BaseWorld getMapWorld() {
		return mapWorld;
	}

	/**
	 * returns 0 if the game world is being rendered and 1 if the full screen map is.
	 * @return
	 */
	public int getActiveView() {
		return activeView;
	}
	
	public void resetGame(){
		GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		WeatherManager wm = (WeatherManager) GameManager.get().getManager(WeatherManager.class);
		MainMenu.player.stopSoundTrack();
		gamestage.clear();
		this.gameWorld.getEntities().clear();
		this.gameWorld = null;
		this.gui = null;
		this.miniMap = null;
		TimeManager.resetInGameTime();
		this.menu.endGame();
		try {
			wm.water.dispose();
		}catch(NullPointerException e){}
		this.menu = new MainMenu(this.gameskin, this.gamestage);
		menu.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		black.clear();
	}

	/**
	 * Toggles activeView.
	 */
	public void toggleActiveView() {
		if (activeView == 0) {
			activeView = 1;
			toggleMapOn();
		} else {
			activeView = 0;
			toggleMapOff();
		}
	}

	private void toggleMapOn() {
		// move camera to centre and zoom out
		cameraPos = camera.position.cpy();
		camera.zoom = Math.max(getWorld().getWidth(),getWorld().getLength())/16;
		camera.position.set((getWorld().getWidth()*58)/2, (getWorld().getLength()*36)/10, 0); //TODO make this work with different map and window sizes
	}

	private void toggleMapOff() {
		camera.position.set(cameraPos);
		camera.zoom = 1;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}
	
	/**
	 * sets the currently used game skin
	 * 
	 * @param skin setskin the skin used to display the
	 * games gui
	 */
	public void setSkin(Skin skin) {
		gameskin = skin;
	}
	
	/**
	 * sets the alternative game skin
	 * 
	 * @param skin setskin the skin used to display the
	 * games gui
	 */
	public void setAltSkin(Skin skin) {
		altskin = skin;
	}
	
	/**
	 * gets the currently used game skin
	 * 
	 * @return Skin the skin used to display the
	 * games gui 
	 */
	public Skin getSkin() {
		return gameskin;
	}
	
	/**
	 * gets the alternative game skin
	 * 
	 * @return Skin the skin used to display the
	 * games gui 
	 */
	public Skin getAltSkin() {
		return altskin;
	}

	/**
	 * sets the currently used game stage
	 * 
	 * @param setsstage the stage used to display the
	 * games gui
	 */
	public void setStage(Stage stage) {
		gamestage = stage;
	}
	
	/**
	 * gets the currently used game stage
	 * 
	 * @return stage the skin used to display the
	 * games gui 
	 */
	public Stage getStage() {
		return gamestage;
	}	
	
	/**
	 * On tick method for ticking managers with the TickableManager interface
	 * @param i
	 */
	@Override
	public void onTick(long i) {
		//this is need to let managers use other managers ontick
		//please don't change it
		List<Manager> deepcopy = new ArrayList<Manager>((List<Manager>) managers);
		Iterator<Manager> managersIter =  deepcopy.iterator();
		while(managersIter.hasNext()) {
			Manager m = managersIter.next();
			if (m instanceof TickableManager) {
				((TickableManager) m).onTick(0);
			}
		}
	}

	/**
	 * returns if unit and building costs are free
	 * @param isFree
	 */
	public boolean areCostsFree() {
		return costsFree;
	}
	
	/**
	 * Cheat to toggle free unit and building costs
	 * @param isFree
	 */
	public void setCostsFree(boolean isFree) {
		costsFree = isFree;
	}
	
	public void setMapType(MapTypes world) {
		this.map = world;
	}
	
	public MapTypes getMapType() {
		return this.map;
	}

}
