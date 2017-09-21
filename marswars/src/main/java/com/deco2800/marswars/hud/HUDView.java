

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
import java.util.Arrays;
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
    private static final int NUMBER_OF_MENU_OPTIONS = 6;

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
	private static Window entitiesPicker; //window that selects available entities
	private CheatBox cheatbox;
	private Window cheatWindow;//window for the cheatbox

	//Buildings available to the player (adds default available buildings)
	private ArrayList<BuildingType> buildingsAvailable = new ArrayList<BuildingType>(Arrays.asList(
			BuildingType.BASE, BuildingType.BUNKER, BuildingType.TURRET, BuildingType.BARRACKS));


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
	
	Dialog pause;
	Dialog help;
	Dialog techTree;
	Dialog quit;

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
		this.cheatbox = new CheatBox(skin,textureManager);
		
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
		Cheat();
		
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

		LOGGER.debug("Add help, quit and message buttons"); //$NON-NLS-1$

		//add dispMainMenu image
		Texture menuImage = textureManager.getTexture("menu_button"); //$NON-NLS-1$
		HUDManip = new Table(); //adding buttons into a table
		HUDManip.setPosition(stage.getWidth()-50, 50);
		TextureRegion menuRegion = new TextureRegion(menuImage);
		TextureRegionDrawable menuRegionDraw = new TextureRegionDrawable(menuRegion);
		ImageButton dispMainMenu = new ImageButton(menuRegionDraw);


		//create help button + image for it
		Texture helpImage = textureManager.getTexture("help_button"); //$NON-NLS-1$
		TextureRegion helpRegion = new TextureRegion(helpImage);
		TextureRegionDrawable helpRegionDraw = new TextureRegionDrawable(helpRegion);
		helpButton = new ImageButton(helpRegionDraw);

		//create message button + image for it
		Texture messageImage = textureManager.getTexture("chat_button"); //$NON-NLS-1$
		TextureRegion messageRegion = new TextureRegion(messageImage);
		TextureRegionDrawable messageRegionDraw = new TextureRegionDrawable(messageRegion);
		messageButton = new ImageButton(messageRegionDraw);

		//create cheat button + image for it
		messageImage = textureManager.getTexture("chat_button");
		messageRegion = new TextureRegion(messageImage);
		messageRegionDraw = new TextureRegionDrawable(messageRegion);
		cheatButton = new ImageButton(messageRegionDraw);

		//add quit button + image for it
		Texture quitImage = textureManager.getTexture("quit_button"); //$NON-NLS-1$
		TextureRegion quitRegion = new TextureRegion(quitImage);
		TextureRegionDrawable quitRegionDraw = new TextureRegionDrawable(quitRegion);
		quitButton = new ImageButton(quitRegionDraw);

		//Create + align time displays 
		LOGGER.debug("Creating time labels"); //$NON-NLS-1$
		gameTimeDisp = new Label("0:00", skin); //$NON-NLS-1$
		gameLengthDisp = new Label("00:00:00", skin); //$NON-NLS-1$
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
		overheadRight.add(cheatButton).padRight(BUTTONPAD);
		overheadRight.add(messageButton).padRight(BUTTONPAD);
		overheadRight.add(helpButton).padRight(BUTTONPAD);
		overheadRight.add(dispMainMenu).padRight(BUTTONPAD);
		overheadRight.add(quitButton).padRight(BUTTONPAD);

		welcomeMsg = new Table();
		welcomeMsg.setWidth(stage.getWidth());
		welcomeMsg.align(Align.center | Align.top).pad(BUTTONPAD*2);
		welcomeMsg.setPosition(0, Gdx.graphics.getHeight());
		//Image clockbgImage1 = new Image(textureManager.getTexture("logo"));
		Label welcomeText = new Label("Welcome to Spacwars!", skin); //$NON-NLS-1$
		welcomeMsg.add(welcomeText);

		stage.addActor(welcomeMsg);
		stage.addActor(overheadRight);

		//Creates the help button listener
		LOGGER.debug("Creating help button listener"); //$NON-NLS-1$
		helpButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				new WorkInProgress("Help  Menu", skin, hud).show(stage); //$NON-NLS-1$
			}
		});

		//Creates the quit button listener
		LOGGER.debug("Creating quit button listener"); //$NON-NLS-1$
		quitButton.addListener(new ChangeListener() {
			@Override
			//could abstract this into another class
			public void changed(ChangeEvent event, Actor actor) {
				new ExitGame("Quit Game", skin, hud).show(stage);
			}});

		//Creates the message button listener
		LOGGER.debug("Creating message button listener"); //$NON-NLS-1$
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
		cheatButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				if (cheatActiveCheck==1){
					cheatWindow.setVisible(false);
					cheatActiveCheck = 0;
				} else {
					cheatWindow.setVisible(true);
					cheatbox.setVisible(true);
					cheatActiveCheck = 1;
				}

			}
		});
	}


	/**
	 * Adds the player Icon, health for a single spacman, and name to the huD (goes into top left).
	 * Also inventory for the hero character
	 * Does this by creating a nested table. The basic parent table layout is shown below:
	 * +---------------------------+
	 * |    :)    |______________  |
	 * |  Player  |______________| |
	 * |   img    |           100  |
	 * |__________|----------------+
	 * | p. Name  | x x x x x x    |
	 * |-----+----|----------------+
	 * |     |    |
	 * | >:( | 12 |
	 * |-----|----|
	 * |  :) | 10 |
	 * |     |    |
	 * |     |    |
	 * +----------+
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
		LOGGER.debug("Creating chat lobby box"); //$NON-NLS-1$
		messageWindow = new Window("Chat Lobby", skin); //$NON-NLS-1$
		messageWindow.setMovable(false);
		messageWindow.setPosition(stage.getWidth()-chatbox.getWidth()-BUTTONPAD,
				Math.round(stage.getHeight()-chatbox.getHeight()-BUTTONPAD*4-BUTTONSIZE));
		messageWindow.add(chatbox);
		messageWindow.setVisible(false);
		messageWindow.pack();

		stage.addActor(messageWindow);
	}

	private void Cheat(){
		LOGGER.debug("cheatbox"); //$NON-NLS-1$
		cheatWindow = new Window("Cheat", skin); //$NON-NLS-1$
		cheatWindow.setMovable(false);
		cheatWindow.setPosition(stage.getWidth()-cheatbox.getWidth()-50,
				Math.round(stage.getHeight()-cheatbox.getHeight()-BUTTONPAD*4-BUTTONSIZE));
		cheatWindow.add(cheatbox);
		cheatWindow.setVisible(false);
		cheatWindow.pack();

		stage.addActor(cheatWindow);
	}

	/**
	 * Adds in the bottom panel of the HUD
	 */
	private void addBottomPanel(){
		addMiniMapMenu();
		addInventoryMenu();


		LOGGER.debug("Creating HUD manipulation buttons"); //$NON-NLS-1$
		
		shopDialog = new ShopDialog("Shop", skin, textureManager);
		//remove dispActions button + image for it 

		Texture minusImage = textureManager.getTexture("minus_button"); //$NON-NLS-1$
		TextureRegion minusRegion = new TextureRegion(minusImage);
		minusRegionDraw = new TextureRegionDrawable(minusRegion);
		removeActions = new ImageButton(minusRegionDraw);

		//add dispActions image
		Texture plusImage = textureManager.getTexture("plus_button"); //$NON-NLS-1$
		TextureRegion plusRegion = new TextureRegion(plusImage);
		plusRegionDraw = new TextureRegionDrawable(plusRegion);
		dispActions = new ImageButton(plusRegionDraw);

		//add dispTech image
		Texture techImage = textureManager.getTexture("tech_button"); //$NON-NLS-1$
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
		HUDManip.add(dispShop).padRight(BUTTONPAD);
		HUDManip.add(removeActions).pad(BUTTONPAD);

		stage.addActor(HUDManip);

		dispActions.addListener(new ChangeListener() {
			@Override
			/*displays the (-) button for setting the hud to invisible*/
			public void changed(ChangeEvent event, Actor actor) {
				if (isInventoryToggle()) {
					LOGGER.debug("Enable hud"); //$NON-NLS-1$
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
				LOGGER.debug("Disable Hud"); //$NON-NLS-1$
				actionsWindow.setVisible(false);
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
				new TechTreeView("TechTree", skin, hud).show(stage); //$NON-NLS-1$
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

		setupEntitiesPickerMenu();
		addEntitiesPickerMenu();
	}

	/**
	 * Toggle fog on an off
	 */
	private void toggleFog(){
		//disable fog
		if (fogToggle) {
			LOGGER.debug("fog of war is now off"); //$NON-NLS-1$
			FogManager.toggleFog(false);
			fogToggle = false;
		}else {
			LOGGER.debug("fog of war is now on"); //$NON-NLS-1$
			FogManager.toggleFog(true);
			fogToggle = true;
		}
	}
	/**
	 * Adds in the selectable menu for the inventory for resources
	 */
	private void addInventoryMenu(){
		LOGGER.debug("Create inventory"); //$NON-NLS-1$
		actionsWindow = new Window("Actions", skin); //$NON-NLS-1$
		resourceTable = new Table();
		resourceTable.align(Align.left | Align.top);
		resourceTable.setHeight(40);
		resourceTable.setPosition(minimap.getWidth(), actionsWindow.getHeight());

		LOGGER.debug("Creating resource labels"); //$NON-NLS-1$
		rockCount = new Label("Rock: 0", skin); //$NON-NLS-1$
		crystalCount = new Label("Crystal: 0", skin); //$NON-NLS-1$
		biomassCount = new Label("Biomass: 0", skin); //$NON-NLS-1$
		waterCount = new Label("Water: 0", skin); //$NON-NLS-1$
		popCount = new Label("0 ", skin);
		maxPopCount = new Label(" / 10", skin);

		//add rock image
		Texture rockTex = textureManager.getTexture("rock_HUD"); //$NON-NLS-1$
		Image rock = new Image(rockTex);
		//add water image
		Texture waterTex = textureManager.getTexture("water_HUD"); //$NON-NLS-1$
		Image water = new Image(waterTex);
		//add biomass image
		Texture biomassTex = textureManager.getTexture("biomass_HUD"); //$NON-NLS-1$
		Image biomass = new Image(biomassTex);
		//add crystal image
		Texture crystalTex = textureManager.getTexture("crystal_HUD"); //$NON-NLS-1$
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

		peonButton = new TextButton("Select a Unit", skin); //$NON-NLS-1$
		helpText = new Label("Welcome to SpacWars!", skin); //$NON-NLS-1$

		actionsWindow.add(peonButton);
		actionsWindow.add(helpText);
		actionsWindow.setMovable(false);
		actionsWindow.align(Align.topLeft);
		actionsWindow.setWidth(stage.getWidth()-700);
		actionsWindow.setHeight(150);
		actionsWindow.setPosition(220, 0);

		//Add action buttons
		addButtonArray();

		stage.addActor(actionsWindow);
	}

	private void addButtonArray() {
		buttonList = new ArrayList<>();
		for (int i = 0; i != NUMBER_ACTION_BUTTONS; i++) {
			TextButton newButton = new TextButton(" ", skin); //$NON-NLS-1$
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
		currentActions = target.getValidActions();
		if (target instanceof Astronaut) {
	        enterActions(false);
		}
		else {
			enterActions(true);
		}
		if(target instanceof AttackableEntity) {
			this.statsTable.setVisible(true);
			this.statsTable.updateSelectedStats(((AttackableEntity) target));

	        // display hero inventory
	        this.statsTable.hideInventory();
	        heroSelected = null;
	        if(target instanceof Commander) {
	        	heroSelected = (Commander) target;
	        	this.statsTable.showInventory();
	        	this.statsTable.updateHeroInventory((Commander)target);
	        }
		}	
    }


    /**
     *  Creates the basic structure of the Entities picker menu
     */
	private void setupEntitiesPickerMenu(){
        entitiesPicker = new Window("Spawn", skin);
        entitiesPicker.align(Align.topLeft);
        entitiesPicker.setPosition(220,0);
        entitiesPicker.setMovable(false);
        entitiesPicker.setVisible(false);
        entitiesPicker.setWidth(stage.getWidth()-220);
        entitiesPicker.setHeight(150);
    }

    /**
     * Add the customise window / entities picker.
     * This method shall only be called when the "Customize" button from the start menu is clicked.
     * If this method is call, it will cause that the actions window be set to not visible.
	 *
     */
    private void addEntitiesPickerMenu(){
		entitiesPicker.clear();
		entitiesPicker.getTitleLabel().setText("Customize");

        Table table = new Table();
        TextButton unitsButton = new TextButton("Units",skin);
        unitsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addUnitsPickerMenu(false);
            }
        });
        TextButton buildingsButton = new TextButton("Buildings",skin);
        buildingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addBuildingsPickerMenu(false);
            }
        });
        TextButton resourcesButton = new TextButton("Resources",skin);
        resourcesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addResourcesPickerMenu();
            }
        });
        TextButton terrainsButton = new TextButton("Terrains",skin);
        terrainsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addTerrainsPickerMenu();
            }
        });

		TextButton mapButton = new TextButton("Maps",skin);
		mapButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				addMapTypesPickerMenu();
			}
		});
		TextButton sizeButton = new TextButton("World Size",skin);
		sizeButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				addMapSizesPickerMenu();
			}
		});

        table.add(unitsButton).width(entitiesPicker.getWidth()/ NUMBER_OF_MENU_OPTIONS).height(entitiesPicker.getHeight());
        table.add(buildingsButton).width(entitiesPicker.getWidth()/ NUMBER_OF_MENU_OPTIONS).height(entitiesPicker.getHeight());
        table.add(resourcesButton).width(entitiesPicker.getWidth()/ NUMBER_OF_MENU_OPTIONS).height(entitiesPicker.getHeight());
        table.add(terrainsButton).width(entitiesPicker.getWidth()/ NUMBER_OF_MENU_OPTIONS).height(entitiesPicker.getHeight());
		table.add(mapButton).width(entitiesPicker.getWidth()/ NUMBER_OF_MENU_OPTIONS).height(entitiesPicker.getHeight());
		table.add(sizeButton).width(entitiesPicker.getWidth()/ NUMBER_OF_MENU_OPTIONS).height(entitiesPicker.getHeight());
        entitiesPicker.add(table);
        stage.addActor(entitiesPicker);

    }

    /**
     * Creates the sub menu that displays all available units
	 *
	 * @param inGame whether is being used during a game play
     */
    public void addUnitsPickerMenu(boolean inGame){
        entitiesPicker.clear();
		entitiesPicker.getTitleLabel().setText("Spawn Units");

		float buttonWidth = entitiesPicker.getWidth()/NUMBER_OF_MENU_OPTIONS;
		float buttonHeight = entitiesPicker.getHeight();

        Table table = new Table();
		table.align(Align.left);
        if(!inGame) {
			TextButton entitiesButton = new TextButton("Entity Types\n (Back)", skin);
			entitiesButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					addEntitiesPickerMenu();
					entitiesPicker.setVisible(true);
				}
			});
			table.add(entitiesButton).width(buttonWidth).height(buttonHeight);
		}
        
        TextButton astronautButton = new TextButton("Astronaut",skin);
        astronautButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameManager.get().getWorld().addEntity( new Astronaut(0,0,0,1));
			}
		});
    
        TextButton carrierButton = new TextButton("Carrier",skin);
        TextButton healerButton = new TextButton("Healer",skin);
        TextButton heroSpacmanButton = new TextButton("Hero Spacman",skin);
        TextButton soldierButton = new TextButton("Soldier",skin);
        TextButton spacmanButton = new TextButton("Spacman",skin);
        TextButton tankButton = new TextButton("Tank",skin);

        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setFadeScrollBars(false);
        table.add(astronautButton).width(buttonWidth).height(buttonHeight);
        table.add(carrierButton).width(buttonWidth).height(buttonHeight);
        table.add(healerButton).width(buttonWidth).height(buttonHeight);
        table.add(heroSpacmanButton).width(buttonWidth).height(buttonHeight);
        table.add(soldierButton).width(buttonWidth).height(buttonHeight);
        table.row();
        table.add(spacmanButton).width(buttonWidth).height(buttonHeight);
        table.add(tankButton).width(buttonWidth).height(buttonHeight);

        entitiesPicker.add(scrollPane).width(entitiesPicker.getWidth()).height(entitiesPicker.getHeight());
    }

    /**
     * Creates the sub menu that displays all available buildings
     * @param isPlaying allows to go back to menu options if true
     */
    private void addBuildingsPickerMenu(boolean addBack){
        entitiesPicker.clear();
        Table table = new Table();
		table.align(Align.left);
        TextButton entitiesButton = new TextButton("Entity Types\n (Back)",skin);
            entitiesButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    addEntitiesPickerMenu();
                    entitiesPicker.setVisible(true);
                }
            });
        float buttonWidth = entitiesPicker.getWidth()/NUMBER_OF_MENU_OPTIONS;
        float buttonHeight = entitiesPicker.getHeight();
        int columns = 0;
        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setFadeScrollBars(false);
        if(addBack) {
            table.add(entitiesButton).width(buttonWidth).height(buttonHeight);
            columns = 1;
        }
        // Adds all build buttons and listeners for each available building
        for (BuildingType b : buildingsAvailable) {
        	Button formatPane = new Button(skin);
    		Texture entity = textureManager.getTexture(b.getBuildTexture());
    		TextureRegion entityRegion = new TextureRegion(entity);
    		TextureRegionDrawable buildPreview = new TextureRegionDrawable(entityRegion);
    		ImageButton addPane = new ImageButton(buildPreview);
    		formatPane.add(addPane).width(buttonWidth * .6f).height(buttonHeight * .5f);
    		formatPane.row().padBottom(20);
    		formatPane.add(new Label(b.toString(),skin)).align(Align.left).padLeft(10);
    		formatPane.add(new Label(String.valueOf(b.getCost()),skin)).align(Align.left);
    		Texture rockTex = textureManager.getTexture("rock_HUD");
    		Image rock = new Image(rockTex);
    		formatPane.add(rock).width(40).height(40).padBottom(30).align(Align.left);
        	formatPane.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                	if(selectedEntity.getAction().isPresent() && selectedEntity.getAction().get() instanceof BuildAction) {
                		BuildAction cancelBuild = (BuildAction)selectedEntity.getAction().get();
                		cancelBuild.cancelBuild();
                		cancelBuild.doAction();
                	}
                	selectedEntity.setAction(new BuildAction(selectedEntity, b));
                }
            });
    		table.add(formatPane).width(buttonWidth).height(buttonHeight).align(Align.center);
    		columns++;
    		if(columns % NUMBER_OF_MENU_OPTIONS == 0){
				table.row();
			}
    		
        }
		entitiesPicker.add(scrollPane).width(entitiesPicker.getWidth()).height(entitiesPicker.getHeight());

    }


    /**
     * Creates the sub menu that displays all available resources
	 *
     */
    private void addResourcesPickerMenu(){
        entitiesPicker.clear();
		entitiesPicker.getTitleLabel().setText("Spawn Resources");

		float buttonWidth = entitiesPicker.getWidth()/NUMBER_OF_MENU_OPTIONS;
		float buttonHeight = entitiesPicker.getHeight();

        Table table = new Table();
		table.align(Align.left);
		TextButton entitiesButton = new TextButton("Entity Types\n (Back)", skin);
		entitiesButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				addEntitiesPickerMenu();
				entitiesPicker.setVisible(true);
			}
		});
		table.add(entitiesButton).width(buttonWidth).height(buttonHeight);

        TextButton biomassButton = new TextButton("Biomass",skin);
        TextButton crystalButton = new TextButton("Crystal",skin);
        TextButton rockButton = new TextButton("Rock",skin);
        TextButton waterButton = new TextButton("Water",skin);

        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setFadeScrollBars(false);

        table.add(biomassButton).width(buttonWidth).height(buttonHeight);
        table.add(crystalButton).width(buttonWidth).height(buttonHeight);
        table.add(rockButton).width(buttonWidth).height(buttonHeight);
        table.add(waterButton).width(buttonWidth).height(buttonHeight);

        entitiesPicker.add(scrollPane).width(entitiesPicker.getWidth()).height(entitiesPicker.getHeight());

    }

    /**
     * Creates the sub menu that displays all available terrains
	 *
     */
    private void addTerrainsPickerMenu(){
        entitiesPicker.clear();
		entitiesPicker.getTitleLabel().setText(" Generate Terrains");

		float buttonWidth = entitiesPicker.getWidth()/NUMBER_OF_MENU_OPTIONS;
		float buttonHeight = entitiesPicker.getHeight();

        Table table = new Table();
		table.align(Align.left);
		TextButton entitiesButton = new TextButton("Entity Types\n (Back)", skin);
		entitiesButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				addEntitiesPickerMenu();
				entitiesPicker.setVisible(true);
			}
		});
		table.add(entitiesButton).width(buttonWidth).height(buttonHeight);

        TextButton caveButton = new TextButton("Cave",skin);
        TextButton lakeButton = new TextButton("Lake",skin);
        TextButton pondButton = new TextButton("Pond",skin);
        TextButton quicksandButton = new TextButton("Quicksand",skin);

        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setFadeScrollBars(false);

        table.add(caveButton).width(buttonWidth).height(buttonHeight);
        table.add(lakeButton).width(buttonWidth).height(buttonHeight);
        table.add(pondButton).width(buttonWidth).height(buttonHeight);
        table.add(quicksandButton).width(buttonWidth).height(buttonHeight);

        entitiesPicker.add(scrollPane).width(entitiesPicker.getWidth()).height(entitiesPicker.getHeight());

    }

	/**
	 * Populate the menu with map size options
	 */
	private void addMapSizesPickerMenu(){
		entitiesPicker.clear();
		entitiesPicker.getTitleLabel().setText(" Choose Map Size");

		float buttonWidth = entitiesPicker.getWidth()/NUMBER_OF_MENU_OPTIONS;
		float buttonHeight = entitiesPicker.getHeight();

		Table table = new Table();
		table.align(Align.left);
		TextButton entitiesButton = new TextButton("Entity Types\n (Back)", skin);
		entitiesButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				addEntitiesPickerMenu();
				entitiesPicker.setVisible(true);
			}
		});
		table.add(entitiesButton).width(buttonWidth).height(buttonHeight);

		TextButton tinyButton = new TextButton("Tiny",skin);
		sizeButtonHandler(tinyButton, MapSizeTypes.TINY);
		TextButton smallButton = new TextButton("Small",skin);
		sizeButtonHandler(smallButton, MapSizeTypes.SMALL);
		TextButton mediumButton = new TextButton("Medium",skin);
		sizeButtonHandler(mediumButton, MapSizeTypes.MEDIUM);
		TextButton largeButton = new TextButton("Large",skin);
		sizeButtonHandler(largeButton, MapSizeTypes.LARGE);
		TextButton veryLargeButton = new TextButton("Very Large",skin);
		sizeButtonHandler(veryLargeButton, MapSizeTypes.VERY_LARGE);

		ScrollPane scrollPane = new ScrollPane(table, skin);
		scrollPane.setScrollingDisabled(true,false);
		scrollPane.setFadeScrollBars(false);

		table.add(tinyButton).width(buttonWidth).height(buttonHeight);
		table.add(smallButton).width(buttonWidth).height(buttonHeight);
		table.add(mediumButton).width(buttonWidth).height(buttonHeight);
		table.add(largeButton).width(buttonWidth).height(buttonHeight);
		table.add(veryLargeButton).width(buttonWidth).height(buttonHeight);

		entitiesPicker.add(scrollPane).width(entitiesPicker.getWidth()).height(entitiesPicker.getHeight());
	}

	/**
	 * Populate the menu with map types options
	 */
	private void addMapTypesPickerMenu(){
		entitiesPicker.clear();
		entitiesPicker.getTitleLabel().setText(" Choose Map");

		float buttonWidth = entitiesPicker.getWidth()/NUMBER_OF_MENU_OPTIONS;
		float buttonHeight = entitiesPicker.getHeight();

		Table table = new Table();
		table.align(Align.left);
		TextButton entitiesButton = new TextButton("Entity Types\n (Back)", skin);
		entitiesButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				addEntitiesPickerMenu();
				entitiesPicker.setVisible(true);
			}
		});
		table.add(entitiesButton).width(buttonWidth).height(buttonHeight);

		TextButton marsButton = new TextButton("Mars",skin);
		typesButtonHandler(marsButton, MapTypes.MARS);
		TextButton moonButton = new TextButton("Moon",skin);
		typesButtonHandler(moonButton, MapTypes.MOON);
		TextButton sunButton = new TextButton("Sun",skin);
		typesButtonHandler(sunButton, MapTypes.SUN);

		ScrollPane scrollPane = new ScrollPane(table, skin);
		scrollPane.setScrollingDisabled(true,false);
		scrollPane.setFadeScrollBars(false);

		table.add(marsButton).width(buttonWidth).height(buttonHeight);
		table.add(moonButton).width(buttonWidth).height(buttonHeight);
		table.add(sunButton).width(buttonWidth).height(buttonHeight);

		entitiesPicker.add(scrollPane).width(entitiesPicker.getWidth()).height(entitiesPicker.getHeight());
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
     * Displays the entities picker menu.
     * If picker is shown then fog is off and game is paused
     *
     * @param isVisible whether to display the picker or hide it.
	 * @param isPlaying whether a game is being played.
     */
    public void showEntitiesPicker( boolean isVisible, boolean isPlaying){
        entitiesPicker.setVisible(isVisible);
        // this call allows the menu to reset instead of using its latest state
            addEntitiesPickerMenu();
        if(!isPlaying) {
			entitiesPicker.setVisible(true);
			toggleFog();
			timeManager.pause();

		}
    }
    
    /**
     * Displays the build menu
     * If picker is shown then fog is off and game is paused
     *
     * @param isVisible whether to display the picker or hide it.
	 * @param isPlaying whether a game is being played.
     */
    public void showBuildMenu(Astronaut builder, boolean isVisible, boolean isPlaying) {
        entitiesPicker.setVisible(isVisible);

        TechnologyManager t = (TechnologyManager) GameManager.get().getManager(TechnologyManager.class);
        
        if(t.getActive().contains(t.getTech(1))){
        	//Implement tech here  (need to change if statement probably)
        	buildingsAvailable.add(BuildingType.HEROFACTORY);
        }
        // this call allows the menu to reset instead of using its latest state
            addBuildingsPickerMenu(false);
        if(!isPlaying) {
			entitiesPicker.setVisible(true);
			toggleFog();
			timeManager.pause();
		}
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
		//Format Actions
		formatActionButtons(0);
		//Format Entities
		formatUnitButtons(currentActions.getActions().size());
		//Format Buildings
		formatBuildingButtons(currentActions.getActions().size() + currentActions.getUnits().size());

		actionsWindow.setVisible(display);
        for (int i = 0; i < currentActions.size(); i++) {
				enableButton(buttonList.get(i));
				if (currentActions.get(i) instanceof ActionType) { //If it is an action
					buttonList.get(i).setText(ActionSetter.getActionName((ActionType)currentActions.get(i)));
				} else { //If it isnt an action it is something to build
					//buttonList.get(i).setText("Build " + (EntityID)currentActions.get(i)); //$NON-NLS-1$
				}
        }
    }

	private void formatUnitButtons(int index) {
		float buttonWidth = actionsWindow.getWidth()/currentActions.size();
		float buttonHeight = actionsWindow.getHeight();
		for (EntityID e : currentActions.getUnits()) {
			buttonList.get(index).setVisible(true);
			buttonList.get(index).clearChildren();
			buttonList.get(index).setWidth(buttonWidth);
			buttonList.get(index).setHeight(buttonHeight);
			Texture entity = textureManager.getTexture((textureManager.loadUnitSprite(e)));
			TextureRegion entityRegion = new TextureRegion(entity);
			TextureRegionDrawable buildPreview = new TextureRegionDrawable(entityRegion);
			ImageButton addPane = new ImageButton(buildPreview);
			buttonList.get(index).add(addPane).width(buttonWidth * .6f).height(buttonHeight * .5f);
			buttonList.get(index).row().padBottom(20);
			buttonList.get(index).add(new Label(e.name(), skin)).align(Align.left).padLeft(10);
			buttonList.get(index).add(new Label(String.valueOf(0),skin)).align(Align.left);
			Texture rockTex = textureManager.getTexture("rock_HUD");
			Image rock = new Image(rockTex);
			buttonList.get(index).add(rock).width(40).height(40).padBottom(30).align(Align.left);
			index++;
		}

	}

	private void formatActionButtons(int index) {
		float buttonWidth = actionsWindow.getWidth()/currentActions.size();
		float buttonHeight = actionsWindow.getHeight();
		for (ActionType a : currentActions.getActions()) {
			buttonList.get(index).setVisible(true);
			buttonList.get(index).clearChildren();
			buttonList.get(index).setWidth(buttonWidth);
			buttonList.get(index).setHeight(buttonHeight);
			Texture entity = textureManager.getTexture("PLACEHOLDER");
			TextureRegion entityRegion = new TextureRegion(entity);
			TextureRegionDrawable buildPreview = new TextureRegionDrawable(entityRegion);
			ImageButton addPane = new ImageButton(buildPreview);
			buttonList.get(index).add(addPane).width(buttonWidth * .6f).height(buttonHeight * .5f);
			buttonList.get(index).row().padBottom(20);
			buttonList.get(index).add(new Label(a.name(), skin)).align(Align.left).padLeft(10);
			Texture rockTex = textureManager.getTexture("rock_HUD");
			Image rock = new Image(rockTex);
			rock.setVisible(false);
			buttonList.get(index).add(rock).width(40).height(40).padBottom(30).align(Align.left);
			index++;
		}
	}


	private void formatBuildingButtons(int index){
		float buttonWidth = actionsWindow.getWidth()/currentActions.size();
		float buttonHeight = actionsWindow.getHeight();
		for (BuildingType b : currentActions.getBuildings()) {
			buttonList.get(index).setVisible(true);
			buttonList.get(index).clearChildren();
			buttonList.get(index).setWidth(buttonWidth);
			buttonList.get(index).setHeight(buttonHeight);
			Texture entity = textureManager.getTexture(b.getBuildTexture());
			TextureRegion entityRegion = new TextureRegion(entity);
			TextureRegionDrawable buildPreview = new TextureRegionDrawable(entityRegion);
			ImageButton addPane = new ImageButton(buildPreview);
			buttonList.get(index).add(addPane).width(buttonWidth * .6f).height(buttonHeight * .5f);
			buttonList.get(index).row().padBottom(20);
			buttonList.get(index).add(new Label(b.toString(),skin)).align(Align.left).padLeft(10);
			buttonList.get(index).add(new Label(String.valueOf(b.getCost()),skin)).align(Align.left);
			Texture rockTex = textureManager.getTexture("rock_HUD");
			Image rock = new Image(rockTex);
			buttonList.get(index).add(rock).width(40).height(40).padBottom(30).align(Align.left);
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
		gameTimeDisp.setText(timeManager.toString()); //$NON-NLS-1$
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
		rockCount.setText("" + resourceManager.getRocks(-1)); //$NON-NLS-1$
		crystalCount.setText("" + resourceManager.getCrystal(-1));  //$NON-NLS-1$
		waterCount.setText("" + resourceManager.getWater(-1)); //$NON-NLS-1$
		biomassCount.setText("" + resourceManager.getBiomass(-1)); //$NON-NLS-1$
		popCount.setText("" + resourceManager.getPopulation(-1)); //$NON-NLS-1$
		maxPopCount.setText("/ " + resourceManager.getMaxPopulation(-1)); //$NON-NLS-1$
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
					 * only add your own commander as option in shop to buy items for. May need to cheng condition when
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
					//peonButton = ((Selectable) e).getButton();
					//helpText = ((Selectable) e).getHelpText();
					somethingSelected = true;
				}
			}
			if (!somethingSelected) {
			
			}
			lastMenuTick = TimeUtils.nanoTime();
			
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
		cheatbox.setVisible(false);
		cheatWindow.setVisible(false);
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
        LOGGER.debug("Window resized, rescaling hud"); //$NON-NLS-1$
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
		actionsWindow.setWidth(stage.getWidth()-700);
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
	
	/** 
	 * When used in the code will set the pauseCheck integer to 1 when there
	 * is an active Pause menu and 0 otherwise
	 */
	public void setPauseCheck(int i) {
		pauseCheck = i;	
	}
	
	public int getPauseCheck() {
		return pauseCheck;
	}
	
	/** 
	 * When used in the code will set the chatActiveCheck integer to 1 when the chat window
	 * toggle is true and 0 otherwise.  Instead of stopping a new instance being created, will be used to
	 * stop the other hotkeys from being used while the chatbox is visible
	 */
	public void setChatActiveCheck(int i) {
		chatActiveCheck = i;
	}
	
	public int getChatActiveCheck() {
		return chatActiveCheck;
	}
	
	/** 
	 * When used in the code will set the exitCheck integer to 1 when there
	 * is an active dialog asking the user if they wish to quit, and 0 otherwise
	 */
	public void setExitCheck(int i) {
		exitCheck = i;
	}
	
	public int getExitCheck() {
		return exitCheck;
	}
	
	/**
	 * When used in the code will set the techCheck integer to 1 when the tech
	 * tree is visible and 0 otherwise
	 */
	public void setTechCheck(int i) {
		techCheck = i;
	}
	
	public int getTechCheck() {
		return techCheck;
	}

	/**
	 *  When used in the code will set the helpCheck integer to 1 when there
	 * is an active Help window and 0 otherwise
	 */
	public void setHelpCheck(int i) {
		helpCheck = i;
		
	}
	
	public int getHelpCheck() {
		return helpCheck;
	}

	public boolean isInventoryToggle() {
		return inventoryToggle;
	}

	public void setInventoryToggle(boolean inventoryToggle) {
		this.inventoryToggle = inventoryToggle;
	}
}
