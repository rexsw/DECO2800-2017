package com.deco2800.marswars.managers;

public class PlayerManager extends AbstractPlayerManager {
	/*
	 * currently mostly just to hold to the players entities
	 */
	private int teamid = 0;


	/**
	 * note team methods where a wip system and have been pushed back and as such
	 * are not used for now		
	 */
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
	
	@Override
	public String toString() {
		return "Player team - " + this.getColour();
	}

}
