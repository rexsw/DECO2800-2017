package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.actions.*;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.EntityID;
import com.deco2800.marswars.entities.EntityStats;
import com.deco2800.marswars.entities.GatheredResource;
import com.deco2800.marswars.entities.TerrainElements.Resource;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.SoundManager;
import com.deco2800.marswars.worlds.BaseWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

//import com.deco2800.marswars.entities.Resource;

/**
 * A combat unit that can gather resources
 * @author Tze Thong Khor
 *
 */
public class Astronaut extends Soldier {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Astronaut.class);
	private GatheredResource gatheredResource = null;
	private BuildAction build;

	public Astronaut(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		this.name = "Astronaut";
		this.addNewAction(BuildingType.BASE);
		this.addNewAction(BuildingType.BARRACKS);
		setAttributes();
	}
	
	/**
	 * Overrides Left click and checks if build action is in progress and handles appropriately
	 * param handler the mouse handler
	 * @author grumpygandalf
	 *
	 */
	@Override
	public void onClick(MouseHandler handler) {
		//check if this belongs to a* player (need to change for multiplayer):
		if (this.getCurrentAction().isPresent()) {
			if(this.getCurrentAction().get() instanceof BuildAction) {
					return;
			}
		}
		if(!this.isAi() & this.getLoadStatus() != 1) {
			handler.registerForRightClickNotification(this);
			SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
			this.setTexture(selectedTextureName);
			LOGGER.info("Clicked on soldier");
			this.makeSelected();
		} else {
			LOGGER.info("Clicked on ai soldier");
			 this.makeSelected();
		}
	}
	
	@Override
	public void onRightClick(float x, float y) {
		List<BaseEntity> entities;
		try {
			entities = ((BaseWorld) GameManager.get().getWorld()).getEntities((int) x, (int) y);

		} catch (IndexOutOfBoundsException e) {
			// if the right click occurs outside of the game world, nothing will happen
			LOGGER.info("Right click occurred outside game world.");
			this.setTexture(defaultTextureName);
			this.deselect();
			return;
		}
		if (this.getCurrentAction().isPresent()) {
			if(this.getCurrentAction().get() instanceof BuildAction) {
				build.finaliseBuild();
				this.setTexture(defaultTextureName);
				this.deselect();
				return;
			}
		}
		boolean attack = !entities.isEmpty() && entities.get(0) instanceof AttackableEntity;
		boolean gatherResource = !entities.isEmpty() && entities.get(0) instanceof Resource;
		
		if (attack) {
			// we cant assign different owner yet
			AttackableEntity target = (AttackableEntity) entities.get(0);
			attack(target);
			
		} else if (gatherResource) { 
			LOGGER.info("Gather resources");
			this.setCurrentAction(Optional.of(new GatherAction(this, entities.get(0))));
		} else {
			this.setCurrentAction(Optional.of(new MoveAction((int) x, (int) y, this)));
			LOGGER.error("Assigned action move to" + x + " " + y);
		}
		this.setTexture(defaultTextureName);
		SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
		sound.playSound(movementSound);
	}


	@Override
	public void makeSelected() {
		super.makeSelected();
		if (!this.isAi()) {
			GameManager.get().getGui().showBuildMenu(this, true, true);
		}
	}

	@Override
	public void deselect() {
		super.deselect();
		if (!this.isAi()) {
			GameManager.get().getGui().showBuildMenu(this, false, true);
		}
	}

	/**
	 * Add gathered resource to unit's backpack
	 * @param resource
	 */
	public void addGatheredResource(GatheredResource resource) {
		this.gatheredResource = resource;
		//LOGGER.error("Gathered "+ resource.getAmount() + " units of "+ resource.getType());
	}
	
	/**
	 * check if the unit has resource in it's backpack
	 * @return true if this unit carries something
	 */
	public boolean checkBackpack() {
		return gatheredResource != null;
	}
	
	/**
	 * remove the resource from unit's backpack
	 * @return gatheredResource
	 */
	public GatheredResource removeGatheredResource() {
		GatheredResource resource = new GatheredResource (gatheredResource.getType(), gatheredResource.getAmount());
		//LOGGER.error("Removed "+ resource.getAmount() + " units of "+ resource.getType());
		gatheredResource = null;
		return resource;
	}
	
	/**
	 * Returns Astronaut as the name
	 * @return String name of Astronaut
	 */
	@Override
	public String toString(){
		return "Astronaut";
	}
	
	/**
	 * Causes the entity to perform the action
	 * @param action the action to perform
	 */
	@Override
	public void setAction(DecoAction action) {
		currentAction = Optional.of(action);
		if (action instanceof BuildAction) {
			build = (BuildAction)action;
		}
	}
	
	/**
	 * Gets the current build action of this entity
	 * @return current BuildAction, null if not currently building
	 */
	public BuildAction getBuild() {
		if (currentAction.isPresent()) {
			return build;
		}
		return null;
	}

	/**
	 * @return The stats of the entity
	 */
	public EntityStats getStats() {
		return new EntityStats("Astronaut", this.getHealth(),this.getMaxHealth(), null, this.getCurrentAction(), this);
	}
}
