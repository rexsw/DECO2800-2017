package com.deco2800.marswars.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.deco2800.marswars.actions.BuildAction;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.GenerateAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.ResourceManager;
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
	
	// this is the resource gathered by this unit, it may shift to other unit in a later stage
	private GatheredResource gatheredResource = null;

	/**
	 * Constructor for the Spacman
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public Spacman(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1, 1, 2);
		this.setTexture("spacman_green");
		this.setCost(10);
		this.setEntityType(EntityType.UNIT);
		this.initActions();
		this.addNewAction(GatherAction.class);
		this.addNewAction(MoveAction.class);
		this.addNewAction(BuildAction.class);
	}

	/**
	 * On tick method for the spacman
	 * @param i
	 */
	@Override
	public void onTick(int i) {
		/* Don't let spacman stand on a tile with another entity,
			do really basic formation stuff instead
		 */
		if (!currentAction.isPresent()) {
			if (GameManager.get().getWorld().getEntities((int)this.getPosX(), (int)this.getPosY()).size() > 1) {
				BaseWorld world = GameManager.get().getWorld();
				/* We are stuck on a tile with another entity
				 * therefore randomize a close by position and see if its a good
				 * place to move to
				 */
				Random r = new Random();
				Point p = new Point(this.getPosX() + r.nextInt(2) - 1, this.getPosY() + r.nextInt(2) - 1);

				/* Ensure new position is on the map */
				if (p.getX() < 0 || p.getY() < 0 || p.getX() > world.getWidth() || p.getY() > world.getLength()) {
					return;
				}
				/* Check that the new position is free */
				if (world.getEntities((int)p.getX(), (int)p.getY()).size() > 1) {
					// No good
					return;
				}

				LOGGER.info("Spacman is on a tile with another entity, move out of the way");

				/* Finally move to that position using a move action */
				this.currentAction = Optional.of(new MoveAction((int)p.getX(), (int)p.getY(), this));
			}
			return;
		}

		/* If the action is completed, remove it otherwise keep doing that action */
		if (!currentAction.get().completed()) {
			currentAction.get().doAction();
		} else {
			LOGGER.info("Action is completed. Deleting");
			currentAction = Optional.empty();
		}
	}

	/**
	 * On click method for the spacman
	 * Should change to Blue
	 * @param handler
	 */
	@Override
	public void onClick(MouseHandler handler) {
		// If Spacman is building, left click will finalise selection
		if (currentAction.isPresent()) {
			if(currentAction.get() instanceof BuildAction) {
				return;
			}
		}
		handler.registerForRightClickNotification(this);
		SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
		this.setTexture("spacman_blue");
		LOGGER.error("Clicked on spacman");
		this.makeSelected();
	}

	/**
	 * On rightclick method for spacman
	 * @param x
	 * @param y
	 */
	@Override
	public void onRightClick(float x, float y) {
		List<BaseEntity> entities;
		try {
			entities = ((BaseWorld) GameManager.get().getWorld()).getEntities((int) x, (int) y);

		} catch (IndexOutOfBoundsException e) {
			// if the right click occurs outside of the game world, nothing will happen
			this.setTexture("spacman_green");
			return;
		}
		if (currentAction.isPresent()) {
			if(currentAction.get() instanceof BuildAction) {
				BuildAction tempCast = (BuildAction) currentAction.get();
				tempCast.finaliseBuild();
				this.setTexture("spacman_green");
				this.deselect();
				return;
			}
		}
		if (entities.size() > 0 && entities.get(0) instanceof Resource) {
			currentAction = Optional.of(new GatherAction(this, entities.get(0)));
			LOGGER.error("Assigned action gather");
		} 
		else {
			currentAction = Optional.of(new MoveAction((int)x, (int)y, this));
			LOGGER.error("Assigned action move to" + x + " " + y);
		}
		this.setTexture("spacman_green");
		SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
		sound.playSound("endturn.wav");
		this.deselect();
	}

	/**
	 * Gets the health for this spacman
	 * @return
	 */
	@Override
	public int getHealth() {
		return health;
	}

	/* Sets the health for this spacman */
	@Override
	public void setHealth(int health) {
		LOGGER.info("Set health to " + health);
		this.health = health;

		if (health < 0) {
			GameManager.get().getWorld().removeEntity(this);
			LOGGER.info("I am kill");
		}
	}
	
	/**
	 * Add gathered resource to unit's backpack
	 * @param resource
	 */
	public void addGatheredResource(GatheredResource resource) {
		this.gatheredResource = resource;
		LOGGER.error("Gathered "+ resource.getAmount() + " units of "+ resource.getType());
	}
	
	/**
	 * check if the unit has resource in it's backpack
	 * @return true if this unit carries something
	 */
	public boolean checkBackpack() {
		return (gatheredResource != null);
	}
	
	/**
	 * remove the resource from unit's backpack
	 * @return gatheredResource
	 */
	public GatheredResource removeGatheredResource() {
		GatheredResource resource = new GatheredResource (gatheredResource.getType(), gatheredResource.getAmount());
		LOGGER.error("Removed "+ resource.getAmount() + " units of "+ resource.getType());
		gatheredResource = null;
		return resource;
	}
	
	public Button getButton() {
		Button button = new TextButton("Make Base", new Skin(Gdx.files.internal("uiskin.json")));
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				buttonWasPressed();
			}
		});
		return button;
	}

	public void buttonWasPressed() {
		ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		if (resourceManager.getRocks() >= 150) {
			resourceManager.setRocks(resourceManager.getRocks() - 150);
		}
		currentAction = Optional.of(new BuildAction(this));
	}

}