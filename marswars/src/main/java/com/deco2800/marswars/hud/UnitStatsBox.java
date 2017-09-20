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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.marswars.entities.Inventory;
import com.deco2800.marswars.entities.items.Armour;
import com.deco2800.marswars.entities.items.Special;
import com.deco2800.marswars.entities.items.Weapon;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.managers.TextureManager;

public class UnitStatsBox extends Table{
	private TextureManager tm;
	private Image unitImage;
	private Pixmap pixmap; 		   //used for progress bar 
	
	//Player stats + progress bar 
	private Label healthLabel;     //numeric indicator for health level
	private Label nameLabel;       //name of player 
	private ProgressBar healthBar; //progress bar displaying spacmen health
	private ProgressBar armourBar;
	

	private ProgressBar.ProgressBarStyle healthBarStyle;
	private Table heroInventory; // hero inventory display
	private static final int CRITICALHEALTH = 20; //critical health of spacmen
		
	private Label atkDmgLabel;
	private Label atkRngLabel;
	private Label atkSpeedLabel;
	private Label armourLabel;
	private Label moveSpeedLabel;
		
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UnitStatsBox.class);

	/**
     * Creates a new instance of the unit stats area.
     * 
     * @param skin The UI skin to be applied to all elements of this area
     * @param textureManager textureManager of the game
     */
    public UnitStatsBox(Skin skin, TextureManager textureManager) {
    	LOGGER.debug("Initiate unit stats table");
        this.tm = textureManager;
        // character table
        Table charTable = new Table();
        this.unitImage = new Image(textureManager.getTexture("spacman_blue"));
        this.nameLabel = new Label("Name", skin);
        initiateProgressBar();
        
        charTable.add(unitImage).height(100).width(90);
		charTable.row();
		charTable.add(nameLabel).height(20);
		this.add(charTable);
		
		// create a table for bar and text display
		Table statsTable = new Table();
		
		//create table for health bar display
		Table barTable = new Table();
		//Create the health bar 
		healthLabel = new Label("Health", skin); 
		armourLabel = new Label("Armour", skin);
		
		barTable.add(healthBar).height(30).width(150);
		barTable.add(healthLabel).right().height(30).expandX();
		barTable.row();
		barTable.add(armourBar).height(30).width(150);
		barTable.add(armourLabel).right().height(30).expandX();
		
		statsTable.add(barTable);
		statsTable.row();
		
		Table textTable = new Table();
		this.atkDmgLabel = new Label("Attack", skin);
		this.atkRngLabel = new Label("Attack Range", skin);
		this.atkSpeedLabel = new Label("Attack Speed", skin);
		this.moveSpeedLabel = new Label("Move Speed", skin);
		textTable.add(this.atkDmgLabel).left().expandX().height(30);
		textTable.add(this.atkSpeedLabel).left().expandX().height(30);
		textTable.row();
		textTable.add(this.atkRngLabel).left();
		textTable.add(this.moveSpeedLabel).left();
		
		statsTable.add(textTable).width(220);
		
		this.add(statsTable);
		this.row();
		
		// add in the hero inventory display
		heroInventory = new Table();
		setUpHeroInventory();
		heroInventory.setVisible(false);
		this.add(heroInventory).colspan(2);
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
		heroInventory.add(weaponBtn).width(35).height(35).pad(3);
		
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
		heroInventory.add(armourBtn).width(35).height(35).pad(3);
		
		int size = specials.size();
		for(Special s : specials) {
			ImageButton specialBtn = generateItemButton(tm.getTexture(s.getTexture()));
			heroInventory.add(specialBtn).width(35).height(35).pad(3);
			// handler here
		}
		for(int i = 0; i < 4-size; i++) {
			ImageButton specialBtn = generateItemButton(tm.getTexture("locked_inventory"));
			heroInventory.add(specialBtn).width(35).height(35).pad(3);
		}
		
		//heroInventory.setVisible(false);
		
	}
	
	public void hideInventory() {
		this.heroInventory.setVisible(false);
	}
	

	public void showInventory() {
		this.heroInventory.setVisible(true);
	}

	private void initiateProgressBar(){
		ProgressBar.ProgressBarStyle armourBarStyle;
		healthBarStyle = new ProgressBar.ProgressBarStyle();
		armourBarStyle = new ProgressBar.ProgressBarStyle();
		
		pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.GRAY);
		pixmap.fill();
		healthBarStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		armourBarStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();
		
		pixmap = new Pixmap(0, 20, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fill();
		healthBarStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		armourBarStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();

		pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fill();
		healthBarStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();

		pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.BLUE);
		pixmap.fill();
		armourBarStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();
		
		healthBar = new ProgressBar(0,100, 1, false, healthBarStyle);
		healthBar.setValue(50);
		
		armourBar = new ProgressBar(0,100, 1, false, armourBarStyle);
		armourBar.setValue(50);
	}
	
    /**
     * Updates the health bar to the selected entity's health stats
     * @param target
     */
    public void updateSelectedStats (AttackableEntity target) {
    	// update health bar
    	if (target.getMaxHealth() == 0) {
    		LOGGER.debug("GOT zero MaxHealth, ERROR!");
    		return;
    	}
    	int healthPercent = (int)((float)target.getHealth() / (float)target.getMaxHealth() * 100);
		healthBar.setValue(healthPercent);
		// update health label
		healthLabel.setText(target.getHealth() + "/" + target.getMaxHealth());
		// update image
		TextureRegion entityRegion = new TextureRegion(tm.getTexture(target.getTexture()));
		TextureRegionDrawable redraw = new TextureRegionDrawable(entityRegion);
		unitImage.setDrawable(redraw);
		// update name label
		nameLabel.setText(target.toString());
		// update armour bar and label
		if (target.getMaxArmor() != 0) {
			armourBar.setValue(target.getArmor()/target.getMaxArmor()*100);
			armourLabel.setText(target.getArmor() +"/" + target.getMaxArmor());
		} else {
			armourBar.setValue(0);
			armourLabel.setText("0/0");
		}
		
		//Update the health progress bad to red once health is below 20 
		if (healthPercent <= CRITICALHEALTH) {
			pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
			pixmap.setColor(Color.RED);
			pixmap.fill();
			healthBarStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
			pixmap.dispose();
		} else {
			pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
			pixmap.setColor(Color.GREEN);
			pixmap.fill();
			healthBarStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
			pixmap.dispose();
		}
		
		this.atkDmgLabel.setText("Atk: "+ target.getDamageDeal());
		this.atkRngLabel.setText("Range: " + target.getAttackRange());
		this.atkSpeedLabel.setText("Atk Speed: "+ target.getAttackSpeed());
		this.moveSpeedLabel.setText("Move Speed: " + target.getSpeed());
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