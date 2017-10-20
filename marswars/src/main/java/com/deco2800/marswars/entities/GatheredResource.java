/**
 * 
 */
package com.deco2800.marswars.entities;

import com.deco2800.marswars.entities.terrainelements.ResourceType;

/**
 * This class is used for resource carried by a unit (farmer)
 * @author Mason
 *
 */
public class GatheredResource {
	private int amount;
	private ResourceType type;
	
	/**
	 * constructor for the GatheredResource class
	 * @param type
	 * @param amount
	 */
	public GatheredResource(ResourceType type, int amount) {
		this.amount = amount < 0 ? 0: amount;
		this.type = type;
	}
	
	/**
	 * Returns the type of the resource
	 * @return ResourceType
	 */
	public ResourceType getType() {
		return type; // is enum mutable or immutable? need confirmation here
	}
	
	/**
	 * Returns the storage of the resource
	 * @return integer
	 */
	public int getAmount() {
		return amount;
	}
}
