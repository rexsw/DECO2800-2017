package com.deco2800.marswars;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.marswars.InitiateGame.InputProcessor;
import com.deco2800.marswars.hud.HUDView;
import com.deco2800.marswars.mainMenu.MainMenu;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.entities.units.*;
import com.deco2800.marswars.managers.*;
import com.deco2800.marswars.renderers.Render3D;
import com.deco2800.marswars.renderers.Renderer;
import com.deco2800.marswars.worlds.CustomizedWorld;
import com.deco2800.marswars.worlds.MapSizeTypes;
import com.deco2800.marswars.worlds.map.tools.MapContainer;
import com.deco2800.marswars.worlds.map.tools.MapTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.*;
import java.util.List;
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

	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	private BackgroundManager bgManager = (BackgroundManager)
			GameManager.get().getManager(BackgroundManager.class);
	private WeatherManager weatherManager = (WeatherManager)
			GameManager.get().getManager(WeatherManager.class);

	long lastGameTick = 0;
	long lastMenuTick = 0;
	long pauseTime = 0;

	public static int invincible;
	

	private boolean gameStarted = false;
	private MainMenu menu;
	private Skin skin;
	private HUDView view;

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
								
		this.camera = new OrthographicCamera(1920, 1080);
		this.inputP = new InputProcessor(this.camera, this.stage, this.skin);
		
		this.view = new com.deco2800.marswars.hud.HUDView(this.stage, this.skin, GameManager.get(), this.reg);
		this.menu = new MainMenu(this.skin, this.stage, this, this.camera);

		createMap();
		this.inputP.setInputProcessor();
		GameManager.get().setCamera(this.camera);
		
		
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

		/* Update the input managers
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
		Texture background = this.reg.getTexture(backgroundString);
		batch.begin();
		batch.draw(background, this.camera.position.x - this.camera.viewportWidth*this.camera.zoom/2 , this.camera.position.y -
				this.camera.viewportHeight*this.camera.zoom/2, this.camera.viewportWidth*this.camera.zoom,
				this.camera.viewportHeight*this.camera.zoom);
		batch.end();
        /* Render the tiles second */
		BatchTiledMapRenderer tileRenderer = this.renderer.getTileRenderer(batch);
		tileRenderer.setView(this.camera);
		tileRenderer.render();
		
		this.menu.renderGame(this.camera, batch);
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
		if(invincible == 1)
		{
			List<BaseEntity> entityl = GameManager.get().getWorld().getEntities();
			for(BaseEntity e:entityl)
			{
				if(e.getOwner() == -1 && e instanceof AttackableEntity)
				{
					((AttackableEntity) e).setHealth(((AttackableEntity) e).getMaxHealth());
				}
			}

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
}
