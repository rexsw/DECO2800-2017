package com.deco2800.marswars;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.marswars.entities.Selectable;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.net.MarsWarsClientConnectionManager;
import com.deco2800.moos.entities.Tickable;
import com.deco2800.moos.managers.GameManager;
import com.deco2800.moos.managers.TextureManager;
import com.deco2800.moos.renderers.Render3D;
import com.deco2800.moos.renderers.Renderable;
import com.deco2800.moos.renderers.Renderer;
import uq.deco2800.soom.client.SoomClient;
import uq.deco2800.soom.client.game.GameClientConnectionManager;

import java.io.IOException;

/**
 * Moos
 * The starting class for the UQ DECO2800 Game Engine.
 * This class handles the application rendering including selection of tileRenderer and creation of the world.
 * @Author Tim Hadwen
 */
public class MarsWars extends ApplicationAdapter implements ApplicationListener {

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
	Button peonButton;
	Label helpText;

	long lastGameTick = 0;
	long lastMenuTick = 0;

	SoomClient networkClient;

	/**
	 * Creates the required objects for the game to start.
	 * Called when the game first starts
	 */
	@Override
	public void create () {

		TextureManager reg = (TextureManager)(GameManager.get().getManager(TextureManager.class));
		reg.saveTexture("tree_selected", "resources/placeholderassets/tree_selected.png");
		reg.saveTexture("rock", "resources/placeholderassets/ground-1.png");
		reg.saveTexture("base", "resources/placeholderassets/base.png");
		reg.saveTexture("spacman_yellow", "resources/placeholderassets/spacman_yellow.png");
		reg.saveTexture("spacman", "resources/placeholderassets/spacman.png");
		reg.saveTexture("spacman_red", "resources/placeholderassets/spacman_red.png");
		reg.saveTexture("spacman_blue", "resources/placeholderassets/spacman_blue.png");
		reg.saveTexture("spacman_green", "resources/placeholderassets/spacman_green.png");
		reg.saveTexture("deded_spacman", "resources/placeholderassets/spacman_ded.png");
		reg.saveTexture("spatman_blue", "spatman_blue.png");

		/*
		 *	Set up new stuff for this game
		 */
		GameManager.get().setWorld(new InitialWorld());

		/* Create a sound manager for the whole game */

		GameClientConnectionManager connectionManager = new MarsWarsClientConnectionManager();
		networkClient = new SoomClient(connectionManager);

		try {
			networkClient.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}

		networkClient.joinLobby("timmy");

		/*
		 * Setup the game itself
		 */
		/* Setup the camera and move it to the center of the world */
		camera = new OrthographicCamera(1920, 1080);
		camera.translate(GameManager.get().getWorld().getWidth()*32, 0);

		/*
		 * Setup GUI
		 */
		stage = new Stage(new ScreenViewport());
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		window = new Window("Menu", skin);

		/* Add a quit button to the menu */
		Button button = new TextButton("Quit", skin);

		/* Add another button to the menu */
		peonButton = new TextButton("Select a Unit", skin);

		/* Add a programatic listener to the quit button */
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.exit(0);
			}
		});

		helpText = new Label("Welcome to MarsWars!", skin);

		/* Add all buttons to the menu */
		window.add(button);
		window.add(helpText);
		window.add(peonButton);
		window.pack();
		window.setMovable(false); // So it doesn't fly around the screen
		window.setPosition(0, 0); // Place at the bottom
		window.setWidth(stage.getWidth());

		/* Add the window to the stage */
		stage.addActor(window);

		/*
		 * Setup inputs for the buttons and the game itself
		 */
		/* Setup an Input Multiplexer so that input can be handled by both the UI and the game */
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage); // Add the UI as a processor

        /*
         * Set up some input managers for panning with dragging.
         */
		inputMultiplexer.addProcessor(new InputAdapter() {

			int originX;
			int originY;

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				originX = screenX;
				originY = screenY;

				Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
				MouseHandler mouseHandler = (MouseHandler)(GameManager.get().getManager(MouseHandler.class));
				mouseHandler.handleMouseClick(worldCoords.x, worldCoords.y, button);
				return true;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {

				originX -= screenX;
				originY -= screenY;

				// invert the y axis
				originY = -originY;

				originX += camera.position.x;
				originY += camera.position.y;

				camera.translate(originX - camera.position.x, originY - camera.position.y);

				originX = screenX;
				originY = screenY;

				return true;
			}
		});

		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	/**
	 * Renderer thread
	 * Must update all displayed elements using a Renderer
	 */
	@Override
	public void render () {

		if(TimeUtils.nanoTime() - lastMenuTick > 1000000000) {
			window.removeActor(peonButton);
			window.removeActor(helpText);
			boolean somethingSelected = false;
			for (Renderable e : GameManager.get().getWorld().getEntities()) {
				if (e instanceof Selectable) {
					if (((Selectable) e).isSelected()) {
						peonButton = ((Selectable) e).getButton();
						helpText = ((Selectable) e).getHelpText();
						somethingSelected = true;
					}
				}

			}
			if (!somethingSelected) {
				peonButton = new TextButton("Select a Unit", new Skin(Gdx.files.internal("uiskin.json")));
				helpText = new Label("Welcome to MarsWars!", new Skin(Gdx.files.internal("uiskin.json")));
			}
			window.add(peonButton);
			window.add(helpText);
			lastMenuTick = TimeUtils.nanoTime();
		}

		if(TimeUtils.nanoTime() - lastGameTick > 10000000) {
			for (Renderable e : GameManager.get().getWorld().getEntities()) {
				if (e instanceof Tickable) {
					((Tickable) e).onTick(0);

				}
			}

			GameManager.get().onTick(0);
			lastGameTick = TimeUtils.nanoTime();
		}

        /*
         * Create a new render batch.
         * At this stage we only want one but perhaps we need more for HUDs etc
         */
		SpriteBatch batch = new SpriteBatch();

        /*
         * Update the input managers
         */
		handleInput();

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

        /* Render the tiles first */
		BatchTiledMapRenderer tileRenderer = renderer.getTileRenderer(batch);
		tileRenderer.setView(camera);
		tileRenderer.render();

		/*
         * Use the selected renderer to render objects onto the map
         */
		renderer.render(batch);

		/* Dispose of the spritebatch to not have memory leaks */
		Gdx.graphics.setTitle("DECO2800 " + this.getClass().getCanonicalName() +  " - FPS: "+ Gdx.graphics.getFramesPerSecond());

		stage.act();
		stage.draw();

		batch.dispose();
	}


	/**
	 * Handles keyboard input.
	 * There probably should be some way to pass this into another class
	 */
	//TODO Handle this elsewhere
	private void handleInput() {
		int speed = 10;

		if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.translate(0, 1*speed*camera.zoom, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.translate(0, -1*speed*camera.zoom, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.translate(-1*speed*camera.zoom, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.translate(1*speed*camera.zoom, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
			if (camera.zoom > 0.1) {
				camera.zoom -= 0.1;
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
			camera.zoom += 0.1;
		}

	}

	/**
	 * Resizes the viewport
	 * @param width
	 * @param height
	 */
	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();

		stage.getViewport().update(width, height, true);
		window.setPosition(0, 0);
	}

	/**
	 * Disposes of assets etc when the rendering system is stopped.
	 */
	@Override
	public void dispose () {
		// Don't need this at the moment
	}

}