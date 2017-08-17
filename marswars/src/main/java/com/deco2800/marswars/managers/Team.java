package com.deco2800.marswars.managers;

public interface Team {
	
	void setTeam(int teamid);
	
	int getTeam();
	
	boolean sameTeam(Manager otherMember);

}
