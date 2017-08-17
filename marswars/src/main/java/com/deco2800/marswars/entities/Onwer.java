package com.deco2800.marswars.entities;

import com.deco2800.marswars.managers.Manager;

public interface Onwer {
	
	void setOnwer(Manager onwer);
	
	Manager getOnwer();
	
	boolean sameOnwer(AbstractEntity entity);
	
	boolean sameTeam(AbstractEntity enetity);

}
