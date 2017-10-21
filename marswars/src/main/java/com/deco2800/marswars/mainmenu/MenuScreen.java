package com.deco2800.marswars.mainmenu;

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
	private Stage stage;
	private static Window mainmenu;

	/* Navigation button styling*/
	static final int BUTTONWIDTH = 150; 
	static final int BUTTONHEIGHT = 40;
	static final int BUTTONPAD = 15; 
	static final int NAVBUTTONSIZE = 50;
	static final int MAPBUTTONSIZE = 120;
	static final int LABELPAD = 40;
	
	/*Character icon sizing*/
	static final int ASTROWIDTH = 120;
	static final int ASTROLENGTH = 150;
	
	/*Always 1 player team */
	static final int PLAYERTEAMS = 1; 
	
	/* Navigation buttons + layout */
	private Table lowerPanel;
	private Table navigationButtons;
	private Table actionButtons; 
	private Button quitButton;
	private Button backButton; 
	private Button nextButton;
	private Label errorWorldSelection;
	private Label errorPlayerSelection;
	private Label errorTeamsSelection;
	
	/* To ensure 'saving' of the old state*/
	private Label currentWorldSelection;
	private Label currentSizeSelection; 
	
	/* Multiplayer toggles */
	private static int playerType;   // checks if multiplayer
	private boolean joinedServer;   // checks if joined server 
	
	/* For recording which map to play in*/
	private MapTypes mapType;
	private MapSizeTypes mapSize;	
	
	/* Always at most two teams*/
	private int allTeams = 0; 
	
	/* For keeping track of the menu stage and allowing for switching back*/
	enum ScreenMode{
		SERVERMODE,     // select choose server or join server, go back to playerMode 
		COMBATMODE,     // chose combat, go back to select character 
		WORLDMODE,      // choosing the world, go back to select playerMode/start server
		CHARACTERMODE;  // choosing character, goes back to select world
	}
	
	private static boolean enabled = false; //FOR DEBUGGING

	// To keep track of whether a map type and size have been selected, 1 is
	// true, 0 is false
	private int mapTypeSet = 0;
	private int mapSizeSet = 0;

	
	private String menuButtonString = "menubutton";
	private String subtitleString = "subtitle";
	private String totalTeamsPlayingString = "Total %d teams playing";
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
		this.stage = stage; 
		this.mainmenu = window;
		this.textureManager = (TextureManager)(GameManager.get().getManager(TextureManager.class));
		
		/* UI prompts */
		currentWorldSelection = new Label("No type selected, ", skin);
		currentSizeSelection = new Label("no map size selected.", skin);
		currentWorldSelection.setAlignment(Align.left);
		currentSizeSelection.setAlignment(Align.left);

		window.align(Align.left | Align.center);
		playerModeSelect();
	}
	
	/**
	 * Loads up the layout for the player selection stage of the main menu
	 */
	public void playerModeSelect() {
		Table playerMode = new Table();
		playerMode.setDebug(enabled);		
		String title = "title";
		String button2 = "button2";
		
		Label modeInfo = new Label("MAIN MENU", this.skin, title);
		//modeInfo.setVisible(false);
		
		Button singlePlayerButton = new TextButton("Single Player", this.skin, button2);
		Button multiplayerButton = new TextButton("Multiplayer", this.skin, button2);
		Button customizeButton = new TextButton("Customize", this.skin, button2);
		Button loadGameButton = new TextButton("Load Game", this.skin, button2);
		Button exitGameButton = new TextButton("Exit Game", this.skin, button2);
		
		/*TODO: Remove later since this is only for debugging*/
		Label menuInfo = new Label("Click 'Quick Select' to fast forward \n"
				+ "to playing a set map of a mars type, medium sized \n"
				+ "map with 1 of each AI and player team \n (for quicker debugging)", this.skin);
		menuInfo.setWrap(true);
		Button quickGame = new TextButton("Quick Select", this.skin);
				
		/* Add in the player mode buttons to the table*/
		playerMode.add(modeInfo).align(Align.left).padBottom(BUTTONPAD*3).row();
		playerMode.add(singlePlayerButton).align(Align.left).padBottom(BUTTONPAD*2).row();
		playerMode.add(multiplayerButton).align(Align.left).padBottom(BUTTONPAD*2).row();
		playerMode.add(customizeButton).align(Align.left).padBottom(BUTTONPAD*2).row();
		playerMode.add(loadGameButton).align(Align.left).padBottom(BUTTONPAD*2).row();
		playerMode.add(exitGameButton).align(Align.left).padBottom(BUTTONPAD*2).row();
		
		/* Add in tables to window*/
		mainmenu.add(playerMode).align(Align.left).row();
		mainmenu.add(quickGame);
		mainmenu.add(menuInfo).row();
		
		/* Button listeners */
		singlePlayerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
			    setPlayerType(0);
				selectWorldMode();
			}
		});
		
		multiplayerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
                setPlayerType(1);
				selectServerMode();
			}
		});

		customizeButton.addListener(new ChangeListener()  {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				menu.startGame(true, mapType, mapSize, allTeams, PLAYERTEAMS);
				GameManager.get().getGui().getSpawnMenu().showEntitiesPicker(true, false);
				mainmenu.setVisible(false);
			}
		});
		
		/* Button listeners */
		exitGameButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.exit(1);
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
		
		loadGameButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mainmenu.setVisible(false);
				menu.loadGame(true);
			}
		});
	}
	
	/**
	 * Creates a 'Select Character' layout for the main menu and 
	 * adds it to the menu.
	 */
	public void selectCharacter() {
		mainmenu.clear(); 
		
		Table playerTable = new Table(); 
		String subHeading = "subheading";
		Label playerInfo = new Label("CHARACTERS", this.skin, "title");
		Label playerPrompt = new Label("SELECT YOUR CHARACTER", this.skin, subHeading);
		Label moreInfo = new Label("click '>' since this feature has not "
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
		mainmenu.add(playerPrompt).align(Align.left).row();
		mainmenu.add(moreInfo).row();
		mainmenu.add(playerTable);
		addNavigationButton(ScreenMode.CHARACTERMODE);
		if (this.joinedServer){
			this.addPlayButton();
		}
	}
	
	public void multiplayerLobby(String hostIP, boolean host){
	    mainmenu.clear();
	    MultiplayerLobby lobby = new MultiplayerLobby(skin, hostIP, host);
	    mainmenu.add(lobby).expand().align(Align.topLeft);
	    mainmenu.row();
	    mainmenu.add(setupExitLobbyButton(mainmenu, stage)).left();
	}
	
	/**
	 * Creates a 'Select world' layout for the main menu and adds it to the
	 * menu.
	 */
	public void selectWorldMode() {
		mainmenu.clear();
		mainmenu.setDebug(enabled);
		
		Table worldTable = new Table();
		worldTable.align(Align.left | Align.center);
		worldTable.setDebug(enabled);
		worldTable.align(Align.topLeft);

		Label worldInfo = new Label("WORLDS", this.skin, "title");
		Label worldPrompt = new Label("SELECT A WORLD TO PLAY IN", this.skin, "subheading");
		errorWorldSelection = new Label("", skin, "error");
		
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
				mapTypeSet = 1;
			}
		});	
		
		mars.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mapType = MapTypes.MARS;
				currentWorldSelection.setText("Mars map selected, ");
				mapTypeSet= 1;
			}
		});	
		
		desert.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mapType = MapTypes.SUN;
				currentWorldSelection.setText("Desert terrain selected, ");
				mapTypeSet = 1;
			}
		});	
		
		/*BUTTONS FOR SELECTING MAP SIZE*/
		Button tiny = new TextButton("XS", skin);
		Button smol = new TextButton("S", skin);
		Button medium = new TextButton("M", skin);
		Button large = new TextButton("L", skin);
		Button veryLarge = new TextButton("XL", skin);
		
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
				currentSizeSelection.setVisible(true);
				currentSizeSelection.setText("tiny map selected.");
				mapSizeSet = 1;
			}
		});
		
		smol.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mapSize = MapSizeTypes.SMALL;
				currentSizeSelection.setVisible(true);
				currentSizeSelection.setText("smol map selected.");
			}
		});

		medium.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mapSize = MapSizeTypes.MEDIUM;
				currentSizeSelection.setVisible(true);
				currentSizeSelection.setText("medium map selected.");
				mapSizeSet = 1;
			}
		});

		large.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mapSize = MapSizeTypes.LARGE;
				currentSizeSelection.setVisible(true);
				currentSizeSelection.setText("large map selected.");
				mapSizeSet = 1;
			}
		});
		
		veryLarge.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mapSize = MapSizeTypes.VERY_LARGE;
				currentSizeSelection.setVisible(true);
				currentSizeSelection.setText("very large map selected.");
				mapSizeSet = 1;
			}
		});
		
		mainmenu.add(worldInfo).align(Align.left | Align.top).row();		
		mainmenu.add(worldPrompt).align(Align.left | Align.top).row();
		worldTable.add(worldTypeButtons).row();
		worldTable.add(worldSizeButtons).row();						
		worldTable.add(worldInfoTable).align(Align.left).row();
		worldTable.add(errorWorldSelection).align(Align.left);

		mainmenu.add(worldTable).align(Align.left | Align.center);
		mainmenu.row();
		addNavigationButton(ScreenMode.WORLDMODE);
	}
	
	/**
	 * Creates a 'select combat' layout for the main menu and 
	 * adds it to the menu.
	 */
	public void selectCombat() {
		mainmenu.clear();		
		errorTeamsSelection = new Label("", skin, "error");

		Label combatInfo = new Label("COMBAT", this.skin, "title");
		Label teamInfo = new Label("SELECT NUMBER OF TEAMS", this.skin, "subheading");
		Label aiInfo = new Label("SELECT AI BEHAVIOUR", this.skin, "subheading");
		Label winInfo = new Label("SELECT WIN CONDITIONS", this.skin, "subheading");
		
		Label selected = new Label(String.format("Total %d teams playing", allTeams), skin, "info");
		Label combatSelected = new Label("Normal AI difficulty selected", skin, "info");

		Table aiButtons = new Table();
		Button ai2 = new TextButton("2", skin, menuButtonString);
		Button ai3 = new TextButton("3", skin, menuButtonString);
		Button ai4 = new TextButton("4", skin, menuButtonString);
		Button ai5 = new TextButton("5", skin, menuButtonString);
		Button[] buttonsList= {ai2, ai3, ai4, ai5};

		ai2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				allTeams = 2;
				selected.setText(String.format(totalTeamsPlayingString, allTeams));
			}
		});

		ai3.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				allTeams = 3;
				selected.setText(String.format(totalTeamsPlayingString, allTeams));
			}
		});

		ai4.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				allTeams = 4;
				selected.setText(String.format(totalTeamsPlayingString, allTeams));
			}
		});

		
		ai5.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				allTeams = 5;
				selected.setText(String.format(totalTeamsPlayingString, allTeams));
			}
		});
		
		for (int i = 0; i < buttonsList.length; i++) {
			aiButtons.add(buttonsList[i]).pad(BUTTONPAD);
		}
		
		/* no of teams buttons*/
		String numButton = "num_button";
		Table aiBehaviorButtons = new Table();
		Button aiEasy = new TextButton("Passive", skin, numButton);
		Button aiNormal = new TextButton("Normal", skin, numButton);
		Button aiHard = new TextButton("Hard", skin, numButton);
		Button[] behaviorButtonsList= {aiEasy, aiNormal, aiHard};

		aiEasy.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				combatSelected.setText("Easy level selected");
			}
		});
		
		aiNormal.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				allTeams = 2;
				combatSelected.setText("Normal level selected");
			}
		});
		
		aiHard.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				allTeams = 2;
				combatSelected.setText("Hard level selected");
			}
		});
		
		for (int i = 0; i < behaviorButtonsList.length; i++) {
			aiBehaviorButtons.add(behaviorButtonsList[i]).pad(BUTTONPAD);
		}
		
		Table winConditionChecks = new Table(); 
		winConditionChecks.align(Align.center);
		
		CheckBox economic = new CheckBox(" Economy", skin, "spac_check");
		CheckBox millitary = new CheckBox(" Millitary", skin, "spac_check");
		CheckBox population = new CheckBox(" Population", skin, "spac_check");
		
		winConditionChecks.add(economic).align(Align.left).pad(BUTTONPAD);
		winConditionChecks.add(millitary).align(Align.left).pad(BUTTONPAD);
		winConditionChecks.add(population).align(Align.left).pad(BUTTONPAD);
								
		mainmenu.add(combatInfo).align(Align.left).row();
		mainmenu.add(teamInfo).align(Align.left).row();
		mainmenu.add(selected).align(Align.left).padBottom(LABELPAD).row();
		
		mainmenu.add(aiButtons).align(Align.center).row();
		mainmenu.add(selected).align(Align.left).row();
		mainmenu.add(aiInfo).align(Align.left).row();
		mainmenu.add(aiBehaviorButtons).align(Align.left).row();
		mainmenu.add(combatSelected).align(Align.left).padBottom(LABELPAD).row();

		mainmenu.add(winInfo).align(Align.left).row();
		mainmenu.add(winConditionChecks);

		mainmenu.add(errorTeamsSelection).align(Align.left).row();
		
		addNavigationButton(ScreenMode.COMBATMODE);
		this.addPlayButton();
	}
	
	/**
	 * Creates a select server layout for the main menu and adds 
	 * it in to the menu
	 */
	public void selectServerMode() {
		mainmenu.clear();
        this.lobby = new LobbyButton(this.skin, mainmenu, stage);
		
		Table serverTable = new Table(); 
		
		Label serverInfo = new Label("Join a server or start your own!", this.skin, "title");
				
		serverTable.add(serverInfo).row();
		serverTable.add(this.lobby.addStartServerButton(this)).pad(BUTTONPAD).height(BUTTONHEIGHT).width(BUTTONWIDTH).row();
		serverTable.add(this.lobby.addJoinServerButton(this)).pad(BUTTONPAD).row();
		mainmenu.add(serverTable);
		addNavigationButton(ScreenMode.SERVERMODE);
	}
	
	/**
	 * Creates a button to navigate one step back or one step forward in the main menu
	 * @param status
	 */
	public Table addNavigationButton(ScreenMode status) {
		this.backButton = new TextButton("<", this.skin, "pre_button");
		this.nextButton = new TextButton(">", this.skin, "next_button");
		this.backButton.addListener(new ChangeListener() {
			@Override 
			public void changed(ChangeEvent event, Actor actor){
				/*If single player mode*/
				if (MenuScreen.this.playerType == 0) {
					switch(status) {
					//go back to previous state
					case WORLDMODE:
						mainmenu.clear(); 
						playerModeSelect();
						mapTypeSet = 0;
						mapSizeSet = 0;
						break; 
					case CHARACTERMODE:
						selectWorldMode();
						mapTypeSet = 0;
						mapSizeSet = 0;
						break;
					case COMBATMODE:
						mapTypeSet = 0;
						mapSizeSet = 0;
						selectCharacter();
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
						playerModeSelect();
						break; 
					case COMBATMODE:
						mainmenu.clear(); 
						selectCharacter();
						break;
					case WORLDMODE:
						mainmenu.clear(); 
						selectServerMode(); 
						break;
					case CHARACTERMODE:
						if (MenuScreen.this.joinedServer){
							selectServerMode();
						}
						else{
							selectWorldMode();
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
						if (checkWorld(mapType, mapSize)) {
							mainmenu.clear();
							selectCharacter();
						}
						break; 
					case CHARACTERMODE:
						selectCombat();
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
						if (checkWorld(mapType, mapSize)) {
							mainmenu.clear(); 
							selectCharacter(); 
						}
						break;
					case CHARACTERMODE:
						if (MenuScreen.this.joinedServer){
							;
						}
						else{
							selectCombat();
						}
						break;
					default:
						break; 
					}
				}
			}
		});
		
		quitButton = new TextButton("x", this.skin, "pre_button");
		quitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				new ExitGame("Quit Game", GameManager.get().getSkin(), hud, false).show(stage);
		}});
		
		mainmenu.row();
		lowerPanel = new Table();
		navigationButtons = new Table();
		actionButtons = new Table();
		
		navigationButtons.setDebug(enabled);
		navigationButtons.add(this.backButton).pad(BUTTONPAD);
		navigationButtons.add(this.nextButton).pad(BUTTONPAD);
		actionButtons.add(this.quitButton).pad(BUTTONPAD);
		
		lowerPanel.add(navigationButtons);
		lowerPanel.add(actionButtons).expandX().align(Align.right);
		mainmenu.add(lowerPanel).expandY().align(Align.left | Align.bottom);
		
		return lowerPanel; 
	}
	
	private boolean checkWorld(MapTypes mapType, MapSizeTypes mapSize){
		if (mapType == null) {
			errorWorldSelection.setText("You need to pick a map type!");
		} else if (mapSize == null) {
			errorWorldSelection.setText("You need to pick a map size!");
		} else {
			return true;
		}
		
		return false; 
	}
	
	private boolean checkTeams() {
		if (allTeams == 0) {
			errorTeamsSelection.setText("Pick how many teams you want in your game!");
			return false;
		}
		return true;
	}
		
	/**
	 * Adds in a play button
	 */
	private void addPlayButton(){
		Button playButton = new TextButton("Play!", this.skin);
		playButton.pad(BUTTONPAD);
		playButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				/* If the final 'select combat' features not selected*/
				if (checkTeams()) {
					mainmenu.setVisible(false);
					menu.startGame(true, mapType, mapSize, allTeams-PLAYERTEAMS, PLAYERTEAMS);
				}
		}});
		
		navigationButtons.removeActor(nextButton);
		actionButtons.add(playButton).size(NAVBUTTONSIZE).expandY();
		actionButtons.swapActor(0, 1); 
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
                selectServerMode();
            }
        });
        
        netManager.getNetworkClient().addConnectionManager(
                new ConnectionManager() {
                    @Override
                    public void received(Connection connection, Object o) {
                        if (o instanceof ServerShutdownAction) {
                            netManager.getNetworkClient().stop();
                            unSetJoinedServer();
                            selectServerMode();
                        }
                    }
                }
        );
        
        return exitButton;
    }

	/**
	 * Set the new player type.
     *
	 * @param playerType the new player type
	 */
	static void setPlayerType(int playerType) {
		MenuScreen.playerType = playerType;
	}

    /**
     * Returns the type of player.
     *
     * @return the type of player.
     */
    public static int getPlayerType() {
        return playerType;
    }
}