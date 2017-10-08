package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.entities.items.*;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TextureManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that defines the layout of the shop window. Overall, the design should be a window with 2 scrollable tables, 
 * left table with rows of clickable item icons with their descriptions to their right of each icon. The right table
 * would have all the player's Commanders' icons. A Commander needs to be selected (by clicking on their icon) before an
 * item can be bought (by click on the item icon). To escape the window, simple click outside of the window. 
 * 
 * NOTE: CURRENTLY THE SHOP DOES NOT CONSIDER TECH TREE UNLOCK CONSIDERATIONS NOR RESOURCE REQUIREMENTS.
 * NOTE2: DESIGN OF THE SHOP IS TO BE RE-DONE BASED ON USER FEEDBACK.
 * @author Mason
 *
 */
public class ShopDialog extends Dialog{
	
    private TextureManager textureManager; // a manager for the textures of the item icons and the Commander icons
    private int iconSize = Gdx.graphics.getWidth() / 12; //getting the standard icon size for the window
    private List<ItemType> itemList; //list of items to be put into the shop
    private Table heroTable; //table of the player's Commander units.
    private Label status; //Essentially a label that acts as a log specifically for the shop
    private Commander selectedHero; //variable for the Commander that needs to be selected to buy an item
    
    /**
     * Constructor of the shop dialog window class.
     * @param title  The title of the window
     * @param skin  the look of the HUD, depending on the world/level the game is being played at
     * @param manager  The texture manager with all the defined item and commander icons
     */
	public ShopDialog(String title, Skin skin, TextureManager manager) {
		super(title, skin);
		this.getTitleLabel().setAlignment(Align.center);
		this.textureManager = manager;	
		this.itemList = new ArrayList<>();
		//Making the items table to buy from
		this.getContentTable().debugCell();
		this.getContentTable().left();
		
		status = new Label("Welcome to the shop!", skin);
		/*
		 * Note: need to change the below for loops to dynamically determine if they have been researched or not.
		 */
		//Adding all the defined weapons in WeaponType enumerate class
		for (WeaponType wep : WeaponType.values()) {
			itemList.add(wep);
		}

		//Adding all the defined armour in ArmourType enumerate class
		for (ArmourType arm : ArmourType.values()) {
			itemList.add(arm);
		}
		//Adding all the defined special items in SpecialType enumerate class
		for (SpecialType spec : SpecialType.values()) {
			itemList.add(spec);
		}
		
		final Table scrollTable = new Table();
		scrollTable.top();
		scrollTable.debugCell();
		scrollTable.add(new Label("Item", skin)).width(iconSize).top().center();
		scrollTable.add(new Label("Description", skin)).width(iconSize).expandX().top().left();
		scrollTable.add(new Label("Cost", skin)).width(iconSize).top();
		scrollTable.row();
		//generating all the item icons, buttons descriptions etc.
		for (ItemType item:itemList) {
			Texture texture = textureManager.getTexture(item.getTextureString());
			ImageButton button = generateItemButton(texture);
			
			button.addListener(new ClickListener() {  
	            public void clicked(InputEvent event, float x, float y){
	                status.setText(item.getName());
//	                boolean enoughResources = checkCost(selectedHero.getOwner(), item);
	                boolean enoughResources = true;
	                if ((selectedHero != null) && (selectedHero.getHealth() > 0) && enoughResources) {
	                	if (item instanceof WeaponType) {
	                		Weapon weapon = new Weapon((WeaponType) item);
		                	selectedHero.addItemToInventory(weapon);
		                	status.setText("Bought " + weapon.getName() + "(Weapon) for " + selectedHero.toString());
	                	} else if (item instanceof ArmourType) {
	                		Armour armour = new Armour((ArmourType) item);
	                		selectedHero.addItemToInventory(armour);
	                		status.setText("Bought " + armour.getName() + "(Armour) for " + selectedHero.toString());
	                	} else {
	                		Special special = new Special((SpecialType) item);
	                		selectedHero.addItemToInventory(special);
	                		status.setText("Bought " + special.getName() + "(Special) for " + selectedHero.toString());
	                	}
	                	selectedHero.setStatsChange(true);
	                	transact(selectedHero.getOwner(), item);
	                } else {
	                	String mes = selectedHero == null ? "unsuccessful shopping, please select a hero." : 
	                		(selectedHero.getHealth() > 0 ? "Not enough resources." : 
	    	                		"Your Commander is dead. Can't buy anything.");
	                	status.setText(mes);
	                }
	                }
	        });
			
			scrollTable.add(button).width(iconSize).height(iconSize).top();
	        scrollTable.add(new Label(item.getDescription(), skin)).width(iconSize).top().left();
	        scrollTable.add(new Label(item.getCostString(), skin)).width(iconSize).top().left();
	        scrollTable.row();
		}
		
		//making the right side table for the Commander icons.
        final ScrollPane scroller = new ScrollPane(scrollTable);

        heroTable = new Table();
        heroTable.top();
        heroTable.add(new Label("Commander", skin)).width(iconSize);
        heroTable.row();
        
        this.getContentTable().add(scroller).fill().expand().top();
        this.getContentTable().add(heroTable).width(iconSize).top();
        
        this.getContentTable().row();
        this.getContentTable().add(status).expandX().center().colspan(2);
	}
	
