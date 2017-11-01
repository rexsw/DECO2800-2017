package com.deco2800.marswars;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.units.MissileEntity;
import com.deco2800.marswars.initiategame.Game;
import com.deco2800.marswars.initiategame.InputProcessor;
import com.deco2800.marswars.mainmenu.MainMenu;
import com.deco2800.marswars.managers.BackgroundManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.renderers.Render3D;
import com.deco2800.marswars.renderers.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Moos
 * The starting class for the UQ DECO2800 Game Engine.
 * This class handles the application rendering including selection of tileRenderer and creation of the world.
 * @Author Tim Hadwen
 */
public class MarsWars extends ApplicationAdapter implements ApplicationListener {

	// logger of the class
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
	private BackgroundManager bgManager = (BackgroundManager)
			GameManager.get().getManager(BackgroundManager.class);
	long pauseTime = 0;
	private static int invincible = 0;
	private Skin skin;
	Set<Integer> downKeys = new HashSet<Integer>();
	TextureManager reg;

	private InputProcessor inputP;

	/**
	 * Creates the required objects for the game to start.
	 * Called when the game first starts
	 */
	@Override
	public void create () {
		this.stage = new Stage(new ScreenViewport());
		this.skin = new Skin(Gdx.files.internal("uiskin.json"));

		GameManager.get().setSkin(this.skin);
		GameManager.get().setStage(this.stage);

		/*All managers */
		this.reg = (TextureManager) (GameManager.get().getManager(TextureManager.class));

		this.camera = new OrthographicCamera(1920, 1080);
		this.inputP = new InputProcessor(this.camera, this.stage, this.skin);

		this.inputP.setInputProcessor();
		GameManager.get().setCamera(this.camera);

		MainMenu mainMenu = new MainMenu(this.skin, this.stage);
		LOGGER.info("Game running: " + mainMenu.gameStarted());
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
		this.inputP.handleInput();

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
		batch.draw(background, this.camera.position.x -
						this.camera.viewportWidth*this.camera.zoom/2 ,
				this.camera.position.y -
						this.camera.viewportHeight*this.camera.zoom/2,
				this.camera.viewportWidth*this.camera.zoom,
				this.camera.viewportHeight*this.camera.zoom);
		batch.end();

		//Render the rest of the game
		GameManager.get().getMainMenu().renderGame(batch, camera);

		/* Dispose of the spritebatch to not have memory leaks */
		if (GameManager.get().getWorld() != null) {
			Gdx.graphics.setTitle("DECO2800 " + this.getClass().getCanonicalName()
					+ " - FPS: " + Gdx.graphics.getFramesPerSecond() + " Tick: " + Game.ticktime + " Entities: " + GameManager.get().getWorld().getEntities().size());
		} else {
			Gdx.graphics.setTitle("DECO2800 " + this.getClass().getCanonicalName()
					+ " - FPS: " + Gdx.graphics.getFramesPerSecond() + " Tick: " + Game.ticktime);
		}
		//trying to eliminate render crashes in the most naive way possible:
		//will likely cause frames to drop, if the problem is present in sequential frames then
		//this will imporve nothing
		try {
			this.stage.act();
			this.stage.draw();
			GameManager.get().setCamera(this.camera);
		} catch(NullPointerException e){
			LOGGER.error("Failed to render frame due to NullPointerException");
		}
		batch.dispose();

		setInvincible();
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
		GameManager.get().getMainMenu().resize(width, height);
	}

	/**
	 * Disposes of assets etc when the rendering system is stopped.
	 */
	@Override
	public void dispose () {
		// Don't need this at the moment
	}

	/**
	 * If the invincible value is 1, make the enemy's attack to be of no effect
	 */
	public void setInvincible(){
		if(invincible == 1)
		{
			List<BaseEntity> entityl = GameManager.get().getWorld().getEntities();
			for(BaseEntity e:entityl)
			{
				if(e.getOwner() > 0 && e instanceof  MissileEntity )
				{
					((MissileEntity) e) .setDamage(0);
				}
			}
		}
	}

	/**
	 * set a new invisible flag
	 *
	 * @param invincible the new flag
	 */
	public static void setInvincible(int invincible) {
		MarsWars.invincible = invincible;
	}
}
