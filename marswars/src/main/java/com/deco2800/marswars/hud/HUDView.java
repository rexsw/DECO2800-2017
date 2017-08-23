package com.deco2800.marswars.hud;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Selectable;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TimeManager;

/**
 * Created by Naziah Siddique on 19/08
 * Initiates the HUD for SpacWars, with the help of instantiations of 
 * other components from other classes in the packages
 */
public class HUDView extends ApplicationAdapter{
	Label test;
	Label test1;
	private Stage stage;
	private Skin skin;
	private Table overheadLeft;
	private Table overheadRight;
	
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
	private Label timeDisp; 
	
	private Window resources; 
	private Window minimap;
	private Window inventory;
	
	private ProgressBar healthBar;
	private GameManager gameManager;
	
	/**
	 * Creates a 'view' instance for the HUD. This includes all the graphics
	 * of the HUD and is mostly for simply displaying components on screen. 
	 * @param stage the game stage
	 * @param skin the look of the HUD, depending on the world/level the game is being played at
	 * @param gameManager handles selectables
	 */
	public HUDView(Stage stage, Skin skin, GameManager gameManager) {
		this.skin = skin;
		this.stage = stage;
		this.gameManager = gameManager;
		this.gameWidth = (int) stage.getWidth();
		this.gameHeight = (int) stage.getHeight();
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
	
	/**
	 * Contains top right section of the HUD to be display on screen and set to stage. 
	 * This includes the message tab, help button and quit button
	 */
	private void topRight(){
		overheadRight = new Table();
		overheadRight.setWidth(stage.getWidth());
		overheadRight.align(Align.right | Align.top);
		overheadRight.setPosition(0, Gdx.graphics.getHeight());
		
		helpButton = new TextButton("Help (?)", skin);
		quitButton = new TextButton("Quit (X)", skin);
		messageButton = new TextButton("Messages", skin);
		
		timeDisp = new Label("Time: 0:00", skin);
		
		overheadRight.add(timeDisp);
		overheadRight.add(messageButton);
		overheadRight.add(helpButton);
		overheadRight.add(quitButton);
		
				
		stage.addActor(overheadRight);
		
		//can we make this a method of it's own? 
		helpButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				new WorkInProgress("help text", skin).show(stage);
			}
		});
		
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
		
		messageButton.addListener(new ChangeListener() {
			@Override 
			public void changed(ChangeEvent event, Actor actor){
				System.out.println("Just let me die");
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
		Label welcomeLabel = new Label("Welcome to SpacWars", skin);
		Table welcomeTable = new Table();
		welcomeTable.align(Align.top | Align.center);
		welcomeTable.setWidth(gameWidth);
		welcomeTable.add(welcomeLabel);
		welcomeTable.setPosition(0, gameHeight);
		stage.addActor(welcomeTable);

		//Adds in the container managing the health status + player deets 
		overheadLeft = new Table();
		overheadLeft.setDebug(true);
		overheadLeft.setWidth(stage.getWidth());
		overheadLeft.align(Align.left | Align.top);
		overheadLeft.setPosition(60, stage.getHeight());
		
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
		Table playerdetails = new Table();
		playerdetails.setDebug(true);
		playerdetails.setWidth(100);
		//playerdetails.setHeight(70);
		playerdetails.align(Align.left | Align.top);
		playerdetails.setPosition(0, stage.getHeight());
				
		Label playerName = new Label("Name", skin);
		playerdetails.add(playerName);
		
		stage.addActor(playerdetails);
	}
	
	/**
	 * Adds in progress bar to the top left 
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
		messageWindow = new Window("Chat Lobby", skin);
		Label message = new Label("Implementing the chat lobby here", skin);
		
		messageWindow.add(message);
		messageWindow.setWidth(400);
		messageWindow.setHeight(gameHeight-200);
		messageWindow.pack();
		messageWindow.setMovable(false);
		messageWindow.setPosition(gameWidth, gameHeight-50, Align.right);
		
		messageWindow.setVisible(false);
		
		stage.addActor(messageWindow);
	}
	
	/**
	 * Adds in the bottom panel of the HUD 
	 */
	private void addBottomPanel(){
		addInventoryMenu();
		addMiniMapMenu();
		//addResources();
		
		Button dispResource = new TextButton("Resources", skin);
		Button dispMap = new TextButton("Map", skin);
		Button dispInventory = new TextButton("Display\nPanel", skin);
		
		//Set button positions 
		dispResource.setPosition(gameWidth-80, 150);
		dispMap.setPosition(gameWidth-80, 100);
		dispInventory.setPosition(gameWidth-80, 50);
		
		stage.addActor(dispResource);
		stage.addActor(dispMap);
		stage.addActor(dispInventory);
		
		// can we make this a method of it's own?
		dispInventory.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (inventoryToggle) {
					inventory.setVisible(true);
					minimap.setVisible(true);
					//resources.setVisible(true);
					inventoryToggle = false;
				} else {
					inventory.setVisible(false);
					minimap.setVisible(false);
					//resources.setVisible(false);
					inventoryToggle = true;
				}
			}
			
			
		});
		
		dispResource.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				new WorkInProgress("Unimplemented", skin).show(stage);
			}
			
		});
		
		dispMap.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				new WorkInProgress("Unimplemented", skin).show(stage);
			}
			
		});
		
	}	
	
	/**
	 * Adds in the selectable menu for the inventory for resources 
	 */
	private void addInventoryMenu(){
		inventory = new Window("Actions", skin);
		//Label resources  = new Label("All the resouces saved here, will implement a proper popup option", skin);
		
		Table resourceTable = new Table();
		resourceTable.align(Align.left | Align.top);
		resourceTable.setHeight(80);
		resourceTable.setWidth(500);
		resourceTable.setColor(Color.DARK_GRAY);
		resourceTable.setPosition(240, 140);
		
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
	 * Adds in the resources count 
	 */
	/*private void addResources(){
		resources = new Window("Resources", skin);
		
		resources.setPosition(200, 0);
		resources.setMovable(false);
		resources.setWidth(150);
		resources.setHeight(200);
		
		stage.addActor(resources);
		
	}
	*/
	/**
	 * Updates any features of the HUD that may change through time/ game actions
	 */
    public void render(){
		/*
		 * Update time & set color depending if night/day
		 */
		TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);
		timeDisp.setText(" Time: " + timeManager.toString());
		if (timeManager.isNight()){
			timeDisp.setColor(Color.FIREBRICK);
		}
		else{
			timeDisp.setColor(Color.BLUE);
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

	public Window getInventory() {
        return inventory;
    }
	
	public Window getMessage() {
		return messageWindow; 
	}

}



