package com.deco2800.marswars.managers;

public interface HasTeam {
	
	void setTeam(int teamid);
	
	int getTeam();
	
	boolean sameTeam(Manager otherMember);

}
