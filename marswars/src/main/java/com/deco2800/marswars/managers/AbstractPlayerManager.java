package com.deco2800.marswars.managers;

import java.util.ArrayList;
import java.util.List;

public class AbstractPlayerManager extends Manager implements HasTeam{

	private int teamId;
	// I expect we will need some stuff in here at some point.
	
	private List<String> colour = new ArrayList<String>();
	
	public String getColour(int teamid){
		return colour.get(teamid);
	}

	public void setColour(String colour, int teamid) {
		this.colour.set(teamid, colour);
	}
	
	@Override
	public void setTeam(int teamId) {
		this.teamId = teamId;
	}
	
	@Override
	public int getTeam() {
		return teamId;
	}
	
	@Override
	public boolean sameTeam(Manager otherMember) {
		if(otherMember instanceof HasTeam) {
			return this.teamId == ((HasTeam) otherMember).getTeam();
		} else {
			return false;
		}
	}

}
