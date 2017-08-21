/**
 * 
 */
package com.deco2800.marswars.entities;

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
	 * @param numbOfResource
	 */
	public GatheredResource(ResourceType type, int amount) {
		this.type = type;
		this.amount = amount;
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