package com.deco2800.marswars.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.MarsWars;
import com.deco2800.marswars.hud.ExitGame;
import com.deco2800.marswars.net.LobbyButton;


/**
 * Creates the Main Menu stage 
 * @author Toby
 *
 */
public class MainMenu {
	private static final int MENUHEIGHT = 300; 
	private static final int MENUWIDTH = 300;
	private Skin skin;
	private MarsWars marswars;
	private Stage stage; 
	private LobbyButton lobby; 
	
	TextButton newGameButton; //starts the new game
	TextButton exitButton;  //exits the game when pressed
	TextButton startServer; 
	TextButton joinServer; // will allow the player to join a multiplayer game
	TextButton playButton; 
	
	Window mainmenu; 
	Label title;
	boolean status = true;
	private Table menuTable; 

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
		this.stage = stage; 
		mainmenu = window; 
        lobby = new LobbyButton(skin, stage);
		createMenu();
    }

	/**
	 * Does all the grunt work for creating the main menu
	 */
	private void createMenu(){
		mainmenu.setSize(MENUWIDTH, MENUHEIGHT);
		menuTable = new Table(); 
		
		title = new Label("SpacWars", skin);	    
	    playButton = new TextButton("Play!", skin);
	    exitButton = new TextButton("Exit", skin);
	    	    
	    menuTable.add(title);
	    menuTable.row(); 
	    menuTable.add(playButton);
	    menuTable.add(exitButton);
	    menuTable.add(lobby.addStartServerButton());
	    menuTable.add(lobby.addJoinServerButton());
	    menuTable.pack(); 
	    
	    mainmenu.add(menuTable);
	    
	    playButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				status = true;
				mainmenu.setVisible(false);
			}
		});
	    
	    exitButton.addListener(new ChangeListener() {
	    	@Override
	    	public void changed(ChangeEvent event, Actor actor) {
	    		status = false;
	    		new ExitGame("Quit Game", skin).show(stage);
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
	
	public void resize(int width, int height) {
		mainmenu.setPosition(width/2-MENUWIDTH/2, height/2-MENUHEIGHT/2);
	}
}