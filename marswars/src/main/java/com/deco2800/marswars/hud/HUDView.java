package com.deco2800.marswars.hud;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.deco2800.marswars.actions.ActionList;
import com.deco2800.marswars.actions.ActionSetter;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.managers.FogManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.renderers.Renderable;
import com.deco2800.marswars.managers.TextureManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
	private static final int CRITICALHEALTH = 30; //critical health of spacmen
	private static final int NUMBER_ACTION_BUTTONS = 10; //The maximum number of buttons
    private static final int TYPES_OF_ENTITIES = 4;

	private  static final int[] INDICES = {1,2,3,4,5,6,7,8,9,10};

	private Stage stage;
	private Skin skin;
	private ImageButton quitButton;
	private ImageButton helpButton;
	private ImageButton messageButton;

	ProgressBar.ProgressBarStyle barStyle;
	//HUD elements 
	private Image selectedImage; //The current image to be displayed in top left
	private Table overheadRight; //contains all basic quit/help/chat buttons
	private Table resourceTable; //contains table of resource images + count
    private Table playerdetails; //contains player icon, health and game stats
    private Table HUDManip;		 //contains buttons for toggling HUD + old menu
    private Table welcomeMsg; 	 //contains welcome message 
	private ChatBox chatbox;	 //table for the chat
	private Window messageWindow;//window for the chatbox 
	private Window minimap;		 //window for containing the minimap
	private Window actionsWindow;    //window for the players actions
	private Window entitiesPicker; //window that selects available entities
		
	private Button peonButton;
	private Label helpText;
	
	//Resources count  
	private Label rockCount;   
	private Label crystalCount; 
	private Label biomassCount; 
	private Label waterCount;
	
	//Player stats + progress bar 
	private Image spacman; 		   //image of spacman
	private Label playerSpacmen;   //counts spacmen on baord
	private Label playerEnemySpacmen; //counts enemies seen on board
	private Label healthLabel;     //numeric indicator for health level
	private Label nameLabel;       //name of player 
	private ProgressBar healthBar; //progress bar displaying spacmen health
	private Pixmap pixmap; 		   //used for progress bar 
	
	//Action buttons
	private List<TextButton> buttonList;
	private ActionList currentActions;


	//Toggles; checks if the feature is visible on-screen or not
	private boolean messageToggle; 
	private boolean inventoryToggle; 
	private boolean fogToggle = true; 
	//Image buttons to display/ remove lower HUD 
	private ImageButton dispActions;//Button for displaying actions window 
	private ImageButton removeActions; //button for removing actions window 
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
	private GameStats stats;

	/**
	 * Creates a 'view' instance for the HUD. This includes all the graphics
	 * of the HUD and is mostly for simply displaying components on screen. 
	 * @param stage the game stage
	 * @param skin the look of the HUD, depending on the world/level the game is being played at
	 * @param gameManager handles selectables
	 * @param textureManager 
	 */
	public HUDView(Stage stage, Skin skin, GameManager gameManager, TextureManager textureManager) {
		LOGGER.debug("Creating Hud");
		this.skin = skin;
		this.stage = stage;
		this.gameManager = gameManager;
		this.textureManager = textureManager;
		
		//Generate the game stats
		this.stats = new GameStats(stage, skin, this, textureManager);
		//create chatbox
		this.chatbox = new ChatBox(skin, textureManager);
		
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
		//overheadRight.add(gamelengthStack).padRight(BUTTONPAD).height(BUTTONSIZE).width(BUTTONSIZE*2);
		overheadRight.add(messageButton).padRight(BUTTONPAD);
		overheadRight.add(helpButton).padRight(BUTTONPAD);
		overheadRight.add(dispMainMenu).padRight(BUTTONPAD);
		overheadRight.add(quitButton).padRight(BUTTONPAD);
		
		welcomeMsg = new Table();
		welcomeMsg.setWidth(stage.getWidth());
		welcomeMsg.align(Align.center | Align.top).pad(BUTTONPAD*2);
		welcomeMsg.setPosition(0, Gdx.graphics.getHeight());
		Label welcomeText = new Label("Welcome to Spacwars!", skin); //$NON-NLS-1$
		welcomeMsg.add(welcomeText);
		
		stage.addActor(welcomeMsg);
		stage.addActor(overheadRight);
		
		//Creates the help button listener
		LOGGER.debug("Creating help button listener"); //$NON-NLS-1$
		helpButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				new WorkInProgress("Help  Menu", skin).show(stage); //$NON-NLS-1$
			}
		});
		
		//Creates the quit button listener
		LOGGER.debug("Creating quit button listener"); //$NON-NLS-1$
		quitButton.addListener(new ChangeListener() {
			@Override
			//could abstract this into another class
			public void changed(ChangeEvent event, Actor actor) {
				new ExitGame("Quit Game", skin).show(stage);
			}});

		//Creates the message button listener 
		LOGGER.debug("Creating message button listener"); //$NON-NLS-1$
		messageButton.addListener(new ChangeListener() {
			@Override 
			public void changed(ChangeEvent event, Actor actor){
				if (messageToggle){
					messageWindow.setVisible(false);
					messageToggle = false; 
				} else {
					messageWindow.setVisible(true);
					messageToggle = true;
				}
				
			}
		});
	}
	
		
	/**
	 * Adds the player Icon, health for a single spacman, and name to the huD (goes into top left).
	 * Does this by creating a nested table. The basic parent table layout is shown below: 
	 * +---------------------------+
	 * |    :)    |______________  |
	 * |  Player  |______________| |
	 * |   img    |           100  |
	 * |__________|----------------+
	 * | p. Name  |
	 * |-----+----|
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
		LOGGER.debug("Adding player icon"); //$NON-NLS-1$
		playerdetails = new Table();
		playerdetails.pad(10);
		playerdetails.setWidth(150);
		playerdetails.setPosition(0, stage.getHeight());
		
		//Icon for player- 
		//TODO get main menu working to select an icon and then display 
		selectedImage = new Image(textureManager.getTexture("spacman_blue"));
		playerdetails.add(selectedImage).height(100).width(100);
		
		//create table for health bar display
		Table healthTable = new Table();
		//Create the health bar 
		LOGGER.debug("Creating health bar"); //$NON-NLS-1$
		addProgressBar();
		healthLabel = new Label("Health: ", skin); //$NON-NLS-1$
		healthTable.add(healthLabel).align(Align.left);
		healthTable.row();
		healthTable.add(healthBar);
		
		playerdetails.add(healthTable);

		//add in player name
		playerdetails.row();
		Label playerName = new Label("Name", skin); //$NON-NLS-1$
		playerdetails.add(playerName);
		
		this.nameLabel = playerName;
		
		//add in player stats to a new table 
		Table playerStats = new Table();
		playerSpacmen = new Label("Alive spacmen: 0", skin); //$NON-NLS-1$
		playerEnemySpacmen = new Label("Evil spacman: 0", skin); //$NON-NLS-1$
		
		//image for spacman
		Texture spacmanTex = textureManager.getTexture("spacman_green"); //$NON-NLS-1$
		spacman = new Image(spacmanTex);
		//image for enemy spatman
		Texture spatmanTex = textureManager.getTexture("spatman_blue"); //$NON-NLS-1$
		Image spatman = new Image(spatmanTex);

		//add in spacmen and enemy stats to stats 
		playerStats.padLeft(20).padTop(60);
		playerStats.align(Align.left);
		playerStats.add(spacman).height(60).width(60).padBottom(10);
		playerStats.add(playerSpacmen).center();
		playerStats.row();
		playerStats.add(spatman).height(60).width(40);
		playerStats.add(playerEnemySpacmen).center();
		
		//add in stats table 
		playerdetails.row();
		playerdetails.add(playerStats);
		stage.addActor(playerdetails);
	}
	
	
	/**
	 * Adds in progress bar to the top left of the screen 
	 */
	private void addProgressBar(){
		pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.DARK_GRAY);
		pixmap.fill();
		barStyle = new ProgressBar.ProgressBarStyle();
		barStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();

		pixmap = new Pixmap(0, 20, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fill();
		barStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();

		pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fill();
		barStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();

		healthBar = new ProgressBar(0,100, 1, false, barStyle);
		healthBar.setValue(100);
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
	
	/**
	 * Adds in the bottom panel of the HUD 
	 */
	private void addBottomPanel(){
		addMiniMapMenu();
		addInventoryMenu();

		LOGGER.debug("Creating HUD manipulation buttons");
			
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
		
		//add toggle Fog of war (FOR DEBUGGING) 
		Button dispFog = new TextButton("Fog", skin);
		
		//add button for game stats (might just move this over to the game menu?)
		Button dispStats = new TextButton("Stat2800", skin);
				
		HUDManip.setSize(50, 80);
		HUDManip.pad(BUTTONPAD);
		HUDManip.add(dispTech).pad(BUTTONPAD);
		HUDManip.add(dispFog).pad(BUTTONPAD);
		HUDManip.add(dispStats).pad(BUTTONPAD);
		HUDManip.add(removeActions).pad(BUTTONPAD);
		
		stage.addActor(HUDManip);
		
		dispActions.addListener(new ChangeListener() {
			@Override
			/*displays the (-) button for setting the hud to invisible*/
			public void changed(ChangeEvent event, Actor actor) {
				if (inventoryToggle) {
					LOGGER.debug("Enable hud"); //$NON-NLS-1$
					actionsWindow.setVisible(true);
					minimap.setVisible(true);
					resourceTable.setVisible(true);
					//show (-) button to make resources invisible
					dispActions.remove();
					HUDManip.add(removeActions);
					inventoryToggle = false;
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
					inventoryToggle = true;
			}	
		});
		
		dispTech.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				new TechTreeView("TechTree", skin).show(stage); //$NON-NLS-1$
			}

		});
		
		/*Display the player's stats*/
		dispStats.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				stats.showStats(); 
			}
		});
		
		
		dispFog.addListener(new ChangeListener() {
			@Override
			/*displays the (-) button for setting the hud to invisible*/
			public void changed(ChangeEvent event, Actor actor) {
				toggleFog();
			}	
		});

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
		resourceTable.add(waterCount).padRight(60);
		
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
					} else {
						ActionSetter.setBuild(selectedEntity, (EntityID)current);
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
     * Currently sets the health to 100 once a selectable unit is selected.
     * @param target unit clicked on by player
     */
    private void setEnitity(BaseEntity target) {
		for (int i = 0; i < NUMBER_ACTION_BUTTONS; i++) {
			disableButton(buttonList.get(i));
		}
		if (selectedEntity == null) {
            return;
        }
		Texture entity = textureManager.getTexture(target.getTexture());
		TextureRegion entityRegion = new TextureRegion(entity);
		TextureRegionDrawable redraw = new TextureRegionDrawable(entityRegion);
		selectedImage.setDrawable(redraw);
        selectedEntity = target;
		currentActions = target.getValidActions();
		EntityStats stats = target.getStats();
		updateSelectedStats(stats);
        enterActions();
    }

    /**
     * Updates the health bar to the selected entity's health stats
     * @param stats
     */
    private void updateSelectedStats (EntityStats stats) {
		healthBar.setValue(stats.getHealth());
		nameLabel.setText(stats.getName());
		healthLabel.setText("Health: " + stats.getHealth()); //$NON-NLS-1$
		//Update the health progress bad to red once health is below 20 
		if (stats.getHealth() <= CRITICALHEALTH) {
			pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
			pixmap.setColor(Color.RED);
			pixmap.fill();
			barStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
			pixmap.dispose();
		} else if (stats.getHealth() > CRITICALHEALTH){
			pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
			pixmap.setColor(Color.GREEN);
			pixmap.fill();
			barStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
			pixmap.dispose();
		}
	}

    /**
     *  Creates the basic structure of the Entities picker menu
     */
	private void setupEntitiesPickerMenu(){
        entitiesPicker = new Window("Customize World", skin);
        entitiesPicker.align(Align.topLeft);
        entitiesPicker.setPosition(220,0);
        entitiesPicker.setMovable(false);
        entitiesPicker.setVisible(false);
        entitiesPicker.setWidth(stage.getWidth()-220);
        entitiesPicker.setHeight(220);
    }

    /**
     * Add the customise window / entities picker.
     * This method shall only be called when the "Customize" button from the start menu is clicked.
     * If this method is call, it will cause that the actions window be set to not visible.
     */
    private void addEntitiesPickerMenu(){
        setupEntitiesPickerMenu();

        Table table = new Table();
        TextButton unitsButton = new TextButton("Units",skin);
        unitsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addUnitsPickerMenu();
            }
        });
        TextButton buildingsButton = new TextButton("Buildings",skin);
        buildingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addBuildingsPickerMenu();
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

        table.add(unitsButton).width(entitiesPicker.getWidth()/TYPES_OF_ENTITIES).height(entitiesPicker.getHeight());
        table.add(buildingsButton).width(entitiesPicker.getWidth()/TYPES_OF_ENTITIES).height(entitiesPicker.getHeight());
        table.add(resourcesButton).width(entitiesPicker.getWidth()/TYPES_OF_ENTITIES).height(entitiesPicker.getHeight());
        table.add(terrainsButton).width(entitiesPicker.getWidth()/TYPES_OF_ENTITIES).height(entitiesPicker.getHeight());
        entitiesPicker.add(table);
        stage.addActor(entitiesPicker);

    }

    /**
     * Creates the sub menu that displays all available units
     */
    private void addUnitsPickerMenu(){
        entitiesPicker.clear();

        Table table = new Table();
        TextButton entitiesButton = new TextButton("Entity Types\n (Back)",skin);
        entitiesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addEntitiesPickerMenu();
                entitiesPicker.setVisible(true);
            }
        });
        TextButton astronautButton = new TextButton("Astronaut",skin);
        astronautButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {

			}
		});
        TextButton carrierButton = new TextButton("Carrier",skin);
        TextButton healerButton = new TextButton("Healer",skin);
        TextButton heroSpacmanButton = new TextButton("Hero Spacman",skin);
        TextButton soldierButton = new TextButton("Soldier",skin);
        TextButton spacmanButton = new TextButton("Spacman",skin);
        TextButton tankButton = new TextButton("Tank",skin);

        float buttonWidth = entitiesPicker.getWidth()/6;
        float buttonHeight = entitiesPicker.getHeight();
        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setFadeScrollBars(false);
        table.add(entitiesButton).width(buttonWidth).height(buttonHeight);
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
     */
    private void addBuildingsPickerMenu(){
        entitiesPicker.clear();

        Table table = new Table();
        TextButton entitiesButton = new TextButton("Entity Types\n (Back)",skin);
        entitiesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addEntitiesPickerMenu();
                entitiesPicker.setVisible(true);
            }
        });
        TextButton barracksButton = new TextButton("Barracks",skin);
        TextButton baseButton = new TextButton("Base",skin);
        TextButton bunkerButton = new TextButton("Bunker",skin);
        TextButton heroFactoryButton = new TextButton("Hero Factory",skin);
        TextButton turretButton = new TextButton("Turret",skin);

        float buttonWidth = entitiesPicker.getWidth()/6;
        float buttonHeight = entitiesPicker.getHeight();
        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setFadeScrollBars(false);

        table.add(entitiesButton).width(buttonWidth).height(buttonHeight);
        table.add(barracksButton).width(buttonWidth).height(buttonHeight);
        table.add(baseButton).width(buttonWidth).height(buttonHeight);
        table.add(bunkerButton).width(buttonWidth).height(buttonHeight);
        table.add(heroFactoryButton).width(buttonWidth).height(buttonHeight);
        table.add(turretButton).width(buttonWidth).height(buttonHeight);

        entitiesPicker.add(scrollPane).width(entitiesPicker.getWidth()).height(entitiesPicker.getHeight());

    }

    /**
     * Creates the sub menu that displays all available resources
     */
    private void addResourcesPickerMenu(){
        entitiesPicker.clear();

        Table table = new Table();
        TextButton entitiesButton = new TextButton("Entity Types\n (Back)",skin);
        entitiesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addEntitiesPickerMenu();
                entitiesPicker.setVisible(true);
            }
        });
        TextButton biomassButton = new TextButton("Biomass",skin);
        TextButton crystalButton = new TextButton("Crystal",skin);
        TextButton rockButton = new TextButton("Rock",skin);
        TextButton waterButton = new TextButton("Water",skin);

        float buttonWidth = entitiesPicker.getWidth()/6;
        float buttonHeight = entitiesPicker.getHeight();
        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setFadeScrollBars(false);

        table.add(entitiesButton).width(buttonWidth).height(buttonHeight);
        table.add(biomassButton).width(buttonWidth).height(buttonHeight);
        table.add(crystalButton).width(buttonWidth).height(buttonHeight);
        table.add(rockButton).width(buttonWidth).height(buttonHeight);
        table.add(waterButton).width(buttonWidth).height(buttonHeight);

        entitiesPicker.add(scrollPane).width(entitiesPicker.getWidth()).height(entitiesPicker.getHeight());

    }

    /**
     * Creates the sub menu that displays all available terrains
     */
    private void addTerrainsPickerMenu(){
        entitiesPicker.clear();

        Table table = new Table();
        TextButton entitiesButton = new TextButton("Entity Types\n (Back)",skin);
        entitiesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addEntitiesPickerMenu();
                entitiesPicker.setVisible(true);
            }
        });
        TextButton caveButton = new TextButton("Cave",skin);
        TextButton lakeButton = new TextButton("Lake",skin);
        TextButton pondButton = new TextButton("Pond",skin);
        TextButton quicksandButton = new TextButton("Quicksand",skin);

        float buttonWidth = entitiesPicker.getWidth()/6;
        float buttonHeight = entitiesPicker.getHeight();
        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setFadeScrollBars(false);

        table.add(entitiesButton).width(buttonWidth).height(buttonHeight);
        table.add(caveButton).width(buttonWidth).height(buttonHeight);
        table.add(lakeButton).width(buttonWidth).height(buttonHeight);
        table.add(pondButton).width(buttonWidth).height(buttonHeight);
        table.add(quicksandButton).width(buttonWidth).height(buttonHeight);

        entitiesPicker.add(scrollPane).width(entitiesPicker.getWidth()).height(entitiesPicker.getHeight());

    }

    /**
     * Displays the entities picker menu.
     * If picker is shown then fog is off and game is paused
     *
     * @param isVisible whether to display the picker or hide it.
     */
    public void showEntitiesPicker( boolean isVisible){
        entitiesPicker.setVisible(isVisible);
        toggleFog();
        // pause not implemented yet.
    }
    
    /**
     * Enables action button based on the actions avaliable to 
     * the selected entity
     */
	private void enterActions() {
		if (currentActions == null) {
			return;
		}
        for (int i = 0; i < currentActions.size(); i++) {
				enableButton(buttonList.get(i));
				if (currentActions.get(i) instanceof ActionType) { //If it is an action
					buttonList.get(i).setText(ActionSetter.getActionName((ActionType)currentActions.get(i)));
				} else { //If it isnt an action it is something to build
					buttonList.get(i).setText("Build " + (EntityID)currentActions.get(i)); //$NON-NLS-1$
				}
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

		addEntitiesToMiniMap();

		if (timeManager.isNight()){
			gameTimeDisp.setColor(Color.FIREBRICK);
			gameLengthDisp.setColor(Color.FIREBRICK);
		}
		else{
			gameTimeDisp.setColor(Color.BLUE);
			gameLengthDisp.setColor(Color.BLUE);
		}
		
		/*Update Minimap*/
		this.updateMiniMapMenu();
		
		/*Update the resources count*/
		ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		rockCount.setText("" + resourceManager.getRocks(-1)); //$NON-NLS-1$
		crystalCount.setText("" + resourceManager.getCrystal(-1));  //$NON-NLS-1$
		waterCount.setText("" + resourceManager.getWater(-1)); //$NON-NLS-1$
		biomassCount.setText("" + resourceManager.getBiomass(-1)); //$NON-NLS-1$
		
		/*Set value for health bar*/
		healthBar.setValue(0);
		
		int spacmenCount = 0; 		//counts the number of spacmen in game
		int enemySpacmanCount = 0;  //counts the number of spatmen in game
		//Get the selected entity
		selectedEntity = null;
		for (BaseEntity e : gameManager.get().getWorld().getEntities()) {
			if (e.isSelected()) {
				selectedEntity = e;
			}
			if (e instanceof Spacman) {
				spacmenCount++; 
			}
		}
		//Get the details from the selected entity
	    setEnitity(selectedEntity);

	    /* Update the spacmen + enemy spatmen counts */
	    playerSpacmen.setText("" + spacmenCount); //$NON-NLS-1$
		playerEnemySpacmen.setText("" + enemySpacmanCount); //$NON-NLS-1$
		
		if (spacmenCount == 0) {
			/*TODO get spacman icon to be spacman_ded when there
			are no spacmen left*/
			spacman = new Image(textureManager.getTexture("spacman_ded")); //$NON-NLS-1$
		}
		
		//keyboard listeners for hotkeys
		
		//help listener
		if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			new ExitGame("Quit Game", skin).show(stage); //$NON-NLS-1$
		}
		
		//chat listener
		if(Gdx.input.isKeyJustPressed(Input.Keys.C)) {
			if (messageToggle){
				messageWindow.setVisible(false);
				messageToggle = false; 
			} else {
				messageWindow.setVisible(true);
				messageToggle = true;
			}
		}
		
		//tech tree listener
		if(Gdx.input.isKeyJustPressed(Input.Keys.T)) {
			new TechTreeView("TechTree", skin).show(stage); //$NON-NLS-1$
		}
		
		//HUD toggle listener
		if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			if (inventoryToggle) {
				LOGGER.debug("Enable hud"); //$NON-NLS-1$
				actionsWindow.setVisible(true);
				minimap.setVisible(true);
				resourceTable.setVisible(true);
				//show (-) button to make resources invisible
				dispActions.remove();
				HUDManip.add(removeActions);
				inventoryToggle = false;
			} else {
				LOGGER.debug("Disable Hud"); //$NON-NLS-1$
				actionsWindow.setVisible(false);
				minimap.setVisible(false);
				resourceTable.setVisible(false);
				//show (+) to show resources again
				removeActions.remove();
				HUDManip.add(dispActions);
				inventoryToggle = true;
			}
		}
		
		//help button listener
		if(Gdx.input.isKeyJustPressed(Input.Keys.H)) {
			new WorkInProgress("Help  Menu", skin).show(stage); //$NON-NLS-1$
		}
		
		//pause menu listener
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			new PauseMenu("Pause Menu", skin).show(stage);
		}
			
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
				//peonButton = new TextButton("Select a Unit", skin);
				//helpText.setText("Welcome to SpacWars");
			}
			//etActionWindow().add(peonButton);
			//etActionWindow().add(helpText);

			lastMenuTick = TimeUtils.nanoTime();

			}
		}
	
	/**
	 * Sets every HUD element invisible
	 */
	public void disableHUD() {
		overheadRight.setVisible(false);
		resourceTable.setVisible(false);
	    playerdetails.setVisible(false);
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
	    playerdetails.setVisible(true);
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
		playerdetails.setWidth(100);
        playerdetails.align(Align.left | Align.top);
        playerdetails.setPosition(0, stage.getHeight());
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
}



