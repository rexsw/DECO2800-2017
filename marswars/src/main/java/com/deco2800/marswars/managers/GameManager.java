package com.deco2800.marswars.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.deco2800.marswars.worlds.BaseWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
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
	
	private OrthographicCamera camera;

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
	 * @param manager
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
	public Manager getManager(Class<?> type) {
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
			e.printStackTrace();
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

	/**
	 * Sets the current game world
	 * @param world
	 */
	public void setWorld(BaseWorld world) {
		this.gameWorld = world;
	}

	/**
	 * Gets the current game world
	 * @return
	 */
	public BaseWorld getWorld() {
		return gameWorld;
	}
	
	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	/**
	 * On tick method for ticking managers with the TickableManager interface
	 * @param i
	 */
	@Override
	public void onTick(long i) {
		for (Manager m : managers) {
			if (m instanceof TickableManager) {
				((TickableManager) m).onTick(0);
			}
		}
	}

}