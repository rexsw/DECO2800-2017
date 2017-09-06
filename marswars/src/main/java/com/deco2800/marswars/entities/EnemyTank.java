package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.LocalEnemyManager;

import java.util.Optional;

/**
 * A generic player instance for the game
 * Created by timhadwen on 19/7/17.
 */
public class EnemyTank extends BaseEntity implements Tickable, HasAction {

	Optional<DecoAction> currentAction = Optional.empty();

	/**
	 * Constructor for the Spacman
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public EnemyTank(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 0.75f, 0.75f, 1);
		this.setTexture("memetank");

		// Ensure the LocalEnemey manager has been created!
		GameManager.get().getManager(LocalEnemyManager.class);
	}

	/**
	 * Gets the current action
	 * @return
	 */
	@Override
	public Optional<DecoAction> getCurrentAction() {
		return currentAction;
	}

	/**
	 * Sets the current action
	 * @param currentAction
	 */
	public void setCurrentAction(DecoAction currentAction) {
		this.currentAction = Optional.of(currentAction);
	}

	/**
	 * On tick method for the tank
	 * @param i
	 */
	@Override
	public void onTick(int i) {
		if (currentAction.isPresent()) {
			currentAction.get().doAction();
		}
	}
}