package com.deco2800.marswars.InitiateGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.deco2800.marswars.managers.MultiSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.net.MessageAction;
import com.deco2800.marswars.net.SpacClient;
import com.deco2800.marswars.net.SpacServer;

public class InputProcessor {

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

	private boolean multiSelectionFlag = false;
	private MultiSelection multiSelection = new MultiSelection();

	MouseHandler mouseHandler = (MouseHandler) (GameManager.get().getManager(MouseHandler.class));

	private static final Logger LOGGER = LoggerFactory.getLogger(InputProcessor.class);

	public InputProcessor(OrthographicCamera camera, Stage stage, Skin skin) {
		this.camera = camera;
		this.stage = stage;
		this.skin = skin;
	}

	/**
	 * Handles keyboard input.
	 */
	public void handleInput(long pauseTime) {

		final int speed = 10; // zoom speed
		final int pxTolerance = 20; // modifies how close to the edge the cursor
									// has to be before the map starts moving.

		int cursorX = Gdx.input.getX();
		int cursorY = Gdx.input.getY();

		int windowWidth = Gdx.graphics.getWidth();
		int windowHeight = Gdx.graphics.getHeight();

		long currentSeconds = this.timeManager.getGlobalTime();

		if (this.downKeys.contains(Input.Keys.M)) {
			// open or close mega map
			this.downKeys.remove(Input.Keys.M);
			LOGGER.info("pos: " + this.camera.position.toString()); //$NON-NLS-1$
			GameManager.get().toggleActiveView();
		}
		if (GameManager.get().getActiveView() == 1) {
			// Don't process any inputs if in map view mode
			return;
		}

		// move the map in the chosen direction
		if (this.downKeys.contains(Input.Keys.UP) || this.downKeys.contains(Input.Keys.W)) {
			this.camera.translate(0, 1 * speed * this.camera.zoom, 0);
		}
		if (this.downKeys.contains(Input.Keys.DOWN) || this.downKeys.contains(Input.Keys.S)) {
			this.camera.translate(0, -1 * speed * this.camera.zoom, 0);
		}
		if (this.downKeys.contains(Input.Keys.LEFT) || this.downKeys.contains(Input.Keys.A)) {
			this.camera.translate(-1 * speed * this.camera.zoom, 0, 0);
		}
		if (this.downKeys.contains(Input.Keys.RIGHT) || this.downKeys.contains(Input.Keys.D)) {
			this.camera.translate(1 * speed * this.camera.zoom, 0, 0);
		}
		if ((this.downKeys.contains(Input.Keys.EQUALS)) && (this.camera.zoom > 0.5)) {
			this.camera.zoom /= 1.05;
		}
		if ((this.downKeys.contains(Input.Keys.MINUS)) && (this.camera.zoom < 10)) {
			this.camera.zoom *= 1.05;
		}

		if (this.downKeys.contains(Input.Keys.C)) {
			if (this.cSwitcher == 0) {
				ArrayList<Float> xyPosition = new ArrayList<Float>();
				xyPosition.add(this.camera.position.x);
				xyPosition.add(this.camera.position.y);
				this.cameraPosition.add(xyPosition);
				this.cSwitcher++;
			}
		} else {
			this.cSwitcher = 0;
		}

		if (this.downKeys.contains(Input.Keys.N) && !this.cameraPosition.isEmpty()) {
			ArrayList<Float> nextPosition = this.cameraPosition.get(this.cameraPointer);
			if (this.switcher == 0) {
				float x = this.camera.position.x - nextPosition.get(0);
				float y = this.camera.position.y - nextPosition.get(1);
				x *= -1;
				y *= -1;
				if (this.camera.position.x > nextPosition.get(0)
						|| (this.camera.position.x <= nextPosition.get(0) && this.camera.position.x > 0)) {
					this.camera.translate(x, 0);
				}
				if (this.camera.position.y > nextPosition.get(1) || (this.camera.position.y <= nextPosition.get(1))) {
					this.camera.translate(0, y);
				}
				this.switcher++;
				this.cameraPointer++;
				this.cameraPointer = this.cameraPointer % this.cameraPosition.size();
			}
		} else {
			this.switcher = 0;
		}

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

	public void setInputProcessor() {
		/*
		 * Setup inputs for the buttons and the game itself
		 */
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
				if (!multiSelectionFlag && GameManager.get().getWorld() != null) {
					MultiSelection.resetSelectedTiles();
					if (GameManager.get().getActiveView() == 1) {
						InputProcessor.this.camera.position.set(InputProcessor.this.camera
								.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)));
						InputProcessor.this.camera.zoom = 1;
						GameManager.get().toggleActiveView();
					} else {
						this.originX = screenX;
						this.originY = screenY;
						// if the click is on the minimap
						if (GameManager.get().getMiniMap().clickedOn(screenX, screenY)) {
							return true;
						}

						Vector3 worldCoords = InputProcessor.this.camera.unproject(new Vector3(screenX, screenY, 0));
						MouseHandler mouseHandler = (MouseHandler) (GameManager.get().getManager(MouseHandler.class));
						mouseHandler.handleMouseClick(worldCoords.x, worldCoords.y, button, false);
					}

				} else {
					// TODO add button 0 and call the function to handle mouse
					// click
					Vector3 worldCoords = InputProcessor.this.camera.unproject(new Vector3(screenX, screenY, 0));
					multiSelection.addStartTile(worldCoords.x, worldCoords.y);

					return true;
				}
				return true;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {// TODO
																						// add
																						// button
																						// 0
																						// and
																						// call
																						// the
																						// function
																						// to
																						// handle
																						// mouse
																						// click
				// this is used for multiselection
				if (multiSelectionFlag) {
					Vector3 worldCoords = InputProcessor.this.camera.unproject(new Vector3(screenX, screenY, 0));
					multiSelection.addEndTile(worldCoords.x, worldCoords.y);
					multiSelection.clickAllTiles();
					MultiSelection.resetSelectedTiles();
					return true;

				}
				return true;

			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				if (multiSelectionFlag) {
					MultiSelection.resetSelectedTiles();
					float tileWidth = (float) GameManager.get().getWorld().getMap().getProperties().get("tilewidth",
							Integer.class);
					float tileHeight = (float) GameManager.get().getWorld().getMap().getProperties().get("tileheight",
							Integer.class);
					Vector3 worldCoords = InputProcessor.this.camera.unproject(new Vector3(screenX, screenY, 0));
					MouseHandler mouseHandler = (MouseHandler) (GameManager.get().getManager(MouseHandler.class));
					mouseHandler.handleMouseClick(worldCoords.x, worldCoords.y, 0, false);
					float projX = worldCoords.x / tileWidth;
					float projY = -(worldCoords.y - tileHeight / 2f) / tileHeight + projX;
					projX -= projY - projX;

					MultiSelection.updateSelectedTiles((int) projX, (int) projY);
					return true;
				}

				this.originX -= screenX;
				this.originY -= screenY;

				// invert the y axis
				this.originY = -this.originY;

				this.originX += InputProcessor.this.camera.position.x;
				this.originY += InputProcessor.this.camera.position.y;

				InputProcessor.this.camera.translate(this.originX - InputProcessor.this.camera.position.x,
						this.originY - InputProcessor.this.camera.position.y);

				this.originX = screenX;
				this.originY = screenY;

				GameManager.get().setCamera(InputProcessor.this.camera);

				return true;

			}

