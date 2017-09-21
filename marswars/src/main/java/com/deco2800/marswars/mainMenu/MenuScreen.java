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
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.worlds.CustomizedWorld;
import com.deco2800.marswars.worlds.MapSizeTypes;
import com.deco2800.marswars.worlds.map.tools.MapContainer;
import com.deco2800.marswars.worlds.map.tools.MapTypes;

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
	private int playerType; 
	private boolean joinedServer; 
	private MainMenu menu;
	MapTypes mapType;
	MapSizeTypes mapSize;
	Table navigationButtons;
	Button playButton;
	Button quitButton;
	

	//Managers
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
		Label modeInfo = new Label("SELECT A MODE", this.skin); //$NON-NLS-1$
		
		Button singlePlayerButton = new TextButton("Single Player", this.skin); //$NON-NLS-1$
		Button multiplayerButton = new TextButton("Multiplayer", this.skin); //$NON-NLS-1$
		Button customizeButton = new TextButton("Customize", this.skin);

		Label menuInfo = new Label("Click 'select world' to fast forward to playing", this.skin); //$NON-NLS-1$
		Button playGame = new TextButton("Select world", this.skin); //$NON-NLS-1$
		
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
				GameManager.get().getGui().showEntitiesPicker(true, false);
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
		
		Label playerInfo = new Label("Pick your character!", this.skin); //$NON-NLS-1$
		
		playerTable.add(playerInfo).row();
		
		
		mainmenu.add(playerTable);
		Table nav = addNavigationButton(ScreenMode.CHARACTERMODE, mainmenu, stage);
		if (this.joinedServer){
			this.addPlayButton(nav, mainmenu);
		}
	}
	
	public void selectWorldMode(Window mainmenu, Stage stage) {
		mainmenu.clear();
		mainmenu.setDebug(true);
		mainmenu.align(Align.center);
		
		Table worldTable = new Table();
		worldTable.setDebug(true);
		worldTable.align(Align.topLeft);
		Label worldInfo = new Label("Select a world to play in!", this.skin); //$NON-NLS-1$
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
		Label combatInfo = new Label("Select a combat mode", this.skin); //$NON-NLS-1$

		gameTable.add(combatInfo).row();
		mainmenu.add(gameTable);
		Table nav = addNavigationButton(ScreenMode.COMBATMODE, mainmenu, stage);
		nav.removeActor(nextButton); //remove the next button since there is no next
	}
	
	public void selectServerMode(Window mainmenu, Stage stage) {
		mainmenu.clear();
        this.lobby = new LobbyButton(this.skin, mainmenu, stage);
		
		Table serverTable = new Table(); 
		
		Label serverInfo = new Label("Join a server or start your own!", this.skin); //$NON-NLS-1$
				
		serverTable.add(serverInfo).row();
		serverTable.add(this.lobby.addStartServerButton(this)).pad(BUTTONPAD).height(BUTTONHEIGHT).width(BUTTONWIDTH).row();
		serverTable.add(this.lobby.addJoinServerButton(this)).pad(BUTTONPAD).height(BUTTONHEIGHT).width(BUTTONWIDTH).row();
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
		this.backButton = new TextButton("<", this.skin); //$NON-NLS-1$
		this.nextButton = new TextButton(">", this.skin); //$NON-NLS-1$
		
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
		
		quitButton = new TextButton("Exit", this.skin); //$NON-NLS-1$
		quitButton.addListener(new ChangeListener() {
			@Override
			//could abstract this into another class
			public void changed(ChangeEvent event, Actor actor) {
				new ExitGame("Quit Game", GameManager.get().getSkin(), hud, false).show(stage);	 //$NON-NLS-1$
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
	
	public void setJoinedServer(){
		this.joinedServer = true; 
	}
}