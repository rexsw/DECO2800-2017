/**
 * 
 */
package com.deco2800.marswars.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.managers.GameManager;

/**
 * This class is for the resource on the map only, player's resource should use GatheredResouce class instead.
 * @author Mason
 *
 */
public class Resource extends BaseEntity implements HasHealth{
	private static final Logger LOGGER = LoggerFactory.getLogger(Resource.class);

	private ResourceType type;
	private final int AMOUNT_CAPACITY = 100;
	private final int HARVEST_CAPACITY = 5; // max 5 farmer at the same time
	
	private int reserves; // current reserves of this resource
	private int harvester = 0; // no. of harvester on the resource
	
	/**
	 * constructor for the resource class
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param height
	 * @param width
	 * @param type
	 */
	public Resource(float posX, float posY, float posZ, float height, float width, ResourceType type) {
		// I'm thinking of make the constructor method shorter, 
		// so for the constructor method, it only takes in type and size
		// and in a separate function, takes in the position to draw it on the map
		// what's your thought?
		super(posX, posY, posZ, height, width, 1f);

		switch (type) {
		case ROCK:
			this.setTexture("large_rock"); 
			break;
		case CRYSTAL:
			this.setTexture("large_crystal"); 
			break;
		case WATER:
			this.setTexture("large_water");
			break;
		case BIOMASS:
			this.setTexture("large_biomass");
			break;
		}
		this.canWalkOver = false; // i think resource shouldn't allow walk over
		this.setCost(10); // don't know what should this value be, may vary for different size, to be changed later
		this.type = type;
		this.reserves = AMOUNT_CAPACITY;
	}
	
	/**
	 * Update the storage station according to the reserves percentage
	 */
	public void updateStorageState() {
		switch (type) {
		case ROCK:
			if ((reserves * 100/AMOUNT_CAPACITY) <= 33) {
				this.setTexture("small_rock");
			} else if ((reserves * 100/AMOUNT_CAPACITY) <= 66) {
				this.setTexture("medium_rock");
			} 
			break;
		case CRYSTAL:
			if ((reserves * 100/AMOUNT_CAPACITY) <= 33) {
				this.setTexture("small_crystal");
			} else if ((reserves * 100/AMOUNT_CAPACITY) <= 66) {
				this.setTexture("medium_crystsal");
			} 
			break;
		case WATER:
			if ((reserves * 100/AMOUNT_CAPACITY) <= 33) {
				this.setTexture("small_water");
			} else if ((reserves * 100/AMOUNT_CAPACITY) <= 66) {
				this.setTexture("medium_water");
			} 
			break;
		case BIOMASS:
			if ((reserves * 100/AMOUNT_CAPACITY) <= 33) {
				this.setTexture("small_biomass");
			} else if ((reserves * 100/AMOUNT_CAPACITY) <= 66) {
				this.setTexture("medium_biomass");
			} 
			break;
		}	
		//remain the same as large size
	}

	/**
	 * Returns the number of units currently harvesting on the resource
	 * @return number of unit
	 */
	public int getHarvesterNumber() {
		return harvester;
	}
	
	/**
	 * Returns the max number of units can harvesting on the resource
	 * @return capacity of harvester
	 */
	public int getHarvesterCapacity() {
		return HARVEST_CAPACITY;
	}
	
	/**
	 * Sets the number of units currently harvesting on the resource. Does 
	 * nothing if value being set is not between 0 and HARVEST_CAPACITY 
	 * (inclusive).
	 * @param value The number of units currently harvesting the resource.
	 */
	public void setHarvestNumber(int value) {
		if ((value <= HARVEST_CAPACITY) && (value >= 0)) {
			LOGGER.error("Setting " + type + " harvester count to " + 
					value);
			harvester = value;
		}
	}
	
	/**
	 * Returns the type of the resource
	 * @return ResourceType
	 */
	public ResourceType getType() {
		return type; 
	}
	
	public String testResource() {
		switch (type) {
		case ROCK:
			return "rock";
		case CRYSTAL:
			return "crystal";
		case WATER:
			return "water";
		case BIOMASS:
			return "biomass";
		}
		return null;
	}
	
	/**
	 * Returns the current reserves of the resource
	 * @return current reserves
	 */
	@Override
	public int getHealth() {
		return (int) reserves;
	}

	/**
	 * Sets the amount of resources left to a value.	
	 * @param health The number of resource left in the resource
	 */
	@Override
	public void setHealth(int health) {
		if (health < AMOUNT_CAPACITY) {
			LOGGER.error("Setting " + type + " reserves to " + health);
			if (health <= 0) {
				LOGGER.error("Resource drained!");
				GameManager.get().getWorld().removeEntity(this);
			}
			reserves = health;
		}
		
	}
}
