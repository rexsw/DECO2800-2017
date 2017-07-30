package com.deco2800.marsinvasion.entities;

import com.deco2800.marsinvasion.actions.DecoAction;
import com.deco2800.marsinvasion.actions.GatherAction;
import com.deco2800.marsinvasion.actions.MoveAction;
import com.deco2800.marsinvasion.handlers.MouseHandler;
import com.deco2800.marsinvasion.util.WorldUtil;
import com.deco2800.moos.entities.Tickable;
import com.deco2800.moos.managers.GameManager;
import com.deco2800.moos.managers.SoundManager;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.WorldEntity;

import java.util.Optional;
import java.util.Random;

/**
 * A generic player instance for the game
 * Created by timhadwen on 19/7/17.
 */
public class Spacman extends WorldEntity implements Tickable, Clickable {

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
		if (!currentAction.isPresent()) {
			return;
		}

		if (!currentAction.get().completed()) {
			currentAction.get().doAction();
		} else {
			currentAction = Optional.empty();
		}
	}

	@Override
	public void onClick(MouseHandler handler) {
		handler.registerForRightClickNotification(this);
		SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
		sound.playSound("ree1.wav");
	}

	@Override
	public void onRightClick(float x, float y) {
		Optional<WorldEntity> entity = WorldUtil.getEntityAtPosition(this.getParent(), x, y);
		if (entity.isPresent()) {
			currentAction = Optional.of(new GatherAction(this, Rock.class));
		} else {
			currentAction = Optional.of(new MoveAction((int)x, (int)y, this));
		}
	}
}