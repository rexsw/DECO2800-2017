package com.deco2800.marswars.managers;

import com.deco2800.marswars.util.Array2D;

/**
 * Fog of War Manager
 * Initializes a 2D array that contains the vision state of all coordinates
 * 0 = unseen (black zone)
 * 1 = seen, but not currently in sight (grayed out)
 * 2 = In sight (normal vision)
 */
public class FogManager extends Manager {
	/**
	 * this array contains grayed-out fog of war
	 */
	private static Array2D<Integer> fogOfWar;

	/**
	 * this array contains black-out fog of war
	 */
	private static Array2D<Integer> blackFogOfWar;

	//this is fog of war activation status
	private static boolean activatedFog = true;


	/**
	 * Initializes the fogOfWar array when the game starts by setting all
	 * elements to 0.
	 *
	 * @param width  the width of the map
	 * @param length the length of the map
	 */
	public static void initialFog(int width, int length) {
		fogOfWar = new Array2D<Integer>(width, length);
		blackFogOfWar  = new Array2D<Integer>(width, length);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++) {
				fogOfWar.set(i, j, 0);
				blackFogOfWar.set(i,j,0);
			}
		}
	}

	/**
	 * Gets the fog value of a coordinate of the gray fog of war
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the fog value: 0 or 2
	 */
	public static int getFog(int x, int y) {
		return fogOfWar.get(x, y);
	}

	/**
	 * Gets the fog value of a coordinate of the black fog of war
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the fog value: 0 or 1
	 */

	public static int getBlackFog(int x, int y) {
		return blackFogOfWar.get(x, y);
	}

	/**
	 * Updates the fogOfWar based on the sight range of an entity.
	 *
	 * @param x     The x position of the entity
	 * @param y     The y position of the entity
	 * @param maxRange The sight range of the entity
	 * @param state deleting or creating new state for the fog of war map
	 */
	public static void sightRange(int x, int y, int maxRange, boolean state) {
		int w = fogOfWar. getWidth();
		int l = fogOfWar.getLength();
			if (state) {//set the new position on the map
				for(int i=-maxRange;i<=maxRange;i++){//for each row
					for(int j=0;j<=maxRange;j++) {//for each column
						if( x+j < w && y+i < l && y+i>=0){//to the right
							fogOfWar.set(x+j,y+i,2);
							blackFogOfWar.set(x+j,y+i,1);//reveal a tile
						}
						if( x-j >= 0 && y+i < l && y+i>=0){//to the right
							fogOfWar.set(x-j,y+i,2);
							blackFogOfWar.set(x-j,y+i,1);//reveal a tile
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
