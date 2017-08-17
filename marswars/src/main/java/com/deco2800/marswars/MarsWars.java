package com.deco2800.marswars;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
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
import com.deco2800.marswars.entities.Tickable;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.net.*;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.renderers.Render3D;
import com.deco2800.marswars.renderers.Renderable;
import com.deco2800.marswars.renderers.Renderer;
import com.deco2800.marswars.worlds.InitialWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

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
	Button peonButton;
	Label helpText;
	Label rocksLabel;
	Label gameTime;

	long lastGameTick = 0;
	long lastMenuTick = 0;

	final int serverPort = 8080;
	SpacClient networkClient;
	SpacServer networkServer;

	Skin skin;

	Set<Integer> downKeys = new HashSet<>();

	/**
	 * Creates the required objects for the game to start.
	 * Called when the game first starts
	 */
	@Override
	public void create () {

		TextureManager reg = (TextureManager)(GameManager.get().getManager(TextureManager.class));

		/*
		 *	Set up new stuff for this game
		 */
		GameManager.get().setWorld(new InitialWorld());
		((InitialWorld)GameManager.get().getWorld()).loadEntities();

		new Thread(new Runnable() {
			@Override
			public void run() {
				// do something important here, asynchronously to the rendering thread
				while(true) {
					if(TimeUtils.nanoTime() - lastGameTick > 10000000) {
						for (Renderable e : GameManager.get().getWorld().getEntities()) {
							if (e instanceof Tickable) {
								((Tickable) e).onTick(0);

							}
						}

						GameManager.get().onTick(0);
						lastGameTick = TimeUtils.nanoTime();
					}

					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

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
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		window = new Window("Menu", skin);

		/* Add a quit button to the menu */
		Button button = new TextButton("Quit", skin);

		/* Add another button to the menu */
		peonButton = new TextButton("Select a Unit", skin);

		/* Start server button */
		Button startServerButton = new TextButton("Start Server", skin);
		startServerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Dialog ipDiag = new Dialog("Local IP", skin, "dialog") {};
				try {
					InetAddress ipAddr = InetAddress.getLocalHost();
					String ip = ipAddr.getHostAddress();
					ipDiag.text("IP Address: " + ip);

					ServerConnectionManager serverConnectionManager = new ServerConnectionManager();
					networkServer = new SpacServer(serverConnectionManager);


					ClientConnectionManager clientConnectionManager = new ClientConnectionManager();
					networkClient = new SpacClient(clientConnectionManager);
					//Initiate Server
					try {
						networkServer.bind(serverPort);
					} catch (IOException e) {
						e.printStackTrace();
					}

					//Join it as a Client
					try {
						networkClient.connect(5000, ip, serverPort);
					} catch (IOException e) {
						e.printStackTrace();
					}
					JoinLobbyAction action = new JoinLobbyAction("Host");
					networkClient.sendObject(action);

					System.out.println(ip);
				} catch (UnknownHostException ex) {
					ipDiag.text("Something went wrong");
					ex.printStackTrace();
				}
				ipDiag.button("Close", null);
				ipDiag.show(stage);
			}

		});

		/* Join server button */
		Button joinServerButton = new TextButton("Join Server", skin);
		joinServerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// Construct inside of dialog
				Table inner = new Table(skin);
				Label ipLabel = new Label("IP", skin);
				TextField ipInput = new TextField("localhost", skin);
				Label usernameLabel = new Label("Username", skin);
				TextField usernameInput = new TextField("wololo", skin);

				inner.add(ipLabel);
				inner.add(ipInput);
				inner.row();
				inner.add(usernameLabel);
				inner.add(usernameInput);
				inner.row();

				Dialog ipDiag = new Dialog("IP", skin, "dialog") {
					@Override
					protected void result(Object o) {
						if(o != null) {
							String username = usernameInput.getText();
							String ip = ipInput.getText();

							ClientConnectionManager connectionManager = new ClientConnectionManager();
							networkClient = new SpacClient(connectionManager);

							try {
								networkClient.connect(5000, ip, serverPort);
							} catch (IOException e) {
								e.printStackTrace();
							}
							JoinLobbyAction action = new JoinLobbyAction(username);
							networkClient.sendObject(action);
						}
					}
				};

				ipDiag.getContentTable().add(inner);
				ipDiag.button("Join", true);
				ipDiag.button("Cancel", null);

				ipDiag.show(stage);
			}
		});

		/* Add a programatic listener to the quit button */
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.exit(0);
			}
		});

		helpText = new Label("Welcome to MarsWars!", skin);
		rocksLabel = new Label("Rocks: 0", skin);
		gameTime = new Label(" Time: 00:00", skin);

		/* Add all buttons to the menu */
		window.add(button);
		window.add(helpText);
		window.add(peonButton);
		window.add(rocksLabel);
		window.add(gameTime);
		window.add(startServerButton);
		window.add(joinServerButton);
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

			@Override
			public boolean keyDown(int keyCode) {
				downKeys.add(keyCode);
				keyPressed(keyCode);
				return true;
			}

			@Override
			public boolean keyUp(int keyCode) {
				downKeys.remove(keyCode);
				return true;
			}

			@Override
			public boolean scrolled(int amount) {
				int cursorX = Gdx.input.getX();
				int cursorY = Gdx.input.getY();
				int windowWidth = Gdx.graphics.getWidth();
				int windowHeight = Gdx.graphics.getHeight();
				if (camera.zoom > 0.5 && amount == -1) { // zoom in
					double xMag = Math.sqrt(Math.pow(windowWidth/2, 2) + Math.pow(cursorX, 2));
					//double xMul = Math.signum(windowWidth-cursorX)*(xMag-0)/((windowWidth/2)-0) * (1-0) + 0;
					double yMag = Math.sqrt(Math.pow(windowHeight/2, 2) + Math.pow(cursorY, 2));
					//double yMul = Math.signum(windowHeight-cursorY)*(yMag-0)/((windowHeight/2)-0) * (1-0) + 0;
					System.out.println(windowWidth/2 + ":" + windowHeight/2 + " - " + cursorX + ":" + cursorY + " - " + xMag + ":" + yMag);
					camera.zoom /= 1.2;
					camera.translate((float)xMag, (float)yMag);
					// TODO implement moving the camera towards the cursor when you zoom in
					//camera.position.set(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)));
					//Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
				} else if (camera.zoom < 10 && amount == 1) { // zoom out
					camera.zoom *= 1.2;
				}
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
		renderer.render(batch, camera);

		ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		rocksLabel.setText("Rocks: " + resourceManager.getRocks() + " Crystal: " + resourceManager.getCrystal() + " Water: " + resourceManager.getWater() + " Biomass: " + resourceManager.getBiomass());

		/*
		 * Update time & set color depending if night/day
		 */
		TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);
		gameTime.setText(" Time: " + timeManager.toString());
		if (timeManager.isNight()){
			gameTime.setColor(Color.FIREBRICK);
		}
		else{
			gameTime.setColor(Color.BLUE);
		}

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
	private void handleInput() {
		final int speed = 10;
		final int pxTolerance = 20; // modifies how close to the edge the cursor has to be before the map
		// starts moving
		int cameraScaleFactor = 32; //the level of skewing caused by the orthographic camera
		int cursorX = Gdx.input.getX();
		int cursorY = Gdx.input.getY();
		int windowWidth = Gdx.graphics.getWidth();
		int windowHeight = Gdx.graphics.getHeight();
		if ((downKeys.contains(Input.Keys.UP) || downKeys.contains(Input.Keys.W))
				&& camera.position.y <= GameManager.get().getWorld().getLength() * cameraScaleFactor / 2) {
			camera.translate(0, 1 * speed * camera.zoom, 0);
		}
		if ((downKeys.contains(Input.Keys.DOWN) || downKeys.contains(Input.Keys.S))
				&& camera.position.y + windowHeight * 4.5 >= 0) {
			camera.translate(0, -1 * speed * camera.zoom, 0);
		}
		if ((downKeys.contains(Input.Keys.LEFT) || downKeys.contains(Input.Keys.A))
				&& camera.position.x - windowWidth / 5 >= 0) {
			camera.translate(-1 * speed * camera.zoom, 0, 0);
		}
		if ((downKeys.contains(Input.Keys.RIGHT) || downKeys.contains(Input.Keys.D))
				&& camera.position.x - windowWidth <= Math.sqrt(2) * GameManager.get().getWorld().getWidth() * (cameraScaleFactor + 1)) {
			camera.translate(1 * speed * camera.zoom, 0, 0);
		}
		if (downKeys.contains(Input.Keys.EQUALS)) {
			if (camera.zoom > 0.5) {
				camera.zoom /= 1.05;
			}
		}
		if (downKeys.contains(Input.Keys.MINUS)) {
			if (camera.zoom < 10) {
				camera.zoom *= 1.05;
			}
		}

		// Move the map dependant on the cursor position
		if ((cursorX > pxTolerance && cursorX + pxTolerance <= windowWidth) &&
				(cursorY > pxTolerance && cursorY + pxTolerance <= windowHeight)) {
			// skip checking for movement
			return;
		}
		// Got rid of moving the map down because it makes it difficult
		if (cursorX > windowWidth - pxTolerance
				&& camera.position.x - windowWidth <= Math.sqrt(2) * GameManager.get().getWorld().getWidth() * (cameraScaleFactor + 1)) { // moving right
			if (cursorY < pxTolerance
					&& camera.position.y <= GameManager.get().getWorld().getLength() * cameraScaleFactor / 2) {
				// move up and right
				camera.translate((float) 0.7071 * speed * camera.zoom, (float) 0.7071 * speed * camera.zoom, 0);
				//} else if (cursorY > windowHeight - pxTolerance) {
				// move down and right
				//camera.translate((float) 0.7071 * speed * camera.zoom, (float) -0.7071 * speed * camera.zoom, 0);
			} else {
				// move right
				camera.translate(1 * speed * camera.zoom, 0, 0);
			}
		} else if (cursorX < pxTolerance
				&& camera.position.x - windowWidth / 5 >= 0) { // moving left
			if (cursorY < pxTolerance
					&& camera.position.y <= GameManager.get().getWorld().getLength() * cameraScaleFactor / 2) {
				// move up and left
				camera.translate((float) -0.7071 * speed * camera.zoom, (float) 0.7071 * speed * camera.zoom, 0);
				//} else if (cursorY > windowHeight - pxTolerance){
				// move down and left
				//camera.translate((float) -0.7071 * speed * camera.zoom, (float) -0.7071 * speed * camera.zoom, 0);
			} else {
				// move left
				camera.translate(-1 * speed * camera.zoom, 0, 0);
			}
			//} else if (cursorY > windowHeight - pxTolerance) {
			// move down
			//camera.translate(0, -1 * speed * camera.zoom, 0);
		} else if (cursorY < pxTolerance
				&& camera.position.y <= GameManager.get().getWorld().getLength() * cameraScaleFactor / 2) {
			// move up
			camera.translate(0, 1 * speed * camera.zoom, 0);
		}
	}

	/**
	 * Called when a key has been pressed
	 * @param keycode key that was pressed
	 */
	private void keyPressed(int keycode) {
		if (keycode == Input.Keys.ENTER) {
			if(this.networkClient != null) {
				Table inner = new Table(skin);
				TextField msgInput = new TextField("", skin);

				inner.add(msgInput);

				Dialog ipDiag = new Dialog("Message", skin, "dialog") {
					@Override
					protected void result(Object o) {
						if(o != null) {
							String msg = msgInput.getText();

							MessageAction action = new MessageAction(msg);
							networkClient.sendObject(action);
						}
					}
				};

				ipDiag.getContentTable().add(inner);
				ipDiag.button("Send", true);
				ipDiag.button("Cancel", null);
				ipDiag.key(Input.Keys.ENTER, true);

				ipDiag.show(stage);
			}
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
		window.setWidth(stage.getWidth());
	}

	/**
	 * Disposes of assets etc when the rendering system is stopped.
	 */
	@Override
	public void dispose () {
		// Don't need this at the moment
	}

}
