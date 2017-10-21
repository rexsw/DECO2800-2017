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
	 * this function will return the whole array for saving the game
	 */
	public static Array2D<Integer> getFog(){
		return fogOfWar;
	}

	/**
	 * this array contains black-out fog of war
	 */
	private static Array2D<Integer> blackFogOfWar;


	/**
	 * this function will return the whole array for saving the game
	 */
	public static Array2D<Integer> getBlackFog(){
		return blackFogOfWar;
	}


	/**
	 * this flag called activatedFog helps to turn on or off the fog of war
	 */
	private static boolean activatedFog = true;


	/**
	 * Initializes the fogOfWar array when the game starts by setting all
	 * elements to 0.
	 *
	 * @param width  the width of the map
	 * @param length the length of the map
	 */
	public static void initialFog(int width, int length) {
		if (width < 1 || length < 1) {
			throw new IllegalArgumentException();
		}
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
		if (x >= GameManager.get().getWorld().getWidth()) {
			x = GameManager.get().getWorld().getWidth() - 1;
		}else if(x < 0){
			x=0;
		}

		if (y >= GameManager.get().getWorld().getLength()) {
			y = GameManager.get().getWorld().getLength() - 1;
		}else if(y<0){
			y=0;
		}
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

		if (x >= GameManager.get().getWorld().getWidth()) {
			x = GameManager.get().getWorld().getWidth() - 1;
		}else if(x < 0){
			x=0;
		}

		if (y >= GameManager.get().getWorld().getLength()) {
			y = GameManager.get().getWorld().getLength() - 1;
		}else if(y<0){
			y=0;
		}

		return blackFogOfWar.get(x, y);
	}

	/**
	 * this function set the fog of war array when the game is loaded
	 * @param newFogOfWar
	 */
	public static void setFog(Array2D<Integer> newFogOfWar){
		fogOfWar = newFogOfWar;
	}

	/**
	 * this function set the fog of war array when the game is loaded
	 * @param newBlackFogOfWar
	 */
	public static void setBlackFog(Array2D<Integer> newBlackFogOfWar){
		blackFogOfWar = newBlackFogOfWar;
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
		if (fogOfWar == null) {
			return;
		}
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
