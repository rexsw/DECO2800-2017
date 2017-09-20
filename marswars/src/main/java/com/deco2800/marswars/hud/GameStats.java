package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Interpolation.Bounce;
import com.badlogic.gdx.math.Polyline;
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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.managers.GameBlackBoard;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.managers.GameBlackBoard.Field;

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
	private static final int BUTTONPAD = 10; 
	private static final int BUTTONSIZE = 60; 

	/*Constructors*/
	private Skin skin; 
	private Stage stage;
	private HUDView hud; 
	private TextureManager textureManager; 
	
	private Window window; 
	private GameGraph gameGraph;
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	private GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
	
	
	public GameStats(Stage stage, Skin skin, HUDView hud, TextureManager textureManager){
		this.stage = stage; 
		this.skin = skin;
		this.hud = hud; 
		this.textureManager = textureManager; 
		this.window = new Window("SPACWARS STATS", skin); //$NON-NLS-1$
		window.setMovable(false);
		window.setVisible(false);
		gameGraph = new GameGraph(null); 
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
	
	/**
	 * Remove statistics window from the game screen.
	 * In doing so, the method must also: 
	 *   - Unpause the game
	 *   - clear the window so that the stats mayb by built again
	 *   - re-enable the HUD
	 */
	private void removeStats(){
		timeManager.unPause();
		hud.setPauseCheck(0);
		window.clear();
		window.setVisible(false);
		hud.enableHUD(); //enable all UI again 
	}
	
	/**
	 * Set the layout of the game stats window 
	 */
	private void setLayout(){
		Table graphTable = setGraph(); 
		graphTable.setPosition(20, STATSHEIGHT);
		window.add(graphTable);
		window.row();
		
		/*Set up the stat buttons*/
		Table statsButtons = setStatusButtons();
		statsButtons.setPosition(0, 0);
		window.add(statsButtons);
	}
	
	/**
	 * Display the graph of the statistics window
	 * @return
	 */
	private Table setGraph(){
		Table graphTable = new Table();
		Label graphInfo = new Label("-Graph goes here-", skin);  //$NON-NLS-1$
		graphTable.add(graphInfo).align(Align.center);
		
		return graphTable; 
	}
	
	private Table setStatusButtons(){
		Table pStatsTable = new Table(); //p denotes 'parent'
		pStatsTable.setDebug(true);

		//Water image button
		Texture waterImage = textureManager.getTexture("water_HUD"); //$NON-NLS-1$
		TextureRegion waterRegion = new TextureRegion(waterImage);
		TextureRegionDrawable waterRegionDraw = new TextureRegionDrawable(waterRegion);
		ImageButton waterButton = new ImageButton(waterRegionDraw);
		
		//Rock image button
		Texture rockImage = textureManager.getTexture("rock_HUD"); //$NON-NLS-1$
		TextureRegion rockRegion = new TextureRegion(rockImage);
		TextureRegionDrawable rockRegionDraw = new TextureRegionDrawable(rockRegion);
		ImageButton rockButton = new ImageButton(rockRegionDraw);
		
		//Biomass image button
		Texture bioImage = textureManager.getTexture("biomass_HUD"); //$NON-NLS-1$
		TextureRegion bioRegion = new TextureRegion(bioImage);
		TextureRegionDrawable bioRegionDraw = new TextureRegionDrawable(bioRegion);
		ImageButton bioButton = new ImageButton(bioRegionDraw);
		
		//Crystal image button
		Texture crystalImage = textureManager.getTexture("crystal_HUD"); //$NON-NLS-1$
		TextureRegion crystalRegion = new TextureRegion(crystalImage);
		TextureRegionDrawable crystalRegionDraw = new TextureRegionDrawable(crystalRegion);
		ImageButton crystalButton = new ImageButton(crystalRegionDraw);
		
		//Buildings button 
		Texture baseImage = textureManager.getTexture("base3"); //$NON-NLS-1$
		TextureRegion baseRegion = new TextureRegion(baseImage);
		TextureRegionDrawable baseRegionDraw = new TextureRegionDrawable(baseRegion);
		ImageButton baseButton = new ImageButton(baseRegionDraw);
		
		//Units Lost
		Texture unitImage = textureManager.getTexture("soldier"); //$NON-NLS-1$
		TextureRegion unitRegion = new TextureRegion(unitImage);
		TextureRegionDrawable unitRegionDraw = new TextureRegionDrawable(unitRegion);
		ImageButton unitButton = new ImageButton(unitRegionDraw);
		
		//Combat Units
		Texture combatImage = textureManager.getTexture("tank"); //$NON-NLS-1$
		TextureRegion combatRegion = new TextureRegion(combatImage);
		TextureRegionDrawable combatRegionDraw = new TextureRegionDrawable(combatRegion);
		ImageButton combatButton = new ImageButton(combatRegionDraw);
		
		//Technology
		Texture techImage = textureManager.getTexture("power_gloves"); //$NON-NLS-1$
		TextureRegion techRegion = new TextureRegion(techImage);
		TextureRegionDrawable techRegionDraw = new TextureRegionDrawable(techRegion);
		ImageButton techButton = new ImageButton(techRegionDraw);

		bioButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				gameGraph = new GameGraph(Field.BIOMASS);
			}
		});
		crystalButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				gameGraph = new GameGraph(Field.CRYSTAL);
			}
		});
		rockButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				gameGraph = new GameGraph(Field.ROCKS);
			}
		});
		waterButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				gameGraph = new GameGraph(Field.WATER);
			}
		});
		combatButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				gameGraph = new GameGraph(Field.COMBAT_UNITS);
			}
		});
		unitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				gameGraph = new GameGraph(Field.UNITS_LOST);
			}
		});
		baseButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				gameGraph = new GameGraph(Field.BUILDINGS);
			}
		});
		b8.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				gameGraph = new GameGraph(Field.TECHNOLOGY);
			}
		});	
		techButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				//gameGraph = new GameGraph("Buildings");
			}
		});	

		pStatsTable.add(bioButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE);
		pStatsTable.add(crystalButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE);
		pStatsTable.add(rockButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE);
		pStatsTable.add(waterButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE).row();
		pStatsTable.add(baseButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE);
		pStatsTable.add(combatButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE);
		pStatsTable.add(unitButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE);
		pStatsTable.add(techButton).pad(BUTTONPAD).size(BUTTONSIZE, BUTTONSIZE).row();		
		return pStatsTable; 
	}
	
	/**
	 * Creates an exit button to close the stats window. 
	 * Unpauses the game and returns previous UI 
	 * @return exit button
	 */
	private Button getExitButton(){
		Button exitStats = new TextButton("Back to game", skin); //$NON-NLS-1$
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
		window.setSize(STATSWIDTH, STATSHEIGHT);
		window.setPosition((Gdx.graphics.getWidth()-STATSWIDTH)/2, (Gdx.graphics.getHeight()-STATSHEIGHT)/2);
		window.align(Align.center);

		Label statsText = new Label("YOUR GAME ACHIEVMENTS THUS FAR", skin); //$NON-NLS-1$
		window.add(statsText).align(Align.left | Align.top).row();
				
		setLayout();
		window.row();
		
		window.add(getExitButton()).align(Align.bottom | Align.right);
		

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

	public void render() {
		if (window.isVisible()){
			gameGraph.render();
		}
	}
	
}