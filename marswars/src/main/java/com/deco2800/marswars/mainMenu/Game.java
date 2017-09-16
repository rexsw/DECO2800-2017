package com.deco2800.marswars.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;

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