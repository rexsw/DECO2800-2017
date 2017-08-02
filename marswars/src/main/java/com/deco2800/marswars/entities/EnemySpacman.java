package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.managers.LocalEnemyManager;
import com.deco2800.moos.entities.Tickable;
import com.deco2800.moos.managers.GameManager;

import java.util.Optional;

/**
 * A generic player instance for the game
 * Created by timhadwen on 19/7/17.
 */
public class EnemySpacman extends BaseEntity implements Tickable {

	Optional<DecoAction> currentAction = Optional.empty();

	/**
	 * Constructor for the Spacman
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public EnemySpacman(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 0.75f, 0.75f, 1);
		this.setTexture("spatman_blue");

		// Ensure the LocalEnemey manager has been created!
		GameManager.get().getManager(LocalEnemyManager.class);
	}

	public Optional<DecoAction> getCurrentAction() {
		return currentAction;
	}

	public void setCurrentAction(DecoAction currentAction) {
		this.currentAction = Optional.of(currentAction);
	}

	@Override
	public void onTick(int i) {
		if (currentAction.isPresent()) {
			currentAction.get().doAction();
		}
	}
}