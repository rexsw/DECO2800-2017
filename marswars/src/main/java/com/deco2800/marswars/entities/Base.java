package com.deco2800.marswars.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.GenerateAction;
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
public class Base extends BaseEntity implements Clickable, Tickable, HasProgress, HasOwner {

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
		super(posX, posY, posZ, 1, 1, 1);
		this.setTexture("base");
		this.setCost(10000000);
	}

	public Base(BaseWorld world, int posX, int posY, int posZ) {
		super(posX, posY, posZ, 1, 1, 1);
		this.setTexture("base");
		this.setCost(10000000);
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
		if(this.getOwner() instanceof PlayerManager) {
			if (!selected) {
				selected = true;
				LOGGER.error("clicked on base");
			}
		} else {
			LOGGER.error("clicked on ai base");
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

		if (selected) {
			this.setTexture("base");
		} else {
			this.setTexture("base");
		}

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

	public void buttonWasPressed() {
		ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		if (resourceManager.getRocks() > 30) {
			resourceManager.setRocks(resourceManager.getRocks() - 30);
			currentAction = Optional.of(new GenerateAction(new Spacman(this.getPosX() + 1, this.getPosY() + 1, 0)));
		}
	}

	public Label getHelpText() {
		return new Label("You have clicked on the base. Click 'Make Spacman' to 'Make Spacman'!", new Skin(Gdx.files.internal("uiskin.json")));
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
	public void setOwner(Manager owner) {
		this.onwer = owner;
	}

	@Override
	public Manager getOwner() {
		return this.onwer;
	}

	@Override
	public boolean sameOwner(AbstractEntity entity) {
		return entity instanceof  HasOwner &&
				this.onwer == ((HasOwner) entity).getOwner();
	}
	
	public boolean isWorking() {
		return currentAction.isPresent();
	}
	
	public void setAction(DecoAction action) {
		currentAction = Optional.of(action);
	}
	
}
