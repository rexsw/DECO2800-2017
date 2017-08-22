package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.managers.Manager;

public interface HasOwner {
	
	void setOwner(Manager owner);
	
	Manager getOwner();
	
	boolean sameOwner(AbstractEntity entity);
	
	public boolean isWorking();
	
	public void setAction(DecoAction action);

}
