package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.SoundManager;
import com.deco2800.marswars.util.Point;
import com.deco2800.marswars.worlds.BaseWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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
		this.setCost(10);

		Random r = new Random();
		this.currentAction = Optional.of(new MoveAction(r.nextInt(150) + 25, r.nextInt(150) + 25, this));
	}

	@Override
	public void onTick(int i) {
		if (!currentAction.isPresent()) {
			if (GameManager.get().getWorld().getEntities((int)this.getPosX(), (int)this.getPosY()).size() > 2) {
				LOGGER.info("Spacman is on a tile with another entity, move out of the way");
				// We are stuck on a tile with another spacman
				Random r = new Random();

				BaseWorld world = GameManager.get().getWorld();

				Point p = new Point(this.getPosX() + r.nextInt(2) - 1, this.getPosY() + r.nextInt(2) - 1);

				if (p.getX() < 0 || p.getY() < 0 || p.getX() > world.getWidth() || p.getY() > world.getLength()) {
					return;
				}

				if (world.getEntities((int)p.getX(), (int)p.getY()).size() > 1) {
					// No good
					return;
				}

				this.currentAction = Optional.of(new MoveAction((int)p.getX(), (int)p.getY(), this));
			}
			return;
		}

		if (!currentAction.get().completed()) {
			currentAction.get().doAction();
		} else {
			LOGGER.info("Action is completed. Deleting");
			currentAction = Optional.empty();
			Random r = new Random();
			this.currentAction = Optional.of(new MoveAction(r.nextInt(150) + 25, r.nextInt(150) + 25, this));
		}
	}

	@Override
	public void onClick(MouseHandler handler) {
		handler.registerForRightClickNotification(this);
		SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
		this.setTexture("spacman_blue");
		LOGGER.error("Clicked on spacman");
	}

	@Override
	public void onRightClick(float x, float y) {
		List<BaseEntity> entities = ((BaseWorld)GameManager.get().getWorld()).getEntities((int)x, (int)y);
		if (entities.size() > 0) {
			currentAction = Optional.of(new GatherAction(this, entities.get(0)));
			LOGGER.error("Assigned action gather");
		} else {
			currentAction = Optional.of(new MoveAction((int)x, (int)y, this));
			LOGGER.error("Assigned action move to" + x + " " + y);
		}
		this.setTexture("spacman_green");
		SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
		sound.playSound("endturn.wav");

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