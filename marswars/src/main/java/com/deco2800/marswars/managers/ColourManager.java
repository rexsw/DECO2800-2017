package com.deco2800.marswars.managers;

import java.util.HashMap;
import java.util.Map;

public class ColourManager extends Manager {
	private Map<Integer, String> colours = new HashMap<Integer, String>();
	
	public void setColour(int teamid, Colours Colour) {
		switch(Colour){
			case BLUE:
				colours.put(teamid, "Blue");
			case YELLOW:
				colours.put(teamid, "Yellow");
			case PINK:
				colours.put(teamid, "Pink");
			case PURPLE:
				colours.put(teamid, "Purple");
			case GREEN:
				colours.put(teamid, "Green");
		}
	}
	
	public String getColour(int teamid) {
		return colours.get(teamid);
	}
	
	
}
