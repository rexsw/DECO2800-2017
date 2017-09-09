package com.deco2800.marswars.entities.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.GenerateAction;
import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.Clickable;
import com.deco2800.marswars.entities.HasHealth;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.entities.HasProgress;
import com.deco2800.marswars.entities.Spacman;
import com.deco2800.marswars.entities.Tickable;
import com.deco2800.marswars.entities.Selectable.EntityType;
import com.deco2800.marswars.managers.AiManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.Manager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.PlayerManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.SoundManager;
import com.deco2800.marswars.worlds.AbstractWorld;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by timhadwen on 19/7/17.
 *
 * A home base for the empire
 */
public class Base extends BuildingEntity implements Clickable, Tickable, HasProgress, HasOwner, HasHealth {

	/* A single action for this building */
	Optional<DecoAction> currentAction = Optional.empty();
	private int owner;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Base.class);
	
	private int health = 500;
	
	boolean selected = false;

	/**
	 * Constructor for the base.
	 * @param world The world that will hold the base.
	 * @param posX its x position on the world.
	 * @param posY its y position on the world.
	 * @param posZ its z position on the world.
	 */

	public Base(AbstractWorld world, float posX, float posY, float posZ) {
	super(posX, posY, posZ, BuildingType.BASE);
		this.setTexture("homeBase");
		this.setEntityType(EntityType.BUILDING);
		this.setCost(250);
		this.setSpeed(2);
	}
	
	/**
	 * Give action to the base
	 * @param action
	 */
	public void giveAction(DecoAction action) {
		if (!currentAction.isPresent()) {
			currentAction = Optional.of(action);
		}
	}

	/**
	 * On click handler
	 */
	@Override
	public void onClick(MouseHandler handler) {
		if(this.getOwner() < 0) {
			if (!selected) {
				selected = true;
				LOGGER.error("clicked on base");
			}
		} else {
			LOGGER.error("clicked on ai base");

		}
	}

	/**
	 * Perform some action when right clicked at x, y
	 * @param x
	 * @param y
	 */
	@Override
	public void onRightClick(float x, float y) {
		//base has no action on right click for now
		SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
		sound.playSound("endturn.wav");
	}

	/**
	 * On Tick handler
	 * @param i time since last tick
	 */
	@Override
	public void onTick(int i) {

		if (currentAction.isPresent()) {
			currentAction.get().doAction();

			if (currentAction.get().completed()) {
				currentAction = Optional.empty();
			}
		}
	}

	/**
	 * Check is the base has been selected
	 * @return true if is selected, false otherwise
	 */
	@Override
	public boolean isSelected() {
		return selected;
	}
	
	/**
	 * Deselect the base
	 */
	@Override
	public void deselect() {
		selected = false;
	}

	/**
	 * Get the 'Make Spacman' button object
	 * @return Button
	 */
	public Button getButton() {
		Button button = new TextButton("Make Spacman", new Skin(Gdx.files.internal("uiskin.json")));
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				buttonWasPressed();
			}
		});
		return button;
	}

	/**
	 * Handler for button pressed action
	 * Reduce the resource the generate spacman
	 */
	public void buttonWasPressed() {
		ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		if (resourceManager.getRocks(this.owner) > 30) {
			resourceManager.setRocks(resourceManager.getRocks(this.owner) - 30, this.owner);
			currentAction = Optional.of(new GenerateAction(new Spacman(this.getPosX() - 1, this.getPosY() - 1, 0)));
		}
		this.deselect();
	}
	
	/**
	 * Get the help text label of this base
	 * @return Label
	 */
	public Label getHelpText() {
		return new Label("You have clicked on the base. Click 'Make Spacman' to 'Make Spacman'!", new Skin(Gdx.files.internal("uiskin.json")));
	}

	/**
	 * Get the progress of current action
	 * @return int
	 */
	@Override
	public int getProgress() {
		if (currentAction.isPresent()) {
			return currentAction.get().actionProgress();
		}
		return 0;
	}

	/**
	 * Check if there is an action currently assigned to the base
	 * @return boolean
	 */
	@Override
	public boolean showProgress() {
		return currentAction.isPresent();
	}

	/**
	 * Set the owner of this base
	 * @param owner
	 */
	@Override
	public void setOwner(int owner) {
		this.owner = owner;
	}

	/**
	 * Get the owner of this base
	 * @return owner
	 */
	@Override
	public int getOwner() {
		return this.owner;
	}

	/**
	 * Check if the AbstractEntity passed in and this entity has the same owner
	 * @return boolean
	 */
	@Override
	public boolean sameOwner(AbstractEntity entity) {
		return entity instanceof  HasOwner &&
				this.owner == ((HasOwner) entity).getOwner();
	}
	
	@Override
	/**
	 * This method is a duplication of the showProgress method, consider delete one of them
	 * sadly two different interfaces
	 * @return boolean
	 */
	public boolean isWorking() {
		return currentAction.isPresent();
	}
	
	@Override
	/**
	 * Set the action of this base
	 * @param action
	 */
	public void setAction(DecoAction action) {
		currentAction = Optional.of(action);
	}

	/**
	 * Returns the current action (used in WeatherManager)
	 * @return
	 */
	public Optional<DecoAction> getAction() {
		return currentAction;
	}

	@Override
	/**
	 * @return current health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 *  Sets the health for this spacman 
	 * 	@param health 
	 */
	@Override
	public void setHealth(int health) {
		LOGGER.info("Set health to " + health);
		this.health = health;
		if (health < 0) {
			GameManager.get().getWorld().removeEntity(this);
			LOGGER.info("I am kill");
		}
	}

	@Override
	public boolean isAi() {
		return owner >= 0;
	}
	
}