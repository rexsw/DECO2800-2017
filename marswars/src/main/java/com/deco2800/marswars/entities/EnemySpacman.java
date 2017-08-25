package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.LocalEnemyManager;
import com.deco2800.marswars.managers.Manager;

import java.util.Optional;

/**
 * A generic player instance for the game
 * Created by timhadwen on 19/7/17.
 */
public class EnemySpacman extends BaseEntity implements Tickable, HasOwner{

	Optional<DecoAction> currentAction = Optional.empty();
	
	private Manager owner = null;

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
		//GameManager.get().getManager(LocalEnemyManager.class);
	}

	/**
	 * Gets the current action
	 * @return
	 */
	public Optional<DecoAction> getCurrentAction() {
		return currentAction;
	}

	/**
	 * Sets the current action
	 * @param currentAction the new action
	 */
	public void setCurrentAction(DecoAction currentAction) {
		this.currentAction = Optional.of(currentAction);
	}

	/**
	 * On tick method for the spacman
	 * @param i
	 */
	@Override
	public void onTick(int i) {
		if (currentAction.isPresent()) {
			currentAction.get().doAction();
		}
	}
	
	@Override
	public void setOwner(Manager owner) {
		this.owner = owner;
	}

	@Override
	public Manager getOwner() {
		return this.owner;
	}

	@Override
	public boolean sameOwner(AbstractEntity entity) {
		return entity instanceof  HasOwner &&
				this.owner == ((HasOwner) entity).getOwner();
	}
	
	@Override
	public boolean isWorking() {
		if(currentAction.isPresent()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void setAction(DecoAction action) {
		currentAction = Optional.of(action);
	}
}