package com.deco2800.marswars.managers;

public class AbstractPlayerManager extends Manager implements HasTeam{

	private int teamId;
	// I expect we will need some stuff in here at some point.
	
	private String colour;
	
	public String getColour(){
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
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
