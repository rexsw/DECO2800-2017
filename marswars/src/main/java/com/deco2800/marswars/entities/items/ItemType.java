package com.deco2800.marswars.entities.items;

/**
 * Interface for Item type enumerate classes which specify the meta data for specific items (regardless of item types).
 * These enumerate classes that will implement this interface will store data such as the item's name, the item's
 * texture string
 * @author Mason
 *
 */
public interface ItemType {
	/**
	 * Method to get the name of the item that is defined in the enumerate tuple.
	 * 
	 * @return String that is the name of the item defined in the enumerate tuple.
	 */
	String getName();

	
	/**
	 * Method to get the saved texture string of the image file to be used as the item's icon defined in the enumerate 
	 * tuple.
	 * 
	 * @return saved texture string of the item icon in string format
	 */
	String getTextureString();
	
	/**
	 * Gets the cost of the item defined in the enumerate tuple as an integer array. 
	 * 
	 * @return array of integers representing the cost to buy/upgrade the item. The order of the resources in the array
	 * is [rocks, crystals, water, biomass]. 
	 */
	int[] getCost();
	
	/**
	 * Gets the description defined in the enumerate tuple. 
	 * 
	 * @return the description string
	 */
	String getDescription();
	
	/**
	 * Gets the build cost of this item in String format
	 * 
	 * @return the string presentation of item cost
	 */
	String getCostString();
}
