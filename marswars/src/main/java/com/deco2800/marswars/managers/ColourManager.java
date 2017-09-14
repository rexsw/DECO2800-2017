package com.deco2800.marswars.managers;

import java.util.HashMap;
import java.util.Map;

public class ColourManager extends Manager {
	private Map<Integer, String> colours = new HashMap<Integer, String>();
	
	public void setColour(int teamid, Colours Colour) {
		switch(Colour){
			case BLUE:
				colours.put(teamid, "Blue");
				break;
			case YELLOW:
				colours.put(teamid, "Yellow");
				break;
			case PINK:
				colours.put(teamid, "Pink");
				break;
			case PURPLE:
				colours.put(teamid, "Purple");
				break;
			case GREEN:
				colours.put(teamid, "Green");
				break;
			default:
				break;
		}
	}
	
	public String getColour(int teamid) {
		return colours.get(teamid);
	}
	
	
}
