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
<<<<<<< HEAD
 * Note that checks for resources are conducted by the tech view before any functions
 * from this class are called.
 * 
=======
 * Note that checks for resources are conducted by the tech view before any
 * functions from this class are called.
>>>>>>> bc1e0d5a031604cebb0b1c8b3461754cc720af67
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
	private Label status; // Essentially a label that acts as a log specifically
							// for the shop
	private Commander selectedHero; // variable for the Commander that needs to
									// be selected to buy an item

	private final Table scrollTable;
	private Skin skin;

	private String boughtString = "Bought ";

	private TechnologyManager technologyManager;

	private boolean specialUnlocked = false;
	private int weaponLevel = 0; //indicator of level of weapons the shop currently has loaded
	private int armourLevel = 0; //indicator of level of armour the shop currently has loaded

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
		// Making the items table to buy from
		this.getContentTable().debugCell();
		this.getContentTable().left();
		this.skin = skin;

		this.technologyManager = (TechnologyManager) GameManager.get()
				.getManager(TechnologyManager.class);

		status = new Label("Welcome to the shop!", skin);

		scrollTable = new Table();
		scrollTable.top();
		scrollTable.debugCell();
		scrollTable.add(new Label("Item", skin)).width(iconSize).top().center();
		scrollTable.add(new Label("Description", skin)).width(iconSize)
				.expandX().top().left();
		scrollTable.add(new Label("Cost", skin)).width(iconSize).top();
		scrollTable.row();

		this.technologyManager = (TechnologyManager) GameManager.get()
				.getManager(TechnologyManager.class);
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

		this.getContentTable().add(scroller).fill().expand().top();

		this.getContentTable().row();
		this.getContentTable().add(status).expandX().center().colspan(2);

	}

	/**
	 * Gets boolean indicating if specials are already unlocked
	 * 
	 * @return true if already unlocked, false otherwise.
	 */
	public boolean getSpecialUnlocked() {
		return this.specialUnlocked;
	}

	/**
	 * Public method to unlock the weapons. Does not replace lower level items,
	 * but adds to them.
	 *
	 */
	public void unlockWeapons(int level) {
		if (level <= weaponLevel) {
			return;
		}
		List<ItemType> items = new ArrayList<>();
		// Adding all the defined weapons in WeaponType enumerate class
		for (WeaponType wep : WeaponType.values()) {
			items.add(wep);
		}
		updateShop(items, level);
		weaponLevel = level;
	}

	/**
	 * Public method to unlock the armours. Does not replace lower level items,
	 * but adds to them.
	 *
	 */
	public void unlockArmours(int level) {
		if (level <= armourLevel) {
			return;
		}
		List<ItemType> items = new ArrayList<>();
		// Adding all the defined weapons in WeaponType enumerate class
		for (ArmourType armour : ArmourType.values()) {
			items.add(armour);
		}
		updateShop(items, level);
		armourLevel = level;
	}

	/**
	 * Public method to unlock the specials.
	 *
	 */
	public void unlockSpecials() {
		if (specialUnlocked) {
			return;
		}
		List<ItemType> items = new ArrayList<>();
		// Adding all the defined special items in SpecialType enumerate class
		for (SpecialType spec : SpecialType.values()) {
			items.add(spec);
		}
		this.specialUnlocked = true;
		updateShop(items, 0);
	}

	/**
<<<<<<< HEAD

	 * Private method to update the items in the shop.
	 * @param items The items to be added
	 * @param level The level of the item
=======
	 * Private method to update the items in the shop, this function will also
	 * add handler to these items for user shopping
	 * 
	 * @param items
	 *            The items to be added
>>>>>>> bc1e0d5a031604cebb0b1c8b3461754cc720af67
	 */
	private void updateShop(List<ItemType> items, int level) {
		for (ItemType item : items) {
			String text = item.getTextureString();
			text = item instanceof SpecialType ? text
					: text.substring(0, text.length() - 1)
							+ Integer.toString(level);
			Texture texture = textureManager.getTexture(text);
			ImageButton button = generateItemButton(texture);

			button.addListener(new ClickListener() {
				public void clicked(InputEvent event, float x, float y) {
					status.setText(item.getName());
					if (selectedHero == null) {
						status.setText("Unsuccessful shopping, No hero exist.");
						return;
					}
					if (selectedHero.getHealth() <= 0) {
						status.setText(
								"Your Commander is dead. Can't buy anything.");
						return;
					}
					boolean enoughResources = checkCost(selectedHero.getOwner(),
							item);
					if (enoughResources) {
						if (item instanceof WeaponType) {
							Weapon weapon = new Weapon((WeaponType) item,
									level);
							selectedHero.addItemToInventory(weapon);
							status.setText(boughtString + weapon.getName()
									+ "(Weapon) for "
									+ selectedHero.toString());
						} else if (item instanceof ArmourType) {
							Armour armour = new Armour((ArmourType) item,
									level);
							selectedHero.addItemToInventory(armour);
							status.setText(boughtString + armour.getName()
									+ "(Armour) for "
									+ selectedHero.toString());
						} else {
							boolean transactSuccess;
							Special special = new Special((SpecialType) item);
							transactSuccess = selectedHero
									.addItemToInventory(special);
							if (transactSuccess) {
								status.setText(boughtString + special.getName()
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
						String mes = "Not enough resources.";
						status.setText(mes);
					}
				}

				private boolean checkCost(int owner, ItemType item) {
					// TODO Auto-generated method stub
					return false;
				}
			});
			scrollTable.add(button).width(iconSize).height(iconSize).top();
			String stats = getItemStats(item, level);
			String cost = getItemCost(item, level);
			scrollTable.add(new Label(stats, skin)).width(iconSize).top()
					.left();
			scrollTable.add(new Label(cost, skin)).width(iconSize).top().left();
			scrollTable.row();
		}
	}

	/**
	 * Private helper method to get the cost of an item of a particular level.
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
			float multiplier = ((WeaponType) item)
					.getItemLevelMultipliers()[level - 1];
			for (int i = 0; i < 3; i++) {
				costs[i] = baseCosts[i] * multiplier;
			}
		} else if (item instanceof ArmourType) {
			float multiplier = ((ArmourType) item)
					.getItemLevelMultipliers()[level - 1];
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

	/**
	 * Private helper function to check cost of an item
	 *
	 */
	private boolean checkCost(int owner, ItemType item) {
		int[] cost = item.getCost();
		ResourceManager rm = (ResourceManager) GameManager.get()
				.getManager(ResourceManager.class);
		if (rm.getRocks(owner) < cost[0]) {
			return false;
		} else if (rm.getCrystal(owner) < cost[1]) {
			return false;
		} else if (rm.getBiomass(owner) < cost[2]) {
			return false;
		}
		return true;
	}

	/**
	 * Private helper method to get the stats of an item of a particular level
	 *
	 * @param item
	 * @param level
	 * @return stats of item as a string
	 */
	private String getItemStats(ItemType item, int level) {
		String stats = "";
		if (item instanceof WeaponType) {
			float multipler = ((WeaponType) item)
					.getItemLevelMultipliers()[level - 1];
			stats = "Name: " + item.getName() + "\nType: Weapon\nDamage: "
					+ (int) (((WeaponType) item).getWeaponDamage() * multipler)
					+ "\nSpeed: "
					+ (int) (((WeaponType) item).getWeaponSpeed() * multipler)
					+ "\nRange: "
					+ (int) (((WeaponType) item).getWeaponRange() * multipler);
		} else if (item instanceof ArmourType) {
			float multiplier = ((ArmourType) item)
					.getItemLevelMultipliers()[level - 1];
			stats = "Name: " + this.getName() + "\nType: Armour\nArmour: "
					+ (int) (((ArmourType) item).getArmourValue() * multiplier)
					+ "\nMaxHealth: "
					+ (int) (((ArmourType) item).getArmourHealth() * multiplier)
					+ "\nMove Speed: "
					+ (int) (((ArmourType) item).getMoveSpeed() * multiplier);
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
	
	/**
	 * Get the level of weapons that are currently loaded in the shop
	 * @return int that is the level of weapons that are currently loaded
	 */
	public int getWeapLvl() {
		return weaponLevel;
	}
	
	/**
	 * Get the level of armours that are currently loaded in the shop
	 * @return int that is the level of armours that are currently loaded
	 */
	public int getArmLvl() {
		return armourLevel;
	}


}
