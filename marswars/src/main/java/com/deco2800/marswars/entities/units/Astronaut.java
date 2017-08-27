package com.deco2800.marswars.entities.units;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.GatheredResource;
import com.deco2800.marswars.entities.Resource;
import com.deco2800.marswars.managers.AbstractPlayerManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.Manager;
import com.deco2800.marswars.managers.SoundManager;
import com.deco2800.marswars.worlds.BaseWorld;

/**
 * A combat unit that can gather resources
 * @author Tze Thong Khor
 *
 */
public class Astronaut extends Soldier {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Astronaut.class);
	private GatheredResource gatheredResource = null;

	public Astronaut(float posX, float posY, float posZ, AbstractPlayerManager owner) {
		super(posX, posY, posZ, owner);
		// set all the attack attributes
		this.addNewAction(ActionType.GATHER);
		this.setMaxHealth(100);
		this.setHealth(100);
		this.setDamage(50);
		this.setArmor(70);
		this.setArmorDamage(10);
		this.setAttackRange(1);
		this.setAttackSpeed(10);
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
			this.setCurrentAction((Optional.of(new MoveAction((int) x, (int) y, this))));
			LOGGER.error("Assigned action move to" + x + " " + y);
		}
		this.setTexture(defaultTextureName);
		SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
		sound.playSound(movementSound);
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
}
