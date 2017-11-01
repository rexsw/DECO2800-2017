

package com.deco2800.marswars.hud;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.deco2800.marswars.actions.*;
import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.EntityID;
import com.deco2800.marswars.entities.Selectable;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.managers.*;
import com.deco2800.marswars.renderers.Renderable;
import com.deco2800.marswars.worlds.CustomizedWorld;
import com.deco2800.marswars.worlds.MapSizeTypes;
import com.deco2800.marswars.worlds.map.tools.MapContainer;
import com.deco2800.marswars.worlds.map.tools.MapTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.Math;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Naziah Siddique on 19/08
 * Initiates the HUD for SpacWars, with the help of instantiations of
 * other components from other classes in the packages
 */
public class HUDView extends ApplicationAdapter{
	private static final Logger LOGGER = LoggerFactory.getLogger(HUDView.class);
	private boolean enabled = false;

	private static final int BUTTONSIZE = 50; //sets size of image buttons
	private static final int BUTTONPAD = 10;  //sets padding between image buttons
	private static final int NUMBER_ACTION_BUTTONS = 10; //The maximum number of buttons
	private static final int ACTIONSHEADERPAD = 160; 
	
	private Stage stage;
	private Skin skin;
	private ImageButton quitButton;
	private ImageButton helpButton;

	//HUD elements
	private Table overheadRight; //contains all basic quit/help/chat buttons
	Table resourceTable;         //contains table of resource images + count
    Table hudManip;		         //contains buttons for toggling HUD + old menu
    private Table welcomeMsg; 	 //contains welcome message
	private UnitStatsBox statsTable; //contains player icon, health and game stats
	private ChatBox chatbox;	 //table for the chat
	Window minimap;		         //window for containing the minimap
	Window actionsWindow;        //window for the players actions
	private ShopDialog shopDialog; // Dialog for shop page

	private Dialog techTree; //view for tech tree
	private Dialog pauseMenu;

	private Image statsbg; 
	private Image headerbg;
	private Image actionsBg;
	private Image actionsBgMain;
	private SpawnMenu spawnMenu; // customized menu that displays available entities to be spawned

	private Button peonButton;
	private Label helpText;

	//Resources count
	private Label rockCount;
	private Label crystalCount;
	private Label biomassCount;
	private Label popCount;
	private Label maxPopCount;

	//Action buttons
	private List<TextButton> buttonList;
	private ActionList currentActions;
	HelpWindow help; 

	//Toggles; checks if the feature is visible on-screen or not
	private boolean inventoryToggle;
	private boolean messageToggle = true;
	private boolean fogToggle = true;
	private boolean floodToggle = true;
	private boolean gameStarted = false;
	//Time displays
	private Label gameTimeDisp;
	private Label gameLengthDisp;

