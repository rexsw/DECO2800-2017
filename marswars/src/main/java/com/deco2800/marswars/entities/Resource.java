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
	private int harvestSpeed = 2000; // this value can be improved with tech tree? if so, this value should a para belongs to the unit
	private final int HARVEST_AMOUNT = 10;
	
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
//		switch(type) {
//		case WATER:
//			// different size for water should be considered here
//			switch(size) {
//			case SMALL:
//				this.setTexture("small_water"); 
//				this.reserves = SMALL_SIZE;
//				this.capacity = SMALL_SIZE;
//				break;
//			case MEDIUM:
//				this.setTexture("medium_water"); 
//				this.reserves = MEDIUM_SIZE;
//				this.capacity = MEDIUM_SIZE;
//				break;
//			case LARGE:
//				this.setTexture("large_water"); 
//				this.reserves = LARGE_SIZE;
//				this.capacity = LARGE_SIZE;
//				break;
//			}
//			break;
//		case ROCK:
//			switch(size) {
//			case SMALL:
//				this.setTexture("small_rock"); 
//				this.reserves = SMALL_SIZE;
//				this.capacity = SMALL_SIZE;
//				break;
//			case MEDIUM:
//				this.setTexture("medium_rock"); 
//				this.reserves = MEDIUM_SIZE;
//				this.capacity = MEDIUM_SIZE;
//				break;
//			case LARGE:
//				this.setTexture("large_rock"); 
//				this.reserves = LARGE_SIZE;
//				this.capacity = LARGE_SIZE;
//				break;
//			}
//			break;
//		case CRYSTAL:
//			switch(size) {
//			case SMALL:
//				this.setTexture("small_crystal"); 
//				this.reserves = SMALL_SIZE;
//				this.capacity = SMALL_SIZE;
//				break;
//			case MEDIUM:
//				this.setTexture("medium_crystal"); 
//				this.reserves = MEDIUM_SIZE;
//				this.capacity = MEDIUM_SIZE;
//				break;
//			case LARGE:
//				this.setTexture("large_crystal"); 
//				this.reserves = LARGE_SIZE;
//				this.capacity = LARGE_SIZE;
//				break;	
//			}
//			break;
//		case BIOMASS:
//			switch(size) {
//			case SMALL:
//				this.setTexture("small_biomass"); 
//				this.reserves = SMALL_SIZE;
//				this.capacity = SMALL_SIZE;
//				break;
//			case MEDIUM:
//				this.setTexture("medium_biomass"); 
//				this.reserves = MEDIUM_SIZE;
//				this.capacity = MEDIUM_SIZE;
//				break;
//			case LARGE:
//				this.setTexture("large_biomass"); 
//				this.reserves = LARGE_SIZE;
//				this.capacity = LARGE_SIZE;
//				break;
//			}
//			break;
//		}
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
	public int getHarvestNumber() {
		return harvester;
	}
	
	/**
	 * Sets the number of units currently harvesting on the resource. Does 
	 * nothing if value being set is not between 0 and HARVEST_CAPACITY 
	 * (inclusive).
	 * @param value The number of units currently harvesting the resource.
	 */
	public void setHarvestNumber(int value) {
		if ((value < HARVEST_CAPACITY) && (value >= 0)) {
			System.err.println("Setting " + type + " harvester count to " + 
					value);
			harvester = value;
		}
	}
	
	
	/**
	 * When a unit come to harvest the resource. Number of unit harvesting the resource shouldn't exceed the capacity
	 * Unit run away with gathered resource
	 * @return GatheredResource
	 */
	 
	/*
	 * I feel like the below function would be handled somewhere else like in 
	 * the actions package because if you look at GatherAction, you see that
	 * the stuff for a unit harvesting stuff is there, but doesn't quote fit 
	 * with what we have here. And to do the speed part we need to wait for the
	 * people doing the entities to make like a speed property there. if not on
	 * the unit then in a Manager class somewhere (because more efficiency in 
	 * storage and less stuff to update if something is upgraded).
	public GatheredResource harvestResource(Object farmUnit) {
		if (harvester >= HARVEST_CAPACITY) {
			return new GatheredResource(type, 0); // throw message that it's exceed the max capacity
		} 
		harvester++;	
		// I'm thinking of get this unit disappear then appear after finish harvest
		// unit disappear depend on the unit's harvest speed
		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						// I used a timer here for the harvest action, the idea is 
						// just let the unit stop for some time to make it more realistic
						// any better implement than a timer task?
					}
				}			
				, harvestSpeed);
		// now the unit has got the resource
		harvester--;
		if(HARVEST_AMOUNT >= reserves) { // no enough resource left
			int harvestAmount = (int) reserves;
			reserves = 0;
			GameManager.get().getWorld().removeEntity(this); // remove this resource
			return new GatheredResource(type, harvestAmount);
		} else {
			reserves -= HARVEST_AMOUNT;
			return new GatheredResource(type, HARVEST_AMOUNT);
		}
		
		
	}
	*/
	
	/**
	 * Returns the type of the resource
	 * @return ResourceType
	 */
	public ResourceType getType() {
		return type; // is enum mutable or immutable? need confirmation here
	}
	
	/**
	 * Returns the size of the resource
	 * @return ResourceSize
	 */
	public ResourceSize getSize() {
		return size; // is enum mutable or immutable? need confirmation here
	}

	
	/**
	 * Returns the current reserves of the resource
	 * @return current reserves
	 */
	@Override
	public int getHealth() {
		// TODO Auto-generated method stub
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
