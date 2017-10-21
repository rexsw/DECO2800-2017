package com.deco2800.marswars.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.marswars.entities.Inventory;
import com.deco2800.marswars.entities.items.Armour;
import com.deco2800.marswars.entities.items.Special;
import com.deco2800.marswars.entities.items.Weapon;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.managers.TextureManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This class defines the layout of unit stats display. It will consists of four tables
 * 1. unit image and name 
 * 2. unit health and armour bar
 * 3. unit attack, attack range, attack speed and move speed
 * 4. inventory display (if the unit is hero)
 * @author Mason
 *
 */
public class UnitStatsBox extends Table{
	private TextureManager tm;
	private Image unitImage;
	private Pixmap pixmap; 		   //used for progress bar 
	private boolean enabled = false; 
	
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
	
	private String lockedInventoryString = "locked_inventory";
		
	
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
        charTable.setDebug(enabled);
        this.unitImage = new Image(textureManager.getTexture("spacman_blue"));
        this.nameLabel = new Label("Name", skin);
        initiateProgressBar();
        
        charTable.add(unitImage).height(100).width(90);
		charTable.row();
		charTable.add(nameLabel).height(20);
		this.add(charTable);
		
		// create a table for bar and text display
		Table statsTable = new Table();
		statsTable.setDebug(enabled);
		
		//create table for health bar display
		Table barTable = new Table();
		//Create the health bar 
		healthLabel = new Label("Health", skin); 
		armourLabel = new Label("Armour", skin);
		
		Image armourStats = new Image(textureManager.getTexture("armour_stats"));
		Image healthStats = new Image(textureManager.getTexture("health_stats"));
		Image attackStats = new Image(textureManager.getTexture("attack_stats"));
		Image attackSpeedStats = new Image(textureManager.getTexture("attack_speed_stats"));
		Image rangeStats = new Image(textureManager.getTexture("range_stats"));
		Image moveSpeedStats = new Image(textureManager.getTexture("move_speed_stats"));
		
		Table rightTable = new Table(); //table to store stats + bars formatted to they can be next to each other.
		
		barTable.add(healthStats).height(30).width(30);
		barTable.add(healthBar).height(30).width(100);
		barTable.add(healthLabel).right().height(30).expandX();
		barTable.row();
		barTable.add(armourStats).height(30).width(30);
		barTable.add(armourBar).height(30).width(100);
		barTable.add(armourLabel).right().height(30).expandX();
		
		statsTable.add(barTable).padRight(10);
		
		
		// table for other stats
		Table textTable = new Table();
		textTable.setDebug(enabled);
		this.atkDmgLabel = new Label("Attack", skin);
		this.atkRngLabel = new Label("Attack Range", skin);
		this.atkSpeedLabel = new Label("Attack Speed", skin);
		this.moveSpeedLabel = new Label("Move Speed", skin);
		textTable.add(attackStats).height(30).width(30).padBottom(10);
		textTable.add(this.atkDmgLabel).expandX().height(30).padBottom(10);
		textTable.add(attackSpeedStats).height(30).width(30).padBottom(10);
		textTable.add(this.atkSpeedLabel).expandX().height(30).padBottom(10);
		textTable.row();
		textTable.add(rangeStats).height(30).width(30).padBottom(10);
		textTable.add(this.atkRngLabel).padBottom(10);
		textTable.add(moveSpeedStats).height(30).width(30).padBottom(10);
		textTable.add(this.moveSpeedLabel).padBottom(10);
		statsTable.add(textTable).width(150).padBottom(10);
		statsTable.row();
		
		
		// add in the hero inventory display
		heroInventory = new Table();
		heroInventory.setDebug(enabled);
		setUpHeroInventory();
		heroInventory.setVisible(false);
		rightTable.setDebug(enabled);
		rightTable.add(statsTable).pad(10);
		rightTable.row();
		rightTable.add(heroInventory);
		
