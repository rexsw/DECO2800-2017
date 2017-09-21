package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;
/**
 * Created by Scott Whittington on 17/08
 * enables the ai to use other classes by giving them methods for the ai to use
 */
public interface HasOwner {
	
	/**
	 * Sets the Owner of an entity, flagging which manager should control this 
	 * entity
	 * 
	 * @param int The manager id to be set for the entity
	 */
	void setOwner(int owner);
	
	/**
	 * returns the entities current manager or null if the entity doesn't have 
	 * one
	 * 
	 * @return int the manager id set for the entity
	 */	
	int getOwner();
	
	/**
	 * tests if two entities have the same manager return true if they do else 
	 * false
	 * 
	 * @param AbstractEntity the entity to compare to
	 * @return boolean true iff both entities have the same manager
	 */		
	boolean sameOwner(AbstractEntity entity);
	
	/**
	 * tests if a entity has a current decoaction, if applicable
	 * 
	 * @return boolean or null true iff entities has a current action, 
	 * null if the entity has no decoaction field
	 */	
	public void setAction(DecoAction action);
	
	/**
	 * test is the entity is controlled by ai ie owner id >= 1
	 * 
	 * @return true iif this entity is controlled by an ai
	 */
	public boolean isAi();

}
