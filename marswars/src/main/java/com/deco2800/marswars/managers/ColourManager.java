package com.deco2800.marswars.managers;

import java.util.HashMap;
import java.util.Map;

public class ColourManager extends Manager {
	private Map<Integer, String> colours = new HashMap<Integer, String>();
	
	enum Colours {
		BLUE,
		YELLOW,
		PINK,
		PURPLE,
		GREEN,
	}
	
	public void setColour(int teamid, String Colour) {
		colours.put(teamid, Colour);
	}
	
	public String getColour(int teamid) {
		return colours.get(teamid);
	}
	
	
}
