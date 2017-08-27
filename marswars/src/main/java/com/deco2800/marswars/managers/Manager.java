package com.deco2800.marswars.managers;

/**
 * :tumbleweed:
 */
public abstract class Manager {
	private String team;
	// I expect we will need some stuff in here at some point.
	
	private String colour;
	
	public String getColour(){
		return colour;
	}
	public String getTeam(){
		return team;
	};

}
