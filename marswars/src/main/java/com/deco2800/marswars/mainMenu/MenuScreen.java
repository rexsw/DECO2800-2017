package com.deco2800.marswars.mainMenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.deco2800.marswars.hud.ExitGame;
import com.deco2800.marswars.hud.HUDView;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.NetManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.net.ConnectionManager;
import com.deco2800.marswars.net.ServerShutdownAction;
import com.deco2800.marswars.worlds.CustomizedWorld;
import com.deco2800.marswars.worlds.MapSizeTypes;
import com.deco2800.marswars.worlds.map.tools.MapContainer;
import com.deco2800.marswars.worlds.map.tools.MapTypes;
import com.esotericsoftware.kryonet.Connection;

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
	static final int BUTTONWIDTH = 150; 
	static final int BUTTONHEIGHT = 40;
	static final int BUTTONPAD = 5; 
	
	static final int NAVBUTTONSIZE = 40;
	
	enum ScreenMode{
		SERVERMODE,     // select choose server or join server, go back to playerMode 
		COMBATMODE,     // chose combat, go back to select character 
		WORLDMODE,      // choosing the world, go back to select playerMode/start server
		CHARACTERMODE;  // choosing character, goes back to select world
	}
	
	private Skin skin; 
	private LobbyButton lobby;
	private HUDView hud;
	private Texture backgroundTex;
	private Button backButton; 
	private Button nextButton; 
	public static int playerType;
	private boolean joinedServer; 
	private MainMenu menu;
	MapTypes mapType;
	MapSizeTypes mapSize;
	Table navigationButtons;
	Button playButton;
	Button quitButton;

	

	//Managers
    // The Net Manager so you can communicate with the server
    private NetManager netManager = (NetManager) GameManager.get().getManager(NetManager.class);
	private TextureManager textureManager; //for loading in resource images
	
	public MenuScreen(Skin skin, Window window, Stage stage, MainMenu mainMenu) {
		this.skin = skin;
		this.menu = mainMenu;
		playerModeSelect(window, stage);
		this.textureManager = (TextureManager) GameManager.get().getManager(TextureManager.class);

		//add background image
	    Texture backgroundTex = textureManager.getTexture("menubackground"); //$NON-NLS-1$
	    TextureRegion backgroundRegion = new TextureRegion(backgroundTex);
	    TextureRegionDrawable backgroundRegionDraw = new TextureRegionDrawable(backgroundRegion);
	    window.setBackground(backgroundRegionDraw);
	}
	
	
	public void playerModeSelect(Window mainmenu, Stage stage) {
		mainmenu.align(Align.center);
		
		Table playerMode = new Table();
		playerMode.setDebug(true);		
		Label modeInfo = new Label("SELECT A MODE", this.skin);
		
		Button singlePlayerButton = new TextButton("Single Player", this.skin);
		Button multiplayerButton = new TextButton("Multiplayer", this.skin);
		Button customizeButton = new TextButton("Customize", this.skin);

		Label menuInfo = new Label("Click 'select world' to fast forward to playing", this.skin);
		Button playGame = new TextButton("Select world", this.skin);
		
		playerMode.add(modeInfo).align(Align.center).row();
		playerMode.add(singlePlayerButton).pad(BUTTONPAD).height(BUTTONHEIGHT).width(BUTTONWIDTH).row();
		playerMode.add(multiplayerButton).pad(BUTTONPAD).height(BUTTONHEIGHT).width(BUTTONWIDTH).row();
		playerMode.add(customizeButton).pad(BUTTONPAD).height(BUTTONHEIGHT).width(BUTTONWIDTH).row();
		mainmenu.add(playerMode).row();

		mainmenu.add(menuInfo).row();
		mainmenu.add(playGame);
		
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
				menu.startGame(true, mapType, mapSize);
				GameManager.get().getGui().getSpawnMenu().showEntitiesPicker(true, false);
				mainmenu.setVisible(false);
			}
		});
		
		playGame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				selectWorldMode(mainmenu, stage);
			}
		});
	}
	
	public void selectCharacter(Window mainmenu, Stage stage) {
		mainmenu.clear(); 
		
		Table playerTable = new Table(); 
		
		Label playerInfo = new Label("Pick your character!", this.skin);
		
		playerTable.add(playerInfo).row();
		
		
		mainmenu.add(playerTable);
		Table nav = addNavigationButton(ScreenMode.CHARACTERMODE, mainmenu, stage);
		if (this.joinedServer){
			this.addPlayButton(nav, mainmenu);
		}
	}
	
	
	public void multiplayerLobby(Window mainmenu, Stage stage, String hostIP, boolean host){
	    mainmenu.clear();
	    MultiplayerLobby lobby = new MultiplayerLobby(skin, hostIP, host);
	    mainmenu.add(lobby).expand().align(Align.topLeft);
	    mainmenu.row();
	    mainmenu.add(setupExitLobbyButton(mainmenu, stage)).left();
	}
	
	public void selectWorldMode(Window mainmenu, Stage stage) {
		mainmenu.clear();
		mainmenu.setDebug(true);
		mainmenu.align(Align.center);
		
		Table worldTable = new Table();
		worldTable.setDebug(true);
		worldTable.align(Align.topLeft);
		Label worldInfo = new Label("Select a world to play in!", this.skin);
		Label worldSelected = new Label("You current selection:", skin);
		Label currentWorldSelection = new Label("No type selected, ", skin);
		Label currentSizeSelection = new Label("no map size selected.", skin);
		Table worldInfoTable = new Table();
		worldInfoTable.add(currentWorldSelection, currentSizeSelection);
		
		/*BUTTONS FOR SELECTING MAP TYPE*/
		Table worldTypeButtons = new Table();
		Button moon = new TextButton("moon", skin);
		Button mars = new TextButton("mars", skin);
		Button desert = new TextButton("desert", skin);		
		
		worldTypeButtons.add(moon, mars, desert);
		
		moon.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mapType = MapTypes.MOON;
				currentWorldSelection.setText("Moon map selected, ");
			}
		});	
		
		mars.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mapType = MapTypes.MARS;
				currentWorldSelection.setText("Mars map selected, ");
			}
		});	
		
		desert.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mapType = MapTypes.SUN;
				currentWorldSelection.setText("Desert terrain selected, ");
			}
		});	
		
		/*BUTTONS FOR SELECTING MAP SIZE*/
		Button tiny = new TextButton("tiny", skin);
		Button smol = new TextButton("smol", skin);
		Button medium = new TextButton("medium", skin);
		Button large = new TextButton("large", skin);
		Button veryLarge = new TextButton("very large", skin);
		
		Table worldSizeButtons = new Table();
		worldSizeButtons.add(tiny, smol, medium, large, veryLarge);

		tiny.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mapSize = MapSizeTypes.TINY;
				currentSizeSelection.setText("tiny map selected.");
			}
		});
		
		smol.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mapSize = MapSizeTypes.SMALL;
				currentSizeSelection.setText("smol map selected.");
			}
		});

		medium.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mapSize = MapSizeTypes.MEDIUM;
				currentSizeSelection.setText("medium map selected.");
			}
		});

		large.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mapSize = MapSizeTypes.LARGE;
				currentSizeSelection.setText("large map selected.");
			}
		});
		
		veryLarge.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mapSize = MapSizeTypes.VERY_LARGE;
				currentSizeSelection.setText("very large map selected.");
			}
		});
		
		Button playGame = new TextButton("Play!", skin);

		//FOR DEBUGGING 
		playGame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(!(mapSize == null|| mapType == null)){
					MapContainer map = new MapContainer(mapType, mapSize);
					CustomizedWorld world = new CustomizedWorld(map);
					GameManager.get().setWorld(world);
					world.loadMapContainer(map);
					GameManager.get().getCamera().translate(GameManager.get().getWorld().getWidth()*32,0);
					
					menu.startGame(true, mapType, mapSize);
					mainmenu.setVisible(false);
				}
			}
		});

		mainmenu.add(worldInfo).align(Align.left | Align.top).row();		
		worldTable.add(worldTypeButtons).row();
		worldTable.add(worldSizeButtons).row();						
		worldTable.add(worldSelected).align(Align.left).row();
		worldTable.add(worldInfoTable).align(Align.left);
		mainmenu.add(worldTable).align(Align.left | Align.center);
		mainmenu.row();
		mainmenu.add(playGame).row();
		addNavigationButton(ScreenMode.WORLDMODE, mainmenu, stage);
	}
	
	public void selectCombat(Window mainmenu, Stage stage) {
		mainmenu.clear();

		Table gameTable = new Table();		
		Label combatInfo = new Label("Select a combat mode", this.skin);

		gameTable.add(combatInfo).row();
		mainmenu.add(gameTable);
		Table nav = addNavigationButton(ScreenMode.COMBATMODE, mainmenu, stage);
		nav.removeActor(nextButton); //remove the next button since there is no next
	}
	
	public void selectServerMode(Window mainmenu, Stage stage) {
		mainmenu.clear();
        this.lobby = new LobbyButton(this.skin, mainmenu, stage);
		
		Table serverTable = new Table(); 
		
		Label serverInfo = new Label("Join a server or start your own!", this.skin);
				
		serverTable.add(serverInfo).row();
		serverTable.add(this.lobby.addStartServerButton(this)).pad(BUTTONPAD).height(BUTTONHEIGHT).width(BUTTONWIDTH).row();
		serverTable.add(this.lobby.addJoinServerButton(this)).pad(BUTTONPAD).row();
		mainmenu.add(serverTable);
		addNavigationButton(ScreenMode.SERVERMODE, mainmenu, stage);
	}
	
	/**
	 * Creates a button to navigate one step back in the main menu
	 * @param status
	 * @param mainmenu
	 * @param stage
	 */
	public Table addNavigationButton(ScreenMode status, Window mainmenu, Stage stage) {
		this.backButton = new TextButton("<", this.skin);
		this.nextButton = new TextButton(">", this.skin);
		
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
						if (MenuScreen.this.joinedServer){
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
				
				/* If multiplayer mode
					 * 		join server > select character 
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
						if (MenuScreen.this.joinedServer){
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
		
		quitButton = new TextButton("Exit", this.skin);
		quitButton.addListener(new ChangeListener() {
			@Override
			//could abstract this into another class
			public void changed(ChangeEvent event, Actor actor) {
				new ExitGame("Quit Game", GameManager.get().getSkin(), hud, false).show(stage);
		}});
		
		mainmenu.row();
		navigationButtons = new Table();
		mainmenu.add(navigationButtons);
		navigationButtons.add(this.backButton).height(NAVBUTTONSIZE).width(NAVBUTTONSIZE);
		navigationButtons.add(this.nextButton).height(NAVBUTTONSIZE).width(NAVBUTTONSIZE);
		navigationButtons.add(quitButton).height(NAVBUTTONSIZE).width(NAVBUTTONSIZE);
		navigationButtons.setPosition(mainmenu.getWidth()-navigationButtons.getWidth(), 0);
		
		return navigationButtons; 
	}
	
	private void addPlayButton(Table nav, Window mainmenu){
		Button playButton = new TextButton("Play!", this.skin);
		playButton.setSize(NAVBUTTONSIZE, NAVBUTTONSIZE);
		playButton.pad(BUTTONPAD);
		playButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mainmenu.setVisible(false);
				menu.startGame(true, mapType, mapSize);
		}});
		nav.addActorAt(2, playButton);
	}
	
	/**
	 * Sets the players joined status to a server to be true.
	 */
	public void setJoinedServer(){
		this.joinedServer = true; 
	}
	
	/**
	 * Sets the players joined status to a server to be false.
	 */
	public void unSetJoinedServer() {
	    this.joinedServer = false;
	}
	
	/**
	 * Creates an exit button to leave a multiplayer lobby when clicked. Also sets up a handler that backs teh player
	 * out of the lobby if they lose connection with the host. 
	 * 
	 * @param mainmenu the window the mainmenu is contained in
	 * @param stage the stage to display on
	 * @return A button that takes them back from the lobby screen to the server selection screen, also disconnects
	 *     the user when they do.
	 */
	private Button setupExitLobbyButton(Window mainmenu, Stage stage) {
	    TextButton exitButton = new TextButton("Exit Lobby", skin);
	    // Add BAck button 
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                netManager.getNetworkClient().stop();
                unSetJoinedServer();
                selectServerMode(mainmenu, stage);
            }
        });
        
        netManager.getNetworkClient().addConnectionManager(
                new ConnectionManager() {
                    @Override
                    public void received(Connection connection, Object o) {
                        if (o instanceof ServerShutdownAction) {
                            netManager.getNetworkClient().stop();
                            unSetJoinedServer();
                            selectServerMode(mainmenu, stage);
                        }
                    }
                }
        );
        
        return exitButton;
    }
}