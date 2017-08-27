package com.deco2800.marswars.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.GenerateAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.Selectable.EntityType;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.Manager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.PlayerManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.worlds.AbstractWorld;
import com.deco2800.marswars.worlds.BaseWorld;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by timhadwen on 19/7/17.
 *
 * A home base for the empire
 */
public class Base extends BuildingEntity implements Clickable, Tickable, HasProgress, HasOwner {

	/* A single action for this building */
	Optional<DecoAction> currentAction = Optional.empty();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Base.class);
	
	private Manager onwer = null;

	boolean selected = false;

	/**
	 * Constructor for the base.
	 * @param world The world that will hold the base.
	 * @param posX its x position on the world.
	 * @param posY its y position on the world.
	 * @param posZ its z position on the world.
	 */

	public Base(AbstractWorld world, float posX, float posY, float posZ) {
	super(posX, posY, posZ, 3f, 3f, 0f, BuildingType.BASE);
		this.setTexture("homeBase");
		this.setEntityType(EntityType.BUILDING);
		this.setCost(10);
		this.setSpeed(2);
		this.initActions();
		//Find a way to render first, then move only add some of collision blocks (miss first column)
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
		if(this.getOwner() instanceof PlayerManager) {
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
		if (resourceManager.getRocks() > 30) {
			resourceManager.setRocks(resourceManager.getRocks() - 30);
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
	public void setOwner(Manager owner) {
		this.onwer = owner;
	}

	/**
	 * Get the owner of this base
	 * @return owner
	 */
	@Override
	public Manager getOwner() {
		return this.onwer;
	}

	/**
	 * Check if the AbstractEntity passed in and this entity has the same owner
	 * @return boolean
	 */
	@Override
	public boolean sameOwner(AbstractEntity entity) {
		return entity instanceof  HasOwner &&
				this.onwer == ((HasOwner) entity).getOwner();
	}
	
	/**
	 * This method is a duplication of the showProgress method, consider delete one of them
	 * @return boolean
	 */
	public boolean isWorking() {
		return currentAction.isPresent();
	}
	
	/**
	 * Set the action of this base
	 * @param action
	 */
	public void setAction(DecoAction action) {
		currentAction = Optional.of(action);
	}
	
}
