package com.deco2800.marswars.mainMenu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.hud.ExitGame;
import com.deco2800.marswars.hud.HUDView;

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
	private HUDView gui;
	private Button backButton; 
	private Button nextButton; 
	private int playerType; 
	private int joinedServer; 
	private MainMenu menu; 
	private HUDView hud;
	
	
	public MenuScreen(Skin skin, Window window, Stage stage, MainMenu mainMenu) {
		this.skin = skin;
		this.menu = mainMenu;
		playerModeSelect(window, stage);
	}
	
	public void playerModeSelect(Window mainmenu, Stage stage) {
		Table playerMode = new Table();
		playerMode.setDebug(true);
		Label modeInfo = new Label("SELECT A MODE", this.skin); //$NON-NLS-1$
		Button singlePlayerButton = new TextButton("Single Player", this.skin); //$NON-NLS-1$
		Button multiplayerButton = new TextButton("Multiplayer", this.skin); //$NON-NLS-1$
		Button customizeButton = new TextButton("Customize", this.skin);

		Label menuInfo = new Label("click play! to remove this window", this.skin); //$NON-NLS-1$
		Button playGame = new TextButton("play!", this.skin); //$NON-NLS-1$
		
		playerMode.add(modeInfo).align(Align.center).row();
		playerMode.add(singlePlayerButton).pad(10).row();
		playerMode.add(multiplayerButton).row();
		playerMode.add(customizeButton).row();
		playerMode.add(menuInfo).align(Align.bottom).row();
		playerMode.add(playGame).align(Align.bottom);
		
		singlePlayerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				MenuScreen.this.playerType = 0; 
				selectWorldMode(mainmenu, stage);
			}
		});
		
		multiplayerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				MenuScreen.this.playerType = 1; 
				selectServerMode(mainmenu, stage);
			}
		});

		customizeButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//MenuScreen.this.gui.showEntitiesPicker(true); to be changed DO NOT DELETE
				//mainmenu.setVisible(false);

			}
		});
		
		playGame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				menu.startGame(true);
				mainmenu.setVisible(false);
			}
		});
		
		mainmenu.align(Align.bottomLeft);

		mainmenu.add(playerMode);
		//why you no work properly :( 
		playerMode.setFillParent(true);
		//playerMode.setPosition(0, 0);
	}
	
	public void selectCharacter(Window mainmenu, Stage stage) {
		mainmenu.clear(); 
		
		Table playerTable = new Table(); 
		
		Label playerInfo = new Label("Pick your character!", this.skin); //$NON-NLS-1$
		
		playerTable.add(playerInfo).row();
		
		
		mainmenu.add(playerTable);
		addNavigationButton(ScreenMode.CHARACTERMODE, mainmenu, stage);
		
	}
	
	public void selectWorldMode(Window mainmenu, Stage stage) {
		mainmenu.clear();
		
		Table worldTable = new Table(); 
		
		Label worldInfo = new Label("Select a world to play in", this.skin); //$NON-NLS-1$
		
		worldTable.add(worldInfo);
		
		addNavigationButton(ScreenMode.WORLDMODE, mainmenu, stage);
		
		mainmenu.add(worldTable);
	}
	
	public void selectCombat(Window mainmenu, Stage stage) {
		mainmenu.clear();

		Table gameTable = new Table();
		
		Label combatInfo = new Label("Select a combat mode", this.skin); //$NON-NLS-1$
		
		gameTable.add(combatInfo).row();
		addNavigationButton(ScreenMode.COMBATMODE, mainmenu, stage);
		mainmenu.add(gameTable);
	}
	
	public void selectServerMode(Window mainmenu, Stage stage) {
		mainmenu.clear();
        this.lobby = new LobbyButton(this.skin, mainmenu, stage);
		
		Table serverTable = new Table(); 
		
		Label serverInfo = new Label("Join a server or start your own!", this.skin); //$NON-NLS-1$
				
		serverTable.add(serverInfo).row();
		serverTable.add(this.lobby.addStartServerButton(this)).row();
		serverTable.add(this.lobby.addJoinServerButton(this));
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
		this.backButton = new TextButton("< Go Back", this.skin); //$NON-NLS-1$
		this.nextButton = new TextButton("> Next", this.skin); //$NON-NLS-1$
		
		this.backButton.addListener(new ChangeListener() {
			@Override 
			public void changed(ChangeEvent event, Actor actor){
				/*If single player mode*/
				if (MenuScreen.this.playerType == 0) {
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
					default:
						break;
					}
				}
				
				/* If multiplayer mode */
				else if(MenuScreen.this.playerType == 1) {
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
						if (MenuScreen.this.joinedServer == 1){
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
				
		this.nextButton.addListener(new ChangeListener() {
			@Override 
			public void changed(ChangeEvent event, Actor actor){
				/*If single player mode*/
				/* Single Player: select world > select character > select combat*/
				if (MenuScreen.this.playerType == 0) {
					switch(status) {
					//go back to next state
					case WORLDMODE:
						mainmenu.clear(); 
						selectCharacter(mainmenu, stage);
						break; 
					case CHARACTERMODE:
						selectCombat(mainmenu, stage);
						break;
					default:
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
				
				else if(MenuScreen.this.playerType == 1) {
					switch(status) {
					//go to next page 
					case WORLDMODE:
						mainmenu.clear(); 
						selectCombat(mainmenu, stage); 
						break;
					case CHARACTERMODE:
						if (MenuScreen.this.joinedServer == 1){
							;
						}
						else{
							selectCombat(mainmenu, stage);
						}
						break;
					default:
						break; 
					}
					
				}
			}
		});
		
		Button quitButton = new TextButton("Exit", this.skin); //$NON-NLS-1$
		quitButton.addListener(new ChangeListener() {
			@Override
			//could abstract this into another class
			public void changed(ChangeEvent event, Actor actor) {
				new ExitGame("Quit Game", MenuScreen.this.skin, hud).show(stage);	 //$NON-NLS-1$
		}});


		mainmenu.row();
		mainmenu.add(this.backButton);
		mainmenu.add(this.nextButton);
		mainmenu.add(quitButton);
	}
	
	public void setJoinedServer(){
		this.joinedServer = 1; 
	}
}