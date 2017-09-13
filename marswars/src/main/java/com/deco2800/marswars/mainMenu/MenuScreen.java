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
import com.deco2800.marswars.hud.ExitGame;
import com.deco2800.marswars.net.LobbyButton;

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
	
	enum Back{
		SINGLEPLAYER,   // single player 
		MULTIPLAYER,    // mutiplayer
		JOINEDSERVER,   // multiplayer, joined server
		STARTEDSERVER,  // multiplayer, started their own server
		 
		SERVERMODE,     // select choose server or join server, go back to playerMode 
		COMBATMODE,     // chose combat, go back to select character 
		WORLDMODE,      // choosing the world, go back to select playerMode/start server
		CHARACTERMODE;  // choosing character, goes back to select world
	}
	
	private Skin skin; 
	private LobbyButton lobby; 
	
	private Button backButton; 

	public MenuScreen(Skin skin, Window window, Stage stage) {
		this.skin = skin;
		playerModeSelect(window, stage);
	}
	
	public void playerModeSelect(Window mainmenu, Stage stage) {
		Table playerMode = new Table(); 
		Label modeInfo = new Label("Select a mode", skin);
		Button singlePlayerButton = new TextButton("Single Player", skin);
		Button multiplayerButton = new TextButton("Multiplayer Player", skin);
		
		playerMode.add(modeInfo);
		playerMode.row();
		playerMode.add(singlePlayerButton);
		playerMode.add(multiplayerButton);
		
		singlePlayerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				selectWorldMode(mainmenu, stage);
			}
		});
		
		multiplayerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				selectServerMode(mainmenu, stage);
			}
		});

		mainmenu.add(playerMode);
	}
	
	public void selectCharacter(Window mainmenu, Stage stage) {
		mainmenu.clear(); 
		
		Table playerTable = new Table(); 
		
		Label playerInfo = new Label("Select a world to play in", skin);
		
		playerTable.add(playerInfo);
		playerTable.add(addBackButton(Back.MULTIPLAYER, Back.CHARACTERMODE, mainmenu, stage));
		
		mainmenu.add(playerTable);
		
	}
	
	public void selectWorldMode(Window mainmenu, Stage stage) {
		mainmenu.clear();
		
		Table worldTable = new Table(); 
		
		Label worldInfo = new Label("Select a world to play in", skin);
		
		worldTable.add(worldInfo);
		worldTable.add(addBackButton(Back.MULTIPLAYER, Back.WORLDMODE, mainmenu, stage));
		
		mainmenu.add(worldTable);
	}
	
	public void selectCombat(Window mainmenu, Stage stage) {
		mainmenu.clear();

		Table gameTable = new Table();
		
		Label combatInfo = new Label("Select a combat mode", skin);
		
		gameTable.add(combatInfo).row();
		gameTable.add(addBackButton(Back.SINGLEPLAYER, Back.COMBATMODE, mainmenu, stage));
		mainmenu.add(gameTable);
	}
	
	public void selectServerMode(Window mainmenu, Stage stage) {
		mainmenu.clear();
        lobby = new LobbyButton(skin, stage);
		
		Table serverTable = new Table(); 
		
		Label serverInfo = new Label("Join a server or start your own!", skin);
				
		serverTable.add(serverInfo).row();
		serverTable.add(lobby.addStartServerButton()).row();
		serverTable.add(lobby.addJoinServerButton());
		serverTable.add(addBackButton(Back.MULTIPLAYER, Back.SERVERMODE, mainmenu, stage));
		mainmenu.add(serverTable);
	}
	
	public Button addBackButton(Back playerMode, Back status, Window mainmenu, Stage stage) {
		backButton = new TextButton("< Go Back", skin);
		
		backButton.addListener(new ChangeListener() {
			@Override 
			public void changed(ChangeEvent event, Actor actor){
				/*Single Player: 
					      select world > select character > select combat
				*/
				if (playerMode == Back.SINGLEPLAYER) {
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
				
				else if(playerMode == Back.MULTIPLAYER) {
					switch(status) {
					//go back to previous page 
					case SERVERMODE:
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
						selectWorldMode(mainmenu, stage);
						break; 
					}
					
				}
			}
		});
		
		return backButton;
	}
	
	
}