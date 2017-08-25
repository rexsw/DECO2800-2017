package com.deco2800.marswars.hud;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Selectable;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TimeManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Naziah Siddique on 19/08
 * Initiates the HUD for SpacWars, with the help of instantiations of 
 * other components from other classes in the packages
 */
public class HUDView extends ApplicationAdapter{
	private static final Logger LOGGER = LoggerFactory.getLogger(HUDView.class);

	private Stage stage;
	private Skin skin;
	private Table overheadLeft;
	private Table overheadRight;
	private Table resourceTable; 
	
	private Button quitButton;
	private Button helpButton;
	private Button messageButton;
	
	private Label rockCount; 
	private Label crystalCount; 
	private Label biomassCount; 
	private Label waterCount; 
	
	private int gameWidth;
	private int gameHeight; 
	
	private Window messageWindow; 
	private boolean messageToggle; 
	private boolean inventoryToggle; 
	private boolean menuToggle; 
	private Label timeDisp; 
	
	private Window mainMenu; 
	private Label gameTimeDisp;
	private Label gameLengthDisp;
	private Window resources; 
	private Window minimap;
	private Window inventory;
	
	private ProgressBar healthBar;
	private GameManager gameManager;
	private TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);	
	private ChatBox chatbox;
	
	/**
	 * Creates a 'view' instance for the HUD. This includes all the graphics
	 * of the HUD and is mostly for simply displaying components on screen. 
	 * @param stage the game stage
	 * @param skin the look of the HUD, depending on the world/level the game is being played at
	 * @param gameManager handles selectables
	 */

	public HUDView(Stage stage, Skin skin, GameManager gameManager) {
		// zero game length clock (i.e. tell TimeManager new game has been launched)
		LOGGER.debug("Creating Hud");
		timeManager.setGameStartTime();
		this.skin = skin;
		this.stage = stage;
		this.gameManager = gameManager;
		this.gameWidth = (int) stage.getWidth();
		this.gameHeight = (int) stage.getHeight();
		this.chatbox = new ChatBox(skin);
		messageToggle = true; 
		createLayout();
	}

	/**
	 * Adds in all components of the HUD 
	 */
	private void createLayout(){
		topLeft();
		topRight();
		addPlayerIcon();
		addMessages();
		addBottomPanel();
	}
	
	/*To allow for old menu use - will be removed later*/
	public void setMenu(Window window){
		mainMenu = window; 
	}
	
	/**
	 * Contains top right section of the HUD to be display on screen and set to stage. 
	 * This includes the message tab, help button and quit button
	 */
	private void topRight(){
		overheadRight = new Table();
		overheadRight.setWidth(stage.getWidth());
		overheadRight.align(Align.right | Align.top);
		overheadRight.setPosition(0, Gdx.graphics.getHeight());

		LOGGER.debug("Add help, quit and message buttons");
		helpButton = new TextButton("Help (?)", skin);
		quitButton = new TextButton("Quit (X)", skin);
		messageButton = new TextButton("Messages", skin);

		LOGGER.debug("Creating time labels");
		gameTimeDisp = new Label("Time: 0:00", skin);
		gameLengthDisp = new Label("00:00:00", skin);

		overheadRight.add(gameTimeDisp).pad(10);
		overheadRight.add(gameLengthDisp).pad(10);
		overheadRight.add(timeDisp).pad(10);
		overheadRight.add(messageButton).pad(10);
		overheadRight.add(helpButton).pad(10);
		overheadRight.add(quitButton).pad(10);
						
		stage.addActor(overheadRight);
		
		//can we make this a method of it's own? 
		LOGGER.debug("Creating help button listener");
		helpButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				new WorkInProgress("help text", skin).show(stage);
			}
		});
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


		LOGGER.debug("Creating message button listener");
		messageButton.addListener(new ChangeListener() {
			@Override 
			public void changed(ChangeEvent event, Actor actor){
				//System.out.println("Just let me die");
				if (messageToggle){
					messageWindow.setVisible(true);
					messageToggle = false; 
				} else {
					messageWindow.setVisible(false);
					messageToggle = true;
				}
				
			}
		});
	}
	
	
	/**
	 * Adds in the top left section of the HUD. This includes the  
	 */
	private void topLeft(){
		//Adds in welcome text
		Label welcomeLabel = new Label("Welcome to SpacWars!", skin);
		Table welcomeTable = new Table();
		welcomeTable.align(Align.top | Align.center);
		welcomeTable.setWidth(gameWidth);
		welcomeTable.add(welcomeLabel).pad(10);
		welcomeTable.setPosition(0, gameHeight);
		stage.addActor(welcomeTable);

		//Adds in the container managing the health status + player deets 
		overheadLeft = new Table();
		overheadLeft.setDebug(true);
		overheadLeft.setWidth(stage.getWidth());
		overheadLeft.align(Align.left | Align.top);
		overheadLeft.setPosition(60, stage.getHeight());
		LOGGER.debug("Creating health bar");
		addProgressBar();
		Label healthLabel = new Label("Health: ", skin);
		healthLabel.setAlignment(Align.left);
		
		overheadLeft.add(healthBar);
		
		overheadLeft.row();
		overheadLeft.add(healthLabel);

		stage.addActor(overheadLeft);
	}
	
	
	/**
	 * HELPER METHOD: 
	 * Adds the player Icon and name to the huD (goes into top left) 
	 */
	private void addPlayerIcon(){
		LOGGER.debug("Adding player icon");
		Table playerdetails = new Table();
		playerdetails.setDebug(true);
		playerdetails.setWidth(100);
		playerdetails.align(Align.left | Align.top);
		playerdetails.setPosition(0, stage.getHeight());
				
		Label playerName = new Label("Name", skin);
		playerdetails.pad(10).add(playerName);
		
		stage.addActor(playerdetails);
	}
	
	/**
	 * Adds in progress bar to the top left of the screen 
	 */
	private void addProgressBar(){
		Pixmap pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.DARK_GRAY);
		pixmap.fill();
		ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle();
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
		Label message = new Label("Implementing the chat lobby here", skin);
		
		messageWindow.add(message);
		messageWindow.setWidth(400);
		messageWindow.setHeight(400);
		messageWindow.pack();
		messageWindow.setMovable(false);
		messageWindow.setPosition(gameWidth, gameHeight-50, Align.right);
		messageWindow.add(chatbox);
		messageWindow.setVisible(false);
		
		stage.addActor(messageWindow);
	}
	
	/**
	 * Adds in the bottom panel of the HUD 
	 */
	private void addBottomPanel(){
		addInventoryMenu();
		addMiniMapMenu();

		LOGGER.debug("Creating HUD manipulation buttons");
		Button dispMainMenu = new TextButton("Old Menu", skin);
		Button dispMap = new TextButton("Map", skin);
		Button dispInventory = new TextButton("Display\nPanel", skin);
		
		//Set button positions 
		dispMainMenu.setPosition(gameWidth-80, 150);
		dispMap.setPosition(gameWidth-80, 100);
		dispInventory.setPosition(gameWidth-80, 50);
		
		stage.addActor(dispMainMenu);
		stage.addActor(dispMap);
		stage.addActor(dispInventory);
		
		// can we make this a method of it's own?
		dispInventory.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (inventoryToggle) {
					LOGGER.debug("Enable hud");
					inventory.setVisible(true);
					minimap.setVisible(true);
					resourceTable.setVisible(true);
					inventoryToggle = false;
				} else {
					LOGGER.debug("Disable Hud");
					inventory.setVisible(false);
					minimap.setVisible(false);
					resourceTable.setVisible(false);
					inventoryToggle = true;
				}
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
		
		dispMap.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				LOGGER.debug("Opening map");
				new WorkInProgress("Unimplemented", skin).show(stage);
			}	
		});
	}	
	
	/**
	 * Adds in the selectable menu for the inventory for resources 
	 */
	private void addInventoryMenu(){
		LOGGER.debug("Create inventory");
		inventory = new Window("Actions", skin);
		//Label resources  = new Label("All the resouces saved here, will implement a proper popup option", skin);
		
		resourceTable = new Table();
		resourceTable.align(Align.left | Align.top);
		resourceTable.setHeight(80);
		resourceTable.setWidth(500);
		resourceTable.setColor(Color.DARK_GRAY);
		resourceTable.setPosition(240, 140);
		LOGGER.debug("Creating resource labels");
		rockCount = new Label("Rock: 0", skin);
		crystalCount = new Label("Crystal: 0", skin);
		biomassCount = new Label("Biomass: 0", skin);
		waterCount = new Label("Water: 0", skin);
		
		resourceTable.add(rockCount).pad(20);
		resourceTable.add(crystalCount).pad(20);
		resourceTable.add(biomassCount).pad(20);
		resourceTable.add(waterCount).pad(20);
		
		stage.addActor(resourceTable);
		
		
		inventory.setMovable(false);
		inventory.align(Align.topLeft);
		inventory.setWidth(gameWidth-700);
		inventory.setHeight(150);
		inventory.setPosition(220, 0);
		
		stage.addActor(inventory);
	}
	
	/**
	 * Adds in the minimap window 
	 */
	private void addMiniMapMenu(){
		LOGGER.debug("Creating minimap menu");
		minimap = new Window("Map", skin);
		
		Label label = new Label("Not sure if this will still be \n implemented, but here's a \n placeholder anyway", skin);
		label.setWrap(true);
				
		minimap.add(label);
		minimap.align(Align.topLeft);
		minimap.setPosition(0, 0);
		minimap.setMovable(false);
		minimap.setWidth(220);
		minimap.setHeight(220);
		
		stage.addActor(minimap);
		
	}
	
	/**
	 * Updates any features of the HUD that may change through time/ game actions
	 */
    public void render(){
		/*
		 * Update time & set color depending if night/day
		 */
		gameTimeDisp.setText(" Time: " + timeManager.toString());
		gameLengthDisp.setText(timeManager.getPlayClockTime());
		
		if (timeManager.isNight()){
			gameTimeDisp.setColor(Color.FIREBRICK);
			gameLengthDisp.setColor(Color.FIREBRICK);
		}
		else{
			gameTimeDisp.setColor(Color.BLUE);
			gameLengthDisp.setColor(Color.BLUE);
		}
		
		ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		
		rockCount.setText("Rocks: " + resourceManager.getRocks());
		crystalCount.setText(" Crystal: " + resourceManager.getCrystal()); 
		waterCount.setText(" Water: " + resourceManager.getWater());
		biomassCount.setText(" Biomass: " + resourceManager.getBiomass());
		
		/*Set value for health bar*/
		healthBar.setValue(0);
		for (BaseEntity e : gameManager.get().getWorld().getEntities()) {
			if (e.isSelected()) {
				setEnitity(e);
			}
		}
    	
    }

    /**
     * Currently sets the health to 100 once a selectable unit is selected. 
     * @param target unit clicked on by player
     */
    private void setEnitity(Selectable target) {
		if (target.getEntityType() == Selectable.EntityType.UNIT) {
			healthBar.setValue(100);
		}
	}

	
	public Window getMessage() {
		return messageWindow; 
	}
	
	/**
	 * Returns the Actions window to display helper text
	 */
	public Window getActionWindow() {
		return inventory;
	}

}



