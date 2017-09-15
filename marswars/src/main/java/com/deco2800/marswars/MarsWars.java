package com.deco2800.marswars;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.entities.buildings.Base;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.entities.units.Tank;
import com.deco2800.marswars.managers.*;
import com.deco2800.marswars.net.*;
import com.deco2800.marswars.renderers.Render3D;
import com.deco2800.marswars.renderers.Renderable;
import com.deco2800.marswars.renderers.Renderer;
import com.deco2800.marswars.hud.*;
import com.deco2800.marswars.mainMenu.MainMenu;
import com.deco2800.marswars.worlds.CustomizedWorld;
import com.deco2800.marswars.worlds.FogWorld;
import com.deco2800.marswars.worlds.map.tools.MapContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
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
	private ArrayList<ArrayList<Float>> cameraPosition = new ArrayList<ArrayList<Float>>();
	private int switcher = 0;
	private int cSwitcher = 0;
	private int cameraPointer = 0;

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
	MainMenu menu;

	Skin skin;
	
	HUDView view;

	Set<Integer> downKeys = new HashSet<>();
	TextureManager reg;
	
	
	/**
	 * Creates the required objects for the game to start.
	 * Called when the game first starts
	 */
	@Override
	public void create () {
		stage = new Stage(new ScreenViewport());
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		/*All managers */
		reg = (TextureManager)(GameManager.get().getManager(TextureManager.class));

		// zero game length clock (i.e. Tell TimeManager new game has been launched)
		timeManager.setGameStartTime();
		
		//not sure why i have to create a window here and pass it into the menu
		//but creating a window in menu crashes the game
		menu = new MainMenu(skin, stage, new Window("its a start", skin), this);
		stage.addActor(menu.buildMenu());
		
		playGame();
	}
	
	/**
	 * Constructs the rest of the game. 
	 * Note: the follow methods will be removed from marswars soon to be abstracted 
	 * into their relevant classes
	 */
	public void playGame(){
		createMiniMap();
		createMap();
		setGame();
		fogOfWar();
		addAIEntities();
		setThread();
		setGUI();
		setInputProcessor();
	}
	
	/**
	 * Creates the game minimap 
	 */
	public void createMiniMap() {
		MiniMap m = new MiniMap("minimap", 220, 220);
		m.render();
		//initialise the minimap and set the image
		GameManager.get().setMiniMap(m);
		GameManager.get().getMiniMap().updateMap(reg);
	}
	
	/**
	 * Creates game map
	 */
	private void createMap() {
		MapContainer map = new MapContainer();
		CustomizedWorld world = new CustomizedWorld(map);
		world.loadMapContainer(map);
		GameManager.get().setWorld(world);
	}
	
	/*
	 * Initializes fog of war
	 */
	private void fogOfWar() {
		FogManager fogOfWar = (FogManager)(GameManager.get().getManager(FogManager.class));
		fogOfWar.initialFog(GameManager.get().getWorld().getWidth(), GameManager.get().getWorld().getLength());
		new FogWorld(GameManager.get().getWorld().getWidth(),GameManager.get().getWorld().getLength());
	}
	
	/*
	 * adds entities for the ai and set then to be ai owned
	 */
	private void addAIEntities() {
		int length = GameManager.get().getWorld().getLength();
		int width = GameManager.get().getWorld().getWidth();
		setAI(length, width, 1);
		setPlayer(length/2, width/2, Colours.PURPLE, -1);
		GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		black.set();
	}
	
	private void setThread() {
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
	}
	
	/*
	 * Setup the game itself
	 */
	private void setGame() {
		/* Setup the camera and move it to the center of the world */
		camera = new OrthographicCamera(1920, 1080);
		GameManager.get().setCamera(camera);
		camera.translate(GameManager.get().getWorld().getWidth()*32, 0);
		GameManager.get().setCamera(camera);
	}
	
	/*
	 * Setup GUI > Refer to com.deco2800.marwars.hud for this now 
	 */
	private void setGUI() {
		/* Add another button to the menu */
		peonButton = new TextButton("Select a Unit", skin);
		helpText = new Label("Welcome to SpacWars!", skin);

		view = new com.deco2800.marswars.hud.HUDView(stage, skin, GameManager.get(), reg);
		view.getActionWindow().add(peonButton);
		view.getActionWindow().add(helpText);
		view.disableHUD();
	}
	
	private void setInputProcessor() {
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
				
				GameManager.get().setCamera(camera);

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
					//if we are currently on the megamap, cancel scroll
					return false;
				}
				
				int cursorX = Gdx.input.getX();
				int cursorY = Gdx.input.getY();
				
				int windowWidth = Gdx.graphics.getWidth();
				int windowHeight = Gdx.graphics.getHeight();
				
				if (camera.zoom > 0.5 && amount == -1) { // zoom in
					//xMag/yMag is how is the mouse far from centre-screen
					//			on each axis
					double xMag = (double)cursorX - (windowWidth/2);
					double yMag = (double)(windowHeight/2) - cursorY;
					
					camera.zoom /= 1.2;
					//shift by mouse offset
					camera.translate((float)xMag, (float)yMag);
				} else if (camera.zoom < 10 && amount == 1) { // zoom out
					camera.zoom *= 1.2;
				}
				forceMapLimits(); //has the user reached the edge?
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
		batch.draw(background, camera.position.x - camera.viewportWidth*camera.zoom/2 , camera.position.y -
				camera.viewportHeight*camera.zoom/2, camera.viewportWidth*camera.zoom,
				camera.viewportHeight*camera.zoom);
		batch.end();
		
        /* Render the tiles second */
		BatchTiledMapRenderer tileRenderer = renderer.getTileRenderer(batch);
		tileRenderer.setView(camera);
		tileRenderer.render();

		/*
         * Use the selected renderer to render objects onto the map
         */
		renderer.render(batch, camera);		

		view.render();

		/* Dispose of the spritebatch to not have memory leaks */
		Gdx.graphics.setTitle("DECO2800 " + this.getClass().getCanonicalName() +  " - FPS: "+ Gdx.graphics.getFramesPerSecond());
		stage.act();
		stage.draw();
		GameManager.get().setCamera(camera);
		batch.dispose();
		if(!gameStarted) {
			GameManager.get().getMiniMap().render();
			GameManager.get().getMiniMap().updateMap((TextureManager)(GameManager.get().getManager(TextureManager.class)));
			view.updateMiniMapMenu();
			view.enableHUD();
			GameManager.get().toggleActiveView();
			gameStarted = true;
		}
	}


	/**
	 * Handles keyboard input.
	 */
	private void handleInput() {
		forceMapLimits(); //intentionally put at the start to create a
						  //nice animation
		
		final int speed = 10; //zoom speed
		final int pxTolerance = 20; // modifies how close to the edge the cursor
									//has to be before the map starts moving.
		
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
		
		//move the map in the chosen direction
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
		if (downKeys.contains(Input.Keys.C)){
			if(cSwitcher == 0){
				ArrayList<Float> xyPosition = new ArrayList<Float>();
				xyPosition.add(camera.position.x);
				xyPosition.add(camera.position.y);
				cameraPosition.add(xyPosition);
				cSwitcher++;
			}
		}else{
			cSwitcher = 0;
		}
		
		if(downKeys.contains(Input.Keys.N) && !cameraPosition.isEmpty()){
			ArrayList<Float> nextPosition = cameraPosition.get(cameraPointer);
			if(switcher == 0){
				float x= camera.position.x - nextPosition.get(0);
				float y = camera.position.y - nextPosition.get(1);
				x *= -1;
				y *= -1;
				if(camera.position.x > nextPosition.get(0) || (camera.position.x <= nextPosition.get(0)&& camera.position.x >0)){
					camera.translate(x, 0);
				}
				if(camera.position.y > nextPosition.get(1) || 
						(camera.position.y <= nextPosition.get(1))){
					camera.translate(0, y);
				}
				switcher++;
				cameraPointer++;
				cameraPointer = cameraPointer % cameraPosition.size();
			}	
		}else{
			switcher = 0;
		}

		// Move the map dependent on the cursor position
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
		GameManager.get().setCamera(camera);
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
		//length&width of the map multiplied by the number of pixels of each
		//tile in each direction.
		int mapWidth = GameManager.get().getWorld().getWidth()*58;
		int mapLength = GameManager.get().getWorld().getLength()*36;
		
		//x axis limits
		if(camera.position.x > mapWidth) {
			camera.position.x = mapWidth;
		}else if(camera.position.x < 0) {
			camera.position.x = 0;
		}
		
		//y axis limits
		if(camera.position.y > mapLength/2) {
			camera.position.y = mapLength/2;
		}else if(camera.position.y < 0-mapLength/2) {
			camera.position.y = 0-mapLength/2;
		}
		GameManager.get().setCamera(camera);
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
		GameManager.get().setCamera(camera);
		stage.getViewport().update(width, height, true);
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
	public void setAI(int lenght, int width, int teams) {
		ArrayList<Colours> colour = new ArrayList<Colours>();
		int x,y;
		colour.add(Colours.BLUE);
		colour.add(Colours.YELLOW);
		colour.add(Colours.PINK);
		colour.add(Colours.GREEN);
		ColourManager cm = (ColourManager) GameManager.get().getManager(ColourManager.class);
		ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		for(int teamid = 1; teamid < teams+1;teamid++) {
			x = ThreadLocalRandom.current().nextInt(1, lenght);
			y = ThreadLocalRandom.current().nextInt(1, width);
			rm.setBiomass(0, teamid);
			rm.setRocks(0, teamid);
			rm.setCrystal(0, teamid);
			rm.setWater(0, teamid);
			cm.setColour(teamid, colour.get(teamid-1));
			AiManager aim = (AiManager) GameManager.get().getManager(AiManager.class);
			aim.addTeam(teamid);
			Astronaut ai = new Astronaut(x, y, 0, teamid);
			Astronaut ai1 = new Astronaut(x, y, 0, teamid);
			Base aibase = new Base(GameManager.get().getWorld(), x, y, 0);
			Soldier soldier = new Soldier(x, y,0,teamid);
			GameManager.get().getWorld().addEntity(soldier);
			Tank tank = new Tank(x,y,0,teamid);
			GameManager.get().getWorld().addEntity(tank);
			GameManager.get().getWorld().addEntity(ai);
			GameManager.get().getWorld().addEntity(ai1);
			aibase.setOwner(teamid);
			GameManager.get().getWorld().addEntity(aibase);
		}
	}

	public void setPlayer(int x, int y, Colours colour, int teamid) {
		ColourManager cm = (ColourManager) GameManager.get().getManager(ColourManager.class);
		cm.setColour(teamid, colour);
		ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		rm.setBiomass(0, teamid);
		rm.setRocks(0, teamid);
		rm.setCrystal(0, teamid);
		rm.setWater(0, teamid);
		Spacman p = new Spacman(x, y, 0);
		Astronaut p1 = new Astronaut(x, y, 0, teamid);
		Base p2 = new Base(GameManager.get().getWorld(), x, y, 0);
		Soldier soldier = new Soldier(x, y,0,teamid);
		GameManager.get().getWorld().addEntity(soldier);
		Tank tank = new Tank(x,y,0,teamid);
		GameManager.get().getWorld().addEntity(tank);
		p.setOwner(teamid);
		GameManager.get().getWorld().addEntity(p);
		p1.setOwner(teamid);
		GameManager.get().getWorld().addEntity(p1);
		p2.setOwner(teamid);
		GameManager.get().getWorld().addEntity(p2);
		
		// test hero
		Commander hero = new Commander(x,y,0,teamid);
		GameManager.get().getWorld().addEntity(hero);
	}
}
