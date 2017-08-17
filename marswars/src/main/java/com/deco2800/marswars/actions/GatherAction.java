package com.deco2800.marswars.actions;

import com.deco2800.marswars.entities.Base;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.GatheredResource;
import com.deco2800.marswars.entities.HasHealth;
import com.deco2800.marswars.entities.ResourceType;
import com.deco2800.marswars.entities.Spacman;
import com.deco2800.marswars.entities.Resource;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.util.Point;
import com.deco2800.marswars.util.WorldUtil;
import com.deco2800.marswars.worlds.BaseWorld;

import java.util.Optional;

import static com.deco2800.marswars.actions.GatherAction.State.SETUP_MOVE;
import static com.deco2800.marswars.actions.GatherAction.State.SETUP_RETURN;

public class GatherAction implements DecoAction {

	enum State {
		SETUP_MOVE,
		MOVE_TOWARDS,
		COLLECT,
		SETUP_RETURN,
		RETURN_TO_BASE
	}

	MoveAction action = null;
	private State state = State.SETUP_MOVE;
	private BaseEntity entity;
	private Class type;
	boolean completed = false;

	private int ticksCollect = 200;

	private BaseEntity goal;
	
//	private ResourceType resourceType;
	private int harvestAmount;

	public GatherAction(BaseEntity entity, BaseEntity goalEntity) {
		this.goal = goalEntity;
		this.entity = entity;
//		resourceType = ((Resource) goal).getType();
		harvestAmount = 10;
	}

	@Override
	public void doAction() {
		switch(state) {
			case SETUP_MOVE:
				// Always move back to the goal entity position
				action = new MoveAction(goal.getPosX(), goal.getPosY(), entity);

				state = State.MOVE_TOWARDS;
				break;
			case MOVE_TOWARDS:
				if (action.completed()) {
					state = State.COLLECT;
					return;
				}

				// Do the move action
				action.doAction();
				break;
			case COLLECT:
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
								if (entity instanceof Spacman) {
									((Spacman) entity).addGatheredResource(new GatheredResource(resourceType, harvestAmount));
								}
								((Resource) goal).setHarvestNumber(((Resource) goal).getHarvesterNumber() - 1);
							}
						} else {
							// if the number of harvester over the capacity, should be handle here
							System.err.println("Resource has reach the maximum capacity of harvester");
						}
						
						ticksCollect = 200;
					}
				} else {
					// Find a new closest entity
					BaseWorld world = GameManager.get().getWorld();

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

				break;
			case SETUP_RETURN:
				Optional<BaseEntity> base = WorldUtil.getClosestEntityOfClass(Base.class, entity.getPosX(), entity.getPosY());

				if (base.isPresent()) {
					action = new MoveAction(base.get().getPosX(), base.get().getPosY(), entity);
				}

				state = State.RETURN_TO_BASE;
				break;
			case RETURN_TO_BASE:
				if (action.completed()) {
					state = State.SETUP_MOVE;
					ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
					// check which type of resource and add it to the player's resource
					if (entity instanceof Spacman) {
						// check if this unit't actually has something to drop
						if (((Spacman) entity).checkBackpack()) {
							GatheredResource resource = ((Spacman) entity).removeGatheredResource();
							ResourceType resourceType = resource.getType();
							int amount = resource.getAmount();
							switch (resourceType) {
							case WATER:
								resourceManager.setWater(resourceManager.getWater() + amount);
								break;
							case ROCK:
								resourceManager.setRocks(resourceManager.getRocks() + amount);
								break;
							case CRYSTAL:
								resourceManager.setCrystal(resourceManager.getCrystal() + amount);
								break;
							case BIOMASS:
								resourceManager.setBiomass(resourceManager.getBiomass() + amount);
								break;
							}
						} else {// if there is nothing
							System.err.println("Bring back nothing");
						}
						
					}
					
//					resourceManager.setRocks(resourceManager.getRocks() + 10);
					return;
				}

				action.doAction();
				break;
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
}
