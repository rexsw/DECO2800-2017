package com.deco2800.marswars.mainMenu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * Manages the features for the game 
 * @author cherr
 *
 */
public class Game{
	private Stage stage; 
	private Window window; 
	private OrthographicCamera camera; 
	
	
	public Game(Stage stage, Window window, OrthographicCamera camera){
		this.stage = stage; 
		this.window = window; 
		this.camera = camera; 
	}
	
	
}