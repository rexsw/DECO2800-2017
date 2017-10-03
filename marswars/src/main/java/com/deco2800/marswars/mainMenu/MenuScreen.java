package com.deco2800.marswars.mainMenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.hud.ExitGame;
import com.deco2800.marswars.hud.HUDView;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.NetManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.net.ConnectionManager;
import com.deco2800.marswars.net.ServerShutdownAction;
import com.deco2800.marswars.worlds.MapSizeTypes;
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
	/* Constructors*/
	private Skin skin; 
	private LobbyButton lobby;
	private HUDView hud;
	private MainMenu menu;

	/* Navigation button styling*/
	static final int BUTTONWIDTH = 150; 
	static final int BUTTONHEIGHT = 40;
	static final int BUTTONPAD = 15; 
	static final int NAVBUTTONSIZE = 50;
	static final int MAPBUTTONSIZE = 90;
	
	/*Character icon sizing*/
	static final int ASTROWIDTH = 150;
	static final int ASTROLENGTH = 150;
	
	/* Navigation buttons + layout */
	private Table navigationButtons;
	private Button quitButton;
	private Button backButton; 
	private Button nextButton;
	private Label notSelected; 
	
	/* Multiplayer toggles */
	public static int playerType;   // checks if multiplayer 
	private boolean joinedServer;   // checks if joined server 
	
	/* For recording which map to play in*/
	private MapTypes mapType;
	private MapSizeTypes mapSize;	
	
	private int AITeams; 
	private int playerTeams;
	
	/* For keeping track of the menu stage and allowing for switching back*/
	enum ScreenMode{
		SERVERMODE,     // select choose server or join server, go back to playerMode 
		COMBATMODE,     // chose combat, go back to select character 
		WORLDMODE,      // choosing the world, go back to select playerMode/start server
		CHARACTERMODE;  // choosing character, goes back to select world
	}
	
	private boolean 
	enabled = false; //FOR DEBUGGING

	/**
	 * Creates a menu screen instance. Responsible for loading up 
	 * layouts to set into the Main Menu window.
	 * @param skin
	 * @param window
	 * @param stage
	 * @param mainMenu menu
	 */
	

	//Managers
    // The Net Manager so you can communicate with the server
    private NetManager netManager = (NetManager) GameManager.get().getManager(NetManager.class);
	private TextureManager textureManager; //for loading in resource images
	
	public MenuScreen(Skin skin, Window window, Stage stage, MainMenu mainMenu) {
		this.skin = skin;
		this.menu = mainMenu;
		this.textureManager = (TextureManager)(GameManager.get().getManager(TextureManager.class));
		window.align(Align.left | Align.center);
		playerModeSelect(window, stage);
	}
	
	/**
	 * Loads up the layout for the player selection stage of the main menu
	 * @param mainmenu window
	 * @param stage
	 */
	public void playerModeSelect(Window mainmenu, Stage stage) {		
		Table playerMode = new Table();
		playerMode.setDebug(enabled);		
		Label modeInfo = new Label("MAIN MENU", skin, "title");
		//modeInfo.setVisible(false);
		
		Button singlePlayerButton = new TextButton("Single Player", this.skin, "menubutton");
		Button multiplayerButton = new TextButton("Multiplayer", this.skin, "menubutton");
		Button customizeButton = new TextButton("Customize", this.skin, "menubutton");
		
		/*TODO: Remove later since this is only for debugging*/
		Label menuInfo = new Label("Click 'Quick Select' to fast forward \n"
				+ "to playing a set map of a mars type, medium sized \n"
				+ "map with 1 of each AI and player team \n (for quicker debugging)", this.skin);
		menuInfo.setWrap(true);
		Button quickGame = new TextButton("Quick Select", this.skin);
				
		/* Add in the player mode buttons to the table*/
		playerMode.add(modeInfo).align(Align.left).padBottom(BUTTONPAD*4).row();
		playerMode.add(singlePlayerButton).align(Align.left).padBottom(BUTTONPAD*2).row();
		playerMode.add(multiplayerButton).align(Align.left).padBottom(BUTTONPAD*2).row();
		playerMode.add(customizeButton).align(Align.left).padBottom(BUTTONPAD*2).row();
				
		/* Add in tables to window*/
		mainmenu.add(playerMode).align(Align.left).row();
		mainmenu.add(menuInfo).row();
		mainmenu.add(quickGame);
		
		/* Button listeners */
		singlePlayerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				MenuScreen.playerType = 0; 
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
				menu.startGame(true, mapType, mapSize, AITeams, playerTeams);
				GameManager.get().getGui().getSpawnMenu().showEntitiesPicker(true, false);
				mainmenu.setVisible(false);
			}
		});
		
		//TODO: remove this around week 12
		quickGame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mainmenu.setVisible(false);
				menu.startGame(true, MapTypes.MARS, MapSizeTypes.MEDIUM, 1, 1);
			}
		});
	}
	
	/**
	 * Creates a 'Select Character' layout for the main menu and 
	 * adds it to the menu.
	 * @param mainmenu
	 * @param stage
	 */
	public void selectCharacter(Window mainmenu, Stage stage) {
		mainmenu.clear(); 
		
		Table playerTable = new Table(); 
		
		Label playerInfo = new Label("PICK YOUR CHARACTER", this.skin, "subtitle");
		Label moreInfo = new Label("click '>' since this feautre has not "
				+ "yet been implemented)", skin);
		
		//TODO talk to the team looking into this 
		/*Just some solider colours, has no actual functionality just yet*/
		Texture blueTex =  this.textureManager.getTexture("astro_blue");
		Image blueAstro = new Image(blueTex);
		
		Texture yellowTex =  this.textureManager.getTexture("astro_yellow");
		Image yellowAstro = new Image(yellowTex);
		
		Texture pinkTex =  this.textureManager.getTexture("astro_pink");
		Image pinkAstro = new Image(pinkTex);
		
		Texture greenTex =  this.textureManager.getTexture("astro_green");
		Image greenAstro = new Image(greenTex);
		
		Texture redTex =  this.textureManager.getTexture("astro_red");
		Image redAstro = new Image(redTex);
		
		Texture purpleTex =  this.textureManager.getTexture("astro_purple");
		Image purpleAstro = new Image(purpleTex);
		
		playerTable.add(greenAstro).pad(BUTTONPAD).size(ASTROWIDTH);
		playerTable.add(yellowAstro).pad(BUTTONPAD).size(ASTROWIDTH);
		playerTable.add(redAstro).pad(BUTTONPAD).size(ASTROWIDTH).row();
		playerTable.add(pinkAstro).pad(BUTTONPAD).size(ASTROWIDTH);
		playerTable.add(purpleAstro).pad(BUTTONPAD).size(ASTROWIDTH);
		playerTable.add(blueAstro).pad(BUTTONPAD).size(ASTROWIDTH);
		
		mainmenu.add(playerInfo).align(Align.left).row();
		mainmenu.add(moreInfo).row();
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
	
	/**
	 * Creates a 'Select world' layout for the main menu and adds it to the
	 * menu.
	 * 
	 * @param mainmenu
	 * @param stage
	 */
	public void selectWorldMode(Window mainmenu, Stage stage) {
		mainmenu.clear();
		mainmenu.setDebug(enabled);
		
		Table worldTable = new Table();
		worldTable.align(Align.left | Align.center);
		worldTable.setDebug(enabled);
		worldTable.align(Align.topLeft);
		Label worldInfo = new Label("SELECT A WORLD TO PLAY IN", this.skin, "subtitle");
		Label worldSelected = new Label("Your current selection:", skin);
		Label currentWorldSelection = new Label("No type selected, ", skin);
		Label currentSizeSelection = new Label("no map size selected.", skin);
		Table worldInfoTable = new Table();
		worldInfoTable.add(currentWorldSelection, currentSizeSelection);
		
		/*BUTTONS FOR SELECTING MAP TYPE*/
		Table worldTypeButtons = new Table();		
		worldTypeButtons.setDebug(enabled);
		//Mars tile button
		Texture marsImage =  this.textureManager.getTexture("mars_map");
		TextureRegion marsRegion = new TextureRegion(marsImage);
		TextureRegionDrawable marsRegionDraw = new TextureRegionDrawable(marsRegion);
		ImageButton mars = new ImageButton(marsRegionDraw);
		
		//Moon tile button
		Texture moonImage =  this.textureManager.getTexture("moon_map");
		TextureRegion moonRegion = new TextureRegion(moonImage);
		TextureRegionDrawable moonRegionDraw = new TextureRegionDrawable(moonRegion);
		ImageButton moon = new ImageButton(moonRegionDraw);
		moon.background(moonRegionDraw);
		
		//Desert tile button
		Texture desertImage =  this.textureManager.getTexture("desert_map");
		TextureRegion desertRegion = new TextureRegion(desertImage);
		TextureRegionDrawable desertRegionDraw = new TextureRegionDrawable(desertRegion);
		ImageButton desert = new ImageButton(desertRegionDraw);
		
		worldTypeButtons.add(moon).pad(BUTTONPAD).size(MAPBUTTONSIZE);
		worldTypeButtons.add(mars).pad(BUTTONPAD).size(MAPBUTTONSIZE);
		worldTypeButtons.add(desert).pad(BUTTONPAD).size(MAPBUTTONSIZE);
				
		
		/* Button listeners*/
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
		Button tiny = new TextButton("XS", skin, "toggle");
		Button smol = new TextButton("S", skin, "toggle");
		Button medium = new TextButton("M", skin, "toggle");
		Button large = new TextButton("L", skin, "toggle");
		Button veryLarge = new TextButton("XL", skin, "toggle");
		
		Table worldSizeButtons = new Table();
		worldSizeButtons.add(tiny).size(NAVBUTTONSIZE).pad(BUTTONPAD);
		worldSizeButtons.add(smol).size(NAVBUTTONSIZE).pad(BUTTONPAD);
		worldSizeButtons.add(medium).size(NAVBUTTONSIZE).pad(BUTTONPAD);
		worldSizeButtons.add(large).size(NAVBUTTONSIZE).pad(BUTTONPAD);
		worldSizeButtons.add(veryLarge).size(NAVBUTTONSIZE).pad(BUTTONPAD);	
		
		/* Map size listeners */
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
		
		mainmenu.add(worldInfo).align(Align.left | Align.top).row();		
		worldTable.add(worldTypeButtons).row();
		worldTable.add(worldSizeButtons).row();						
		worldTable.add(worldSelected).align(Align.left).row();
		worldTable.add(worldInfoTable).align(Align.left);
		mainmenu.add(worldTable).align(Align.left | Align.center);
		mainmenu.row();
		addNavigationButton(ScreenMode.WORLDMODE, mainmenu, stage);
	}
	
	/**
	 * Creates a 'select combat' layout for the main menu and 
	 * adds it to the menu.
	 * @param mainmenu window
	 * @param stage
	 */
	public void selectCombat(Window mainmenu, Stage stage) {
		mainmenu.clear();

		Table gameTable = new Table();		
		gameTable.align(Align.left | Align.center);
		Label combatInfo = new Label("SELECT A COMBAT MODE", this.skin, "subtitle");
		
		//int ai teams, int player teams
		
		Label aiSelect = new Label("Pick the number of AI teams", skin);
		Label playerSelect = new Label("Pick the number of player teams", skin);
		Label selected = new Label(String.format("Selected %d AI teams and %d "
				+ "player teams", AITeams, playerTeams), skin);
		notSelected = new Label("You need to select at least 1 AI team and 1 "
				+ "player team!", this.skin, "error");
		notSelected.setVisible(false);
		
		Button AI1 = new TextButton("1", skin, "menubutton");
		Button AI2 = new TextButton("2", skin, "menubutton");
		Button P1 = new TextButton("1", skin, "menubutton");
		Button P2 = new TextButton("2", skin, "menubutton");
		
		AI1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				AITeams = 1;
				selected.setText(String.format("Selected 1 AI team and %d player "
						+ "team(s)", playerTeams));
				notSelected.setVisible(false);
			}
		});
		
		AI2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				AITeams = 2;
				selected.setText(String.format("Selected 2 AI teams and %d player "
						+ "team(s)", playerTeams));
				notSelected.setVisible(false);
			}
		});
		
		P1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				playerTeams = 1;
				selected.setText(String.format("Selected %s AI team(s) and 1 player "
						+ "team", playerTeams));
			}
		});
		
		P2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				playerTeams = 2;
				selected.setText(String.format("Selected %s AI team(s) and 2 player "
						+ "teams", playerTeams));
			}
		});

		Table AIButtons = new Table();
		Table playerButtons = new Table();
		
		AIButtons.add(AI1).pad(BUTTONPAD);
		AIButtons.add(AI2).pad(BUTTONPAD);
		playerButtons.add(P1).pad(BUTTONPAD);
		playerButtons.add(P2).pad(BUTTONPAD);

		mainmenu.add(combatInfo).align(Align.left).row();
		mainmenu.add(aiSelect).align(Align.left).row();
		mainmenu.add(AIButtons).row();
		mainmenu.add(playerSelect).align(Align.left).row();
		mainmenu.add(playerButtons).row();
		mainmenu.add(gameTable).row();
		mainmenu.add(selected).align(Align.left).row();
		mainmenu.add(notSelected).align(Align.left).row();
		Table nav = addNavigationButton(ScreenMode.COMBATMODE, mainmenu, stage);
		nav.align(Align.left);
		this.addPlayButton(nav, mainmenu);
	}
	
	/**
	 * Creates a select server layout for the main menu and adds 
	 * it in to the menu
	 * @param mainmenu window
	 * @param stage
	 */
	public void selectServerMode(Window mainmenu, Stage stage) {
		mainmenu.clear();
        this.lobby = new LobbyButton(this.skin, mainmenu, stage);
		
		Table serverTable = new Table(); 
		
		Label serverInfo = new Label("Join a server or start your own!", this.skin, "subtitle");
				
		serverTable.add(serverInfo).row();
		serverTable.add(this.lobby.addStartServerButton(this)).pad(BUTTONPAD).height(BUTTONHEIGHT).width(BUTTONWIDTH).row();
		serverTable.add(this.lobby.addJoinServerButton(this)).pad(BUTTONPAD).row();
		mainmenu.add(serverTable);
		addNavigationButton(ScreenMode.SERVERMODE, mainmenu, stage);
	}
	
	/**
	 * Creates a button to navigate one step back in the main menu
	 * @param status
	 * @param mainmenu window
	 * @param stage
	 */
	public Table addNavigationButton(ScreenMode status, Window mainmenu, Stage stage) {
		this.backButton = new TextButton("<", this.skin, "menubutton");
		this.nextButton = new TextButton(">", this.skin, "menubutton");
		
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
				} else if(MenuScreen.this.playerType == 1) {
					/* If multiplayer mode
					 * 		join server > select character 
					 * 		start server > select world > select character > select combat 
					 */				
					switch(status) {
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
		navigationButtons.add(this.backButton).pad(BUTTONPAD);
		navigationButtons.add(this.nextButton).pad(BUTTONPAD);
		navigationButtons.add(this.quitButton).pad(BUTTONPAD);
		navigationButtons.setPosition(mainmenu.getWidth()-navigationButtons.getWidth(), 0);
		
		return navigationButtons; 
	}
	
	public void resizeMenu(Window mainmenu){
		navigationButtons.setPosition(mainmenu.getWidth()-navigationButtons.getWidth(), 0);
	}
	
	/*
	 * Adds in a play button to the select world mode
	 * FOR DEBUGGING ONLY  
	 * //TODO remove this once menu is fully functional 
	 * @param nav
	 * @param mainmenu
	 */
	private void addPlayButton(Table nav, Window mainmenu){
		Button playButton = new TextButton("Play!", this.skin);
		playButton.pad(BUTTONPAD);
		playButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				/* If a certain feature if not selected*/
				if(! (mapType == null || mapSize == null || 
						AITeams == 0 || playerTeams == 0 )){
					mainmenu.setVisible(false);
					menu.startGame(true, mapType, mapSize, AITeams, playerTeams);
				}
				//TODO talk to AI team and see if at least one of each must be selected
				//TODO come up with a better user notif of an unselected option
				else if (mapType == null || mapSize == null){
					notSelected.setText("Map not selected!");
					notSelected.setVisible(true);
				} else if (AITeams == 0 || playerTeams == 0){
					notSelected.setText("You need to select at least 1 AI team and 1 player team!");
					notSelected.setVisible(true);
				}
		}});
		nav.removeActor(nextButton);
		nav.add(playButton).size(NAVBUTTONSIZE);
		nav.swapActor(playButton, quitButton); //TODO figure out why this doesn't work 
		
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