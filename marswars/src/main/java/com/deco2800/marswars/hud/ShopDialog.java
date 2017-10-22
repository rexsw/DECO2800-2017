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
import com.deco2800.marswars.managers.TechnologyManager;
import com.deco2800.marswars.managers.TextureManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that defines the layout of the shop window. Overall, the design should
 * be a window with 2 scrollable tables, left table with rows of clickable item
 * icons with their descriptions to their right of each icon. The right table
 * would have all the player's Commanders' icons. A Commander needs to be
 * selected (by clicking on their icon) before an item can be bought (by click
 * on the item icon). To escape the window, simple click outside of the window.
 * Note that checks for resources are conducted by the tech view before any functions
 * from this class are called.
 * 
 *
 * 
 * @author Mason
 *
 */
public class ShopDialog extends Dialog {
	private float windowSize = Gdx.graphics.getWidth() / 4f;
	private TextureManager textureManager; // a manager for the textures of the
											// item icons and the Commander
											// icons
	private int iconSize = (int) (windowSize / 3); // getting the standard icon
													// size for the window
	// private List<ItemType> itemList; // list of items to be put into the shop
	// private Table heroTable; //table of the player's Commander units.
	private Label status; // Essentially a label that acts as a log specifically
							// for the shop
	private Commander selectedHero; // variable for the Commander that needs to
									// be selected to buy an item

	private final Table scrollTable;
	private Skin skin;

	private TechnologyManager technologyManager;
	
	private int weapLevel = 0;
	private int armourLevel = 0;
	private boolean specialUnlocked = false;

	/**
	 * Constructor of the shop dialog window class.
	 * 
	 * @param title
	 *            The title of the window
	 * @param skin
	 *            the look of the HUD, depending on the world/level the game is
	 *            being played at
	 * @param manager
	 *            The texture manager with all the defined item and commander
	 *            icons
	 */
	public ShopDialog(String title, Skin skin, TextureManager manager) {
		super(title, skin);
		this.getTitleLabel().setAlignment(Align.center);
		this.textureManager = manager;
		// this.itemList = new ArrayList<>();
		// Making the items table to buy from
		this.getContentTable().debugCell();
		this.getContentTable().left();
		this.skin = skin;
		this.technologyManager = (TechnologyManager) GameManager.get().getManager(TechnologyManager.class);

		

		status = new Label("Welcome to the shop!", skin);

		scrollTable = new Table();
		scrollTable.top();
		scrollTable.debugCell();
		scrollTable.add(new Label("Item", skin)).width(iconSize).top().center();
		scrollTable.add(new Label("Description", skin)).width(iconSize)
				.expandX().top().left();
		scrollTable.add(new Label("Cost", skin)).width(iconSize).top();
		scrollTable.row();
		if (technologyManager.armourIsUnlocked(1)) {
			unlockArmours(1);
		}
		if (technologyManager.armourIsUnlocked(2)) {
			unlockArmours(2);
		}
		if (technologyManager.armourIsUnlocked(3)) {
			unlockArmours(3);
		}
		if (technologyManager.weaponIsUnlocked(1)) {
			unlockWeapons(1);
		}
		if (technologyManager.weaponIsUnlocked(2)) {
			unlockWeapons(2);
		}
		if (technologyManager.weaponIsUnlocked(3)) {
			unlockWeapons(3);
		}
		if (technologyManager.specialIsUnlocked()) {
			unlockSpecials();
		}

		// making the right side table for the Commander icons.
		final ScrollPane scroller = new ScrollPane(scrollTable);

		// heroTable = new Table();
		// heroTable.top();
		// heroTable.add(new Label("Commander", skin)).width(iconSize);
		// heroTable.row();

		this.getContentTable().add(scroller).fill().expand().top();
		// this.getContentTable().add(heroTable).width(iconSize).top();

		this.getContentTable().row();
		this.getContentTable().add(status).expandX().center().colspan(2);
	}
	
	/**
	 * Gets the weapon level unlocked in the store
	 * @return current weapon level in the shop
	 */
	public int getWeapLvl() {
		return this.weapLevel;
	}
	
	/**
	 * Gets the armour level unlocked in the store
	 * @return current armour level in the shop
	 */
	public int getArmourLvl() {
		return this.armourLevel;
	}
	
	/**
	 * Gets boolean indicating if specials are already unlocked
	 * @return true if already unlocked, false otherwise.
	 */
	public boolean getSpecialUnlocked() {
		return this.specialUnlocked;
	}

	/**
	 * Public method to unlock the weapons. Replaces lower levelled items, if they exist, with current level items.
	 *
	 */
	public void unlockWeapons(int level) {
		List<ItemType> items = new ArrayList<>();
		// Adding all the defined weapons in WeaponType enumerate class
		for (WeaponType wep : WeaponType.values()){
			items.add(wep);
		}
		this.weapLevel = level;
		updateShop(items, level);
	}

