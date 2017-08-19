package com.deco2800.marswars.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.marswars.actions.BuildAction;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.GenerateAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.Selectable.EntityType;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.worlds.AbstractWorld;

import java.util.Optional;

/**
 * Created by timhadwen on 19/7/17.
 *
 * A home base for the empire
 */
public class Base extends BaseEntity implements Clickable, Tickable, HasProgress {

	/* A single action for this building */
	Optional<DecoAction> currentAction = Optional.empty();

	boolean selected = false;

	/**
	 * Constructor for the base
	 * @param world
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public Base(AbstractWorld world, float posX, float posY, float posZ) {
		super(posX, posY, posZ, 4 , 4, 1, 4, 4, false);
		this.setTexture("grass");
		this.setEntityType(EntityType.BUILDING);
		this.setCost(10000000);
		this.setSpeed(2);
		this.initActions();
		this.addNewAction(BuildAction.class);
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
		System.out.println("Base got clicked");
		this.currentAction = Optional.of(new BuildAction(this));
		if (!selected) {
			selected = true;
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
			currentAction = Optional.of(new GenerateAction(new Spacman(this.getPosX() - 1, this.getPosY() - 1, 0)));
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
}
