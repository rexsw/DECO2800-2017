

package com.deco2800.marswars.hud;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.deco2800.marswars.actions.ActionList;
import com.deco2800.marswars.actions.ActionSetter;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.BuildAction;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.EntityID;
import com.deco2800.marswars.entities.Selectable;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.managers.*;
import com.deco2800.marswars.renderers.Renderable;
import com.deco2800.marswars.worlds.CustomizedWorld;
import com.deco2800.marswars.worlds.MapSizeTypes;
import com.deco2800.marswars.worlds.map.tools.MapContainer;
import com.deco2800.marswars.worlds.map.tools.MapTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final int BUTTONSIZE = 50; //sets size of image buttons
	private static final int BUTTONPAD = 10;  //sets padding between image buttons 
	private static final int NUMBER_ACTION_BUTTONS = 10; //The maximum number of buttons

	private  static final int[] INDICES = {1,2,3,4,5,6,7,8,9,10};

	private Stage stage;
	private Skin skin;
	private ImageButton quitButton;
	private ImageButton helpButton;
	private ImageButton messageButton;
	private ImageButton cheatButton;

	//HUD elements 
	private Table overheadRight; //contains all basic quit/help/chat buttons
	Table resourceTable; //contains table of resource images + count
    Table HUDManip;		 //contains buttons for toggling HUD + old menu
    private Table welcomeMsg; 	 //contains welcome message 
	private UnitStatsBox statsTable; //contains player icon, health and game stats
	private ChatBox chatbox;	 //table for the chat
	private Window messageWindow;//window for the chatbox 
	Window minimap;		 //window for containing the minimap
	Window actionsWindow;    //window for the players actions 
	private ShopDialog shopDialog; // Dialog for shop page

	private SpawnMenu spawnMenu; // customized menu that displays available entities to be spawned

	private Button peonButton;
	private Label helpText;

	//Resources count
	private Label rockCount;
	private Label crystalCount;
	private Label biomassCount;
	private Label waterCount;
	private Label popCount;
	private Label maxPopCount;

	//Action buttons
	private List<TextButton> buttonList;
	private ActionList currentActions;


	//Toggles; checks if the feature is visible on-screen or not
	private boolean messageToggle; 
	private boolean inventoryToggle; 
	private boolean fogToggle = true; 
	private boolean gameStarted = false;
	//Image buttons to display/ remove lower HUD 
	ImageButton dispActions;//Button for displaying actions window 
	ImageButton removeActions; //button for removing actions window 

	private TextureRegionDrawable plusRegionDraw;
	private TextureRegionDrawable minusRegionDraw;

	//Time displays
	private Label gameTimeDisp;
	private Label gameLengthDisp;

	//Managers
	private GameManager gameManager;
	private TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);
	private TextureManager textureManager; //for loading in resource images

	private BaseEntity selectedEntity;	//for differentiating the entity selected
	
	// hero manage
	private HashSet<Commander> heroMap = new HashSet<>();
	private Commander heroSelected;
	
	private GameStats stats;
	
	HUDView hud = this;
	Hotkeys hotkeys;
	
	int pauseCheck = 0;
	int helpCheck = 0;
	int techCheck = 0;
	int chatActiveCheck = 0;
	int cheatActiveCheck = 0;
	int exitCheck = 0;


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

		this.chatbox = new ChatBox(skin, textureManager, this);

		
		//initialise the minimap and set the image
		MiniMap m = new MiniMap("minimap", 220, 220);
		GameManager.get().setMiniMap(m);
		GameManager.get().getMiniMap().updateMap(this.textureManager);
		
		//create the HUD + set gui to GM 
		createLayout();
		GameManager.get().setGui(this);
	}

	/**
	 * Adds in all components of the HUD.
	 */
	private void createLayout(){
		topRight();
		addPlayerDetails();
		addMessages();
		addBottomPanel();

		
		this.hotkeys = new Hotkeys(stage, skin, this, this.stats, this.messageWindow);
	}

	/**
	 * Contains top right section of the HUD to be displayed
	 * on screen and set to stage.
	 * This includes the message tab, help button and quit button
	 */
	private void topRight(){
		overheadRight = new Table();
		overheadRight.setWidth(stage.getWidth());
		overheadRight.align(Align.right | Align.top);
		overheadRight.setPosition(0, Gdx.graphics.getHeight());

		LOGGER.debug("Add help, quit and message buttons");

		//add dispMainMenu image
		Texture menuImage = textureManager.getTexture("menu_button");
		HUDManip = new Table(); //adding buttons into a table
		HUDManip.setPosition(stage.getWidth()-50, 50);
		TextureRegion menuRegion = new TextureRegion(menuImage);
		TextureRegionDrawable menuRegionDraw = new TextureRegionDrawable(menuRegion);
		ImageButton dispMainMenu = new ImageButton(menuRegionDraw);


		//create help button + image for it
		Texture helpImage = textureManager.getTexture("help_button");
		TextureRegion helpRegion = new TextureRegion(helpImage);
		TextureRegionDrawable helpRegionDraw = new TextureRegionDrawable(helpRegion);
		helpButton = new ImageButton(helpRegionDraw);

		//create message button + image for it
		Texture messageImage = textureManager.getTexture("chat_button");
		TextureRegion messageRegion = new TextureRegion(messageImage);
		TextureRegionDrawable messageRegionDraw = new TextureRegionDrawable(messageRegion);
		messageButton = new ImageButton(messageRegionDraw);


		//add quit button + image for it
		Texture quitImage = textureManager.getTexture("quit_button");
		TextureRegion quitRegion = new TextureRegion(quitImage);
		TextureRegionDrawable quitRegionDraw = new TextureRegionDrawable(quitRegion);
		quitButton = new ImageButton(quitRegionDraw);

		//Create + align time displays 
		LOGGER.debug("Creating time labels");
		gameTimeDisp = new Label("0:00", skin);
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
		overheadRight.add(messageButton).padRight(BUTTONPAD);
		overheadRight.add(helpButton).padRight(BUTTONPAD);
		overheadRight.add(dispMainMenu).padRight(BUTTONPAD);
		overheadRight.add(quitButton).padRight(BUTTONPAD);

		welcomeMsg = new Table();
		welcomeMsg.setWidth(stage.getWidth());
		welcomeMsg.align(Align.center | Align.top).pad(BUTTONPAD*2);
		welcomeMsg.setPosition(0, Gdx.graphics.getHeight());
		//Image clockbgImage1 = new Image(textureManager.getTexture("logo"));
		Label welcomeText = new Label("Welcome to Spacwars!", skin);
		welcomeMsg.add(welcomeText);

		stage.addActor(welcomeMsg);
		stage.addActor(overheadRight);

		//Creates the help button listener
		LOGGER.debug("Creating help button listener");
		helpButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				new WorkInProgress("Help  Menu", skin, hud).show(stage);
			}
		});

		//Creates the quit button listener
		LOGGER.debug("Creating quit button listener");
		quitButton.addListener(new ChangeListener() {
			@Override
			//could abstract this into another class
			public void changed(ChangeEvent event, Actor actor) {
				new ExitGame("Quit Game", skin, hud, true).show(stage);
			}});

		//Creates the message button listener
		LOGGER.debug("Creating message button listener");
		messageButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				if (messageToggle){
					messageWindow.setVisible(false);

					messageToggle = false; 
					hud.setChatActiveCheck(0);

				} else {
					messageWindow.setVisible(true);
					messageToggle = true;
					hud.setChatActiveCheck(1);
				}

			}
		});

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
		statsTable.pad(5);
		this.statsTable.setVisible(false);
		stage.addActor(statsTable);
	}

	/**
	 * Implements a collapsible tab for the chat lobby
	 */
	private void addMessages(){
		LOGGER.debug("Creating chat lobby box");
		messageWindow = new Window("Chat Lobby", skin);
		messageWindow.setMovable(false);
		messageWindow.setPosition(stage.getWidth()-chatbox.getWidth()-BUTTONPAD,
				Math.round(stage.getHeight()-chatbox.getHeight()-BUTTONPAD*4-BUTTONSIZE));
		messageWindow.add(chatbox);
		messageWindow.setVisible(false);
		messageWindow.pack();

		stage.addActor(messageWindow);
	}



	/**
	 * Adds in the bottom panel of the HUD
	 */
	private void addBottomPanel(){
		addMiniMapMenu();
		addInventoryMenu();


		LOGGER.debug("Creating HUD manipulation buttons");
		
		shopDialog = new ShopDialog("Shop", skin, textureManager);
		//remove dispActions button + image for it 

		Texture minusImage = textureManager.getTexture("minus_button");
		TextureRegion minusRegion = new TextureRegion(minusImage);
		minusRegionDraw = new TextureRegionDrawable(minusRegion);
		removeActions = new ImageButton(minusRegionDraw);

		//add dispActions image
		Texture plusImage = textureManager.getTexture("plus_button");
		TextureRegion plusRegion = new TextureRegion(plusImage);
		plusRegionDraw = new TextureRegionDrawable(plusRegion);
		dispActions = new ImageButton(plusRegionDraw);

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
		
		//add toggle Fog of war (FOR DEBUGGING) 
		Button dispFog = new TextButton("Fog", skin);
				

		HUDManip.setSize(50, 80);
		HUDManip.pad(BUTTONPAD);
		HUDManip.add(dispTech).pad(BUTTONPAD);
		HUDManip.add(dispFog).pad(BUTTONPAD);
		HUDManip.add(dispShop).padRight(BUTTONPAD).width(50).height(50);
		HUDManip.add(removeActions).pad(BUTTONPAD);

		stage.addActor(HUDManip);

		dispActions.addListener(new ChangeListener() {
			@Override
			/*displays the (-) button for setting the hud to invisible*/
			public void changed(ChangeEvent event, Actor actor) {
				if (isInventoryToggle()) {
					LOGGER.debug("Enable hud");
					actionsWindow.setVisible(true);
					minimap.setVisible(true);
					resourceTable.setVisible(true);
					//show (-) button to make resources invisible
					dispActions.remove();
					HUDManip.add(removeActions);
					setInventoryToggle(false);
				}
			}
		});

		removeActions.addListener(new ChangeListener() {
			@Override
			/*displays the (+) button for setting the hud to visible*/
			public void changed(ChangeEvent event, Actor actor) {
				LOGGER.debug("Disable Hud");
				actionsWindow.setVisible(true);
				minimap.setVisible(false);
				resourceTable.setVisible(false);
				//show (+) to show resources again
				removeActions.remove();
				HUDManip.add(dispActions);
				setInventoryToggle(true);
			}
		});

		dispTech.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				new TechTreeView("TechTree", skin, hud).show(stage);
			}

		});

		dispShop.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				shopDialog.show(stage);
			}
		});
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
		

		dispFog.addListener(new ChangeListener() {
			@Override
			/*displays the (-) button for setting the hud to invisible*/
			public void changed(ChangeEvent event, Actor actor) {
				toggleFog();
			}
		});

		spawnMenu = new SpawnMenu(stage, skin);
		spawnMenu.setupEntitiesPickerMenu();
		spawnMenu.addEntitiesPickerMenu();
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
	 * Adds in the selectable menu for the inventory for resources
	 */
	private void addInventoryMenu(){
		LOGGER.debug("Create inventory");
		actionsWindow = new Window("Actions", skin);
		resourceTable = new Table();
		resourceTable.align(Align.left | Align.top);
		resourceTable.setHeight(40);
		resourceTable.setPosition(minimap.getWidth(), actionsWindow.getHeight());

		LOGGER.debug("Creating resource labels");
		rockCount = new Label("Rock: 0", skin);
		crystalCount = new Label("Crystal: 0", skin);
		biomassCount = new Label("Biomass: 0", skin);
		waterCount = new Label("Water: 0", skin);
		popCount = new Label("0 ", skin);
		maxPopCount = new Label(" / 10", skin);

		//add rock image
		Texture rockTex = textureManager.getTexture("rock_HUD");
		Image rock = new Image(rockTex);
		//add water image
		Texture waterTex = textureManager.getTexture("water_HUD");
		Image water = new Image(waterTex);
		//add biomass image
		Texture biomassTex = textureManager.getTexture("biomass_HUD");
		Image biomass = new Image(biomassTex);
		//add crystal image
		Texture crystalTex = textureManager.getTexture("crystal_HUD");
		Image crystal = new Image(crystalTex);

		resourceTable.add(rock).width(40).height(40).pad(10);
		resourceTable.add(rockCount).padRight(60);
		resourceTable.add(crystal).width(40).height(40).pad(10);
		resourceTable.add(crystalCount).padRight(60);
		resourceTable.add(biomass).width(40).height(40).pad(10);
		resourceTable.add(biomassCount).padRight(60);
		resourceTable.add(water).width(40).height(40).pad(10);
		resourceTable.add(waterCount).padRight(50);
		resourceTable.add(popCount).padRight(10);
		resourceTable.add(maxPopCount);

		stage.addActor(resourceTable);

		peonButton = new TextButton("Select a Unit", skin);
		helpText = new Label("Welcome to SpacWars!", skin);

		actionsWindow.add(peonButton);
		actionsWindow.add(helpText);
		actionsWindow.setMovable(false);
		actionsWindow.align(Align.topLeft);
		actionsWindow.setWidth(stage.getWidth()-500);
		actionsWindow.setHeight(150);
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
				if (index < currentActions.size()) {
					Object current = currentActions.get(index);
					if (current instanceof ActionType) {
						selectedEntity.setNextAction((ActionType)current);
					} else if (currentActions.get(index) instanceof BuildingType) {
						LOGGER.info("Is entity");
						LOGGER.info("Try to build");
						if (selectedEntity.getAction().isPresent() && selectedEntity.getAction().get() instanceof BuildAction) {
							BuildAction cancelBuild = (BuildAction) selectedEntity.getAction().get();
							cancelBuild.cancelBuild();
							cancelBuild.doAction();
						}
						selectedEntity.setAction(new BuildAction(selectedEntity, (BuildingType) currentActions.get(index)));
					} else {
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
		minimap = new Window("Map", skin);

		//set the properties of the minimap window
		GameManager.get().getMiniMap().stageReference = minimap;
		minimap.add(GameManager.get().getMiniMap().getBackground());
		minimap.align(Align.topLeft);
		minimap.setPosition(0, 0);
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
					if (miniMap.entitiesOnMiniMap[i][j] == null) { // skip if there is already an icon there
						miniMap.entitiesOnMiniMap[i][j] = new Image(textureManager.getTexture(miniMap.getEntity(i, j).getTexture()));
						miniMap.entitiesOnMiniMap[i][j].setPosition(i, j);
						try {
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
		//clear the current image
		minimap.clearChildren();
		//get the new image
		minimap.add(GameManager.get().getMiniMap().getBackground());
	}

	/**
     * Display an attackable unit's stats once it's been selected
     * If target is a hero, display inventory
     * @param target unit clicked on by player
     */
    private void setEnitity(BaseEntity target) {

		for (int i = 0; i < NUMBER_ACTION_BUTTONS; i++) {
			disableButton(buttonList.get(i));
		}
		if (selectedEntity == null) {
			this.statsTable.setVisible(false);
            return;
        }
		
        selectedEntity = target;
        if (selectedEntity instanceof Astronaut) {
        	selectedEntity.giveAllBuilding();
        }
		currentActions = target.getValidActions();
	    enterActions(true);

		if(target instanceof AttackableEntity) {
			// display the stats once a unit been selected
			this.statsTable.setVisible(true);
			this.statsTable.updateSelectedStats(((AttackableEntity) target));

	        // display hero inventory
	        this.statsTable.hideInventory();
	        heroSelected = null;
	        if(target instanceof Commander) {
	        	// display the inventory once a commander been selected
	        	heroSelected = (Commander) target;
	        	this.statsTable.showInventory();
	        	this.statsTable.updateHeroInventory((Commander)target);
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


    /**
     * Enables action button based on the actions avaliable to 
     * the selected entity
     */

	private void enterActions(boolean display) {
		if (currentActions == null) {
			return;
		}
		for (int i = 0; i < currentActions.size(); i++) {
			enableButton(buttonList.get(i));
		}
		//Format all actions
		ActionButtons();

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
	private void ActionButtons() {
		float buttonWidth = actionsWindow.getWidth()/ currentActions.size();
		float buttonHeight = actionsWindow.getHeight();
		if (buttonWidth >= (actionsWindow.getWidth()/4)){
			buttonWidth = (actionsWindow.getWidth()/4);
		}
		int index = 0;
		for (Object e : currentActions.getallActions()) {
			buttonList.get(index).setVisible(true);
			buttonList.get(index).clearChildren();
			Label name = new Label("", skin);
			Label cost = new Label("", skin);
			Image rock = null;
			Texture entity = textureManager.getTexture("PLACEHOLDER");
			if (e instanceof BuildingType) {
				entity = textureManager.getTexture(((BuildingType) e).getBuildTexture());
				name = new Label(e.toString(), skin);
				cost = new Label(String.valueOf(((BuildingType) e).getCost()), skin);
				Texture rockTex = textureManager.getTexture("rock_HUD");
				rock = new Image(rockTex);
			} else if (e instanceof EntityID) {
				entity = textureManager.getTexture((textureManager.loadUnitSprite((EntityID) e)));
				name = new Label(((EntityID) e).name(), skin);
				cost = new Label(String.valueOf(0), skin);
			} else if (e instanceof ActionType) {
				entity = textureManager.getTexture("PLACEHOLDER");
				name = new Label(e.toString(), skin);
			}
			name.setFontScale(.9f);
			cost.setFontScale(.9f);
			TextureRegion entityRegion = new TextureRegion(entity);
			TextureRegionDrawable buildPreview = new TextureRegionDrawable(entityRegion);
			ImageButton addPane = new ImageButton(buildPreview);
			buttonList.get(index).add(addPane).width(buttonWidth * .6f).height(buttonHeight * .5f);
			buttonList.get(index).row().padBottom(20);
			buttonList.get(index).add(rock).width(buttonWidth * .2f).height(buttonHeight * .2f).align(Align.left).padTop(20).padRight(0);
			buttonList.get(index).add(cost).align(Align.left).height(buttonHeight * .2f).padLeft(-.3f*buttonWidth).padTop(20);
			buttonList.get(index).add(name).width(buttonWidth * .7f).align(Align.right).height(buttonHeight * .2f)
			.padLeft(-.6f*buttonWidth).padRight(.2f*buttonWidth).padTop(-.2f * buttonHeight);
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
	public Window getChatWindow() {
		return messageWindow;
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
		addEntitiesToMiniMap();
		this.updateMiniMapMenu();

		if (timeManager.isNight()){
			gameTimeDisp.setColor(Color.FIREBRICK);
			gameLengthDisp.setColor(Color.FIREBRICK);
		}
		else{
			gameTimeDisp.setColor(Color.BLUE);
			gameLengthDisp.setColor(Color.BLUE);
		}

		/*Update the resources count*/
		ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		rockCount.setText("" + resourceManager.getRocks(-1));
		crystalCount.setText("" + resourceManager.getCrystal(-1));
		waterCount.setText("" + resourceManager.getWater(-1));
		biomassCount.setText("" + resourceManager.getBiomass(-1));
		popCount.setText("" + resourceManager.getPopulation(-1));
		maxPopCount.setText("/ " + resourceManager.getMaxPopulation(-1));
		//Get the selected entity
		selectedEntity = null;
		for (BaseEntity e : gameManager.get().getWorld().getEntities()) {
			if (e.isSelected()) {
				selectedEntity = e;
			}
			if (e instanceof Commander) {
				if (!heroMap.contains((Commander)e)) {
					heroMap.add((Commander) e);
					/*
					 * only add your own commander as option in shop to buy items for. May need to change condition when
					 * multiplayer gets implemented
					 */
					if (!e.isAi()) {
						shopDialog.addHeroIcon((Commander) e);
					}
				}
			}
		}
		//Get the details from the selected entity
	    setEnitity(selectedEntity);
		
		//chat listener
		if(Gdx.input.isKeyJustPressed(Input.Keys.Z) && cheatActiveCheck ==0) {
			if (messageToggle){
				messageWindow.setVisible(false);
				messageToggle = false; 
				this.setChatActiveCheck(0);

			} else {
				messageWindow.setVisible(true);
				messageToggle = true;
				this.setChatActiveCheck(1);
			}
		}
			
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
			GameManager.get().toggleActiveView();
			gameStarted = true;
		}
		
	}

	/**
	 * Sets every HUD element invisible
	 */
	public void disableHUD() {
		overheadRight.setVisible(false);
		resourceTable.setVisible(false);
	    HUDManip.setVisible(false);

		chatbox.setVisible(false);
		messageWindow.setVisible(false);
		minimap.setVisible(false);
		actionsWindow.setVisible(false);

	}

	/**
	 * Sets every HUD element visible
	 */
	public void enableHUD() {
		overheadRight.setVisible(true);
		resourceTable.setVisible(true);
	    HUDManip.setVisible(true);

		chatbox.setVisible(true);
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
		statsTable.setPosition(0, stage.getHeight());
        //Top of panel
        overheadRight.setWidth(stage.getWidth());
        overheadRight.align(Align.right | Align.top);
        overheadRight.setPosition(0, Gdx.graphics.getHeight());
        welcomeMsg.setWidth(Gdx.graphics.getWidth());

		welcomeMsg.setPosition(0, Gdx.graphics.getHeight());
		welcomeMsg.align(Align.center | Align.top).pad(BUTTONPAD*2);
		messageWindow.align(Align.right | Align.top);
		messageWindow.setPosition(stage.getWidth()-chatbox.getWidth()-BUTTONPAD,
				Math.round(stage.getHeight()-chatbox.getHeight()-BUTTONPAD*5-BUTTONSIZE));
		//Bottom Panel
		//Map
		minimap.align(Align.topLeft);
		minimap.setPosition(0, 0);
		minimap.setMovable(false);
		minimap.setSize(220, 220);
		//Avaliable actions
		actionsWindow.align(Align.topLeft);
		actionsWindow.setWidth(stage.getWidth()-500);
		actionsWindow.setHeight(150);
		actionsWindow.setPosition(220, 0);
		//Resources
		resourceTable.align(Align.left | Align.center);
		resourceTable.setHeight(60);
		resourceTable.setPosition(minimap.getWidth(), actionsWindow.getHeight());
		//Menu manipulator
		HUDManip.setSize(50, 80);
		HUDManip.setPosition(stage.getWidth()-50, 50);
		HUDManip.align(Align.right| Align.bottom);

		//resize stats
		stats.resizeStats(width, height);
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
}
