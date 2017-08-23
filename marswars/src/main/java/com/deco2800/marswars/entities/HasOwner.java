package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.managers.Manager;

public interface HasOwner {
	
	/**
	 * Sets the Owner of an entity, flagging which manager should control this 
	 * entity
	 * 
	 * @param Manager The manager to be set for the entity
	 */
	void setOwner(Manager owner);
	
	/**
	 * returns the entities current manager or null if the entity doesn't have 
	 * one
	 * 
	 * @return Manager the manager set for the entity
	 */	
	Manager getOwner();
	
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
	public boolean isWorking();
	
	/**
	 * sets the current decoaction of an entity, if applicable  
	 * 
	 * @param DecoAction the action to give the entity
	 */	
	public void setAction(DecoAction action);

}
