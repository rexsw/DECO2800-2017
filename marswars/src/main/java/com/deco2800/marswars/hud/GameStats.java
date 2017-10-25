package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.managers.GameBlackBoard;
import com.deco2800.marswars.managers.GameBlackBoard.Field;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.managers.TimeManager;

/**
 * Displays the game stats in a separate window during the 
 * game when the game is paused.
 * 
 * @author Scott Whittington 
 * @author Naziah Siddique
 *
 */
public class GameStats{
	/*some basic constants button/window dimensions + padding*/
	private static final int BUTTONPAD = 5; 
	private static final int BUTTONSIZE = 60;
	private static final int WINDOWPAD = 10; 
	
	private static final int STATSWIDTH = (9*(BUTTONSIZE + 2 * BUTTONPAD)) + 2 * WINDOWPAD; 
	private static final int STATSHEIGHT = 300;

	/*Constructors*/
	private Skin skin; 
	private Stage stage;
	private HUDView hud; 
	private TextureManager textureManager; 
	private Window window; 
	private Field gameGraph;
	private Label graphInfo;
	private Label graphType;
	private Label graphPrompt;

	
	/* Managers */
	private TimeManager timeManager = (TimeManager) 
			GameManager.get().getManager(TimeManager.class);
	private GameBlackBoard black = (GameBlackBoard) 
			GameManager.get().getManager(GameBlackBoard.class);
	
	/* Stats window layouts */
	private Table statsButtons;
	
	private boolean enabled = false; 
	
	/**
	 * Constructs the GameStats instance. 
	 * This will trigger the building of the game stats display for the player, 
	 * including the game stats layout and the gameGraph of the first graph.  
	 * @param stage the game stage
	 * @param skin the game skin
	 * @param hud the game's hud display
	 * @param textureManager the game's texture manager
	 */
	public GameStats(Stage stage, Skin skin, HUDView hud, TextureManager textureManager){
		this.stage = stage; 
		this.gameGraph = null;
		this.skin = skin;
		this.hud = hud; 
		this.textureManager = textureManager; 
		this.window = new Window("", skin);
		window.setMovable(false);
		window.setVisible(false);
		window.setDebug(enabled);
		this.statsButtons = setStatusButtons();
	}
	
	/**
	 * Displays statistics window on game screen. 
	 * In doing so, this method must also: 
	 *  - Pause the game
	 *  - Disable all other game UI 
	 */
	public void showStats(){
		timeManager.pause();
		stage.addActor(buildStats());
		window.setVisible(true);
	}
	
	/*
	 * Remove statistics window from the game screen.
	 * In doing so, the method must also: 
	 *   - Unpause the game
	 *   - clear the window so that the stats may be by built again
	 *   - re-enable the HUD
	 */
	private void removeStats(){
		timeManager.unPause();
		hud.setPauseCheck(0);
		window.clear();
		window.setVisible(false);
		hud.enableHUD(); //enable all UI again 
	}
	
	/*
	 * Set the layout of the game stats window 
	 */
	private void setLayout(){
		graphPrompt = new Label("Please select which stat you would like to view", skin);
		graphType = new Label("", skin, "title");
		graphInfo = new Label("", skin, "subtitle");
		window.add(graphPrompt).align(Align.center).row();
		window.add(graphType).align(Align.center).expandY().row();
		window.add(graphInfo).align(Align.center).expandY().row(); //for the next entry below
				
		/*Set up the stat buttons*/
		statsButtons.setPosition(0, 0);
		window.add(statsButtons);
	}
	