	/**
	 * Private method to check if the owner of the Commander has enough resources to buy the item.
	 * @param owner  The team id of the owner of the Commander to buy the item for.
	 * @param item  The item type enumerate value to get the cost from.
	 * @return true if transaction was successful. false if not enough resources.
	 */
	private boolean checkCost(int owner, ItemType item) {
		int[] cost = item.getCost();
		ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		if (rm.getRocks(owner) < cost[0]) {
			return false;
		}
		if (rm.getCrystal(owner) < cost[1]) {
			return false;
		}
		if (rm.getWater(owner) < cost[2]) {
			return false;
		}
		if (rm.getBiomass(owner) < cost[3]) {
			return false;
		}
		return true;
	}
	
	/**
	 * Private method to handle the resource transaction to buy an item.
	 * @param owner  The team id of the owner of the Commander to buy the item for.
	 * @param item  The item type enumerate value to get the cost from.
	 */
	private void transact(int owner, ItemType item) {
		int[] cost = item.getCost();
		ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		rm.setRocks(rm.getRocks(owner) - cost[0], owner);
		rm.setCrystal(rm.getCrystal(owner) - cost[1], owner);
		rm.setWater(rm.getWater(owner) - cost[2], owner);
		rm.setBiomass(rm.getBiomass(owner) - cost[3], owner);
	}
	
	/**
	 * Private helper method to make image buttons for the items with the provided texture (the item icon image).
	 * @param image  Texture that is the desired item icon for the button.
	 * @return ImageButton object that has the provided image for the item icon
	 */
	private ImageButton generateItemButton(Texture image) {
		TextureRegion imgRegion = new TextureRegion(image);
		TextureRegionDrawable imgDraw = new TextureRegionDrawable(imgRegion);
		return new ImageButton(imgDraw);
	}
	
	/**
	 * Private helper method to make image buttons for the commanders with the provided texture (the Commander icon 
	 * image). Thi button will have 2 icon looks, 1 for when it is selected and another for when it is not selected.
	 * @param image  Texture that is the desired Commander icon for the button for when the commander is selected.
	 * @param offImage  Texture that is the desired Commander icon for the button for when the commander is not
	 * selected.
	 * @return ImageButton object that has the provided images for the selected and deselected icons.
	 */
	private ImageButton generateHeroButton(Texture image, Texture offImage) {
		TextureRegion imgRegion = new TextureRegion(image);
		TextureRegionDrawable imgDraw = new TextureRegionDrawable(imgRegion);
		TextureRegion offImgRegion = new TextureRegion(offImage);
		TextureRegionDrawable offImgDraw = new TextureRegionDrawable(offImgRegion);

		ImageButton button = new ImageButton(offImgDraw);
		button.getStyle().imageChecked = imgDraw;
        
		return button;
	}
	
	/**
	 * Method to add Commander Hero icons to the right side table of the shop dialog window.
	 * @param hero Commander unit to get/make an icon for.
	 */
	public void addHeroIcon(Commander hero) {
		//making the button object
		Texture heroImage = textureManager.getTexture("hero_button");
		Texture heroOffImage = textureManager.getTexture("hero_button_off");
        ImageButton heroButton = generateHeroButton(heroImage, heroOffImage);
        heroTable.add(heroButton).width(iconSize).height(iconSize).top();
        //adding the listener to the button created
        heroButton.addListener(new ClickListener(){
        	public void clicked(InputEvent event, float x, float y){
                if(heroButton.isChecked()) {
                	heroButton.setChecked(true);
                	status.setText("Selected " + hero.toString());
                	selectedHero = hero;
                } else {
                	heroButton.setChecked(false);
                	status.setText("Unselected "+ hero.toString());
                	selectedHero = null;
                }
                }
        	});
        heroTable.row();
	}
	
	/**
	 * Gets the preferred window width for the shop dialog window.
	 * @return float that is the preferred window width for the shop dialog window.
	 */
	@Override
    public float getPrefWidth() {
        return Gdx.graphics.getWidth() /2.5f;
    }

	/**
	 * Gets the preferred window height for the shop dialog window.
	 * @return float that is the preferred window height for the shop dialog window.
	 */
    @Override
    public float getPrefHeight() {
        return Gdx.graphics.getHeight() /1.8f;
    }
	
    
	@Override
	protected void result(Object object){
		
	}
}
