package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.managers.TextureManager;

/**
 * Displays the game stats in a separate window during the 
 * game when the game is paused.
 * 
 * @author Scott Whittington 
 * @author Naziah Siddique
 *
 */
public class GameStats{
	private static final int STATSWIDTH = 600; 
	private static final int STATSHEIGHT = 400;
	private static final int BUTTONPAD = 20; 
	private static final int BUTTONSIZE = 60; 


	/*Constructors*/
	private Skin skin; 
	private Stage stage;
	private HUDView hud; 
	private TextureManager textureManager; 
	
	private Window window; 
	
	
	public GameStats(Stage stage, Skin skin, HUDView hud, TextureManager textureManager){
		this.stage = stage; 
		this.skin = skin;
		this.hud = hud; 
		this.textureManager = textureManager; 
		this.window = new Window("SPACWARS STATS", skin);
		new GameGraph(); 
	}
	
	/**
	 * Set the layout of the game stats window 
	 */
	private void setLayout(){
		Table graphTable = setGraph(); 
		graphTable.setPosition(0, STATSHEIGHT);
		window.add(graphTable);
		window.row();
		
		/*Set up the stat buttons*/
		Table statsButtons = setStatusButtons();
		statsButtons.setPosition(0, 0);
		window.add(statsButtons);
		window.row();
	}
	
	private Table setGraph(){
		Table graphTable = new Table();
		
		Label graphInfo = new Label("-Graph goes here-", skin); 
		graphTable.add(graphInfo).align(Align.center);
		
		return graphTable; 
	}
	
	private Table setStatusButtons(){
		Table pStatsTable = new Table(); //p denotes 'parent'
		pStatsTable.setDebug(true);

		//Water image button
		Texture waterImage = textureManager.getTexture("water_HUD");
		TextureRegion waterRegion = new TextureRegion(waterImage);
		TextureRegionDrawable waterRegionDraw = new TextureRegionDrawable(waterRegion);
		ImageButton waterButton = new ImageButton(waterRegionDraw);
		
		//Rock image button
		Texture rockImage = textureManager.getTexture("rock_HUD");
		TextureRegion rockRegion = new TextureRegion(rockImage);
		TextureRegionDrawable rockRegionDraw = new TextureRegionDrawable(rockRegion);
		ImageButton rockButton = new ImageButton(rockRegionDraw);
	
		
		//Biomass image button
		Texture bioImage = textureManager.getTexture("biomass_HUD");
		TextureRegion bioRegion = new TextureRegion(bioImage);
		TextureRegionDrawable bioRegionDraw = new TextureRegionDrawable(bioRegion);
		ImageButton bioButton = new ImageButton(bioRegionDraw);
		
		//Crystal image button
		Texture crystalImage = textureManager.getTexture("crystal_HUD");
		TextureRegion crystalRegion = new TextureRegion(crystalImage);
		TextureRegionDrawable crystalRegionDraw = new TextureRegionDrawable(crystalRegion);
		ImageButton crystalButton = new ImageButton(crystalRegionDraw);

		Button b5 = new TextButton("Combat Units", skin);
		Button b6 = new TextButton("Units Lost", skin);
		Button b7 = new TextButton("Buildings", skin);
		Button b8 = new TextButton("Technology", skin);


		pStatsTable.add(bioButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE);
		pStatsTable.add(crystalButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE);
		pStatsTable.add(rockButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE);
		pStatsTable.add(waterButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE).row();
		pStatsTable.add(b5).pad(BUTTONPAD);
		pStatsTable.add(b6).pad(BUTTONPAD);
		pStatsTable.add(b7).pad(BUTTONPAD);
		pStatsTable.add(b8).pad(BUTTONPAD).row();		
		
		return pStatsTable; 
	}
	
	/**
	 * Creates an exit button to close the stats window. 
	 * Unpauses the game and returns previous UI 
	 * @return exit button
	 */
	private Button getExitButton(){
		Button exitStats = new TextButton("Back to game", skin);
		exitStats.setPosition(STATSWIDTH- exitStats.getWidth(), 0);
		
		/*Closes the stats and goes back to the game*/
		exitStats.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				removeStats(); 
				//TODO unpause game
			}
		});
	
		return exitStats;
	}
	
	private Window buildStats(){
		//TODO PAUSE GAME HERE  
		hud.disableHUD();
		window.setDebug(true);
		window.setVisible(true);
		
		window.setSize(STATSWIDTH, STATSHEIGHT);
		window.setPosition((Gdx.graphics.getWidth()-STATSWIDTH)/2, (Gdx.graphics.getHeight()-STATSHEIGHT)/2);

		Label statsText = new Label("YOUR GAME ACHIEVMENTS THUS FAR", skin);
		
		window.align(Align.top | Align.left);
		
		window.add(statsText).align(Align.left | Align.top).row();
				
		setLayout();
		window.row();
		
		window.add(getExitButton()).align(Align.bottom | Align.right);
		return window; 
	}
	
	/**
	 * Displays statistics window on game screen. 
	 * In doing so, this method must also: 
	 *  - Pause the game
	 *  - Disable all other game UI 
	 */
	public void showStats(){
		stage.addActor(buildStats());
	}
	
	private void removeStats(){
		window.clear();
		window.setVisible(false);
		hud.enableHUD(); //enable all UI again 
	}
	
	
	/**
	 * Resize the in-game statistics 
	 * @param width
	 * @param height
	 */
	public void resizeStats(int width, int height) {
		window.setPosition(width/2-STATSWIDTH/2, height/2-STATSHEIGHT/2);
	}
}