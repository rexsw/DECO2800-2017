/**
 * 
 */
package com.deco2800.marswars.entities.resources;

/**
 * This class used for two purpose,
 * 1. used for civilisation's stock pile
 * 2. used for resource harvest by a farm unit
 * @author Mason
 *
 */
public class GatheredResource {
	private int reserves;
	private ResourceType type;
	
	/**
	 * constructor for the GatheredResource class
	 * @param type
	 * @param numbOfResource
	 */
	GatheredResource(ResourceType type, int numbOfResource) {
		this.type = type;
		this.reserves = numbOfResource;
	}
	
	/**
	 * Call this function when add resource to the stock pile
	 */
	public void addResource(GatheredResource resource) {
		if (this.type != resource.type) {
			return; // throw something here? error?
		}
		reserves += resource.getResource();
	}
	
	/**
	 * Call this function when trying to use a resource
	 */
	public void useResource(GatheredResource resource) {
		if (this.type != resource.type) {
			return; // throw something here? error?
		}
		reserves -= resource.getResource(); 
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
	public int getResource() {
		return reserves;
	}
}
