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
	private ResourceSize size;
	private final int LARGE_SIZE = 5000;
	private final int MEDIUM_SIZE = 3000;
	private final int SMALL_SIZE = 1000;
	private final int HARVEST_CAPACITY = 5; // max 5 farmer at the same time
	
	private int reserves; // current reserves of this resource
	private int capacity; // max capacity of the resource
	private int harvester = 0; // no. of harvester on the resource
	
	/**
	 * constructor for the resource class
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param height
	 * @param width
	 * @param type
	 * @param size
	 */
	public Resource(float posX, float posY, float posZ, float height, float width, ResourceType type, ResourceSize size) {
		// I'm thinking of make the constructor method shorter, 
		// so for the constructor method, it only takes in type and size
		// and in a separate function, takes in the position to draw it on the map
		// what's your thought?
		super(posX, posY, posZ, height, width, 1f);
		
		if (type == ResourceType.ROCK) {
			if(size == ResourceSize.SMALL) {
				this.setTexture("small_rock"); 
				this.reserves = SMALL_SIZE;
				this.capacity = SMALL_SIZE;
			} else if(size == ResourceSize.MEDIUM) {
				this.setTexture("medium_rock"); 
				this.reserves = MEDIUM_SIZE;
				this.capacity = MEDIUM_SIZE;
			} else {
				this.setTexture("large_rock"); 
				this.reserves = LARGE_SIZE;
				this.capacity = LARGE_SIZE;
			}
		} else if (type == ResourceType.CRYSTAL) {
			if(size == ResourceSize.SMALL) {
				this.setTexture("small_crystal"); 
				this.reserves = SMALL_SIZE;
				this.capacity = SMALL_SIZE;
			} else if(size == ResourceSize.MEDIUM) {
				this.setTexture("medium_crystal"); 
				this.reserves = MEDIUM_SIZE;
				this.capacity = MEDIUM_SIZE;
			} else {
				this.setTexture("large_crystal"); 
				this.reserves = LARGE_SIZE;
				this.capacity = LARGE_SIZE;
			}
		} else if (type == ResourceType.WATER) {
			if(size == ResourceSize.SMALL) {
				this.setTexture("small_water"); 
				this.reserves = SMALL_SIZE;
				this.capacity = SMALL_SIZE;
			} else if(size == ResourceSize.MEDIUM) {
				this.setTexture("medium_water"); 
				this.reserves = MEDIUM_SIZE;
				this.capacity = MEDIUM_SIZE;
			} else {
				this.setTexture("large_water"); 
				this.reserves = LARGE_SIZE;
				this.capacity = LARGE_SIZE;
			}
		} else if (type == ResourceType.BIOMASS) {
			if(size == ResourceSize.SMALL) {
				this.setTexture("small_biomass"); 
				this.reserves = SMALL_SIZE;
				this.capacity = SMALL_SIZE;
			} else if(size == ResourceSize.MEDIUM) {
				this.setTexture("medium_biomass"); 
				this.reserves = MEDIUM_SIZE;
				this.capacity = MEDIUM_SIZE;
			} else {
				this.setTexture("large_biomass"); 
				this.reserves = LARGE_SIZE;
				this.capacity = LARGE_SIZE;
			}
		}
		this.canWalkOver = false; // i think resource shouldn't allow walk over
		this.setCost(10); // don't know what should this value be, may vary for different size, to be changed later
		this.type = type;
		this.size = size;
	}
	
	/**
	 * Returns the current reserves percentage of the resource
	 * @return reserve percentage
	 */
	public int getReservePercentage() {
		return (reserves * 100/capacity );
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
			System.err.println("Setting " + type + " harvester count to " + 
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
	
	/**
	 * Returns the size of the resource
	 * @return ResourceSize
	 */
	public ResourceSize getSize() {
		return size; 
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
		if (health < capacity) {
			System.err.println("Setting " + type + " reserves to " + health);
			if (health <= 0) {
				GameManager.get().getWorld().removeEntity(this);
			}
			reserves = health;
		}
		
	}
}
