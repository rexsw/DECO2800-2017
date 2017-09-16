package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;

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
	/*Constructors*/
	private Skin skin; 
	private Stage stage;
	private HUDView hud; 
	
	private Window window; 
	
	
	public GameStats(Stage stage, Skin skin, HUDView hud){
		this.stage = stage; 
		this.skin = skin;
		this.hud = hud; 
		this.window = new Window("SPACWARS STATS", skin);
	}
	
	/**
	 * Set the layout of the game stats window 
	 */
	private Table getLayout(){
		Table pStatsTable = new Table(); //p denotes 'parent'
		pStatsTable.setDebug(true);
		
	
		
		return pStatsTable; 
	}
	
	/**
	 * Creates an exit button to close the stats window. 
	 * Unpauses the game and returns previous UI 
	 * @return exit button
	 */
	private Button getExitButton(){
		Button exitStats = new TextButton("Back to game", skin);
		
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
		window.setVisible(true);
		
		window.setSize(STATSWIDTH, STATSHEIGHT);
		window.setPosition((Gdx.graphics.getWidth()-STATSWIDTH)/2, (Gdx.graphics.getHeight()-STATSHEIGHT)/2);
		
		window.add(getExitButton()).align(Align.bottom | Align.right);
		
		window.add(getLayout());
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