package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.worlds.AbstractWorld;

import java.util.Optional;

/**
 * A hero for the game
 * Created by timhadwen on 19/7/17.
 */
public class HeroSpacman extends BaseEntity implements Tickable, Clickable {

	Optional<DecoAction> currentAction = Optional.empty();

	/**
	 * Constructor for a hero
	 * @param world
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public HeroSpacman(AbstractWorld world, float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1, 1f, 1f);
		this.setTexture("spacman_red");
		this.setEntityType(EntityType.HERO);
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
		this.makeSelected();
		handler.registerForRightClickNotification(this);
	}

	@Override
	public void onRightClick(float x, float y) {
		currentAction = Optional.of(new MoveAction((int)x, (int)y, this));
	}

	@Override
	public boolean isSelected() {
		return false;
	}

	@Override
	public void deselect() {

	}


}