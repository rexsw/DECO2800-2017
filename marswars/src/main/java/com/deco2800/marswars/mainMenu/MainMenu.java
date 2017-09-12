package com.deco2800.marswars.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.MarsWars;
import com.deco2800.marswars.net.LobbyButton;


/**
 * Creates the Main Menu stage 
 * @author Toby
 *
 */
public class MainMenu {
	private Skin skin;
	private MarsWars marswars;
	private LobbyButton lobby; 
	
	TextButton newGameButton; //starts the new game
	TextButton exitButton;  //exits the game when pressed
	TextButton startServer; 
	TextButton joinServer; // will allow the player to join a multiplayer game
	TextButton playButton; 
	
	Window mainmenu; 
	Label title;
	boolean status = true;

	/**
	 * Creates the initial Main Menu instance before starting the game
	 * @param skin
	 * @param stage
	 * @param window
	 * @param marswars
	 */
	public MainMenu(Skin skin, Stage stage, Window window, MarsWars marswars) {
		this.skin = skin;
		this.marswars = marswars;
		mainmenu = window; 
        lobby = new LobbyButton(skin, stage);
		createMenu();
    }

	/**
	 * Does all the grunt work for creating the main menu
	 */
	private void createMenu(){
		mainmenu.setSize(300, 300);
		mainmenu.setPosition(Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth()/2, Align.center);
				
		title = new Label("SpacWars", skin);
	    title.setPosition(600, 600);
	    
	    newGameButton = new TextButton("New game", skin);
	    newGameButton.setPosition(600, 400);
	    
	    playButton = new TextButton("Play!", skin);
	    
	    exitButton = new TextButton("Exit", skin);
	    exitButton.setPosition(600, 100);
	    	    
	    mainmenu.add(title);
	    mainmenu.add(newGameButton);
	    mainmenu.add(playButton);
	    mainmenu.add(exitButton);
	    mainmenu.add(lobby.addStartServerButton());
	    mainmenu.add(lobby.addJoinServerButton());
	    mainmenu.pack();
	    
	    newGameButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				status = true;
			}
		});
	    
	    exitButton.addListener(new ChangeListener() {
	    	@Override
	    	public void changed(ChangeEvent event, Actor actor) {
	    		status = false;
	    		System.exit(0);
	    	}
	    });   
	}
	
	/**
	 * Adds in the chat server buttons to the main menu
	 */
	public void addLobbyButton(){
	    mainmenu.add(lobby.addStartServerButton());
	    mainmenu.add(lobby.addJoinServerButton());
	    mainmenu.pack();
	}
	
	public Window buildMenu(){
		return mainmenu;
	}    
}