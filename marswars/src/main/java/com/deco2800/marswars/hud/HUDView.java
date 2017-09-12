package com.deco2800.marswars.hud;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.managers.FogManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.managers.TextureManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Naziah Siddique on 19/08
 * Initiates the HUD for SpacWars, with the help of instantiations of 
 * other components from other classes in the packages
 */
public class HUDView extends ApplicationAdapter{
	private static final Logger LOGGER = LoggerFactory.getLogger(HUDView.class);
	
	private static final int BUTTONSIZE = 40; //sets size of image buttons 
	private static final int BUTTONPAD = 10;  //sets padding between image buttons 
	private static final int CRITICALHEALTH = 30; //critical health of spacmen 

	private Stage stage;
	private Skin skin;
	
	ProgressBar.ProgressBarStyle barStyle;
	//HUD elements 
	private Table overheadRight; //contains all basic quit/help/chat buttons
	private Table resourceTable; //contains table of resource images + count
    private Table playerdetails; //contains player icon, health and game stats
    private Table HUDManip;		 //contains buttons for toggling HUD + old menu
    private Table welcomeMsg; 	 //contains welcome message 
	private ChatBox chatbox;	 //table for the chat
	private Window messageWindow;//window for the chatbox 
	private Window mainMenu;     //window for the old menu
	private Window minimap;		 //window for containing the minimap
	private Window actionsWindow;    //window for the players actions 
	
    //Action buttons 
	private Button quitButton; 	 // quits game
	private Button helpButton;   // calls help
	private Button messageButton;//opens or closes chatbox
	
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
	private Button attackButton;	//spacman commands
	private Button gatherButton;	
	private Button moveButton;

	//Toggles; checks if the feature is visible on-screen or not
	private boolean messageToggle; 
	private boolean inventoryToggle; 
	private boolean menuToggle;
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

