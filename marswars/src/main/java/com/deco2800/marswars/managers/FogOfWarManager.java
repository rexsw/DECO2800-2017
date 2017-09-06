package com.deco2800.marswars.managers;
import java.util.List;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.util.Array2D;

/**
 * Fog of War Manager
 * Initializes a 2D array that contains the vision state of all coordinates
 * 0 = unseen (black zone)
 * 1 = seen, but not currently in sight (grayed out)
 * 2 = In sight (normal vision)
 */
public class FogOfWarManager extends Manager {
	private static Array2D<Integer> fogOfWar;
	private static boolean on;
	private int maxWidth;
	private int maxLength;
	private static boolean activatedFog = false;

	public static Array2D<Integer> getFogOfWar(){
		return fogOfWar;
	}

	/**
	 * Initializes the fogOfWar array when the game starts by setting all
	 * elements to 0.
	 *
	 * @param width  the width of the map
	 * @param length the length of the map
	 */
	public void initialFog(int width, int length) {
		this.fogOfWar = new Array2D<Integer>(width, length);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++) {
				fogOfWar.set(i, j, 0);
			}
		}
		this.on = true;
		this.maxWidth = width;
		this.maxLength = length;
	}

	/**
	 * Sets coordinate to a certain sight level.
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param s 0, 1 or 2
	 * @return true if set correctly and false otherwise
	 */
	public boolean setFog(int x, int y, int s) {
		if (s != 0 || s != 1 || s != 2) {
			return false;
		}
		fogOfWar.set(x, y, s);
		return true;
	}

	/**
	 * Gets the fog value of a coordinate
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the fog value: 0, 1 or 2
	 */
	public static int getFog(int x, int y) {
		return fogOfWar.get(x, y);
	}

	/**
	 * Updates the fogOfWar based on the sight range of an entity.
	 *
	 * @param x     The x position of the entity
	 * @param y     The y position of the entity
	 * @param maxRange The sight range of the entity
	 */
	public static void sightRange(int x, int y, int maxRange, boolean state) {
		int w = fogOfWar. getWidth();
		int l = fogOfWar.getLength();
		if (on) {
			if (state) {//set the new position on the map
				for(int i=-maxRange;i<=maxRange;i++){//for each row
					for(int j=0;j<=maxRange;j++) {//for each column
						if( x+j < w && y+i < l && y+i>=0){//to the right
							fogOfWar.set(x+j,y+i,2);
						}
						if( x-j >= 0 && y+i < l && y+i>=0){//to the right
							fogOfWar.set(x-j,y+i,2);
						}
					}

				}

			} else {//delete the old position on the map
				for(int i=-maxRange;i<=maxRange;i++){//for each row
					for(int j=0;j<=maxRange;j++) {//for each column
						if( x+j < w && y+i < l && y+i>=0){//to the right
							fogOfWar.set(x+j,y+i,0);
						}
						if( x-j >= 0  && y+i < l && y+i>=0){//to the right
							fogOfWar.set(x-j,y+i,0);
						}
					}
				}
			}
		}
	}

	/**
	 * this function will toggle the fog on or off depends on the parameter given
	 * @param status
	 */
	public static void toggleFog(boolean status) {
		activatedFog = status;
	}

	/**
	 * This function will return the status of the fog (on or off)
	 * @return
	 */
	public static boolean getToggleFog(){
		return activatedFog;
	}
	
}
