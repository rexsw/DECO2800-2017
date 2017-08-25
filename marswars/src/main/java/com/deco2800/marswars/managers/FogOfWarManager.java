package com.deco2800.marswars.managers;
import com.deco2800.marswars.util.Array2D;


/**
 * Fog of War Manager
 * Initializes a 2D array that contains the vision state of all coordinates
 * 0 = unseen (black zone)
 * 1 = seen, but not currently in sight (grayed out)
 * 2 = In sight (normal vision)
 * 
 * @author jdtran21 - Joseph Tran
 */
public class FogOfWarManager extends Manager {
	private Array2D<Integer> fogOfWar;		
	
	
	/**
	 * Constructs the fogOfWar array when the game starts by setting all 
	 * elements to 0.
	 * 
	 * @param width the width of the map
	 * @param length the length of the map
	 */
	public FogOfWarManager(int width, int length) {
		this.fogOfWar = new Array2D<Integer>(width, length);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++) {
				fogOfWar.set(i, j, 0);
			}
		}
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
		if (s != 0 || s != 1 || s!= 2) {
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
	public int getFog(int x, int y) {
		return fogOfWar.get(x, y);
	}
	
	/**
	 * Updates the FogOfWar after each clock tick
	 * NOT FINISHED, REQUIRES ENTITY SIGHT
	 */
	public void updateFog() {
		Array2D<Integer> updatedFog = new Array2D<Integer>(fogOfWar.getWidth(),
				fogOfWar.getLength());
		//gets Sight areas of allied entities, to be implemented
		for (int i = 0; i < fogOfWar.getWidth(); i++) {
			for (int j = 0; j < fogOfWar.getLength(); j++) {
				if (updatedFog.get(i, j) != fogOfWar.get(i, j)) {
					if (updatedFog.get(i, j) == 2) {
						fogOfWar.set(i, j, 2);						
					} else if (fogOfWar.get(i, j) == 2) {
						fogOfWar.set(i, j, 1);
					}
				}
			}
		}
	}
	
	
}