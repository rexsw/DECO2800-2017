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
	 * Helper method for the basic layout 
	 */
	private void createLayout(){
		topLeft();
		topRight();
		addPlayerIcon();
		addMessages();
		addBottomPanel();
	}
	
	/**
	 * Contains the HUD the top right section of the screen. 
	 * This includes the message tab, help button and quit button
	 */
	private void topRight(){
		overheadRight = new Table();
		overheadRight.setWidth(stage.getWidth());
		overheadRight.align(Align.right | Align.top);
		overheadRight.setPosition(0, Gdx.graphics.getHeight());
		
		Button helpButton = new TextButton("Help (?)", skin);
		Button quitButton = new TextButton("Quit (X)", skin);
		Button messageButton = new TextButton("Messages", skin);
		
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
		
		Label playerPhoto = new Label("Photo", skin);
		Image image1 = new Image();
		
		
		playerdetails.add(playerPhoto);
		playerdetails.row();
		
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
		//message.setWrap(true);
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
		addResources();
		
		Button dispResource = new TextButton("Resources", skin);
		Button dispMap = new TextButton("Map", skin);
		Button dispInventory = new TextButton("Display\nPanel", skin);
		Button dispTechTree = new TextButton("Technology Tree", skin);
		
		//Set button positions
		dispTechTree.setPosition(gameWidth-80, 200);
		dispResource.setPosition(gameWidth-80, 150);
		dispMap.setPosition(gameWidth-80, 100);
		dispInventory.setPosition(gameWidth-80, 50);

		stage.addActor(dispTechTree);
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
					resources.setVisible(true);
					inventoryToggle = false;
				} else {
					inventory.setVisible(false);
					minimap.setVisible(false);
					resources.setVisible(false);
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

		dispTechTree.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor){
				new TechTreeView("TechTree", skin).show(stage);
			}

		});
		
	}	
	
	/**
	 * Adds in the selectable menu for the inventory for resources 
	 */
	private void addInventoryMenu(){
		inventory = new Window("Inventory", skin);
		Label resources  = new Label("All the resouces saved here, will implement a proper popup option", skin);
	
		//inventory.add(resources);
		inventory.setMovable(false);
		inventory.align(Align.topLeft);
		//inventory.pack();
		inventory.setWidth(gameWidth-700);
		inventory.setHeight(200);
		inventory.setPosition(350, 0);
		
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
		minimap.setWidth(200);
		minimap.setHeight(200);
		
		stage.addActor(minimap);
		
	}
	
	/**
	 * Adds in in the resources count 
	 */
	private void addResources(){
		resources = new Window("Resources", skin);
		
		resources.setPosition(200, 0);
		resources.setMovable(false);
		resources.setWidth(150);
		resources.setHeight(200);
		
		stage.addActor(resources);
		
	}
	
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
		healthBar.setValue(0);
		for (BaseEntity e : gameManager.get().getWorld().getEntities()) {
			if (e.isSelected()) {
				setEnitity(e);
			}
		}
    	
    }

    private void setEnitity(Selectable target) {
		if (target.getEntityType() == Selectable.EntityType.UNIT) {
			healthBar.setValue(100);
		}
	}

	public Window getInventory() {
        return inventory;
    }

}



