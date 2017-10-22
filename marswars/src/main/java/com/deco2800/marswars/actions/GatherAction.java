package com.deco2800.marswars.actions;

import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.GatheredResource;
import com.deco2800.marswars.entities.HasHealth;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.entities.terrainelements.Resource;
import com.deco2800.marswars.entities.terrainelements.ResourceType;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.util.Point;
import com.deco2800.marswars.util.WorldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.deco2800.marswars.actions.GatherAction.State.SETUP_MOVE;
import static com.deco2800.marswars.actions.GatherAction.State.SETUP_RETURN;

public class GatherAction implements DecoAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatherAction.class);
    
	enum State {
		SETUP_MOVE,
		MOVE_TOWARDS,
		COLLECT,
		SETUP_RETURN,
		RETURN_TO_BASE
	}

	private MoveAction action = null;
	private State state = State.SETUP_MOVE;
	private BaseEntity entity;
	private boolean completed = false;
	// Variables for pause
	private boolean actionPaused = false;
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);

	private int ticksCollect = 200;

	private BaseEntity goal;
	
	private int harvestAmount;

	public GatherAction(BaseEntity entity, BaseEntity goalEntity) {
		this.goal = goalEntity;
		this.entity = entity;
		harvestAmount = 10;
	}

	@Override
	public void doAction() {
		if (! timeManager.isPaused() && ! actionPaused) {
			switch (state) {
				case SETUP_MOVE:
					setupMove();
					break;
				case MOVE_TOWARDS:
					moveTowards();
					break;
				case COLLECT:
					collect();
					break;
				case SETUP_RETURN:
					setupReturn();
					break;
				case RETURN_TO_BASE:
					returnToBase();
					break;
				default:
					break;
			}
		}
	}

	@Override
	public boolean completed() {
		return completed;
	}

	@Override
	public int actionProgress() {
		return 0;
	}


	private void setupMove() {
		// Always move back to the goal entity position
		action = new MoveAction(goal.getPosX(), goal.getPosY(), entity);
		state = State.MOVE_TOWARDS;
	}

	private void moveTowards() {
		if (action.completed()) {
			state = State.COLLECT;
			return;
		}
		// Do the move action
		action.doAction();
	}

	private void collect() {
		if (GameManager.get().getWorld().getEntities().contains(goal)) {
			if (ticksCollect == 200) {
				((Resource) goal).setHarvestNumber(((Resource) goal).getHarvesterNumber() + 1);
			}
			// Our goal object still exists, mine it
			ticksCollect--;
			if (ticksCollect == 0) {
				state = SETUP_RETURN;
				if (((Resource) goal).getHarvesterNumber() < ((Resource) goal).getHarvesterCapacity()) {
					ResourceType resourceType = ((Resource) goal).getType();
					if (goal instanceof HasHealth) {
						((HasHealth) goal).setHealth(((HasHealth) goal).getHealth() - harvestAmount);
						if (entity instanceof Astronaut) {
							((Astronaut) entity).addGatheredResource(new GatheredResource(resourceType, harvestAmount));
						}
						((Resource) goal).setHarvestNumber(((Resource) goal).getHarvesterNumber() - 1);
					}
				} else {
					// if the number of harvester over the capacity, should be handle here
					LOGGER.error("Resource has reach the maximum capacity of harvester");
				}

				ticksCollect = 200;
			}
		} else {
			// Find a new closest entity
			Optional<BaseEntity> surround = WorldUtil.getClosestEntityOfClass(goal.getClass(), goal.getPosX(), goal.getPosY());
			if (surround.isPresent()) {
				Point p = new Point(surround.get().getPosX(), surround.get().getPosY());
				Point o = new Point(entity.getPosX(), entity.getPosY());

				if (p.distanceTo(o) < 2f) {
					this.goal = surround.get();
					this.state = SETUP_MOVE;
					return;
				} else {
					this.completed = true;
					return;
				}
			}
		}
	}
	private void returnToBase() {
		if (action.completed()) {
			if (!((HasOwner) entity).isAi()) {
				state = State.SETUP_MOVE;
			} else {
				((AttackableEntity) entity).setEmptyAction();
			}
			if (entity instanceof HasOwner && ((HasOwner) entity).isAi()) {
				//if controlled by the ai added the resources to the ai's pile
				ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);;
				depositHarvest(resourceManager);
				return;
			}
			ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
			// check which type of resource and add it to the player's resource
			if (entity instanceof Astronaut) {
				depositHarvest(resourceManager);
			}
			return;
		}
		action.doAction();
		}

	private void setupReturn() {
		Optional<BaseEntity> base = WorldUtil.getClosestEntityOfClass(Base.class, entity.getPosX(), entity.getPosY());
		if(entity instanceof HasOwner) {
			base = WorldUtil.getClosestEntityOfClassAndOwner(Base.class, entity.getPosX(), entity.getPosY(), ((HasOwner) entity).getOwner());
		} 
		
		if (base.isPresent()) {
			action = new MoveAction(base.get().getPosX(), base.get().getPosY(), entity);
		}

		state = State.RETURN_TO_BASE;
	}

	private void depositHarvest(ResourceManager rm) {
		// check if this unit't actually has something to drop
		if (((Astronaut) entity).checkBackpack()) {
			ResourceManager resourceManager = rm;
			GatheredResource resource = ((Astronaut) entity).removeGatheredResource();
			ResourceType resourceType = resource.getType();
			int amount = resource.getAmount();
			switch (resourceType) {
				case ROCK:
					resourceManager.setRocks(resourceManager.getRocks(((Astronaut) entity).getOwner()) + amount, ((Astronaut) entity).getOwner());
					break;
				case CRYSTAL:
					resourceManager.setCrystal(resourceManager.getCrystal(((Astronaut) entity).getOwner()) + amount, ((Astronaut) entity).getOwner());
					break;
				case BIOMASS:
					resourceManager.setBiomass(resourceManager.getBiomass(((Astronaut) entity).getOwner()) + amount, ((Astronaut) entity).getOwner());
					break;
				default :
					break;
			}
		} else {// if there is nothing
			LOGGER.error("Bring back nothing");
		}
	}

	/**
	 * Prevents the current action from progressing.
	 */
	@Override
	public void pauseAction() {
		actionPaused = true;
	}

	/**
	 * Resumes the current action
	 */
	@Override
	public void resumeAction() {
		actionPaused = false;
	}
}
