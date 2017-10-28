package com.deco2800.marswars.managers;

import com.badlogic.gdx.graphics.Color;

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
	private Map<Integer, Colours> colours = new HashMap<Integer, Colours>();
	private ArrayList<Colours> colour = setColour();
	private int index = 0;

	/**
	 * return index to save game
	 */
	public int getIndex(){
		return index;
	}

	/**
	 *
	 * set index for save game
	 */
	public void setIndex(int index){
		this.index = index;
	}
	
	/**
	 * maps a team to a colour only different for the first five mappings 
	 * 
	 * @param teamid int the teamid to map to a colour
	 */
	public void   setColour(int teamid) {
		Colours teamcolour = colour.get(index);
		index++;
		index %= 5;
		colours.put(teamid, teamcolour);
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
		if(colours.containsKey(teamid)) {
			return colours.get(teamid).toString();
		} else {
			return Colours.RED.toString();
		}
	}
	
	public Color getLibColour(int teamid) {
		Colours teamcolour = colours.get(teamid);
		switch(teamcolour){
			case BLUE:
				return Color.BLUE;
			case RED:
				return Color.RED;
			case YELLOW:
				return Color.YELLOW;
			case PINK:
				return Color.PINK;
			case PURPLE:
				return Color.PURPLE;
			case GREEN:
				return Color.GREEN;

		}
		return null;
	}
	
	/**
	 * a helper method to set a list of vaild colours for mapping
	 * 
	 * @return ArrayList<Colours> a list of vaild colours
	 */
	private ArrayList<Colours> setColour(){
		ArrayList<Colours> list = new ArrayList<Colours>();
		list.add(Colours.RED);
		list.add(Colours.BLUE);
		list.add(Colours.YELLOW);
		list.add(Colours.PINK);
		list.add(Colours.GREEN);
		list.add(Colours.PURPLE);
		return list;
	}
}
