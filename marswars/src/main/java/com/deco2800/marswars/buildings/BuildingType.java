package com.deco2800.marswars.buildings;
/**
 * /**
 * A list of all Building Types
 */
public enum BuildingType {

	BASE(3f, 350, "base3"), 
	BARRACKS(3f, 300, "barracks3"), 
	BUNKER(2f, 100, "bunker3"), 
	TURRET(2f, 200, "turret3"),
	HEROFACTORY(3f, 300, "NA");
	
	private final float buildingSize;
	private final int buildCost;
	private final String buildTexture;
    BuildingType(float buildingSize, int buildCost, String buildTexture) {
        this.buildingSize = buildingSize;
        this.buildCost = buildCost;
        this.buildTexture = buildTexture;	
    }
    public float getBuildSize() {
    	return buildingSize;
    }
    public int getCost() {
    	return buildCost;
    }
    public String getBuildTexture() {
    	return buildTexture;
    }
}
