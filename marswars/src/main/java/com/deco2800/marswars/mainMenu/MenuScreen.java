package com.deco2800.marswars.mainMenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.hud.ExitGame;

/**
 * @author Naziah Siddique
 * Flow process for the main menu
 * 
 * Single Player: 
 *     select world > select character > select combat
 *     
 * Multiplayer: 
 * 		join server > select character 
 * 
 * Multiplayer: 
 * 		start server > select world > select character > select combat 
 */
public class MenuScreen{
	
	enum ScreenMode{
		SERVERMODE,     // select choose server or join server, go back to playerMode 
		COMBATMODE,     // chose combat, go back to select character 
		WORLDMODE,      // choosing the world, go back to select playerMode/start server
		CHARACTERMODE;  // choosing character, goes back to select world
	}
	
	private Skin skin; 
	private LobbyButton lobby; 
	
	private Button backButton; 
	private Button nextButton; 
	private int playerType; 
	private int joinedServer; 
	
	public MenuScreen(Skin skin, Window window, Stage stage) {
		this.skin = skin;
		playerModeSelect(window, stage);
	}
	
	public void playerModeSelect(Window mainmenu, Stage stage) {
		Table playerMode = new Table();
		playerMode.setDebug(true);
		Label modeInfo = new Label("SELECT A MODE", skin);
		Button singlePlayerButton = new TextButton("Single Player", skin);
		Button multiplayerButton = new TextButton("Multiplayer", skin);
		
		Label menuInfo = new Label("click play! to remove this window", skin);
		Button playGame = new TextButton("play!", skin);
		
		playerMode.add(modeInfo).align(Align.center).row();
		playerMode.add(singlePlayerButton).pad(10).row();
		playerMode.add(multiplayerButton).row();
		playerMode.add(menuInfo).align(Align.bottom).row();
		playerMode.add(playGame).align(Align.bottom);
		
		singlePlayerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				playerType = 0; 
				selectWorldMode(mainmenu, stage);
			}
		});
		
		multiplayerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				playerType = 1; 
				selectServerMode(mainmenu, stage);
			}
		});
		
		playGame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mainmenu.setVisible(false);
			}
		});

		mainmenu.add(playerMode);
	}
	
	public void selectCharacter(Window mainmenu, Stage stage) {
		mainmenu.clear(); 
		
		Table playerTable = new Table(); 
		
		Label playerInfo = new Label("Pick your character!", skin);
		
		playerTable.add(playerInfo).row();
		
		
		mainmenu.add(playerTable);
		addNavigationButton(ScreenMode.CHARACTERMODE, mainmenu, stage);
		
	}
	
	public void selectWorldMode(Window mainmenu, Stage stage) {
		mainmenu.clear();
		
		Table worldTable = new Table(); 
		
		Label worldInfo = new Label("Select a world to play in", skin);
		
		worldTable.add(worldInfo);
		
		addNavigationButton(ScreenMode.WORLDMODE, mainmenu, stage);
		
		mainmenu.add(worldTable);
	}
	
	public void selectCombat(Window mainmenu, Stage stage) {
		mainmenu.clear();

		Table gameTable = new Table();
		
		Label combatInfo = new Label("Select a combat mode", skin);
		
		gameTable.add(combatInfo).row();
		addNavigationButton(ScreenMode.COMBATMODE, mainmenu, stage);
		mainmenu.add(gameTable);
	}
	
	public void selectServerMode(Window mainmenu, Stage stage) {
		mainmenu.clear();
        lobby = new LobbyButton(skin, mainmenu, stage);
		
		Table serverTable = new Table(); 
		
		Label serverInfo = new Label("Join a server or start your own!", skin);
				
		serverTable.add(serverInfo).row();
		serverTable.add(lobby.addStartServerButton(this)).row();
		serverTable.add(lobby.addJoinServerButton(this));
		addNavigationButton(ScreenMode.SERVERMODE, mainmenu, stage);
		mainmenu.add(serverTable);
	}
	
	/**
	 * Creates a button to navigate one step back in the main menu
	 * @param status
	 * @param mainmenu
	 * @param stage
	 */
	public void addNavigationButton(ScreenMode status, Window mainmenu, Stage stage) {
		backButton = new TextButton("< Go Back", skin);
		nextButton = new TextButton("> Next", skin);
		
		backButton.addListener(new ChangeListener() {
			@Override 
			public void changed(ChangeEvent event, Actor actor){
				/*If single player mode*/
				if (playerType == 0) {
					switch(status) {
					//go back to previous state
					case WORLDMODE:
						mainmenu.clear(); 
						playerModeSelect(mainmenu, stage);
						break; 
					case CHARACTERMODE:
						selectWorldMode(mainmenu, stage);
						break;
					case COMBATMODE:
						selectCharacter(mainmenu, stage);
						break;
					}
				}
				
				/* If multiplayer mode */
				else if(playerType == 1) {
					switch(status) {
					//go back to previous page 
					case SERVERMODE:
						//go back to choose player
						mainmenu.clear();
						playerModeSelect(mainmenu, stage);
						break; 
					case COMBATMODE:
						mainmenu.clear(); 
						selectCharacter(mainmenu, stage);
						break;
					case WORLDMODE:
						mainmenu.clear(); 
						selectServerMode(mainmenu, stage); 
						break;
					case CHARACTERMODE:
						if (joinedServer == 1){
							selectServerMode(mainmenu, stage);
						}
						else{
							selectWorldMode(mainmenu, stage);
						}
						break; 
					}
					
				}
			}
		});
				
		nextButton.addListener(new ChangeListener() {
			@Override 
			public void changed(ChangeEvent event, Actor actor){
				/*If single player mode*/
				/* Single Player: select world > select character > select combat*/
				if (playerType == 0) {
					switch(status) {
					//go back to next state
					case WORLDMODE:
						mainmenu.clear(); 
						selectCharacter(mainmenu, stage);
						break; 
					case CHARACTERMODE:
						selectCombat(mainmenu, stage);
						break;
					}
				}
				
				/* If multiplayer mode */
				
				/* Multiplayer: 
					 * 		join server > select character 
					 * 
					 * Multiplayer: 
					 * 		start server > select world > select character > select combat 
				*/
				
				else if(playerType == 1) {
					switch(status) {
					//go to next page 
					case WORLDMODE:
						mainmenu.clear(); 
						selectCombat(mainmenu, stage); 
						break;
					case CHARACTERMODE:
						if (joinedServer == 1){
							;
						}
						else{
							selectCombat(mainmenu, stage);
						}
						break; 
					}
					
				}
			}
		});
		
		Button quitButton = new TextButton("Exit", skin);
		quitButton.addListener(new ChangeListener() {
			@Override
			//could abstract this into another class
			public void changed(ChangeEvent event, Actor actor) {
				new ExitGame("Quit Game", skin).show(stage);	
		}});


		mainmenu.row();
		mainmenu.add(backButton);
		mainmenu.add(nextButton);
		mainmenu.add(quitButton);
	}
	
	public void setJoinedServer(){
		joinedServer = 1; 
	}
}