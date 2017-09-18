package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.EntityStats;
import com.deco2800.marswars.entities.GatheredResource;
import com.deco2800.marswars.entities.TerrainElements.Resource;
import com.deco2800.marswars.managers.GameManager;
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

	public Astronaut(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		this.name = "Astronaut";
		setAttributes();
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
			return;
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
		GameManager.get().getGui().showEntitiesPicker(true, true);

	}

	@Override
	public void deselect() {
		super.deselect();
		GameManager.get().getGui().showEntitiesPicker(false, true);

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
	
	@Override
	public String toString(){
		return "Astronaut";
	}
	
	/**
	 * @return The stats of the entity
	 */
	public EntityStats getStats() {
		return new EntityStats("Astronaut", this.getHealth(), null, this.getCurrentAction(), this);
	}
}
