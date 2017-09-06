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
import com.deco2800.marswars.managers.TechnologyManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.worlds.AbstractWorld;

import com.deco2800.marswars.technology.*;

import java.util.Optional;

/**
 * Created by team name pending on 17/8/17.
 *
 * A test base for the tech tree
 */
public class Base2 extends BaseEntity implements Clickable, Tickable,
		HasProgress, HasAction {

	/* A single action for this building */
	Optional<DecoAction> currentAction = Optional.empty();

	private boolean selected = false;

	/**
	 * Constructor for the base
	 * @param world The world that will hold the base.
	 * @param posX its x position on the world.
	 * @param posY its y position on the world.
	 * @param posZ its z position on the world.
	 */
	public Base2(AbstractWorld world, float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1, 1, 1);
		this.setTexture("base2");
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
		System.out.println("Base2 got clicked");

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
			this.setTexture("base2");
		} else {
			this.setTexture("base2");
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
		Button button = new TextButton("Research test tech", new Skin(Gdx.files.internal("uiskin.json")));
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("The button was pressed\n");
				buttonWasPressed();
			}
		});
		return button;
	}

	public void buttonWasPressed() {
		System.out.println("The button was pressed\n");
		TechnologyManager techMan = (TechnologyManager) GameManager.get().getManager(TechnologyManager.class);
		Technology tech1 = techMan.getTech(1);
		Technology tech2 = techMan.getTech(2);
		ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		if (!techMan.getActive().contains(tech2)) {
			if (resourceManager.getRocks() > tech1.getCost()[0]) {
				resourceManager.setRocks(resourceManager.getRocks() - tech1.getCost()[0]);
				currentAction = Optional.of(new GenerateAction(new Spacman(this.getPosX() + 1, this.getPosY() + 1, 0)));
				techMan.addActiveTech(tech2);
			}
		} else {
			if (resourceManager.getRocks() > tech2.getCost()[0]) {
				resourceManager.setRocks(resourceManager.getRocks() - tech2.getCost()[0]);
				currentAction = Optional.of(new GenerateAction(new Spacman(this.getPosX() + 1, this.getPosY() + 1, 0)));
			}
		}
	}

	public Label getHelpText() {
		return new Label("You have clicked on the base. Click 'research test tech' to research tstuuffff!", new Skin(Gdx.files.internal("uiskin.json")));
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

	/**
	 * Returns the current action of the entity
	 * @return current action
	 */
	@Override
	public Optional<DecoAction> getCurrentAction() {
		return currentAction;
	}
}
