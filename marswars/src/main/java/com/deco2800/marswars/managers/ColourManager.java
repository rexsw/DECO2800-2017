package com.deco2800.marswars.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Saves a mapping of colours to teams in a safe way ensuring that it can be used to
 * load files correctly 
 * 
 * @author Scott Whittington
 *
 */
public class ColourManager extends Manager {
	private Map<Integer, String> colours = new HashMap<Integer, String>();
	private ArrayList<Colours> colour = setColour();
	private int index = 0;
	
	/**
	 * maps a team to a colour only different for the first five mappings 
	 * 
	 * @param teamid int the teamid to map to a colour
	 */
	public void setColour(int teamid) {
		Colours teamcolour = colour.get(index);
		index++;
		index %= 5;
		colours.put(teamid, teamcolour.toString());
	}
	
	/**
	 * get the colour a team is mapped to
	 * 
	 * @ensure the teamid has already been mapped i.e .setColour(teamid) has been
	 * called at some point
	 * @param teamid the team to get the colour of
	 * @return String the colour a team is mapped to
	 */
	public String getColour(int teamid) {
		return colours.get(teamid);
	}
	
	/**
	 * a helper method to set a list of vaild colours for mapping
	 * 
	 * @return ArrayList<Colours> a list of vaild colours
	 */
	private ArrayList<Colours> setColour(){
		ArrayList<Colours> list = new ArrayList<Colours>();
		list.add(Colours.BLUE);
		list.add(Colours.YELLOW);
		list.add(Colours.PINK);
		list.add(Colours.GREEN);
		list.add(Colours.PURPLE);
		return list;
	}
}