	/**
	 * Public method to unlock the armours.  Replaces lower levelled items, if they exist, with current level items.
	 *
	 */
	public void unlockArmours(int level) {
		List<ItemType> items = new ArrayList<>();
		// Adding all the defined weapons in WeaponType enumerate class
		for (ArmourType armour: ArmourType.values()){
			items.add(armour);
		}
		this.armourLevel = level;
		updateShop(items, level);
	}

	/**
	 * Public method to unlock the specials.
	 *
	 */
	public void unlockSpecials() {
		List<ItemType> items = new ArrayList<>();
		// Adding all the defined special items in SpecialType enumerate class
		for (SpecialType spec: SpecialType.values()){
			items.add(spec);
		}
		this.specialUnlocked = true;
		updateShop(items, 0);
	}
	/**
	 * Private method to update the items in the shop.
	 * @param items The items to be added
	 * @param level The level of the item
	 */
	private void updateShop(List<ItemType> items, int level) {
		for (ItemType item : items) {
			String text = item.getTextureString();
			text = item instanceof SpecialType ? text : text.substring(0, text.length() - 1) + Integer.toString(level);
			Texture texture = textureManager
					.getTexture(text);
			ImageButton button = generateItemButton(texture);

			button.addListener(new ClickListener() {
				public void clicked(InputEvent event, float x, float y) {
					status.setText(item.getName());
					// boolean enoughResources =
					// checkCost(selectedHero.getOwner(), item);
					// if ((selectedHero != null) && (selectedHero.getHealth() >
					// 0) && enoughResources) {
					if ((selectedHero != null)
							&& (selectedHero.getHealth() > 0)) {
						if (item instanceof WeaponType) {
							Weapon weapon = new Weapon((WeaponType) item, level);
							selectedHero.addItemToInventory(weapon);
							status.setText("Bought " + weapon.getName()
									+ "(Weapon) for "
									+ selectedHero.toString());
						} else if (item instanceof ArmourType) {
							Armour armour = new Armour((ArmourType) item, level);
							selectedHero.addItemToInventory(armour);
							status.setText("Bought " + armour.getName()
									+ "(Armour) for "
									+ selectedHero.toString());
						} else {
							boolean transactSuccess = false;
							Special special = new Special((SpecialType) item);
							transactSuccess = selectedHero
									.addItemToInventory(special);
							if (transactSuccess) {
								status.setText("Bought " + special.getName()
										+ "(Special) for "
										+ selectedHero.toString());
							} else {
								status.setText(
										"Unsuccessful Shopping, can only hold 4 specials");
								return;
							}

						}
						selectedHero.setStatsChange(true);
						transact(selectedHero.getOwner(), item);
					} else {
						String mes = selectedHero == null
								? "Unsuccessful shopping. You need a hero to buy items."
								: (selectedHero.getHealth() > 0
								? "Not enough resources."
								: "Your Commander is dead. You can't buy anything.");
						status.setText(mes);
					}
				}
			});
			scrollTable.add(button).width(iconSize).height(iconSize).top();
			String stats = getItemStats(item, level);
			String cost = getItemCost(item, level);
			scrollTable.add(new Label(stats, skin))
					.width(iconSize).top().left();
			scrollTable.add(new Label(cost, skin))
					.width(iconSize).top().left();
			scrollTable.row();
		}
	}

	/** Private helper method to get the cost of an item of a particular level.
	 *
	 * @param item
	 * @param level
	 * @return cost of item in Rocks, Crystal and Biomass, as a string
	 */
	private String getItemCost(ItemType item, int level) {
		// gets item cost
		int[] baseCosts = item.getCost();
		float[] costs = new float[3];
		if (item instanceof WeaponType) {
			float multiplier = ((WeaponType) item).getItemLevelMultipliers()[level - 1];
			for (int i = 0; i < 3; i++) {
				costs[i] = baseCosts[i] * multiplier;
			}
		} else if (item instanceof ArmourType) {
			float multiplier = ((ArmourType) item).getItemLevelMultipliers()[level - 1];
			for (int i = 0; i < 3; i++) {
				costs[i] = baseCosts[i] * multiplier;
			}
		} else if (item instanceof SpecialType) {
			for (int i = 0; i < 3; i++) {
				costs[i] = baseCosts[i] * 1.0f;
			}
		}
		// creates cost string
		String costString = "";
		if (baseCosts[0] > 0) {
			costString += "Rock: " + (int) costs[0] + "\n";
		}
		if (baseCosts[1] > 0) {
			costString += "Crystal: " + (int) costs[1] + "\n";
		}
		if (baseCosts[2] > 0) {
			costString += "Biomass: " + (int) costs[2] + "\n";
		}
		return costString;
	}