	/**
	 * Creates the buttons and their respective 
	 * listeners for the game stats window
	 * @return  the stats buttons table.
	 */
	private Table setStatusButtons(){
		Table pStatsTable = new Table(); //p denotes 'parent'
		pStatsTable.setDebug(enabled);
		
		//Rock image button
		TextureRegionDrawable rockRegionDraw = 
				new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("rock_HUD")));
		ImageButton rockButton = new ImageButton(rockRegionDraw);
		
		//Biomass image button
		TextureRegionDrawable bioRegionDraw = 
				new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("biomass_HUD")));
		ImageButton bioButton = new ImageButton(bioRegionDraw);
		
		//Crystal image button
		TextureRegionDrawable crystalRegionDraw = 
				new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("crystal_HUD")));
		ImageButton crystalButton = new ImageButton(crystalRegionDraw);
		
		//Buildings button 
		TextureRegionDrawable baseRegionDraw = 
				new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("base3")));
		ImageButton baseButton = new ImageButton(baseRegionDraw);
		
		//Units Lost
		TextureRegionDrawable unitRegionDraw = 
				new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("soldier")));
		ImageButton unitButton = new ImageButton(unitRegionDraw);
		
		//Combat Units
		TextureRegionDrawable combatRegionDraw = 
				new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("tank")));
		ImageButton combatButton = new ImageButton(combatRegionDraw);
		
		//Technology
		TextureRegionDrawable techRegionDraw = 
				new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("power_gloves")));
		ImageButton techButton = new ImageButton(techRegionDraw);
		
		/*All the button listeners*/
		bioButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				gameGraph = Field.BIOMASS;
				graphPrompt.setText("");
				graphType.setText("Biomass");
				graphInfo.setText(String.format("You: %d Highest: %d", 
						black.count(-1, gameGraph), black.highCount(gameGraph)));
			}
		});
		crystalButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				gameGraph = Field.CRYSTAL;
				graphPrompt.setText("");
				graphType.setText("Crystal");
				graphInfo.setText(String.format("You: %d Highest: %d", 
						black.count(-1, gameGraph), black.highCount(gameGraph)));
			}
		});
		rockButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				gameGraph = Field.ROCKS;
				graphPrompt.setText("");
				graphType.setText("Rocks");
				graphInfo.setText(String.format("You: %d Highest: %d", 
						black.count(-1, gameGraph), black.highCount(gameGraph)));

			}
		});
		combatButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				gameGraph = Field.COMBAT_UNITS;
				graphPrompt.setText("");
				graphType.setText("Combat Units");
				graphInfo.setText(String.format("You: %d Highest: %d", 
						black.count(-1, gameGraph), black.highCount(gameGraph)));
			}
		});
		unitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				gameGraph = Field.UNITS_LOST;
				graphPrompt.setText("");
				graphType.setText("Units Lost");
				graphInfo.setText(String.format("You: %d Highest: %d", 
						black.count(-1, gameGraph), black.highCount(gameGraph)));
			}
		});
		baseButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				gameGraph = Field.BUILDINGS;
				graphPrompt.setText("");
				graphType.setText("Buildings");
				graphInfo.setText(String.format("You: %d Highest: %d", 
						black.count(-1, gameGraph), black.highCount(gameGraph)));
			}
		});
		techButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				gameGraph = Field.TECHNOLOGY;
				graphPrompt.setText("");
				graphType.setText("Technology");
				graphInfo.setText(String.format("You: %d Highest: %d", 
						black.count(-1, gameGraph), black.highCount(gameGraph)));
			}
		});	

		pStatsTable.add(bioButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE);
		pStatsTable.add(crystalButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE);
		pStatsTable.add(rockButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE);
		pStatsTable.add(baseButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE);
		pStatsTable.add(combatButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE);
		pStatsTable.add(unitButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE);
		pStatsTable.add(techButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE);
		pStatsTable.add(this.getExitButton()).pad(BUTTONPAD).size(BUTTONSIZE);
		
		return pStatsTable; 
	}
	
	/**
	 * Creates an exit button to close the stats window. 
	 * Unpauses the game and returns previous UI 
	 * @return exit button
	 */
	private Button getExitButton(){
		TextureRegionDrawable exitRegionDraw = 
				new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("back_button")));
		ImageButton exitStats = new ImageButton(exitRegionDraw);
		exitStats.pad(BUTTONPAD).setSize(40, 40);

		exitStats.setSize(BUTTONSIZE, BUTTONSIZE);
		exitStats.setPosition(STATSWIDTH- exitStats.getWidth(), 0);
		
		/*Closes the stats and goes back to the game*/
		exitStats.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				removeStats(); 
			}
		});
		exitStats.addListener(new TextTooltip("Back to game", skin));
	
		return exitStats;
	}
	
	/*
	 * Basically adds the remaining stats components to the window
	 * @return the window the stats were added to
	 */
	private Window buildStats(){
		window.setSize(STATSWIDTH, STATSHEIGHT);
		// Center 
		window.setPosition((Gdx.graphics.getWidth()-STATSWIDTH)/2, (Gdx.graphics.getHeight()-STATSHEIGHT)/2);
		window.align(Align.center);
		window.pad(WINDOWPAD);
		window.padTop(WINDOWPAD*3);
		setLayout();
		window.row();

		return window; 
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