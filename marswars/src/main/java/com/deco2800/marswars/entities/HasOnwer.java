package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.managers.Manager;

public interface HasOnwer {
	
	void setOnwer(Manager onwer);
	
	Manager getOnwer();
	
	boolean sameOnwer(AbstractEntity entity);
	
	public boolean isWorking();
	
	public void setAction(DecoAction action);

}
