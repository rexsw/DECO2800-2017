/**
 * 
 */
package com.deco2800.marswars.entities.resources;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.GameManager;

/**
 * This class is for the resource on the map only, player's resource should use GatheredResouce class instead.
 * @author Mason
 *
 */
public class Resource extends BaseEntity {
	private ResourceType type;
	private ResourceSize size;
	private final double LARGE_SIZE = 5000.00;
	private final double MEDIUM_SIZE = 3000.00;
	private final double SMALL_SIZE = 1000.00;
	private final int HARVEST_CAPACITY = 5; // max 5 farmer at the same time
	private int harvestSpeed = 2000; // this value can be improved with tech tree? if so, this value should a para belongs to the unit
	private final int HARVEST_AMOUNT = 10;
	
	private double reserves; // current reserves of this resource
	private double capacity; // max capacity of the resource
	private int harvester = 0; // no. of harvester on the resource
	
	/**
	 * constructor for the resource class
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param xLength
	 * @param yLength
	 * @param zLength
	 * @param type
	 * @param size
	 */
	public Resource(float posX, float posY, float posZ, float xLength,
			float yLength, float zLength, ResourceType type, ResourceSize size) {
		// I'm thinking of make the constructor method shorter, 
		// so for the constructor method, it only takes in type and size
		// and in a separate function, takes in the position to draw it on the map
		// what's your thought?
		super(posX, posY, posZ, xLength, yLength, zLength);
		this.canWalkOver = false; // i think resource shouldn't allow walk over
		this.setCost(10); // don't know what should this value be, may vary for different size, to be changed later
		this.type = type;
		this.size = size;
		switch(type) {
		case WATER:
			// different size for water should be considered here
			switch(size) {
			case SMALL:
				this.setTexture("smallWater"); 
				this.reserves = SMALL_SIZE;
				this.capacity = SMALL_SIZE;
				break;
			case MEDIUM:
				this.setTexture("mediumWater"); 
				this.reserves = MEDIUM_SIZE;
				this.capacity = MEDIUM_SIZE;
				break;
			case LARGE:
				this.setTexture("largeWater"); 
				this.reserves = LARGE_SIZE;
				this.capacity = LARGE_SIZE;
				break;
			}
		case ROCK:
			switch(size) {
			case SMALL:
				this.setTexture("smallRock"); 
				this.reserves = SMALL_SIZE;
				this.capacity = SMALL_SIZE;
				break;
			case MEDIUM:
				this.setTexture("mediumRock"); 
				this.reserves = MEDIUM_SIZE;
				this.capacity = MEDIUM_SIZE;
				break;
			case LARGE:
				this.setTexture("largeRock"); 
				this.reserves = LARGE_SIZE;
				this.capacity = LARGE_SIZE;
				break;
			}
		case CRYSTAL:
			switch(size) {
			case SMALL:
				this.setTexture("smallCrystal"); 
				this.reserves = SMALL_SIZE;
				this.capacity = SMALL_SIZE;
				break;
			case MEDIUM:
				this.setTexture("mediumCrystal"); 
				this.reserves = MEDIUM_SIZE;
				this.capacity = MEDIUM_SIZE;
				break;
			case LARGE:
				this.setTexture("largeCrystal"); 
				this.reserves = LARGE_SIZE;
				this.capacity = LARGE_SIZE;
				break;
			}
		case BIOMASS:
			switch(size) {
			case SMALL:
				this.setTexture("smallBiomass"); 
				this.reserves = SMALL_SIZE;
				this.capacity = SMALL_SIZE;
				break;
			case MEDIUM:
				this.setTexture("mediumBiomass"); 
				this.reserves = MEDIUM_SIZE;
				this.capacity = MEDIUM_SIZE;
				break;
			case LARGE:
				this.setTexture("largeBiomass"); 
				this.reserves = LARGE_SIZE;
				this.capacity = LARGE_SIZE;
				break;
			}
		}
	}

	/**
	 * Returns the current reserves of the resource
	 * @return current reserves
	 */
	public int getReserve() {
		return (int) reserves;
	}
	
	/**
	 * Returns the current reserves percentage of the resource
	 * @return reserve percentage
	 */
	public int getReservePercentage() {
		return (int) (reserves/capacity * 100);
	}

	/**
	 * Returns the number of units currently harvesting on the resource
	 * @return number of unit
	 */
	public int getHarvestNumber() {
		return harvester;
	}
	
	/**
	 * When a unit come to harvest the resource. Number of unit harvesting the resource shouldn't exceed the capacity
	 * Unit run away with gathered resource
	 * @return GatheredResource
	 */
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
}
