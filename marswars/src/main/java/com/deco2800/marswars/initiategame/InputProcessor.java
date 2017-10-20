package com.deco2800.marswars.initiategame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.deco2800.marswars.functionkeys.ShortCut;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.MultiSelection;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.net.MessageAction;
import com.deco2800.marswars.net.SpacClient;
import com.deco2800.marswars.net.SpacServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Initially a part of marswars.java, this class takes care of all the 
 * in-game input.
 */
public class InputProcessor {
	/*DECLARING VARIABLES*/
	
	/*Multiplayer chat*/
	static final int SERVER_PORT = 8080;
	SpacClient networkClient;
	SpacServer networkServer;

	OrthographicCamera camera;
	private Stage stage;
	private Skin skin;
	
	/*Keyboard inputs*/
	private Set<Integer> downKeys;
	private ShortCut shortCut;
	
	/*Mixed input types*/
	private MultiSelection multiSelection; 
	
	/*Toggles*/
	private boolean multiSelectionFlag = false;

	/*Initialising through GameManager*/
	MouseHandler mouseHandler = (MouseHandler) (GameManager.get().getManager(MouseHandler.class));
	TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);

	private static final Logger LOGGER = LoggerFactory.getLogger(InputProcessor.class);

	/**
	 * Creates and sets the game's input processors
	 * @param camera
	 * @param stage
	 * @param skin
	 */
	public InputProcessor(OrthographicCamera camera, Stage stage, Skin skin) {
		this.shortCut = new ShortCut();
		this.multiSelection = new MultiSelection();
		this.downKeys = new HashSet<>();
		this.camera = camera;
		this.stage = stage;
		this.skin = skin;
	}

	/**
	 * Handles keyboard input.
	 */
	public void handleInput() {

		final int speed = 10; // zoom speed
		final int pxTolerance = 20; // modifies how close to the edge the cursor
									// has to be before the map starts moving.

		int cursorX = Gdx.input.getX();
		int cursorY = Gdx.input.getY();

		int windowWidth = Gdx.graphics.getWidth();
		int windowHeight = Gdx.graphics.getHeight();
		
		if (this.downKeys.contains(Input.Keys.M)) {
			// open or close mega map
			this.downKeys.remove(Input.Keys.M);
			LOGGER.info("pos: " + this.camera.position.toString());
			GameManager.get().toggleActiveView();
		}
		if (GameManager.get().getActiveView() == 1) {
			// Don't process any inputs if in map view mode
			return;
		}
		
		shortCut.process(camera);

		// Move the map dependent on the cursor position
		if ((cursorX > pxTolerance && cursorX + pxTolerance <= windowWidth)
				&& (cursorY > pxTolerance && cursorY + pxTolerance <= windowHeight)) {
			// skip checking for movement
			return;
		}
		// Got rid of moving the map down because it makes it difficult
		if (cursorX > windowWidth - pxTolerance) { // moving right
			if (cursorY < pxTolerance) {
				// move up and right
				this.camera.translate((float) 0.7071 * speed * this.camera.zoom,
						(float) 0.7071 * speed * this.camera.zoom, 0);
			} else {
				// move right
				this.camera.translate(1 * speed * this.camera.zoom, 0, 0);
			}
		} else if (cursorX < pxTolerance) { // moving left
			if (cursorY < pxTolerance) {
				// move up and left
				this.camera.translate((float) -0.7071 * speed * this.camera.zoom,
						(float) 0.7071 * speed * this.camera.zoom, 0);
			} else {
				// move left
				this.camera.translate(-1 * speed * this.camera.zoom, 0, 0);
			}
		} else if (cursorY < pxTolerance) {
			// move up
			this.camera.translate(0, 1 * speed * this.camera.zoom, 0);
		}
		GameManager.get().setCamera(this.camera);
	}

	/**
	 * Setup inputs for the buttons and the game itself
	 */
	public void setInputProcessor() {
		/*
		 * Setup an Input Multiplexer so that input can be handled by both the
		 * UI and the game
		 */
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(this.stage); // Add the UI as a processor

		/*
		 * Set up some input managers for panning with dragging.
		 */
		inputMultiplexer.addProcessor(new InputAdapter() {

			int originX;
			int originY;

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				if (GameManager.get().getWorld() != null && !multiSelectionFlag) {
					MultiSelection.resetSelectedTiles();
					this.originX = screenX;
					this.originY = screenY;
					// if the click is on the minimap
					if (GameManager.get().getMiniMap().clickedOn(screenX, screenY)) {
						return true;
					}

					Vector3 worldCoords = InputProcessor.this.camera.unproject(new Vector3(screenX, screenY, 0));
					mouseHandler = (MouseHandler) (GameManager.get().getManager(MouseHandler.class));
					mouseHandler.handleMouseClick(worldCoords.x, worldCoords.y, button, false);
					return true;
				}
				// nothing was done so return false
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				// this is used for multiselection
				if (multiSelectionFlag) {
					multiSelectionFlag = false;
					Vector3 worldCoords = InputProcessor.this.camera.unproject(new Vector3(screenX, screenY, 0));
					multiSelection.addEndTile(worldCoords.x, worldCoords.y);
					multiSelection.clickAllTiles();
					MultiSelection.resetSelectedTiles();
					return true;

				}
				return false;

			}
			
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				if (GameManager.get().getWorld() != null) {
					//if click and drag, multiselect things instead of moving the map
					if (!multiSelectionFlag) {
						startMultiSelect(screenX, screenY);
					}
					MultiSelection.resetSelectedTiles();
					mouseHandler.multiSelect(true);
					float tileWidth = (float) GameManager.get().getWorld().getMap().getProperties().get("tilewidth",
							Integer.class);
					float tileHeight = (float) GameManager.get().getWorld().getMap().getProperties().get("tileheight",
							Integer.class);
					Vector3 worldCoords = InputProcessor.this.camera.unproject(new Vector3(screenX, screenY, 0));
					mouseHandler.handleMouseClick(worldCoords.x, worldCoords.y, 0, false);
					float projX = worldCoords.x / tileWidth;
					float projY = -(worldCoords.y - tileHeight / 2f) / tileHeight + projX;
					projX -= projY - projX;
	
					MultiSelection.updateSelectedTiles((int) projX, (int) projY);
					return true;
				} else {
					return false;
				}
			}

			@Override
			public boolean keyDown(int keyCode) {
				if (keyCode == Input.Keys.CONTROL_LEFT || keyCode == Input.Keys.CONTROL_RIGHT) {
					mouseHandler.multiSelect(true);
				}

				InputProcessor.this.downKeys.add(keyCode);
				shortCut.addKey(keyCode);
				keyPressed(keyCode);
				return true;
			}

			@Override
			public boolean keyUp(int keyCode) {

				// disable multiSelection through touch and drag if Shift is not held
				if (keyCode == Input.Keys.SHIFT_LEFT || keyCode == Input.Keys.SHIFT_RIGHT) {
					multiSelectionFlag = false;
					mouseHandler.multiSelect(false);
				}
				if (keyCode == Input.Keys.CONTROL_LEFT || keyCode == Input.Keys.CONTROL_RIGHT) {
					mouseHandler.multiSelect(false);
				}

				InputProcessor.this.downKeys.remove(keyCode);
				shortCut.removeKey(keyCode);
				return true;
			}

			/**
			 * Enable player to zoom in and zoom out through scroll wheel
			 */
			@Override
			public boolean scrolled(int amount) {
				// if there is no world loaded, or we are currently on the megamap, cancel scroll
				if (GameManager.get().getWorld() != null || GameManager.get().getActiveView() == 1){
					int cursorX = Gdx.input.getX();
					int cursorY = Gdx.input.getY();

					int windowWidth = Gdx.graphics.getWidth();
					int windowHeight = Gdx.graphics.getHeight();

					if (InputProcessor.this.camera.zoom > 0.5 && amount == -1) { // zoom
																					// in
						// xMag/yMag is how is the mouse far from centre-screen
						// on each axis
						double xMag = (double) cursorX - (windowWidth / 2);
						double yMag = (double) (windowHeight / 2) - cursorY;

						InputProcessor.this.camera.zoom /= 1.2;
						// shift by mouse offset
						InputProcessor.this.camera.translate((float) xMag, (float) yMag);
					} else if (InputProcessor.this.camera.zoom < 10 && amount == 1) { // zoom
																						// out
						InputProcessor.this.camera.zoom *= 1.2;
					}
					forceMapLimits(); // has the user reached the edge?
					return true;
				}
				//no world loaded, or on megamap
				return false;
			}
		});

		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	/**
	 * initiates the multiselection process
	 */
	private void startMultiSelect(int screenX, int screenY) {
		multiSelectionFlag = true;
		Vector3 worldCoords = InputProcessor.this.camera.unproject(new Vector3(screenX, screenY, 0));
		multiSelection.addStartTile(worldCoords.x, worldCoords.y);
	}

	/**
	 * Called when a key has been pressed
	 * 
	 * @param keycode
	 *            key that was pressed
	 */
	public void keyPressed(int keycode) {
		if ((keycode == Input.Keys.ENTER) && (this.networkClient != null)) {
			Table inner = new Table(this.skin);
			TextField msgInput = new TextField("", this.skin);

			inner.add(msgInput);

			Dialog ipDiag = new Dialog("Message", this.skin, "dialog") {
				@Override
				protected void result(Object o) {
					if (o != null) {
						String msg = msgInput.getText();

						MessageAction action = new MessageAction(msg);
						InputProcessor.this.networkClient.sendObject(action);
					}
				}
			};

			ipDiag.getContentTable().add(inner);
			ipDiag.button("Send", true);
			ipDiag.button("Cancel", null);
			ipDiag.key(Input.Keys.ENTER, true);

			ipDiag.show(this.stage);
		}
	}

	/**
	 * Helper method called whenever the camera position is moved to ensure the
	 * camera is never well of the map (in the black).
	 */
	private void forceMapLimits() {
		// length&width of the map multiplied by the number of pixels of each
		// tile in each direction.
		int mapWidth = GameManager.get().getWorld().getWidth() * 58;
		int mapLength = GameManager.get().getWorld().getLength() * 36;

		// x axis limits
		if (this.camera.position.x > mapWidth) {
			this.camera.position.x = mapWidth;
		} else if (this.camera.position.x < 0) {
			this.camera.position.x = 0;
		}

		// y axis limits
		if (this.camera.position.y > mapLength / 2) {
			this.camera.position.y = mapLength / 2;
		} else if (this.camera.position.y < 0 - mapLength / 2) {
			this.camera.position.y = 0 - mapLength / 2;
		}
		GameManager.get().setCamera(this.camera);
	}
}
