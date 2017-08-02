package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.util.WorldUtil;
import com.deco2800.moos.entities.AbstractEntity;
import com.deco2800.moos.entities.Tickable;
import com.deco2800.moos.managers.GameManager;
import com.deco2800.moos.managers.SoundManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Random;

/**
 * A generic player instance for the game
 * Created by timhadwen on 19/7/17.
 */
public class Spacman extends BaseEntity implements Tickable, Clickable, HasHealth {

	private static final Logger LOGGER = LoggerFactory.getLogger(Spacman.class);

	private Optional<DecoAction> currentAction = Optional.empty();

	private int health = 100;

	/**
	 * Constructor for the Spacman
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public Spacman(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1, 1, 1);
		this.setTexture("spacman_green");

		Random r = new Random();
		currentAction = Optional.of(new MoveAction(r.nextInt(24), r.nextInt(24), this));
	}

	@Override
	public void onTick(int i) {
		if (!currentAction.isPresent()) {
			return;
		}

		if (!currentAction.get().completed()) {
//			LOGGER.info("Action is incomplete");
			currentAction.get().doAction();
		} else {
			LOGGER.info("Action is completed. Deleting");
//			currentAction = Optional.empty();

			Random r = new Random();
			currentAction = Optional.of(new MoveAction(r.nextInt(24), r.nextInt(24), this));
		}
	}

	@Override
	public void onClick(MouseHandler handler) {
		handler.registerForRightClickNotification(this);
		SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
		sound.playSound("ree1.wav");
		this.setTexture("spacman_blue");
		LOGGER.error("Clicked on spacman");
	}

	@Override
	public void onRightClick(float x, float y) {
		Optional<AbstractEntity> entity = WorldUtil.getEntityAtPosition(this.getParent(), x, y);
		if (entity.isPresent()) {
			currentAction = Optional.of(new GatherAction(this, Rock.class));
			LOGGER.error("Assigned action gather");
		} else {
			currentAction = Optional.of(new MoveAction((int)x, (int)y, this));
			LOGGER.error("Assigned action move to" + x + " " + y);
		}
		this.setTexture("spacman_green");
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public void setHealth(int health) {
		LOGGER.info("Set health to " + health);
		this.health = health;

		if (health < 0) {
			GameManager.get().getWorld().removeEntity(this);
			LOGGER.info("I am kill");
		}
	}
}