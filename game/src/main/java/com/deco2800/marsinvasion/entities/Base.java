package com.deco2800.marsinvasion.entities;

import com.deco2800.marsinvasion.actions.DecoAction;
import com.deco2800.moos.entities.Tickable;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.WorldEntity;

import java.util.Optional;

/**
 * Created by timhadwen on 19/7/17.
 *
 * A home base for the empire
 */
public class Base extends WorldEntity implements Clickable, Tickable, Selectable {

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
		super(world, posX, posY, posZ, 1, 2, 2);
		this.setTexture("station");
	}

	/**
	 * On click handler
	 */
	@Override
	public void onClick() {
		System.out.println("Base got clicked");

		if (!selected) {
			selected = true;
		}
	}

	/**
	 * On Tick handler
	 * @param i time since last tick
	 */
	@Override
	public void onTick(int i) {

		if (selected) {
			this.setTexture("tree_selected");
		} else {
			this.setTexture("tree");
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
}
