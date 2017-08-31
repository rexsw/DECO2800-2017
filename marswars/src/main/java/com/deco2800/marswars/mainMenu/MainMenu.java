package com.deco2800.marswars.mainMenu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * 
 * @author Toby
 *
 */
public class MainMenu {
	
	private Stage stage; 
	private Skin skin;
	
	TextButton newGameButton; //starts the new game
	TextButton exitButton;  //exits the game when pressed
	TextButton startServer; 
	TextButton joinServer; // will allow the player to join a multiplayer game
	
	Label title;
	
	public void mainMenu() {
        this.skin = skin;
        this.stage = stage;
        createMenu();
	}
	
	private void createMenu(){
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

	    createSkin();
	    
	    title = new Label("SpacWars", skin);
	    title.setPosition(600, 600);
	    stage.addActor(title);
	    
	    newGameButton = new TextButton("New game", skin);
	    newGameButton.setPosition(600, 400);
	    stage.addActor(newGameButton);
	    
	    exitButton = new TextButton("Exit", skin);
	    exitButton.setPosition(600, 100);
	    stage.addActor(exitButton);
	    
	    startServer = new TextButton("Start Server", skin);
	    startServer.setPosition(600, 300);
	    stage.addActor(startServer);
	    
	    joinServer = new TextButton("Join Server", skin);
	    joinServer.setPosition(600, 200);
	    
	    
	    
	    newGameButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//currently not implemented
			}
		});
	    
	    exitButton.addListener(new ChangeListener() {
	    	@Override
	    	public void changed(ChangeEvent event, Actor actor) {
	    		System.exit(0);
	    	}
	    });
	    
	    startServer.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
			}
	    });
	    
	    joinServer.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
			}
	    });
	}
    

	private void createSkin(){
	  //creates a font to be used in the menu
	  BitmapFont font = new BitmapFont();
	  skin = new Skin();
	  skin.add("default", font);
	  
	  //Sets the background colour for the screen
	  Pixmap pixmap = new Pixmap((int)Gdx.graphics.getWidth()/4,(int)Gdx.graphics.getHeight()/10, Pixmap.Format.RGB888);
	  pixmap.setColor(Color.BLUE);
	  pixmap.fill();
	  skin.add("background",new Texture(pixmap));
	  
	  //Sets the button style for the main menu
	  TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
	  textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
	  textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
	  textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
	  textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
	  textButtonStyle.font = skin.getFont("default");
	  skin.add("default", textButtonStyle);
	 
	}

	public void render() {
        Gdx.gl.glClearColor(0, 0, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
 
        stage.act();
        stage.draw();
    }
}





