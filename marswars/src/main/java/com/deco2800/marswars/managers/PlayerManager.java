package com.deco2800.marswars.managers;

public class PlayerManager extends Manager implements HasTeam {
	/*
	 * currently mostly just to hold to the players entities
	 */
	private int teamid = 0;
	//the colour of the sprites associated with the team, used by 
	//TextureManager to load the correct sprite
	
	
	public String getColour(){
		return colour;
	}
	@Override
	public void setTeam(int teamid) {
		this.teamid = teamid;
	}

	@Override
	public int getTeam() {
		return teamid;
	}

	@Override
	public boolean sameTeam(Manager otherMember) {
		if(otherMember instanceof HasTeam) {
			return this.teamid == ((HasTeam) otherMember).getTeam();
		} else {
			return false;
		}
	}

}
