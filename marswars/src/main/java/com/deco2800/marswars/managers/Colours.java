package com.deco2800.marswars.managers;

/**
 * Colours that are valid for the in game spites
 * 
 * @author Scott Whittington
 *
 */
public enum Colours {
	BLUE, YELLOW, PINK, PURPLE, GREEN, RED;

	
	@Override
	/**
	 * Tostring the colours to format that can be used by the texture manager
	 * 
	 * @return String the string reposition of colour
	 * 
	 */
	public String toString() {
		// only capitalize the first letter to work with the file formate
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}
}
