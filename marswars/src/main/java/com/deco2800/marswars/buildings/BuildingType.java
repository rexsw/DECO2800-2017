package com.deco2800.marswars.buildings;
/**
 * /**
 * A list of all Building Types
 */
public enum BuildingType {

	BASE(3f), 
	BARRACKS(3f), 
	BUNKER(2f), 
	TURRET(2f);
	
	private final float buildingSize;
    BuildingType(float buildingSize) {
        this.buildingSize = buildingSize;
    }
    public float getBuildSize() {
    	return buildingSize;
    }
}
