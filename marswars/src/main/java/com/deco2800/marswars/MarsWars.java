package com.deco2800.marswars;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.managers.AiManagerTest;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.managers.*;
import com.deco2800.marswars.net.*;
import com.deco2800.marswars.renderers.Render3D;
import com.deco2800.marswars.renderers.Renderable;
import com.deco2800.marswars.renderers.Renderer;
import com.deco2800.marswars.hud.*;
import com.deco2800.marswars.worlds.CustomizedWorld;
import com.deco2800.marswars.worlds.map.tools.MapContainer;
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
	Label gameTimeDisp;
	Label gameLengthDisp;

	TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);
	BackgroundManager bgManager = (BackgroundManager) GameManager.get().getManager(BackgroundManager.class);

	long lastGameTick = 0;
	long lastMenuTick = 0;

	static final int SERVER_PORT = 8080;
	SpacClient networkClient;
	SpacServer networkServer;
	
	private boolean gameStarted = false;

	Skin skin;
	
	HUDView view;

	Set<Integer> downKeys = new HashSet<>();
	
	/**
	 * Creates the required objects for the game to start.
	 * Called when the game first starts
	 */
	@Override
	public void create () {

		// zero game length clock (i.e. Tell TimeManager new game has been launched)
		timeManager.setGameStartTime();
		TextureManager reg = (TextureManager)(GameManager.get().getManager(TextureManager.class));
		reg.saveTexture("minimap", "resources/HUDAssets/minimap.png");
		// TODO get rid of this once the minimap loads based on the randomly loaded map
		
		/*
		 *	Set up new stuff for this game
		 * TODO some way to choose which map is being loaded
		 */
		MapContainer map = new MapContainer();
		CustomizedWorld world = new CustomizedWorld(map);
		world.loadMapContainer(map);
		GameManager.get().setWorld(world);
		GameManager.get().setMiniMap(new MiniMap(map.getMap(), 220, 220));


		/*
		 * sets all starting entities to be player owned
		 */
		for( BaseEntity e : GameManager.get().getWorld().getEntities()) {
			if(e instanceof HasOwner) {
				((HasOwner) e).setOwner(GameManager.get().getManager(AiManagerTest.class));
			}
		}
		/*
		 * adds entities for the ai and set then to be ai owned
		 */
		int length = GameManager.get().getWorld().getLength();
		int width = GameManager.get().getWorld().getWidth();
		setAI(length -1, width -1);
		setAI(1, 1);

		AiManagerTest aim1 = new AiManagerTest();
		GameManager.get().addManager(aim1);
		Spacman ai = new Spacman(0, 1, 0);
		Spacman ai1 = new Spacman(1, 0, 0);
		Base aibase = new Base(GameManager.get().getWorld(), 3, 3, 0);
		EnemySpacman aienemy = new EnemySpacman(length-1, width-1, 0);
		ai.setOwner(aim1);
		GameManager.get().getWorld().addEntity(ai);
		ai1.setOwner(aim1);
		GameManager.get().getWorld().addEntity(ai1);
		aibase.setOwner(aim1);
		GameManager.get().getWorld().addEntity(aibase);
		aienemy.setOwner(aim1);
		GameManager.get().getWorld().addEntity(aienemy);
		
		AiManagerTest aim2 = new AiManagerTest();
		GameManager.get().addManager(aim2);
		Spacman ai2 = new Spacman(1, 2, 0);
		Spacman ai21 = new Spacman(0, 1, 0);
		Base aibase2 = new Base(GameManager.get().getWorld(), 9, 9, 0);
		EnemySpacman aienemy2 = new EnemySpacman(length-2, length-2, 0);
		ai2.setOwner(aim2);
		GameManager.get().getWorld().addEntity(ai2);
		ai21.setOwner(aim2);
		GameManager.get().getWorld().addEntity(ai21);
		aibase2.setOwner(aim2);
		GameManager.get().getWorld().addEntity(aibase2);
		aienemy2.setOwner(aim2);
		GameManager.get().getWorld().addEntity(aienemy2);

		// add soldier for combat testing
		Soldier soldierA = new Soldier(7, 7, 0);
		Soldier soldierB = new Soldier(5, 5, 0);
		soldierA.setOwner(GameManager.get().getManager(PlayerManager.class));
		soldierB.setOwner(GameManager.get().getManager(PlayerManager.class));
		GameManager.get().getWorld().addEntity(soldierA);
		GameManager.get().getWorld().addEntity(soldierB);
		
		// do something important here, asynchronously to the rendering thread
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
						LOGGER.error(e.toString());
					}
				}
			}
		}).start();

		/*
		 * Setup the game itself
		 */
		/* Setup the camera and move it to the center of the world */
		camera = new OrthographicCamera(1920, 1080);
		GameManager.get().setCamera(camera);
		camera.translate(GameManager.get().getWorld().getWidth()*32, 0);

		/*
		 * Setup GUI > Refer to com.deco2800.marwars.hud for this now 
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
						networkServer.bind(SERVER_PORT);
					} catch (IOException e) {
						LOGGER.error("Error when initiating server", e);
					}

					//Join it as a Client
					try {
						networkClient.connect(5000, ip, SERVER_PORT);
					} catch (IOException e) {
						LOGGER.error("Error when joinging as client", e);
					}
					JoinLobbyAction action = new JoinLobbyAction("Host");
					networkClient.sendObject(action);

					LOGGER.info(ip);
				} catch (UnknownHostException ex) {
					ipDiag.text("Something went wrong");
					LOGGER.error("Unknown Host", ex);
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
								networkClient.connect(5000, ip, SERVER_PORT);
							} catch (IOException e) {
								LOGGER.error("Join server error", e);
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

		helpText = new Label("Welcome to SpacWars!", skin);
		rocksLabel = new Label("Rocks: 0", skin);
		gameTimeDisp = new Label(" Time: 00:00", skin);
		gameLengthDisp = new Label(" Game Length: 00:00:00", skin);

		/* Add all buttons to the menu */
		window.add(button);
		//window.add(helpText);
		//window.add(peonButton);
		window.add(rocksLabel);
		window.add(gameTimeDisp);
		window.add(gameLengthDisp);
		window.add(startServerButton);
		window.add(joinServerButton);
		window.pack();
		window.setMovable(false); // So it doesn't fly around the screen
		window.setPosition(400, 0); // Place at the bottom
		window.setWidth((stage.getWidth())-300);

		view = new com.deco2800.marswars.hud.HUDView(stage, skin, GameManager.get(), reg);
		view.setMenu(window);
		view.getActionWindow().add(peonButton);
		view.getActionWindow().add(helpText);
		view.toggleHUD();
		
		/* Add the window to the stage */
		//stage.addActor(window);

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

				if (GameManager.get().getActiveView() == 1) {
					camera.position.set(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)));
					camera.zoom = 1;
					GameManager.get().toggleActiveView();
				} else {

					originX = screenX;
					originY = screenY;
					// if the click is on the minimap
					if (GameManager.get().getMiniMap().clickedOn(screenX, screenY)) {
						return true;
					}

					Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
					MouseHandler mouseHandler = (MouseHandler) (GameManager.get().getManager(MouseHandler.class));
					mouseHandler.handleMouseClick(worldCoords.x, worldCoords.y, button);
				}
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
			/**
			 * Enable player to zoom in and zoom out through scroll wheel
			 */
			@Override
			public boolean scrolled(int amount) {
				if (GameManager.get().getActiveView() == 1) {
					return false;
				}
				int cursorX = Gdx.input.getX();
				int cursorY = Gdx.input.getY();
				int windowWidth = Gdx.graphics.getWidth();
				int windowHeight = Gdx.graphics.getHeight();
				if (camera.zoom > 0.5 && amount == -1) { // zoom in
					double xMag = (double)cursorX - (windowWidth/2);
					double yMag = (double)(windowHeight/2) - cursorY;
					camera.zoom /= 1.2;
					camera.translate((float)xMag, (float)yMag);
				} else if (camera.zoom < 10 && amount == 1) { // zoom out
					camera.zoom *= 1.2;
				}
				forceMapLimits();
				return true;
			}
		});

		Gdx.input.setInputProcessor(inputMultiplexer);
		GameManager.get().toggleActiveView();
	}


	/**
	 * Renderer thread
	 * Must update all displayed elements using a Renderer
	 */
	@Override
	public void render () {
		if(TimeUtils.nanoTime() - lastMenuTick > 100000) {
			view.getActionWindow().removeActor(peonButton);
			view.getActionWindow().removeActor(helpText);
			
			boolean somethingSelected = false;
			for (Renderable e : GameManager.get().getWorld().getEntities()) {
				if ((e instanceof Selectable) && ((Selectable) e).isSelected()) {
					peonButton = ((Selectable) e).getButton();
					helpText = ((Selectable) e).getHelpText();
					somethingSelected = true;
				}

			}
			if (!somethingSelected) {
				peonButton = new TextButton("Select a Unit", skin);
				helpText.setText("Welcome to SpacWars");
			}
			view.getActionWindow().add(peonButton);
			view.getActionWindow().add(helpText);

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

		// Render background first
		String backgroundString = bgManager.getBackground();
		TextureManager textureManager = (TextureManager) GameManager.get().getManager(TextureManager.class);
		Texture background = textureManager.getTexture(backgroundString);
		batch.begin();
		batch.draw(background, window.getOriginX(), window.getOriginY(), 1920, 1080);
		batch.end();

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

		gameTimeDisp.setText(" Time: " + timeManager.toString());
		gameLengthDisp.setText(timeManager.getPlayClockTime());
//		if (timeManager.isNight()){
//			gameTimeDisp.setColor(Color.FIREBRICK);
//			gameLengthDisp.setColor(Color.FIREBRICK);
//		}
//		else{
//			gameTimeDisp.setColor(Color.BLUE);
//			gameLengthDisp.setColor(Color.BLUE);
//		}

		view.render();

		/* Dispose of the spritebatch to not have memory leaks */
		Gdx.graphics.setTitle("DECO2800 " + this.getClass().getCanonicalName() +  " - FPS: "+ Gdx.graphics.getFramesPerSecond());

		stage.act();
		stage.draw();
		
		batch.dispose();
		if(!gameStarted) {
			renderMiniMap();
			GameManager.get().toggleActiveView();
			gameStarted = true;
		}
	}


	/**
	 * Handles keyboard input.
	 * There probably should be some way to pass this into another class
	 */
	private void handleInput() {
		forceMapLimits(); //intentionally put at the start to create a nice animation
		final int speed = 10;
		final int pxTolerance = 20; // modifies how close to the edge the cursor has to be before the map
		// starts moving
		int cursorX = Gdx.input.getX();
		int cursorY = Gdx.input.getY();
		int windowWidth = Gdx.graphics.getWidth();
		int windowHeight = Gdx.graphics.getHeight();
		if (downKeys.contains(Input.Keys.M)) {
			// open or close mega map
			downKeys.remove(Input.Keys.M);
			LOGGER.info("pos: " + camera.position.toString());
			GameManager.get().toggleActiveView();
		}
		if (GameManager.get().getActiveView() == 1) {
			// Don't process any inputs if in map view mode
			return;
		}
		if (downKeys.contains(Input.Keys.UP) || downKeys.contains(Input.Keys.W)) {
			camera.translate(0, 1 * speed * camera.zoom, 0);
		}
		if (downKeys.contains(Input.Keys.DOWN) || downKeys.contains(Input.Keys.S)) {
			camera.translate(0, -1 * speed * camera.zoom, 0);
		}
		if (downKeys.contains(Input.Keys.LEFT) || downKeys.contains(Input.Keys.A)) {
			camera.translate(-1 * speed * camera.zoom, 0, 0);
		}
		if (downKeys.contains(Input.Keys.RIGHT) || downKeys.contains(Input.Keys.D)) {
			camera.translate(1 * speed * camera.zoom, 0, 0);
		}
		if ((downKeys.contains(Input.Keys.EQUALS)) && (camera.zoom > 0.5)) {
			camera.zoom /= 1.05;
		}
		if ((downKeys.contains(Input.Keys.MINUS)) && (camera.zoom < 10)) {
			camera.zoom *= 1.05;
		}

		// Move the map dependant on the cursor position
		if ((cursorX > pxTolerance && cursorX + pxTolerance <= windowWidth) &&
				(cursorY > pxTolerance && cursorY + pxTolerance <= windowHeight)) {
			// skip checking for movement
			return;
		}
		// Got rid of moving the map down because it makes it difficult
		if (cursorX > windowWidth - pxTolerance) { // moving right
			if (cursorY < pxTolerance) {
				// move up and right
				camera.translate((float) 0.7071 * speed * camera.zoom, (float) 0.7071 * speed * camera.zoom, 0);
			} else {
				// move right
				camera.translate(1 * speed * camera.zoom, 0, 0);
			}
		} else if (cursorX < pxTolerance) { // moving left
			if (cursorY < pxTolerance) {
				// move up and left
				camera.translate((float) -0.7071 * speed * camera.zoom, (float) 0.7071 * speed * camera.zoom, 0);
			} else {
				// move left
				camera.translate(-1 * speed * camera.zoom, 0, 0);
			}
		} else if (cursorY < pxTolerance) {
			// move up
			camera.translate(0, 1 * speed * camera.zoom, 0);
		}
	}

	/**
	 * Called when a key has been pressed
	 * @param keycode key that was pressed
	 */
	private void keyPressed(int keycode) {
		if ((keycode == Input.Keys.ENTER) && (this.networkClient != null)) {			
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
	
	/**
	 * Helper method called whenever the camera position is moved
	 * to ensure the camera is never well of the map (in the black).
	 */
	private void forceMapLimits() {
		int mapWidth = GameManager.get().getWorld().getWidth()*58;
		int mapLength = GameManager.get().getWorld().getLength()*36;
		
		if(camera.position.x > mapWidth) {
			camera.position.x = mapWidth;
		}else if(camera.position.x < 0) {
			camera.position.x = 0;
		}
		
		if(camera.position.y > mapLength/2) {
			camera.position.y = mapLength/2;
		}else if(camera.position.y < 0-mapLength/2) {
			camera.position.y = 0-mapLength/2;
		}
	}
	
	private void renderMiniMap() {
		byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);
		Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
		BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
		PixmapIO.writePNG(Gdx.files.local("resources/HUDAssets/minimap.png"), pixmap);
		pixmap.dispose();
		view.toggleHUD();
	}

	/**
	 * Resizes the viewport.
	 *
	 * @param width the new width of the viewport.
	 * @param height the new height of the viewport.
	 */
	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();

		stage.getViewport().update(width, height, true);
		window.setPosition(300, 0);
		window.setWidth(stage.getWidth());
		view.resize(width, height);
	}

	/**
	 * Disposes of assets etc when the rendering system is stopped.
	 */
	@Override
	public void dispose () {
		// Don't need this at the moment
	}
	
	/**
	 * generates a new AI team with basic unit at a give x-y co-ord
	 * @ensure the x,y pair are within the game map
	 */
	public void setAI(int x, int y) {
		AiManagerTest aim1 = new AiManagerTest();
		GameManager.get().addManager(aim1);
		Spacman ai = new Spacman(x, y, 0);
		Spacman ai1 = new Spacman(x, y, 0);
		Base aibase = new Base(GameManager.get().getWorld(), x, y, 0);
		EnemySpacman aienemy = new EnemySpacman(x, y, 0);
		ai.setOwner(aim1);
		GameManager.get().getWorld().addEntity(ai);
		ai1.setOwner(aim1);
		GameManager.get().getWorld().addEntity(ai1);
		aibase.setOwner(aim1);
		GameManager.get().getWorld().addEntity(aibase);
		aienemy.setOwner(aim1);
		GameManager.get().getWorld().addEntity(aienemy);
	}

}
