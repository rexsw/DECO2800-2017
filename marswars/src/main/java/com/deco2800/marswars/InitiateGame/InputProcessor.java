package com.deco2800.marswars.InitiateGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.deco2800.marswars.MarsWars;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.net.MessageAction;
import com.deco2800.marswars.net.SpacClient;
import com.deco2800.marswars.net.SpacServer;

public class InputProcessor{
	
	/**
	 * Create a camera for panning and zooming.
	 * Camera must be updated every render cycle.
	 */
	
	static final int SERVER_PORT = 8080;
	SpacClient networkClient;
	SpacServer networkServer;

	OrthographicCamera camera;
	private Stage stage; 
	private Skin skin;
	private ArrayList<ArrayList<Float>> cameraPosition = new ArrayList<ArrayList<Float>>();
	private int switcher = 0;
	private int cSwitcher = 0;
	private int cameraPointer = 0;
	Set<Integer> downKeys = new HashSet<>();
	TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InputProcessor.class);

	public InputProcessor(OrthographicCamera camera, Stage stage, Skin skin){
		this.camera = camera;
		this.stage = stage;
		this.skin = skin;
	}
	
	/**
	 * Handles keyboard input.
	 */
	public void handleInput(long pauseTime) {
		final int speed = 10; //zoom speed
		final int pxTolerance = 20; // modifies how close to the edge the cursor
									//has to be before the map starts moving.
		
		int cursorX = Gdx.input.getX();
		int cursorY = Gdx.input.getY();
		
		int windowWidth = Gdx.graphics.getWidth();
		int windowHeight = Gdx.graphics.getHeight();

		long currentSeconds = timeManager.getGlobalTime();

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

		if (downKeys.contains(Input.Keys.ESCAPE)) {
			if (currentSeconds > pauseTime + 1000) {
				if (timeManager.isPaused()) {
					timeManager.unPause();
					pauseTime = timeManager.getGlobalTime();
				} else {
					LOGGER.info("PAUSING #############################");
					timeManager.pause();
					pauseTime = timeManager.getGlobalTime();
				}
			}
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
	
	public void setInputProcessor() {
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
	 * Called when a key has been pressed
	 * @param keycode key that was pressed
	 */
	public void keyPressed(int keycode) {
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
}
