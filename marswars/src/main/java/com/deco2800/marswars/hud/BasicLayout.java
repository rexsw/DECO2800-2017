package com.deco2800.marswars.hud;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Naziah Siddique on 19/08
 * Initiates the HUD for SpacWars, with the help of instantiations of 
 * other components from other classes in the packages
 */

public class BasicLayout extends ApplicationAdapter{
	Label test;
	Label test1;
	private Stage stage;
	private Skin skin;
	private Table overheadLeft;
	private Table overheadRight;
	SpriteBatch batch; 
	Texture img;
	Button quitButton;
	
	public BasicLayout(Stage stage, Skin skin) {
		this.skin = skin;
		this.stage = stage;
		
		createLayout();
	}

	/**
	 * Helper method for the basic layout 
	 */
	private void createLayout(){
		topLeft();
		topRight();
	}
	
	
	/**
	 * Contains the HUD the top right section of the screen. 
	 * This includes the message tab, help button and quit button
	 */
	private void topRight(){
		
		overheadRight = new Table();
		overheadRight.setWidth(stage.getWidth());
		overheadRight.align(Align.right | Align.top);
		overheadRight.setPosition(0, Gdx.graphics.getHeight());
		
		test1 = new Label("Right of the screen", skin);
		Label messageBox = new Label("Chat Lobby", skin);
		Button helpButton = new TextButton("?", skin);
		Button quitButton = new TextButton("X", skin);
		overheadRight.add(messageBox);
		overheadRight.add(helpButton);
		overheadRight.add(quitButton);
				
		stage.addActor(overheadRight);
		
		//can we make this a method of it's own? 
		quitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.exit(0);
			}
		});

	}
	
	
	/**
	 * Adds in the top left section of the HUD. This includes the  
	 */
	private void topLeft(){
		
		//Adds in the container managing the health status + player deets 
		overheadLeft = new Table();
		overheadLeft.setDebug(true);
		overheadLeft.setWidth(stage.getWidth());
		overheadLeft.align(Align.left | Align.top);
		overheadLeft.setPosition(0, Gdx.graphics.getHeight());
		
		test = new Label("Health bar goes here", skin);
		Label healthLabel = new Label("Health: ", skin);
		healthLabel.setAlignment(Align.left);
		
		
		overheadLeft.add(test);
		//overheadLeft.add(healthBar);
		
		overheadLeft.row();
		overheadLeft.add(healthLabel);

		
		stage.addActor(overheadLeft);
	}
	
	
	/**
	 * HELPER METHOD: 
	 * Adds the player Icon and name to the huD (goes into top left) 
	 */
	private void addPlayerIcon(){
		Table playerdetails = new Table();
		playerdetails.setDebug(true);
		
		//add 
		
		
	}
	
	/**
	 * Adds in progress bar to the top left 
	 */
	private void addProgressBar(){
		//I feel the progress bar needs a class of itself
		;
		
	}
	
	
	/**
	 * Implements a collapsible tab for the chat lobby 
	 */
	private void addMessages(){
		;
		
	}
	
	
	/**
	 * Adds in the selectabl menu for the 
	 */
	private void addInventoryMenu(){
		
		
	}
	
	/**
	 * Adds in in the resources count 
	 */
	private void addResources(){
		
		
	}
	
	
	/**
     * Add handler to the quit button
     * @param handler
     * 		the handler to be added 
     */
    void addQuitListener(){
		quitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.exit(0);
			}
		});
		
    }

}



