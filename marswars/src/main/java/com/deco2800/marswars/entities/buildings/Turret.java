package com.deco2800.marswars.entities.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.Clickable;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.entities.HasProgress;
import com.deco2800.marswars.entities.Tickable;
import com.deco2800.marswars.entities.Selectable.EntityType;
import com.deco2800.marswars.managers.Manager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.PlayerManager;
import com.deco2800.marswars.worlds.AbstractWorld;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by judahbennett on 25/8/17.
 *
 * A turret that can be used for base defence
 */

public class Turret extends BuildingEntity implements Clickable, Tickable, HasProgress, HasOwner {

	/* A single action for this building */
	Optional<DecoAction> currentAction = Optional.empty();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Turret.class);
	
	private int owner;

	boolean selected = false;

	/**
	 * Constructor for the turret.
	 * @param world The world that will hold the turret.
	 * @param posX its x position on the world.
	 * @param posY its y position on the world.
	 * @param posZ its z position on the world.
	 */
	public Turret(AbstractWorld world, float posX, float posY, float posZ) {
		super(posX, posY, posZ, BuildingType.TURRET);
		this.setTexture("turret");
		this.setEntityType(EntityType.BUILDING);
		this.setCost(0);
		this.setSpeed(2);
		this.addNewAction(ActionType.GENERATE);
		world.deSelectAll();
	}


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
		if(!this.isAi()) {
			if (!selected) {
				selected = true;
				LOGGER.error("clicked on Turret");
			}
		} else {
			LOGGER.error("clicked on ai turret");

		}
	}

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

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void deselect() {
		selected = false;
	}

	public Label getHelpText() {
		return new Label("You have clicked on the turret.", new Skin(Gdx.files.internal("uiskin.json")));
	}

	@Override
	public int getProgress() {
		if (currentAction.isPresent()) {
			return currentAction.get().actionProgress();
		}
		return 0;
	}

	@Override
	public boolean showProgress() {
		return currentAction.isPresent();
	}

	@Override
	public void setOwner(int owner) {
		this.owner = owner;
	}

	@Override
	public int getOwner() {
		return this.owner;
	}

	@Override
	public boolean sameOwner(AbstractEntity entity) {
		return entity instanceof  HasOwner &&
				this.owner == ((HasOwner) entity).getOwner();
	}
	
	public boolean isWorking() {
		return currentAction.isPresent();
	}
	
	public void setAction(DecoAction action) {
		currentAction = Optional.of(action);
	}
	
	@Override
	public boolean isAi() {
		return owner >= 0;
	}
	
}