	/**
	 * Creates a 'view' instance for the HUD. This includes all the graphics
	 * of the HUD and is mostly for simply displaying components on screen. 
	 * @param stage the game stage
	 * @param skin the look of the HUD, depending on the world/level the game is being played at
	 * @param gameManager handles selectables
	 * @param textureManager 
	 */
	public HUDView(Stage stage, Skin skin, GameManager gameManager, TextureManager textureManager) {
		// zero game length clock (i.e. tell TimeManager new game has been launched)
		LOGGER.debug("Creating Hud");
		timeManager.setGameStartTime();
		this.skin = skin;
		this.stage = stage;
		this.gameManager = gameManager;
		this.textureManager = textureManager;
		this.chatbox = new ChatBox(skin, textureManager);
		
		//create the HUD 
		createLayout();
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
	 * To allow for old menu use - will be removed later
	 * */
	public void setMenu(Window window){
		mainMenu = window; 
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

		LOGGER.debug("Creating time labels");
		gameTimeDisp = new Label("Time: 0:00", skin);
		gameLengthDisp = new Label("00:00:00", skin);

		//add in quit + help + chat buttons and time labels
		overheadRight.add(gameTimeDisp).padRight(BUTTONPAD);
		overheadRight.add(gameLengthDisp).padRight(BUTTONPAD);
		overheadRight.add(messageButton).padRight(BUTTONPAD);
		overheadRight.add(helpButton).padRight(BUTTONPAD);
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
		LOGGER.debug("Creating help button listener");
		helpButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				new WorkInProgress("help text", skin).show(stage);
			}
		});
		
		//Creates the quit button listener
		LOGGER.debug("Creating quit button listener");
		quitButton.addListener(new ChangeListener() {
			@Override
			//could abstract this into another class
			public void changed(ChangeEvent event, Actor actor) {
				new Dialog("Confirm exit", skin){
					{
						text("Are you sure you want to quit? ");
						button("Yes", 1);
						button("No, keep playing", 2);
					}
					
					@Override
					protected void result(final Object object){
						if(object == (Object) 1){
							System.exit(0);
						}
					}	
				}.show(stage);	
		}});

		//Creates the message button listener 
		LOGGER.debug("Creating message button listener");
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
		LOGGER.debug("Adding player icon");
		playerdetails = new Table();
		playerdetails.pad(10);
		playerdetails.setWidth(150);
		//playerdetails.align(Align.left | Align.top);
		playerdetails.setPosition(0, stage.getHeight());
		
		//Icon for player- 
		//TODO get main menu working to select an icon and then display 
		Image playerIcon = new Image(textureManager.getTexture("spacman_blue"));
		playerdetails.add(playerIcon).height(100).width(100);
		
		//create table for health bar display
		Table healthTable = new Table();
		//Create the health bar 
		LOGGER.debug("Creating health bar");
		addProgressBar();
		healthLabel = new Label("Health: ", skin);
		healthTable.add(healthLabel).align(Align.left);
		healthTable.row();
		healthTable.add(healthBar);
		
		playerdetails.add(healthTable);

		//add in player name
		playerdetails.row();
		Label playerName = new Label("Name", skin);
		playerdetails.add(playerName);
		
		this.nameLabel = playerName;
		
		//add in player stats to a new table 
		Table playerStats = new Table();
		playerSpacmen = new Label("Aliv spacmen: 0", skin);
		playerEnemySpacmen = new Label("Evil spacman: 0", skin);
		
		//image for spacman
		Texture spacmanTex = textureManager.getTexture("spacman_green");
		spacman = new Image(spacmanTex);
		//image for enemy spatman
		Texture spatmanTex = textureManager.getTexture("spatman_blue");
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

		Button dispMainMenu = new TextButton("Menu", skin);
			
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

		//add tech button (uses arrow icon for now)
		Texture techImage = textureManager.getTexture("tech_button");
		HUDManip = new Table(); //adding buttons into a table
		HUDManip.setPosition(stage.getWidth()-50, 50);
		TextureRegion techRegion = new TextureRegion(techImage);
		TextureRegionDrawable techRegionDraw = new TextureRegionDrawable(techRegion);
		ImageButton dispTech = new ImageButton(techRegionDraw);
		
		//add toggle Fog of war FOR (DEBUGGING) 
		Button dispFog = new TextButton("Fog", skin);
		
		
		HUDManip.setSize(50, 80);
		HUDManip.pad(BUTTONPAD);
		HUDManip.add(dispMainMenu);
		HUDManip.add(dispTech).pad(BUTTONPAD*2).height(BUTTONSIZE).width(BUTTONSIZE);
		HUDManip.add(dispFog);
		HUDManip.add(removeActions);
		
		stage.addActor(HUDManip);
		
		dispActions.addListener(new ChangeListener() {
			@Override
			/*displays the (-) button for setting the hud to invisible*/
			public void changed(ChangeEvent event, Actor actor) {
				if (inventoryToggle) {
					LOGGER.debug("Enable hud");
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
					LOGGER.debug("Disable Hud");
					actionsWindow.setVisible(false);
					minimap.setVisible(false);
					resourceTable.setVisible(false);
					//show (+) to show resources again
					removeActions.remove();
					HUDManip.add(dispActions);
					inventoryToggle = true;
			}	
		});
		
		dispMainMenu.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				if(menuToggle){
					LOGGER.debug("Enable old hud");
					mainMenu.setVisible(true);
					menuToggle = false;
				} else {
					LOGGER.debug("Disable old Hud");
					mainMenu.setVisible(false);
					menuToggle = true;
				}
			}
			
		});

		dispTech.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				new TechTreeView("TechTree", skin).show(stage);
			}

		});
		
		
		dispFog.addListener(new ChangeListener() {
			@Override
			/*displays the (-) button for setting the hud to invisible*/
			public void changed(ChangeEvent event, Actor actor) {
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
		});
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
		
		//add rock image 
		Texture rockTex = textureManager.getTexture("large_rock");
		Image rock = new Image(rockTex);
		//add water image
		Texture waterTex = textureManager.getTexture("large_water");
		Image water = new Image(waterTex);
		//add biomass image
		Texture biomassTex = textureManager.getTexture("large_biomass");
		Image biomass = new Image(biomassTex);
		//add crystal image
		Texture crystalTex = textureManager.getTexture("large_crystal");
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
		
		actionsWindow.setMovable(false);
		actionsWindow.align(Align.topLeft);
		actionsWindow.setWidth(stage.getWidth()-700);
		actionsWindow.setHeight(150);
		actionsWindow.setPosition(220, 0);
		
		//Add action buttons
		addMoveButton();
		addGatherButton();
		addAttackButton();
		addCreateUnitButtons();
		
		stage.addActor(actionsWindow);
	}

	private void addCreateUnitButtons() {
	}

	/**
	 * Adds the attack button
	 */
	private void addAttackButton() {
		attackButton = new TextButton("Attack", skin);
		attackButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				LOGGER.info("Attack button pressed");
				selectedEntity.setNextAction(ActionType.DAMAGE);
			}
		});
		actionsWindow.add(attackButton);
		enableButton(attackButton);
	}

	/**
	 * Adds the gather button
	 */
	private void addGatherButton() {
		gatherButton = new TextButton("Gather", skin);
		gatherButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				LOGGER.info("Gather button pressed");
				selectedEntity.setNextAction(ActionType.GATHER);
			}
		});
		actionsWindow.add(gatherButton);
		enableButton(gatherButton);
	}

	/**
	 * Adds the move button
	 */
	private void addMoveButton() {
		moveButton = new TextButton("Move", skin);
		moveButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				LOGGER.info("Move button pressed");
				selectedEntity.setNextAction(ActionType.MOVE);
			}
		});
		actionsWindow.add(moveButton);
		enableButton(moveButton);
	}

	/**
	 * Adds in the minimap window 
	 */
	private void addMiniMapMenu(){
		LOGGER.debug("Creating minimap menu");
		minimap = new Window("Map", skin);
		
		//set the properties of the minimap window
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
		List<MiniMapEntity> entities = GameManager.get().getMiniMap().getEntitiesOnMap();
		for (int i = 0; i < entities.size(); i++) {
			Image unit = new Image(textureManager.getTexture(entities.get(i).getTexture()));
			unit.setPosition(entities.get(i).x, entities.get(i).y);
			stage.addActor(unit);
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
		disableButton(moveButton);
		disableButton(attackButton);
		disableButton(gatherButton);
		if (selectedEntity == null) {
            return;
        }
        selectedEntity = target;
		EntityStats stats = target.getStats();
		updateSelectedStats(stats);
        enterActions(target.getValidActions());
    }

    /**
     * Updates the health bar to the selected entity's health stats
     * @param stats
     */
    private void updateSelectedStats (EntityStats stats) {
		healthBar.setValue(stats.getHealth());
		nameLabel.setText(stats.getName());
		healthLabel.setText("Health: " + stats.getHealth());
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
     * Enables action button based on the actions avaliable to 
     * the selected entity
     * @param actions avaliable to the selected entity
     */
	private void enterActions(List<ActionType> actions) {
		if (actions == null) {
			return;
		}
        for (ActionType c : actions) {
			switch (c) {
				case MOVE:
					enableButton(moveButton);
					break;
				case DAMAGE:
					enableButton(attackButton);
					break;
				case GATHER:
					enableButton(gatherButton);
					break;
				default:
					LOGGER.error("Unrecognised action type, please implement in HUDView");
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
	 */
	public void render(){
		/* Update time & set color depending if night/day */
		gameTimeDisp.setText(" Time: " + timeManager.toString());
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
		
		/*Update the resources count*/
		ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		rockCount.setText("" + resourceManager.getRocks(-1));
		crystalCount.setText("" + resourceManager.getCrystal(-1)); 
		waterCount.setText("" + resourceManager.getWater(-1));
		biomassCount.setText("" + resourceManager.getBiomass(-1));
		
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
	    playerSpacmen.setText("" + spacmenCount);
		playerEnemySpacmen.setText("" + enemySpacmanCount);
		
		if (spacmenCount == 0) {
			/*TODO get spacman icon to be spacman_ded when there
			are no spacmen left*/
			spacman = new Image(textureManager.getTexture("spacman_ded"));
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
        LOGGER.debug("Window resized, rescaling hud");
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
    }
}