		this.add(rightTable).pad(10);
		this.row();		
		this.setVisible(false);
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
		heroInventory.setDebug(enabled);
		pixmap.dispose();
	}
	
	/**
	 * This method will gets called when user select a hero character, this method then 
	 * display this hero's items on the HUD
	 * @param hero defined which hero's inventory shall we display
	 */
	public void updateHeroInventory(Commander hero) {	
		if(!hero.getStatsChange()) { // if there is no status change, do nothing
			return;
		}
		hero.setStatsChange(false);
		ImageTextButton weaponBtn;
		ImageTextButton armourBtn;
		heroInventory.clear();
		
		pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.DARK_GRAY);
		pixmap.fill();
		
		Inventory inventory = hero.getInventory();
		Weapon weapon = inventory.getWeapon();
		Armour armour = inventory.getArmour();
		List<Special> specials = inventory.getSpecials();
		if(weapon != null) {
			 weaponBtn= generateItemButton(tm.getTexture(weapon.getTexture()));
		} else {
			weaponBtn = generateItemButton(tm.getTexture(lockedInventoryString));
		}
		weaponBtn.setText("");
		heroInventory.add(weaponBtn).width(35).height(35).pad(3);
		
		if(armour != null) {
			armourBtn = generateItemButton(tm.getTexture(armour.getTexture()));
			//will add handler later
		} else {
			armourBtn = generateItemButton(tm.getTexture(lockedInventoryString));
		}
		armourBtn.setText("");
		heroInventory.add(armourBtn).width(35).height(35).pad(3);
		
		int size = specials.size();
		for(Special s : specials) {
			ImageTextButton specialBtn = generateItemButton(tm.getTexture(s.getTexture()));
			// handler button click here
			specialBtn.addListener( new ClickListener() {              
			    @Override
			    public void clicked(InputEvent event, float x, float y) {
			       hero.getInventory().useItem(s);
			    };
			});
			specialBtn.setText(String.valueOf(s.getUsage()));
			heroInventory.add(specialBtn).width(35).height(35).pad(3);
		}
		for(int i = 0; i < 4-size; i++) {
			ImageTextButton specialBtn = generateItemButton(tm.getTexture(lockedInventoryString));
			specialBtn.setText("");
			heroInventory.add(specialBtn).width(35).height(35).pad(3);
		}
		
	}
	
	/**
	 * This method hides the inventory display. 
	 * Should gets called when the selected unit is not hero
	 */
	public void hideInventory() {
		this.heroInventory.setVisible(false);
	}
	
	/**
	 * This method display the inventory
	 * Should gets called when the selected unit is hero
	 */
	public void showInventory() {
		this.heroInventory.setVisible(true);
	}

	/**
	 * This method initiate the health bar and armour bar
	 * they will have a grey background and green foreground for health and blue for armour
	 */
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
    	int healthPercent = (int)((float)target.getHealth() * 100 / (float)target.getMaxHealth());
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
			armourBar.setValue(target.getArmor()*100/target.getMaxArmor());
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
		
		this.atkDmgLabel.setText("" + target.getDamageDeal());
		this.atkRngLabel.setText("" + target.getAttackRange());
		this.atkSpeedLabel.setText(""+ target.getAttackSpeed());
		this.moveSpeedLabel.setText(String.format("%.2f", target.getSpeed()));
	}

    /**
	 * Private helper method to make image buttons for the items with the provided texture (the item icon image).
	 * @param image  Texture that is the desired item icon for the button
	 * @return ImageButton object that has the provided image for the item icon
	 */
	private ImageTextButton generateItemButton(Texture image) {
		TextureRegion imgRegion = new TextureRegion(image);
		TextureRegionDrawable imgDraw = new TextureRegionDrawable(imgRegion);
		BitmapFont font = new BitmapFont();
		font.setColor(Color.BLACK);
		ImageTextButtonStyle style = new ImageTextButtonStyle(imgDraw, imgDraw, imgDraw, font); 
		return new ImageTextButton("", style);
	}
}