			@Override
			public boolean keyDown(int keyCode) {

				// enable multiSelection through touch and drag
				if (keyCode == Input.Keys.SHIFT_LEFT || keyCode == Input.Keys.SHIFT_RIGHT) {
					multiSelectionFlag = true;
					MultiSelection.resetSelectedTiles();
				}

				InputProcessor.this.downKeys.add(keyCode);
				keyPressed(keyCode);
				return true;
			}

			@Override
			public boolean keyUp(int keyCode) {

				// disable multiSelection through touch and drag
				if (keyCode == Input.Keys.SHIFT_LEFT || keyCode == Input.Keys.SHIFT_RIGHT)
					multiSelectionFlag = false;

				InputProcessor.this.downKeys.remove(keyCode);

				return true;
			}

			/**
			 * Enable player to zoom in and zoom out through scroll wheel
			 */
			@Override
			public boolean scrolled(int amount) {
				if (GameManager.get().getActiveView() == 1) {
					// if we are currently on the megamap, cancel scroll
					return false;
				}

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
		});

		Gdx.input.setInputProcessor(inputMultiplexer);
		if (GameManager.get().getWorld() != null) {
			GameManager.get().toggleActiveView();
		}
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
			TextField msgInput = new TextField("", this.skin); //$NON-NLS-1$

			inner.add(msgInput);

			Dialog ipDiag = new Dialog("Message", this.skin, "dialog") { //$NON-NLS-1$ //$NON-NLS-2$
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
			ipDiag.button("Send", true); //$NON-NLS-1$
			ipDiag.button("Cancel", null); //$NON-NLS-1$
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
