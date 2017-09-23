package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;

import java.util.Optional;

/**
 * A generic player instance for the game
 * Created by timhadwen on 19/7/17.
 */
public class EnemySpacman extends BaseEntity implements Tickable, HasOwner, HasProgress{

	Optional<DecoAction> currentAction = Optional.empty();
	
	private int owner = -1;

	/**
	 * Constructor for the Spacman
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public EnemySpacman(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 0.75f, 0.75f, 1);
		this.setTexture("spatman_blue");
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
	public void setOwner(int owner) {
		this.owner = owner;
	}

	@Override
	public int getOwner() {
		return this.owner;
	}

	@Override
	public boolean sameOwner(AbstractEntity entity) {
		return entity instanceof  HasOwner &&
				this.owner == ((HasOwner) entity).getOwner();
	}
	
	@Override
	public void setAction(DecoAction action) {
		currentAction = Optional.of(action);
	}

	@Override
	/**
	 * Get the progress of current action
	 * @return int
	 */
	public int getProgress() {
		if (currentAction.isPresent()) {
			return currentAction.get().actionProgress();
		}
		return 0;
	}

	@Override
	/**
	 * Returns true if there is a current action, false if not
	 * @return boolean
	 */
	public boolean showProgress() {
		return currentAction.isPresent();
	}
}