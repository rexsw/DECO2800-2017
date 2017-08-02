package com.deco2800.marsinvasion.entities;

import com.deco2800.marsinvasion.actions.DecoAction;
import com.deco2800.marsinvasion.actions.MoveAction;
import com.deco2800.marsinvasion.managers.MouseHandler;
import com.deco2800.moos.entities.Tickable;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.entities.AbstractEntity;

import java.util.Optional;

/**
 * A hero for the game
 * Created by timhadwen on 19/7/17.
 */
public class HeroSpacman extends AbstractEntity implements Tickable, Clickable {

	Optional<DecoAction> currentAction = Optional.empty();

	/**
	 * Constructor for a hero
	 * @param world
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public HeroSpacman(AbstractWorld world, float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1, 1.2f, 1.2f);
		this.setTexture("spacman");
	}

	@Override
	public void onTick(int i) {
		if (!currentAction.isPresent()) {
			return;
		}

		if (!currentAction.get().completed()) {
			currentAction.get().doAction();
		}
	}

	@Override
	public void onClick(MouseHandler handler) {
		handler.registerForRightClickNotification(this);
	}

	@Override
	public void onRightClick(float x, float y) {
		currentAction = Optional.of(new MoveAction((int)x, (int)y, this));
	}
}