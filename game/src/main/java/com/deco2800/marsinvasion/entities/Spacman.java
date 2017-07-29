package com.deco2800.marsinvasion.entities;

import com.deco2800.marsinvasion.actions.DecoAction;
import com.deco2800.marsinvasion.actions.MoveAction;
import com.deco2800.moos.entities.Tickable;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.WorldEntity;

import java.util.Optional;
import java.util.Random;

/**
 * A generic player instance for the game
 * Created by timhadwen on 19/7/17.
 */
public class Spacman extends WorldEntity implements Tickable {

	Optional<DecoAction> currentAction = Optional.empty();

	/**
	 * Constructor for the Spacman
	 * @param world
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public Spacman(AbstractWorld world, float posX, float posY, float posZ) {
		super(world, posX, posY, posZ, 1, 1, 1);

		Random rand = new Random();
		switch(rand.nextInt(4)) {
			case 0:
				this.setTexture("spacman_red");
				break;
			case 1:
				this.setTexture("spacman_yellow");
				break;
			case 2:
				this.setTexture("spacman_blue");
				break;
			case 3:
				this.setTexture("spacman_green");
				break;
		}
	}

	@Override
	public void onTick(int i) {
		if (!currentAction.isPresent() || currentAction.isPresent() && currentAction.get().completed()) {
			Random r = new Random();
			currentAction = Optional.of(new MoveAction(r.nextInt(25), r.nextInt(25), this));
		} else {
			currentAction.get().doAction();
		}
	}
}