	//Managers
	private GameManager gameManager;
	private TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);
	private TextureManager textureManager; //for loading in resource images
	private TechnologyManager technologyManager = (TechnologyManager) GameManager.get().getManager(TechnologyManager.class);
	private BaseEntity selectedEntity;	//for differentiating the entity selected
	private List<BaseEntity> selectedList = new ArrayList<>();
	// hero manage
	private HashSet<Commander> heroMap = new HashSet<>();
	private Commander heroSelected;
	private Commander heroExist;
	private GameStats stats;

	HUDView hud = this;
	Hotkeys hotkeys;

	int pauseCheck = 0;
	int helpCheck = 0;
	int techCheck = 0;
	int chatActiveCheck = 0;
	int cheatActiveCheck = 0;
	int exitCheck = 0;
	private Table selectedTable;


	/**
	 * Creates a 'view' instance for the HUD. This includes all the graphics
	 * of the HUD and is mostly for simply displaying components on screen.
	 * @param stage the game stage
	 * @param skin the look of the HUD, depending on the world/level the game is being played at
	 * @param gameManager handles selectables
	 */
	public HUDView(Stage stage, Skin skin, GameManager gameManager) {

		LOGGER.debug("Creating Hud");
		this.skin = skin;
		this.stage = stage;
		this.gameManager = gameManager;
		this.textureManager = (TextureManager) GameManager.get().getManager(TextureManager.class);

		//Generate the game stats
		this.stats = new GameStats(stage, skin, this, textureManager);
		//create chatbox
		this.chatbox = new ChatBox(skin);

		//initialise the minimap and set the image
		MiniMap m = new MiniMap("minimap", 220, 220);
		GameManager.get().setMiniMap(m);
		GameManager.get().getMiniMap().updateMap(this.textureManager);

		//create the HUD + set gui to GM
		createLayout();
		GameManager.get().setGui(this);
	}

	/**
	 * Updates shop when item levels are unlocked.
	 */
	public void updateShop() {
		if (technologyManager.armourIsUnlocked(1)) {
			shopDialog.unlockArmours(1);
		}
		if (technologyManager.armourIsUnlocked(2)) {
			shopDialog.unlockArmours(2);
		}
		if (technologyManager.armourIsUnlocked(3)) {
			shopDialog.unlockArmours(3);
		}
		if (technologyManager.weaponIsUnlocked(1)) {
			shopDialog.unlockWeapons(1);
		}
		if (technologyManager.weaponIsUnlocked(2)) {
			shopDialog.unlockWeapons(2);
		}
		if (technologyManager.weaponIsUnlocked(3)) {
			shopDialog.unlockWeapons(3);
		}
		if (technologyManager.specialIsUnlocked()  && !(shopDialog.getSpecialUnlocked())) {
			shopDialog.unlockSpecials();
		}
	}
	

	/**
	 * Adds in all components of the HUD.
	 */
	private void createLayout(){
		topRight();
		addPlayerDetails();
		addMessages();
		addBottomPanel();
		generateTextures(19);
		this.hotkeys = new Hotkeys(stage, skin, this, this.stats, this.chatbox);
		this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	/**
	 * Contains top right section of the HUD to be displayed
	 * on screen and set to stage.
	 * This includes the message tab, help button and quit button
	 */
	private void topRight(){
		overheadRight = new Table();
		overheadRight.setDebug(enabled);
		overheadRight.setPosition(BUTTONPAD, Gdx.graphics.getHeight()-BUTTONPAD);
		
		Texture headerTex = textureManager.getTexture("header");
		headerbg = new Image(headerTex);
		stage.addActor(headerbg);
		overheadRight.setPosition(Gdx.graphics.getWidth()-overheadRight.getWidth()-BUTTONPAD, 
				Gdx.graphics.getHeight()-overheadRight.getHeight()-BUTTONPAD);
		headerbg.toBack();

		LOGGER.debug("Add help, quit and message buttons");

		//add dispMainMenu image
		Texture menuImage = textureManager.getTexture("menu_button");
		TextureRegion menuRegion = new TextureRegion(menuImage);
		TextureRegionDrawable menuRegionDraw = new TextureRegionDrawable(menuRegion);
		ImageButton dispMainMenu = new ImageButton(menuRegionDraw);


		//create help button + image for it
		Texture helpImage = textureManager.getTexture("help_button");
		TextureRegion helpRegion = new TextureRegion(helpImage);
		TextureRegionDrawable helpRegionDraw = new TextureRegionDrawable(helpRegion);
		helpButton = new ImageButton(helpRegionDraw);

		//add quit button + image for it
		Texture quitImage = textureManager.getTexture("quit_button");
		TextureRegion quitRegion = new TextureRegion(quitImage);
		TextureRegionDrawable quitRegionDraw = new TextureRegionDrawable(quitRegion);
		quitButton = new ImageButton(quitRegionDraw);

		//Create + align time displays
		LOGGER.debug("Creating time labels");
		gameTimeDisp = new Label("0:00", this.skin, "seven-seg");
		gameLengthDisp = new Label("00:00:00", skin);
		gameTimeDisp.setAlignment(Align.center);
		gameLengthDisp.setAlignment(Align.center);

		/*images for the time display*/
		Image clockbgImage0 = new Image(textureManager.getTexture("clock"));
		Image clockbgImage1 = new Image(textureManager.getTexture("clock"));

		/*stack time display on top of images*/
		Stack gametimeStack = new Stack();
		gametimeStack.add(clockbgImage0);
		gametimeStack.add(gameTimeDisp);

		Stack gamelengthStack = new Stack();
		gamelengthStack.add(clockbgImage1);
		gamelengthStack.add(gameLengthDisp);

		//add in quit + help + chat buttons and time labels
		overheadRight.add(gametimeStack).padRight(BUTTONPAD).height(BUTTONSIZE).width(BUTTONSIZE*2);
		overheadRight.add(helpButton).padRight(BUTTONPAD);
		overheadRight.add(dispMainMenu).padRight(BUTTONPAD);
		overheadRight.add(quitButton).padRight(BUTTONPAD);

		welcomeMsg = new Table();
		welcomeMsg.setWidth(stage.getWidth());
		welcomeMsg.align(Align.center | Align.top).pad(BUTTONPAD*2);
		welcomeMsg.setPosition(0, Gdx.graphics.getHeight());
		Label welcomeText = new Label("Welcome to Spacwars!", skin);
		welcomeMsg.add(welcomeText);

		stage.addActor(welcomeMsg);
		stage.addActor(overheadRight);
		
		//Creates the help button listener
		LOGGER.debug("Creating main menu button listener");
		dispMainMenu.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (getPauseCheck() == 0) {
					setPauseCheck(1);
					pauseMenu = new PauseMenu("Pause Menu", skin, stage, stats, hud).show(stage);			
					setPause(pauseMenu);
				} else {
					setPauseCheck(0);
					hidePause();
				}
			}
		});
		dispMainMenu.addListener(new TextTooltip("Pause Game and to go menu", skin));

		help = new HelpWindow(stage, skin, "help");
		stage.addActor(help);
		help.setPosition(Gdx.graphics.getWidth()/2 - help.getWidth()/2, 
				Gdx.graphics.getHeight()/2 - help.getHeight()/2);
		help.setVisible(false);

		//Creates the help button listener
		LOGGER.debug("Creating help button listener");
		helpButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (helpCheck == 1){
					help.setVisible(false);
					helpCheck = 0;
				} else {
					help.setVisible(true);
					helpCheck = 1; 
				}
			}
		});
		
		helpButton.addListener(new TextTooltip("Help", skin));

		//Creates the quit button listener
		LOGGER.debug("Creating quit button listener");
		quitButton.addListener(new ChangeListener() {
			@Override
			//could abstract this into another class
			public void changed(ChangeEvent event, Actor actor) {
				new ExitGame("Quit Game", skin, hud, true).show(stage);
			}
		});
		quitButton.addListener(new TextTooltip("Quit Game", skin));
	}


	/**
	 * Adds the player Icon, health for a single spacman, and name to the huD (goes into top left).
	 * Also inventory for the hero character
	 * Does this by creating a nested table. The basic parent table layout is shown below:
	 * +----------------------------------+
	 * |    :)    |______________         |
	 * |  Player  |______________|100/100 |
	 * |   img    |______________         |
	 * |          |______________|100/100 |
	 * |__________|_______________________|
	 * |          | atk |xx|atk.speed | x |
	 * | p. Name  |range|xx| mv.speed | x |
	 * |-----+----|-----------------------+
	 *
	 *
	 */
	private void addPlayerDetails(){
		LOGGER.debug("drawing unit stats");
		this.statsTable = new UnitStatsBox(this.skin, this.textureManager);
		statsTable.setWidth(200);
		statsTable.pad(15);
		statsTable.setDebug(enabled);
		this.statsTable.setVisible(false);
		stage.addActor(statsTable);
		
		Texture statsTex = textureManager.getTexture("stats");
		statsbg = new Image(statsTex);
		stage.addActor(statsbg);
		statsbg.setPosition(BUTTONPAD, stage.getHeight()-statsbg.getHeight()-BUTTONPAD);
		statsbg.toBack();
		statsbg.setVisible(false);
	}

	/**
	 * Implements a collapsible tab for the chat lobby
	 */
	private void addMessages(){
		LOGGER.debug("Creating chat lobby box");
		
		chatbox.setPosition(stage.getWidth()-chatbox.getWidth()-BUTTONPAD,
				Math.round(stage.getHeight()-chatbox.getHeight()-BUTTONPAD*4-BUTTONSIZE));
		
		chatbox.setVisible(false);
		chatbox.pack();

		stage.addActor(chatbox);
	}



	/**
	 * Adds in the bottom panel of the HUD
	 */
	private void addBottomPanel(){
		addMiniMapMenu();
		addInventoryMenu();
		hudManip = new Table(); //adding buttons into a table
		hudManip.setPosition(Gdx.graphics.getWidth()-hudManip.getWidth(), actionsWindow.getHeight());

		LOGGER.debug("Creating HUD manipulation buttons");

		shopDialog = new ShopDialog("Shop", skin, textureManager);

		//add dispTech image
		Texture techImage = textureManager.getTexture("tech_button");
		TextureRegion techRegion = new TextureRegion(techImage);
		TextureRegionDrawable techRegionDraw = new TextureRegionDrawable(techRegion);
		ImageButton dispTech = new ImageButton(techRegionDraw);

		//add shop button (uses arrow icon for now)
		Texture shopImage = textureManager.getTexture("shop_button");
		TextureRegion shopRegion = new TextureRegion(shopImage);
		TextureRegionDrawable shopRegionDraw = new TextureRegionDrawable(shopRegion);
		ImageButton dispShop = new ImageButton(shopRegionDraw);
		
		hudManip.setDebug(enabled);
		
		Table options = new Table();
		options.setDebug(enabled);
		options.add(dispShop).width(50).height(50);
		//REMOVING TECH BUTTON IN FAVOR OF TECH BUILDING
		//options.add(dispTech);

		hudManip.add(options);
		stage.addActor(hudManip);
		
		techTree = new TechTreeView("TechTree", skin, hud);
		dispTech.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				if (getTechCheck() == 0) {
					setTechCheck(1);
					techTree = new TechTreeView("TechTree", skin, hud).show(HUDView.this.stage);
					setTechTree(techTree);
				} else {
					setTechCheck(0);
					timeManager.pause();
					hideTechTree();
				}
			}
		});
		dispTech.addListener(new TextTooltip("Open Technology", skin));

		dispShop.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				shopDialog.show(stage);
				shopDialog.setPosition(stage.getWidth(), 0, Align.right | Align.bottom);
			}
		});

		techTree.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (techCheck == 0) {
					techTree.show(stage);
				}
				if (techCheck == 1) {
					techTree.hide();
				}
				if (x < 0 || x > techTree.getWidth() || y < 0 || y > techTree.getHeight()){
					techTree.hide();
					return true;
				}
				return false;
			}
		});

		dispShop.addListener(new TextTooltip("Open Shop", skin));

		/*
		 * listener for to determine whether shop should remain enabled. Is disabled if player clicks outside the shop
		 * window.
		 */
		shopDialog.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	if (heroSelected != null) {
            		statsTable.updateHeroInventory(heroSelected);
            	}
                if (x < 0 || x > shopDialog.getWidth() || y < 0 || y > shopDialog.getHeight()){
                	shopDialog.hide();
                    return true;
                }
                return false;
            }
       });

		spawnMenu = new SpawnMenu(stage, skin);
		SpawnMenu.setupEntitiesPickerMenu();
		spawnMenu.addEntitiesPickerMenu();
	}
	
	/**
	 * Directly oads tech menu interface
	 */
	private void loadTechMenu() {
		
		if (getTechCheck() == 0) {
			setTechCheck(1);
			techTree = new TechTreeView("TechTree", skin, hud).show(HUDView.this.stage);
			setTechTree(techTree);
		} 
		techTree.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (techCheck == 0) {
					techTree.show(stage);
				}
				if (techCheck == 1) {
					techTree.hide();
					selectedEntity.deselect();
				}
				if (x < 0 || x > techTree.getWidth() || y < 0 || y > techTree.getHeight()){
					techTree.hide();
					return true;
				}
				return false;
			}
		});

	}

	/**
	 * Toggle fog on an off
	 */
	private void toggleFog(){
		//disable fog
		if (fogToggle) {
			LOGGER.debug("fog of war is now off");
			FogManager.toggleFog(false);
			fogToggle = false;
		}else {
			LOGGER.debug("fog of war is now on");
			FogManager.toggleFog(true);
			fogToggle = true;
		}
	}

	/**
	 * Toggle flood effect on and off. See WeatherManager.toggleFlood() for
	 * further information.
	 */
	private void toggleFlood(){
		WeatherManager weatherManager = (WeatherManager)
				GameManager.get().getManager(WeatherManager.class);
		//disable fog
		if (floodToggle) {
			LOGGER.debug("fog of war is now off");
			weatherManager.toggleFlood(false);
			floodToggle = false;
		}else {
			LOGGER.debug("fog of war is now on");
			weatherManager.toggleFlood(true);
			floodToggle = true;
		}
	}

	/**
	 * Adds in the selectable menu for the inventory for resources
	 */
	private void addInventoryMenu(){
		LOGGER.debug("Create inventory");
		actionsWindow = new Window("", skin, "clear");
		actionsWindow.pad(BUTTONPAD);
		actionsWindow.setDebug(enabled);

		Texture actionsTex = textureManager.getTexture("actions_window_cropped");
		Texture actionsTopTex = textureManager.getTexture("actions_window_top");
		
		actionsBg = new Image(actionsTopTex);
		actionsBgMain = new Image(actionsTex);
		
		stage.addActor(actionsBg);
		stage.addActor(actionsBgMain);
		actionsBg.setPosition(ACTIONSHEADERPAD, actionsWindow.getHeight() + actionsWindow.getY());
		actionsBgMain.setPosition(Gdx.graphics.getWidth()-minimap.getWidth()/2, BUTTONPAD);
		actionsBgMain.setVisible(false);
				
		actionsWindow.setBackground(new TextureRegionDrawable(new TextureRegion(actionsTex)));
		resourceTable = new Table();
		resourceTable.align(Align.left | Align.top);
		resourceTable.setHeight(40);
		resourceTable.setPosition(minimap.getWidth(), actionsWindow.getHeight());

		selectedTable = new Table();
		selectedTable.align(Align.left | Align.top);
		selectedTable.setHeight(40);
		selectedTable.setPosition(minimap.getWidth(), actionsWindow.getHeight()+resourceTable.getHeight());

		LOGGER.debug("Creating resource labels");
		rockCount = new Label("Rock: 0", skin);
		crystalCount = new Label("Crystal: 0", skin);
		biomassCount = new Label("Biomass: 0", skin);
		popCount = new Label("0 ", skin);
		maxPopCount = new Label(" / 10", skin);

		//add resource images 
		Image rock = new Image(textureManager.getTexture("rock_HUD"));
		Image biomass = new Image(textureManager.getTexture("biomass_HUD"));
		Image crystal = new Image(textureManager.getTexture("crystal_HUD"));

		resourceTable.add(rock).width(40).height(40).pad(10);
		resourceTable.add(rockCount).padRight(50);
		resourceTable.add(crystal).width(40).height(40).pad(10);
		resourceTable.add(crystalCount).padRight(50);
		resourceTable.add(biomass).width(40).height(40).pad(10);
		resourceTable.add(biomassCount).padRight(50);
		resourceTable.add(popCount).padRight(10);
		resourceTable.add(maxPopCount);

		stage.addActor(resourceTable);
		stage.addActor(selectedTable);

		peonButton = new TextButton("Select a Unit", skin);
		helpText = new Label("Welcome to SpacWars!", skin);

		actionsWindow.add(peonButton);
		actionsWindow.add(helpText);
		actionsWindow.setMovable(false);
		actionsWindow.align(Align.topLeft);
		actionsWindow.setWidth(stage.getWidth()-500);
		actionsWindow.setPosition(220, 0);

		//Add action buttons
		addButtonArray();

		stage.addActor(actionsWindow);
	}

	private void addButtonArray() {
		buttonList = new ArrayList<>();
		for (int i = 0; i != NUMBER_ACTION_BUTTONS; i++) {
			TextButton newButton = new TextButton(" ", skin);
			addButtonListener(newButton);
			buttonList.add(newButton);
			actionsWindow.add(newButton);
			enableButton(newButton);
		}
	}

	private void addButtonListener(Button button) {

		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int index = buttonList.indexOf(actor); //Get the index of this button
				if (index < currentActions.size() && index >= 0) {
					Object current = currentActions.get(index);
					if (current instanceof ActionType) {
						if (((ActionType)current) == ActionType.BUILDGATE) {
							selectedEntity.setAction(new BuildGateAction((BuildingEntity) selectedEntity));
						}else {
							selectedEntity.setNextAction((ActionType)current);
						}
					}
					if (current instanceof ActionType) {
						if (((ActionType)current) == ActionType.UNLOAD) {
							selectedEntity.setAction(new UnloadAction((BuildingEntity) selectedEntity));
						}else {
							selectedEntity.setNextAction((ActionType)current);
						}
					}else if (currentActions.get(index) instanceof BuildingType) {
						LOGGER.info("Try to build");
						if (currentActions.get(index) == BuildingType.WALL) {
							if (selectedEntity.getAction().isPresent() && selectedEntity.getAction().get() instanceof BuildWallAction) {
								BuildWallAction cancelBuild = (BuildWallAction) selectedEntity.getAction().get();
								cancelBuild.cancelBuild();
								cancelBuild.doAction();
							}
							if (selectedEntity.getAction().isPresent() && selectedEntity.getAction().get() instanceof BuildAction) {
								BuildAction cancelBuild = (BuildAction) selectedEntity.getAction().get();
								cancelBuild.cancelBuild();
								cancelBuild.doAction();
							}
							selectedEntity.setAction(new BuildWallAction(selectedEntity));
						}
						else {
							if (selectedEntity.getAction().isPresent() && selectedEntity.getAction().get() instanceof BuildWallAction) {
								BuildWallAction cancelBuild = (BuildWallAction) selectedEntity.getAction().get();
								cancelBuild.cancelBuild();
								cancelBuild.doAction();
							}
							if (selectedEntity.getAction().isPresent() && selectedEntity.getAction().get() instanceof BuildAction) {
								BuildAction cancelBuild = (BuildAction) selectedEntity.getAction().get();
								cancelBuild.cancelBuild();
								cancelBuild.doAction();
							}
							selectedEntity.setAction(new BuildAction(selectedEntity, (BuildingType) currentActions.get(index)));
						}
					} else {
						if((EntityID) currentActions.get(index) == EntityID.COMMANDER) {
							if(heroExist != null) {
								if(heroExist.getHealth() <= 0) {
									heroExist.setHealth(heroExist.getMaxHealth());
									heroExist.setPosition(selectedEntity.getPosX(), selectedEntity.getPosY(), 0);
									heroExist.setEmptyAction();
									GameManager.get().getWorld().addEntity(heroExist);
									
								}
								return;
							}
						}
						ActionSetter.setGenerate(selectedEntity, (EntityID) currentActions.get(index));
					}
					}

			}
		});
	}

	/**
	 * Adds in the minimap window
	 */
	private void addMiniMapMenu(){
		LOGGER.debug("Creating minimap menu");
		//the minimap wont look right until the skin is changed to something reasonable, without a title/title-bar
		//TODO update the skin
		minimap = new Window("", skin, "clear");

		//set the properties of the minimap window
		GameManager.get().getMiniMap().stageReference = minimap;
		minimap.add(GameManager.get().getMiniMap().getBackground());
		minimap.setMovable(false);
		minimap.setWidth(GameManager.get().getMiniMap().getWidth());
		minimap.setHeight(GameManager.get().getMiniMap().getHeight());

		//add the map window to the stage
		stage.addActor(minimap);
	}

	/**
	 *
	 * adds everything in GameManager.get().getMiniMap().getEntitiesOnMap() to the minimap
	 */
	private void addEntitiesToMiniMap() {
		MiniMap miniMap = GameManager.get().getMiniMap();
		for (int i = 0; i < miniMap.getWidth(); i++) {
			for (int j = 0; j < miniMap.getHeight(); j++) {
				if (miniMap.miniMapDisplay[i][j] > 0) { // if there is a unit there, add it on to the minimap
					//TODO add a case for if there WAS a friendly unit there but NOW an enemy unit is there
					MiniMapEntity entity = miniMap.getEntity(i, j);
					if (miniMap.entitiesOnMiniMap[i][j] == null && entity.toBeDisplayed()) {
						// skip if there is already an icon there or if the entity is concealed by the fog
						try {
                            miniMap.entitiesOnMiniMap[i][j] = new Image(textureManager.getTexture(entity.getTexture()));
                            miniMap.entitiesOnMiniMap[i][j].setPosition(i, j);
							stage.addActor(miniMap.entitiesOnMiniMap[i][j]);
						} catch (NullPointerException e) {
							// entity hasn't reached that position yet so do nothing
						}
					}
				}
			}
		}
	}

	/**
	 * Clears the currently displayed minimap
	 * then updates the image from the texture manager.
	 */
	public void updateMiniMapMenu() {
		try {
			//clear the current image
			minimap.clearChildren();
			//get the new image
			minimap.add(GameManager.get().getMiniMap().getBackground());
		}
		catch(NullPointerException NPE){
			LOGGER.error("NULL child in minimap table");
			return;
		}
	}

	/**
     * Display an attackable unit's stats once it's been selected
     * If target is a hero, display inventory
     * @param target unit clicked on by player
     */
    private void setEnitity(BaseEntity target) {
		for (int i = 0; i < NUMBER_ACTION_BUTTONS; i++) { //Disable buttons
			disableButton(buttonList.get(i));
		}
		selectedTable.clear();
		if (selectedEntity == null) { //If there is not selected entity hide the stats then return
			this.statsTable.setVisible(false);
			this.statsbg.setVisible(false);
			return;
		}
		populateSelectedTable();
		updateHealthBars();
		selectedEntity = target;
		if (selectedEntity instanceof Astronaut) { //For Testing Purposes
			selectedEntity.giveAllBuilding();
		}
		if (target instanceof AttackableEntity) {
			// display the stats once a unit been selected
			this.statsTable.setVisible(true);
			this.statsbg.setVisible(true);
			this.statsTable.updateSelectedStats(((AttackableEntity) target));

			// display hero inventory
			this.statsTable.hideInventory();
			heroSelected = null;
			if (target instanceof Commander) {
				// display the inventory once a commander been selected
				heroSelected = (Commander) target;
				if (!heroSelected.isAi()) {
					this.statsTable.showInventory();
					this.statsTable.updateHeroInventory((Commander) target);
				}
			}
		}
		if (!target.isAi()) {
			if (target instanceof BuildingEntity && ((BuildingEntity)target).toString().equals("TechBuilding")
					&& ((BuildingEntity)target).getBuilt()) {
				loadTechMenu();
			}
			currentActions = target.getValidActions();
			enterActions(true); //Set up the buttons
		}
	}

	private void updateHealthBars() {
		for (BaseEntity b : selectedList) {
			if (b instanceof Soldier){
				b.getHealthBar();
			}
		}

	}

	/**
	 * Handler for the size buttons of the customization menu
	 *
	 * @param button the button that will use the handler
	 * @param mapSize the new map size
	 */
	private void sizeButtonHandler(TextButton button, MapSizeTypes mapSize){
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				CustomizedWorld oldWorld = (CustomizedWorld) GameManager.get().getWorld();
				MapContainer map = new MapContainer(oldWorld.getMapType(), mapSize);
				CustomizedWorld world = new CustomizedWorld(map);
				GameManager.get().setWorld(world);
				world.loadMapContainer(map);
			}
		});
	}

	/**
	 * Handler for the map types buttons of the customization menu
	 *
	 * @param button the button that will use the handler
	 * @param mapType the new map type
	 */
	private void typesButtonHandler(TextButton button, MapTypes mapType){
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				CustomizedWorld oldWorld = (CustomizedWorld) GameManager.get().getWorld();
				MapContainer map = new MapContainer(mapType, oldWorld.getMapSizeType());
				CustomizedWorld world = new CustomizedWorld(map);
				GameManager.get().setWorld(world);
				world.loadMapContainer(map);
			}
		});
	}


	public void generateTextures(int number) {
		PixmapIO pIO = new PixmapIO();
		for (int i = 0; i <= number; i++) {
			FileHandle f = new FileHandle("resources/UnitAssets/HealthBar/Health" + i + ".png");
			int width = 512;
			int fillPoint = (width * i) / number;
			Pixmap p = new Pixmap(width, 20, Pixmap.Format.RGBA8888);
			p.setColor(Color.GRAY);
			p.fill();
			if (i == 0) {
				p.dispose();
				continue;
			}
			p.setColor(Color.GREEN);
			if (i < number/2) p.setColor(Color.ORANGE);
			if (i < number/8) p.setColor(Color.RED);
			p.fillRectangle(0,0,fillPoint,20);
			pIO.writePNG(f,p);
			p.dispose();
		}
	}

    /**
     * Enables action button based on the actions avaliable to
     * the selected entity
     */

	private void enterActions(boolean display) {
		if (currentActions == null) { //If the selected unit has no actions return
			return;
		}
		for (int i = 0; i < currentActions.size(); i++) { //Enable a button for each action
			enableButton(buttonList.get(i));
		}
		//Format all actions
		actionsButtons();

		actionsWindow.setVisible(display);
		// Sets text of button if display doesn't show
        for (int i = 0; i < currentActions.size(); i++) {
				enableButton(buttonList.get(i));
				if (currentActions.get(i) instanceof ActionType) { //If it is an action
					buttonList.get(i).setText(ActionSetter.getActionName((ActionType)currentActions.get(i)));
				} else { //If it isnt an action it is something to build
					buttonList.get(i).setText("Build " + currentActions.get(i).toString());
				}
        }
    }
    /**
     * Sets up all buttons for available actions
     */
	private void actionsButtons() {
		float buttonWidth = (actionsWindow.getWidth() - actionsWindow.getPadX())/ currentActions.size() *.7f;
		float buttonHeight = actionsWindow.getHeight();
		if (buttonWidth >= (actionsWindow.getWidth()/4)){
			buttonWidth = (actionsWindow.getWidth()/4);
		}
		
		int index = 0;
		int owner = currentActions.getActor().getOwner();
		for (Object e : currentActions.getallActions()) {
			buttonList.get(index).setVisible(true);
			buttonList.get(index).clearChildren();
			Label name = new Label("", skin);
			Label costRocks = new Label("", skin);
			Label costCrystal = new Label("", skin);
			Label costBiomass = new Label("", skin);
			Image rock = new Image(textureManager.getTexture("rock_HUD"));
			Image biomass = new Image(textureManager.getTexture("biomass_HUD"));
			Image crystal = new Image(textureManager.getTexture("crystal_HUD"));
			Texture entity = textureManager.getTexture("PLACEHOLDER");
			boolean dispRock = false;
			boolean dispCrystal = false;
			boolean dispBiomass = false;
			if (e instanceof BuildingType) {
				if (((BuildingType)e) == BuildingType.WALL) {
					entity = textureManager.getTexture(textureManager.getBuildingSprite(((BuildingType)e).getTextName(), 
							((ColourManager) GameManager.get().getManager(ColourManager.class)).getColour(owner), "1"));
				}
				else {
					entity = textureManager.getTexture(textureManager.getBuildingSprite(((BuildingType)e).getTextName(), 
							((ColourManager) GameManager.get().getManager(ColourManager.class)).getColour(owner), "3"));
				}
				name = new Label(e.toString(), skin);
				costRocks = new Label(String.valueOf(((BuildingType) e).getCost()), skin);
				dispRock = true;
			} else if (e instanceof EntityID) {
				entity = textureManager.getTexture((textureManager.loadUnitSprite((EntityID) e, owner)));
				name = new Label(((EntityID) e).name(), skin);

				int valRock = ((EntityID) e).getCostRocks();
				int valCrystal = ((EntityID) e).getCostCrystals();
				int valBiomass = ((EntityID) e).getCostBiomass();
				costRocks = new Label(String.valueOf(valRock), skin);
				costCrystal = new Label(String.valueOf(valCrystal), skin);
				costBiomass = new Label(String.valueOf(valBiomass), skin);
				costCrystal.setColor(Color.PURPLE);
				costBiomass.setColor(Color.GREEN);
				if (valRock>0) {
					dispRock = true;
				}
				if (valBiomass>0) {
					dispBiomass = true;
				}
				if (valCrystal>0) {
					dispCrystal = true;
				}

			} else if (e instanceof ActionType) {
				entity = textureManager.getTexture("PLACEHOLDER");
				name = new Label(e.toString(), skin);
			}
			name.setFontScale((1.2f/(float)(Math.pow(name.getText().length,.2f))));
			costRocks.setFontScale(.7f);
			costCrystal.setFontScale(.7f);
			costBiomass.setFontScale(.7f);
			TextureRegion entityRegion = new TextureRegion(entity);
			TextureRegionDrawable buildPreview = new TextureRegionDrawable(entityRegion);
			ImageButton addPane = new ImageButton(buildPreview);
			Label spacer = new Label("", skin);
			buttonList.get(index).add(addPane).width(buttonWidth * .6f).height(buttonHeight * .5f);
			buttonList.get(index).row().padBottom(20);
			buttonList.get(index).add(spacer).width(buttonWidth * .4f).height(buttonHeight * .2f).align(Align.left).padTop(20).padRight(0);
			buttonList.get(index).add(name).width(buttonWidth * .4f).align(Align.left).height(buttonHeight * .2f)
			.padLeft(-.63f*buttonWidth).padRight(.2f*buttonWidth).padTop(-.2f * buttonHeight);
			if (dispRock) {
				buttonList.get(index).add(rock).width(buttonWidth * .1f).align(Align.right).height(buttonHeight * .2f)
				.padTop(-.2f * buttonHeight).padLeft(-1.85f*buttonWidth);
				buttonList.get(index).add(costRocks).width(buttonWidth * .1f).align(Align.right).height(buttonHeight * .2f)
						.spaceRight(0f*buttonWidth).padTop(-.6f * buttonHeight).padLeft(-1.85f*buttonWidth);;
			}
			if (dispCrystal) {
				buttonList.get(index).add(crystal).width(buttonWidth * .1f).align(Align.right).height(buttonHeight * .2f)
						.padLeft(-.0f*buttonWidth).padRight(.0f*buttonWidth).padTop(-.2f * buttonHeight);
				buttonList.get(index).add(costCrystal).width(buttonWidth * .1f).align(Align.right).height(buttonHeight * .2f)
				.padLeft(-.1f*buttonWidth).spaceRight(.02f*buttonWidth).padTop(-.6f * buttonHeight);
			}
			if (dispBiomass) {
				buttonList.get(index).add(biomass).width(buttonWidth * .1f).align(Align.right).height(buttonHeight * .2f)
						.padLeft(-.0f*buttonWidth).padRight(.0f*buttonWidth).padTop(-.2f * buttonHeight);
				buttonList.get(index).add(costBiomass).width(buttonWidth * .1f).align(Align.right).height(buttonHeight * .2f)
				.padLeft(-.1f*buttonWidth).spaceRight(.02f*buttonWidth).padTop(-.6f * buttonHeight);
			}

			index++;
		}
	}

	/**
	 * Disables the given button
	 * @param b to be disabled
	 */
	private void disableButton(Button b) {
		b.setTouchable(Touchable.disabled);
		b.setVisible(false);
		b.align(Align.right | Align.bottom);
	}

	/**
	 * Enables the given button
	 * @param b to be enabled
	 */
	private void enableButton(Button b) {
		b.setTouchable(Touchable.enabled);
		b.setVisible(true);
		b.align(Align.left | Align.top);
	}


	/**
	 * Returns the chat window
	 * @return chat window
	 */
	public ChatBox getChatWindow() {
		return chatbox;
	}

	/**
	 * Returns the Actions window to display helper text
	 */
	public Window getActionWindow() {
		return actionsWindow;
	}

	/**
	 * Updates any features of the HUD that may change through time/ game actions
	 * @param lastMenuTick
	 */
	public void render(long lastMenuTick){
		/* Update time & set color depending if night/day */
		gameTimeDisp.setText(timeManager.toString());
		gameLengthDisp.setText(timeManager.getPlayClockTime());

		/*Update Minimap*/
		actionsWindow.toBack();
		addEntitiesToMiniMap();
		this.updateMiniMapMenu();

		/*Update the resources count*/
		ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		rockCount.setText("" + resourceManager.getRocks(-1));
		crystalCount.setText("" + resourceManager.getCrystal(-1));
		biomassCount.setText("" + resourceManager.getBiomass(-1));
		popCount.setText("" + resourceManager.getPopulation(-1));
		maxPopCount.setText("/ " + resourceManager.getMaxPopulation(-1));
		//Get the selected entity
		selectedEntity = null;
		selectedList.clear();
		for (BaseEntity e : gameManager.get().getWorld().getFloodableEntityList()) {
			if (e.isSelected()) {
				selectedList.add(e);
			}
			if (e instanceof Commander) {
				if (!heroMap.contains((Commander)e)) {
					heroMap.add((Commander) e);
					/*
					 * only add your own commander as option in shop to buy items for. May need to change condition when
					 * multiplayer gets implemented
					 */
					if (!e.isAi()) {
						shopDialog.connectHero((Commander) e);
						heroExist = (Commander) e;
					}
				}
			}
		}
		if (selectedList.size() > 0)
		    selectedEntity = selectedList.get(0);
		//Get the details from the selected entity
	    setEnitity(selectedEntity);

		//Will check all of the specified hotkeys to see if any have been pressed
		hotkeys.checkKeys();

		if(TimeUtils.nanoTime() - lastMenuTick > 100000) {
			getActionWindow().removeActor(peonButton);
			getActionWindow().removeActor(helpText);

			boolean somethingSelected = false;
			for (Renderable e : GameManager.get().getWorld().getEntities()) {
				if ((e instanceof Selectable) && ((Selectable) e).isSelected()) {
					somethingSelected = true;
				}
			}
			lastMenuTick = TimeUtils.nanoTime();

		}
			
		if (!gameStarted){
			LOGGER.debug("Game just started, toggle activew view");
			gameStarted = true;
		}

	}

	/**
	 * Sets every HUD element invisible
	 */
	public void disableHUD() {
		overheadRight.setVisible(false);
		resourceTable.setVisible(false);
	    hudManip.setVisible(false);

		chatbox.setVisible(false);
		minimap.setVisible(false);
		actionsWindow.setVisible(false);
		actionsBg.setVisible(false);
	}

	/**
	 * Sets every HUD element visible
	 */
	public void enableHUD() {
		overheadRight.setVisible(true);
		resourceTable.setVisible(true);
	    hudManip.setVisible(true);
	    actionsBg.setVisible(true);
		minimap.setVisible(true);
		actionsWindow.setVisible(true);
	}

	/**
	 * This function is used to refit the hud component when the window size changes
	 * @param width the stages width
	 * @param height the stages height
	 */
	public void resize(int width, int height) {
        //Top Left
        LOGGER.debug("Window resized, rescaling hud");
		statsTable.setWidth(100);
		statsTable.align(Align.left | Align.top);
		statsTable.setPosition( BUTTONPAD, stage.getHeight()-BUTTONPAD);
		statsbg.setPosition(BUTTONPAD, stage.getHeight()-statsbg.getHeight()-BUTTONPAD);
		statsbg.toBack();
		
        //Top of panel
        overheadRight.align(Align.right | Align.top);
        overheadRight.setPosition(width-overheadRight.getWidth()-BUTTONPAD, Gdx.graphics.getHeight()-overheadRight.getHeight()-BUTTONPAD);
        welcomeMsg.setWidth(Gdx.graphics.getWidth());
        headerbg.setPosition(width-headerbg.getWidth()-BUTTONPAD, height-headerbg.getHeight()-BUTTONPAD);
        headerbg.setHeight(BUTTONSIZE +  2 * BUTTONPAD);

		welcomeMsg.setPosition(0, Gdx.graphics.getHeight());
		welcomeMsg.align(Align.center | Align.top).pad(BUTTONPAD*2);
		chatbox.align(Align.right | Align.top);
		chatbox.setPosition(2, 230);
		//Bottom Panel
		//Map
		minimap.align(Align.topLeft);
		minimap.setPosition(0, 0);
		minimap.setSize(220, 220);
		//Avaliable actions
		actionsWindow.align(Align.topLeft);
		actionsWindow.setWidth(stage.getWidth()-minimap.getWidth()/2 - BUTTONPAD);
		actionsWindow.setHeight(minimap.getHeight()/2 - BUTTONPAD);
		actionsWindow.setPosition(minimap.getWidth()/2, BUTTONPAD);
		actionsBg.setPosition(ACTIONSHEADERPAD, actionsWindow.getHeight() + BUTTONPAD);
		actionsBgMain.setPosition(Gdx.graphics.getWidth()-minimap.getWidth()/2, BUTTONPAD);
		actionsBgMain.setHeight(actionsWindow.getHeight());
		actionsBgMain.setWidth(actionsWindow.getWidth());

		//Resources
		resourceTable.align(Align.left | Align.center);
		resourceTable.setHeight(60);
		resourceTable.setPosition(minimap.getWidth(), actionsWindow.getHeight() + BUTTONPAD*2);
		//Menu manipulator
		hudManip.setPosition(Gdx.graphics.getWidth()-BUTTONSIZE-BUTTONPAD*2, resourceTable.getY() + BUTTONPAD*2);

		//resize stats
		stats.resizeStats(width, height);
		
		//resize help window
		help.setPosition(width/2 - help.getWidth()/2, height/2 - help.getHeight()/2);

    }
	
	public void setTechTree(Dialog techtree) {
		this.techTree = techtree;
	}
	
	public void hideTechTree() {
		this.techTree.hide();
	}
	
	public void setPause(Dialog pause) {
		this.pauseMenu = pause;
	}
	
	public void hidePause() {
		this.pauseMenu.hide();
	}

	/**When used in the code will set the pauseCheck integer to 1 when there
	 * is an active Pause menu and 0 otherwise
	 */
	public void setPauseCheck(int i) {
		pauseCheck = i;
	}

	/**
	 * returns the integer value curently assigned to the pauseCheck variable
	 * @return int
	 */

	public int getPauseCheck() {
		return pauseCheck;
	}

	/**
	 * When used in the code will set the chatActiveCheck integer to 1 when the chat window
	 * toggle is true and 0 otherwise.  Instead of stopping a new instance being created, will be used to
	 * stop the other hotkeys from being used while the chatbox is visible
	 * @param i
	 */
	public void setChatActiveCheck(int i) {
		chatActiveCheck = i;
	}

	/**
	 * returns the value stored in the ChatActiveCheck variable
	 * @return int
	 */
	public int getChatActiveCheck() {
		return chatActiveCheck;
	}

	/**
	 * When used in the code will set the exitCheck integer to 1 when there
	 * is an active dialog asking the user if they wish to quit, and 0 otherwise
	 * @param i
	 */
	public void setExitCheck(int i) {
		exitCheck = i;
	}

	/**
	 * returns the value in the exitCheck variable
	 * @return int
	 */
	public int getExitCheck() {
		return exitCheck;
	}

	/**
	 * When used in the code will set the techCheck integer to 1 when the tech
	 * tree is visible and 0 otherwise
	 * @param i
	 */
	public void setTechCheck(int i) {
		techCheck = i;
	}

	/**
	 *  returns the value stored in the techCheck variable
	 * @return int
	 */
	public int getTechCheck() {
		return techCheck;
	}

	/**
	 * When used in the code will set the helpCheck integer to 1 when there
	 * is an active Help window and 0 otherwise
	 * @param i
	 */
	public void setHelpCheck(int i) {
		helpCheck = i;

	}

	/**
	 *  returns the value stored in the helpCheck variable
	 * @return int
	 */
	public int getHelpCheck() {
		return helpCheck;
	}

	/**
	 * returns the boolean stored in the inventoryToggle variable for testing purposes
	 * @return boolean
	 */

	public boolean isInventoryToggle() {
		return inventoryToggle;
	}

	/**
	 * sets the inventoryToggle variable
	 * @param inventoryToggle
	 */
	public void setInventoryToggle(boolean inventoryToggle) {
		this.inventoryToggle = inventoryToggle;
	}


	/**
	 * Returns the spawn menu. This can throw a NullPointer exception
	 * if called before the hud is instantiated.
	 *
	 * @return the spawn menu
	 */
	public SpawnMenu getSpawnMenu(){
		return this.spawnMenu;
	}

	/**
	 * Returns the current selected entity.
	 *
	 * @return an entity selected, null if nothing is selected
	 */
	public BaseEntity getSelectedEntity(){
		return this.selectedEntity;
	}
	
	/**
	 * Makes the chat box visible on the screen and sets the appropriate flags.
	 */
	public void showChatBox() {
	    chatbox.enableTextField();
        chatbox.setVisible(true);
        messageToggle = true;
        hud.setChatActiveCheck(1);
        stage.setKeyboardFocus(chatbox.getMessageField());
    }
	
	/**
	 * Makes the chat box not visible and sets the appropriate flags.
	 */
	public void hideChatBox() {
	    chatbox.disableTextField();
        chatbox.setVisible(false);
        messageToggle = false;
        hud.setChatActiveCheck(0);
        stage.unfocusAll();
	}


	private void populateSelectedTable() {
		for (BaseEntity be: selectedList) {
			EntityPortrait ep = be.getPortrait();
			if (ep == null) continue;
			selectedTable.add(ep);
		}
	}

}
