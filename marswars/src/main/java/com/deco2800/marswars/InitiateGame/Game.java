package com.deco2800.marswars.InitiateGame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.deco2800.marswars.hud.HUDView;
import com.deco2800.marswars.hud.MiniMap;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;

/**
 * Manages the features for the game 
 * @author cherr
 *
 */
public class Game{
	private Stage stage; 
	private HUDView view; 
	private Skin skin; 
	private TextureManager reg; 
	private InputProcessor inputP;
	private OrthographicCamera camera; 
	
	long lastGameTick = 0;
	long lastMenuTick = 0;
	long pauseTime = 0;

	
	public Game(Skin skin, Stage stage, OrthographicCamera camera){
		this.stage = stage; 
		this.skin = skin; 
		this.camera = camera;
		this.reg = (TextureManager)(GameManager.get().getManager(TextureManager.class));
		startGame();

	}
	
	private void startGame(){
		//inputP = new InputProcessor(this.camera, this.stage, this.skin);
		//inputP.setInputProcessor();
		createMiniMap();
		setGUI();	
	}
	
	/*
	 * Setup GUI > Refer to com.deco2800.marwars.hud for this now 
	 */
	private void setGUI() {
		/* Add another button to the menu */
		this.view = new com.deco2800.marswars.hud.HUDView(this.stage, this.skin, GameManager.get(), this.reg);
		this.view.disableHUD();
	}
	
	/**
	 * Creates the game minimap 
	 */
	private void createMiniMap() {
		MiniMap m = new MiniMap("minimap", 220, 220); //$NON-NLS-1$
		m.render();
		//initialise the minimap and set the image
		GameManager.get().setMiniMap(m);
		GameManager.get().getMiniMap().updateMap(this.reg);
	}

	/**
	 * Can assume that since this class has been instantiated
	 * that the game is in full play
	 */
	public void render(){
		GameManager.get().getMiniMap().render();
		GameManager.get().getMiniMap().updateMap((TextureManager)(GameManager.get().getManager(TextureManager.class)));
		this.view.updateMiniMapMenu();
		this.view.enableHUD();
		GameManager.get().toggleActiveView();
	}
	
	public void resize(int width, int height){
		this.view.resize(width, height);
	}
	
}