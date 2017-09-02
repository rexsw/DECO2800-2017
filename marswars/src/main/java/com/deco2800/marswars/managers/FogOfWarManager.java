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
	 * Updates the FogOfWar after each clock tick based on the position of entities.
	 *
	 * @param map The collision map of entities
	 */
//	public void updateFog(Array2D<List<BaseEntity>> map) {
//		for (int i = 0; i < map.getWidth(); i++) {
//			for (int j = 0; j < map.getLength(); j++) {
//				List<BaseEntity> entity = map.get(i, j);
//				if (!(entity.isEmpty())) {
//					sightRange(i, j, 5,);
//				}
//			}
//		}
//	}

	/**
	 * Updates the fogOfWar based on the sight range of an entity.
	 *
	 * @param x     The x position of the entity
	 * @param y     The y position of the entity
	 * @param maxRange The sight range of the entity
	 */
	public static void sightRange(int x, int y, int maxRange, boolean state) {
		if (state) {//set the new position on the map
			for (int range = 1; range <= maxRange; range++) {
				int w = fogOfWar.getWidth();
				int l = fogOfWar.getLength();
				for (int i = 0; i < range; i++) {
					if (x + i < w && y + range - i < l) {
						fogOfWar.set(x + i, y + range - i, 2);
					}
					if (x + i < w && y - range + i >= 0) {
						fogOfWar.set(x + i, y - range + i, 2);
					}
					if (x - i >= 0 && y + range - i < l) {
						fogOfWar.set(x - i, y + range - i, 2);
					}
					if (x - i >= 0 && y - range + i >= 0) {
						fogOfWar.set(x - i, y - range + i, 2);
					}
				}
			}
		}
		else {//delete the old position on the map
			for (int range = 1; range <= maxRange; range++) {
				int w = fogOfWar.getWidth();
				int l = fogOfWar.getLength();
				for (int i = 0; i < range; i++) {
					if (x + i < w && y + range - i < l) {
						fogOfWar.set(x + i, y + range - i, 0);
					}
					if (x + i < w && y - range + i >= 0) {
						fogOfWar.set(x + i, y - range + i, 0);
					}
					if (x - i >= 0 && y + range - i < l) {
						fogOfWar.set(x - i, y + range - i, 0);
					}
					if (x - i >= 0 && y - range + i >= 0) {
						fogOfWar.set(x - i, y - range + i, 0);
					}
				}

			}
		}

	}
}