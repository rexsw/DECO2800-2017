package com.deco2800.marswars.actions;

import com.deco2800.marswars.entities.Base;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.HasHealth;
import com.deco2800.marswars.entities.ResourceType;
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

	private int ticksCollect = 10;

	private BaseEntity goal;
	
	private ResourceType resourceType;

	public GatherAction(BaseEntity entity, BaseEntity goalEntity) {
		this.goal = goalEntity;
		this.entity = entity;
		resourceType = ((Resource) goal).getType();
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
					// Our goal object still exists, mine it
					ticksCollect--;
					if (ticksCollect == 0) {
						state = SETUP_RETURN;
						if (goal instanceof HasHealth) {
							((HasHealth) goal).setHealth(((HasHealth) goal).getHealth() - 10);
						}
						ticksCollect = 100;
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
					switch (resourceType) {
					case WATER:
						resourceManager.setWater(resourceManager.getWater() + 10);
						break;
					case ROCK:
						resourceManager.setRocks(resourceManager.getRocks() + 10);
						break;
					case CRYSTAL:
						resourceManager.setCrystal(resourceManager.getCrystal() + 10);
						break;
					case BIOMASS:
						resourceManager.setBiomass(resourceManager.getBiomass() + 10);
						break;
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
