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
import com.deco2800.marswars.hud.ExitGame;
import com.deco2800.marswars.net.LobbyButton;

/**
 * 
 * @author Naziah Siddique
 *
 */
public class MenuScreen{
	
	private Skin skin; 
	private LobbyButton lobby; 
	
	TextButton startServer; 
	TextButton joinServer; // will allow the player to join a multiplayer game

	
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
				selectGame(mainmenu, stage);
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
	
	public void selectGame(Window mainmenu, Stage stage) {
		mainmenu.clear();

		Table gameTable = new Table();
		
		Label combatInfo = new Label("Select a combat mode", skin);
		Label gameWorldInfo = new Label("Select a game world map", skin);
		Label playerSelect = new Label("Select a character", skin);
		
		gameTable.add(combatInfo).row();
		gameTable.add(gameWorldInfo).row();
		gameTable.add(playerSelect);
		
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
		
		mainmenu.add(serverTable);
	}
}