	/** Private helper method to get the stats of an item of a particular level
	 *
	 * @param item
	 * @param level
	 * @return stats of item as a string
	 */
	private String getItemStats(ItemType item, int level) {
		String stats = "";
		if (item instanceof WeaponType) {
			float multipler = ((WeaponType) item).getItemLevelMultipliers()[level - 1];
			stats = "Name: " + item.getName() + "\nType: Weapon\nDamage: " +
					(int)(((WeaponType) item).getWeaponDamage() * multipler) + "\nSpeed: " + (int)(((WeaponType) item).getWeaponSpeed() * multipler)
					+ "\nRange: " + (int)(((WeaponType) item).getWeaponRange() * multipler);
		} else if (item instanceof ArmourType) {
			float multiplier = ((ArmourType) item).getItemLevelMultipliers()[level - 1];
			stats = "Name: " + this.getName() + "\nType: Armour\nArmour: " +
					(int) (((ArmourType) item).getArmourValue() * multiplier) + "\nMaxHealth: " + (int) (((ArmourType) item).getArmourHealth() * multiplier)
					+ "\nMove Speed: " + (int)(((ArmourType) item).getMoveSpeed() * multiplier);
		} else if (item instanceof SpecialType) {
			stats = item.getDescription();
		}
		return stats;
		}

	/**
	 * Private method to handle the resource transaction to buy an item.
	 * 
	 * @param owner
	 *            The team id of the owner of the Commander to buy the item for.
	 * @param item
	 *            The item type enumerate value to get the cost from.
	 */
	private void transact(int owner, ItemType item) {
		int[] cost = item.getCost();
		ResourceManager rm = (ResourceManager) GameManager.get()
				.getManager(ResourceManager.class);
		rm.setRocks(rm.getRocks(owner) - cost[0], owner);
		rm.setCrystal(rm.getCrystal(owner) - cost[1], owner);
		rm.setBiomass(rm.getBiomass(owner) - cost[2], owner);
	}

	/**
	 * Private helper method to make image buttons for the items with the
	 * provided texture (the item icon image).
	 * 
	 * @param image
	 *            Texture that is the desired item icon for the button.
	 * @return ImageButton object that has the provided image for the item icon
	 */
	private ImageButton generateItemButton(Texture image) {
		TextureRegion imgRegion = new TextureRegion(image);
		TextureRegionDrawable imgDraw = new TextureRegionDrawable(imgRegion);
		return new ImageButton(imgDraw);
	}

	// /**
	// * Private helper method to make image buttons for the commanders with the
	// provided texture (the Commander icon
	// * image). Thi button will have 2 icon looks, 1 for when it is selected
	// and another for when it is not selected.
	// * @param image Texture that is the desired Commander icon for the button
	// for when the commander is selected.
	// * @param offImage Texture that is the desired Commander icon for the
	// button for when the commander is not
	// * selected.
	// * @return ImageButton object that has the provided images for the
	// selected and deselected icons.
	// */
	// @Deprecated
	// private ImageButton generateHeroButton(Texture image, Texture offImage) {
	// TextureRegion imgRegion = new TextureRegion(image);
	// TextureRegionDrawable imgDraw = new TextureRegionDrawable(imgRegion);
	// TextureRegion offImgRegion = new TextureRegion(offImage);
	// TextureRegionDrawable offImgDraw = new
	// TextureRegionDrawable(offImgRegion);
	//
	// ImageButton button = new ImageButton(offImgDraw);
	// button.getStyle().imageChecked = imgDraw;
	//
	// return button;
	// }

	// /**
	// * Method to add Commander Hero icons to the right side table of the shop
	// dialog window.
	// * @param hero Commander unit to get/make an icon for.
	// */
	// @Deprecated
	// public void addHeroIcon(Commander hero) {
	// //making the button object
	// Texture heroImage = textureManager.getTexture("hero_button");
	// Texture heroOffImage = textureManager.getTexture("hero_button_off");
	// ImageButton heroButton = generateHeroButton(heroImage, heroOffImage);
	//// heroTable.add(heroButton).width(iconSize).height(iconSize).top();
	// //adding the listener to the button created
	// heroButton.addListener(new ClickListener(){
	// public void clicked(InputEvent event, float x, float y){
	// if(heroButton.isChecked()) {
	// heroButton.setChecked(true);
	// status.setText("Selected " + hero.toString());
	// selectedHero = hero;
	// } else {
	// heroButton.setChecked(false);
	// status.setText("Unselected "+ hero.toString());
	// selectedHero = null;
	// }
	// }
	// });
	//// heroTable.row();
	// }

	public void connectHero(Commander hero) {
		this.selectedHero = hero;
	}

	/**
	 * Gets the preferred window width for the shop dialog window.
	 * 
	 * @return float that is the preferred window width for the shop dialog
	 *         window.
	 */
	@Override
	public float getPrefWidth() {
		return windowSize * 1.2f;
	}

	/**
	 * Gets the preferred window height for the shop dialog window.
	 * 
	 * @return float that is the preferred window height for the shop dialog
	 *         window.
	 */
	@Override
	public float getPrefHeight() {
		return windowSize * 0.6f;
	}

}
