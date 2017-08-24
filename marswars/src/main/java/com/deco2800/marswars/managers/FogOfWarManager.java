package com.deco2800.marswars.managers;
import com.deco2800.marswars.util.Array2D;

/**
 * Fog of War Manager
 * @param <T>
 * 
 */

public class FogOfWarManager extends Manager {
	private Array2D<Integer> fogOfWar;		//2D array corresponding to map coordinates
									//0 = unseen
									//1 = explored but not in sight
									//2 = in sight
	
	
	public FogOfWarManager(int width, int height) {
		this.fogOfWar = new Array2D<Integer>(width, height);
		for(int i=0; i < width; i++) {
			for(int j=0; j < height; j++) {
				fogOfWar.set(i, j, 0);
			}
		}
	}
	
	
	public void setFog(int x, int y, int s) {
		fogOfWar.set(x, y, s);
	}
	
	
	public void updateFog() {
		
	}
	
	
}