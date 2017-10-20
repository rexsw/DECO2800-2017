/**
 * 
 */
package com.deco2800.marswars.entities.terrainelements;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.HasHealth;
import com.deco2800.marswars.managers.GameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is for the resource on the map only, player's resource should use GatheredResouce class instead.
 * @author Mason
 *
 */
public class Resource extends BaseEntity implements HasHealth{
	private static final Logger LOGGER = LoggerFactory.getLogger(Resource.class);

	private ResourceType type;
	private static final int AMOUNT_CAPACITY = 100;
	private static final int HARVEST_CAPACITY = 5; // max 5 farmer at the same time
	
	private int reserves; // current reserves of this resource
	private int harvester = 0; // no. of harvester on the resource

	//NEVER DELETE THIS
	public Resource(){}
	
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
		default:
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
	private void updateStorageState() {
		switch (type) {
		case ROCK:
			resetTexture("small_rock", "medium_rock");
			break;
		case CRYSTAL:
			resetTexture("small_crystal", "medium_crystal");
			break;
		default:
			resetTexture("small_biomass", "medium_biomass");
			break;
		}	
		//remain the same as large size
	}

	/**
	 * Set the display to small texture or medium texture
	 * @param small
	 * @param medium
	 */
	private void resetTexture(String small, String medium) {
		if ((reserves * 100/AMOUNT_CAPACITY) < 33) {
			this.setTexture(small);
		} else if ((reserves * 100/AMOUNT_CAPACITY) < 66) {
			this.setTexture(medium);
		} 
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
	
	/**
	 * Returns the current reserves of the resource
	 * @return current reserves
	 */
	@Override
	public int getHealth() {
		return reserves;
	}

	/**
	 * Sets the amount of resources left to a value.
	 * Also update the depletion state
	 * @param health The number of resource left in the resource
	 */
	@Override
	public void setHealth(int health) {
		if (health < AMOUNT_CAPACITY) {
			LOGGER.error("Setting " + type + " reserves to " + health);
			if (health <= 0) {
				LOGGER.error("Resource drained!");
				GameManager.get().getWorld().removeEntity((BaseEntity) this);
			}
			reserves = health;
			updateStorageState(); // update the depletion state
		}	
	}
}
