package com.deco2800.marsinvasion.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.marsinvasion.actions.DecoAction;
import com.deco2800.marsinvasion.actions.GenerateAction;
import com.deco2800.marsinvasion.handlers.MouseHandler;
import com.deco2800.moos.entities.HasProgress;
import com.deco2800.moos.entities.Tickable;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.WorldEntity;

import java.util.Optional;

/**
 * Created by timhadwen on 19/7/17.
 *
 * A home base for the empire
 */
public class Base extends WorldEntity implements Clickable, Tickable, Selectable, HasProgress {

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
		super(world, posX, posY, posZ, 1, 1, 1);
		this.setTexture("base");
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

		if (selected) {
			this.setTexture("tree");
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

	@Override
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

	@Override
	public void buttonWasPressed() {
		/* We probably don't want these in random spots */
		currentAction = Optional.of(new GenerateAction(new Spacman(this.getParent(), this.getPosX(), this.getPosY(), 0), this.getParent()));
	}

	@Override
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
