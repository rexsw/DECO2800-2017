package com.deco2800.marswars.hud;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.actions.AttackAction;
import com.deco2800.marswars.entities.EntityStats;
import com.deco2800.marswars.entities.Inventory;
import com.deco2800.marswars.entities.items.Armour;
import com.deco2800.marswars.entities.items.Special;
import com.deco2800.marswars.entities.items.Weapon;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.managers.TextureManager;

public class UnitStatsBox extends Table{
	private Skin skin;
	private TextureManager tm;
	private Image unitImage;
	private String unitName;
	private Pixmap pixmap; 		   //used for progress bar 
	ProgressBar.ProgressBarStyle barStyle;
	//Player stats + progress bar 
		private Label healthLabel;     //numeric indicator for health level
		private Label nameLabel;       //name of player 
		private ProgressBar healthBar; //progress bar displaying spacmen health
		

		private Table heroInventory; // hero inventory display
		private static final int CRITICALHEALTH = 30; //critical health of spacmen
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UnitStatsBox.class);

	/**
     * Creates a new instance of the unit stats area.
     * 
     * @param skin The UI skin to be applied to all elements of this area
     * @param textureManager textureManager of the game
     */
    public UnitStatsBox(Skin skin, TextureManager textureManager) {
        this.skin = skin;
        this.tm = textureManager;
        // Create the elements of chat box
        unitImage = new Image(textureManager.getTexture("spacman_blue"));
		this.add(unitImage).height(100).width(100);
		
		//create table for health bar display
		Table healthTable = new Table();
		//Create the health bar 
		LOGGER.debug("Creating health bar"); //$NON-NLS-1$
		addProgressBar();
		healthLabel = new Label("Health: ", skin); //$NON-NLS-1$
		healthTable.add(healthLabel).align(Align.left);
		healthTable.row();
		healthTable.add(healthBar);
		
		this.add(healthTable);

		//add in player name
		this.row();
		this.nameLabel = new Label("Name", skin); //$NON-NLS-1$
		this.add(nameLabel);
		
		
		// add in the hero inventory display
		heroInventory = new Table();
		setUpHeroInventory();
		heroInventory.setVisible(false);
		this.add(heroInventory);
    }
    
    /**
	 * This method just set up the hero inventory to let it has a dark background
	 */
	private void setUpHeroInventory() {
		heroInventory = new Table();
		pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.DARK_GRAY);
		pixmap.fill();
		heroInventory.background(new TextureRegionDrawable(new TextureRegion(new Texture(pixmap))));
		pixmap.dispose();
	}
	
	/**
	 * This method will gets called when user select a hero character, this method then 
	 * display this hero's items on the HUD
	 */
	public void updateHeroInventory(Commander hero) {	
		ImageButton weaponBtn;
		ImageButton armourBtn;
		heroInventory.clear();
		
		pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.DARK_GRAY);
		pixmap.fill();
		
		Inventory inventory = hero.getInventory();
		Weapon weapon = inventory.getWeapon();
		Armour armour = inventory.getArmour();
		List<Special> specials = inventory.getSpecials();
		//heroInventory.debugAll();
		if(weapon != null) {
			 weaponBtn= generateItemButton(tm.getTexture(weapon.getTexture()));
			//will add handler later
//			weaponBtn.addListener(new ClickListener(Buttons.RIGHT)
//			{
//			    @Override
//			    public void clicked(InputEvent event, float x, float y)
//			    {
//			        
//			    }
//			});
		} else {
			weaponBtn = generateItemButton(tm.getTexture("locked_inventory"));
		}
		heroInventory.add(weaponBtn).width(30).height(30).pad(3);
		
		if(armour != null) {
			armourBtn = generateItemButton(tm.getTexture(armour.getTexture()));
			//will add handler later
//			weaponBtn.addListener(new ClickListener(Buttons.RIGHT)
//			{
//			    @Override
//			    public void clicked(InputEvent event, float x, float y)
//			    {
//			        
//			    }
//			});
		} else {
			armourBtn = generateItemButton(tm.getTexture("locked_inventory"));
		}
		heroInventory.add(armourBtn).width(30).height(30).pad(3);
		
		int size = specials.size();
		for(Special s : specials) {
			ImageButton specialBtn = generateItemButton(tm.getTexture(s.getTexture()));
			heroInventory.add(specialBtn).width(30).height(30).pad(3);
			// handler here
		}
		for(int i = 0; i < 4-size; i++) {
			ImageButton specialBtn = generateItemButton(tm.getTexture("locked_inventory"));
			heroInventory.add(specialBtn).width(30).height(30).pad(3);
		}
		
		//heroInventory.setVisible(false);
		
	}
	
	public void hideInventory() {
		this.heroInventory.setVisible(false);
	}
	

	public void showInventory() {
		this.heroInventory.setVisible(true);
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
     * Updates the health bar to the selected entity's health stats
     * @param stats
     */
    public void updateSelectedStats (EntityStats stats) {
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
    
    public void updateImage(Texture texture) {
    	TextureRegion entityRegion = new TextureRegion(texture);
		TextureRegionDrawable redraw = new TextureRegionDrawable(entityRegion);
		unitImage.setDrawable(redraw);
    }

    /**
	 * Private helper method to make image buttons for the items with the provided texture (the item icon image).
	 * @param image  Texture that is the desired item icon for the button
	 * @return ImageButton object that has the provided image for the item icon
	 */
	private ImageButton generateItemButton(Texture image) {
		TextureRegion imgRegion = new TextureRegion(image);
		TextureRegionDrawable imgDraw = new TextureRegionDrawable(imgRegion);
		return new ImageButton(imgDraw);
	}
}
