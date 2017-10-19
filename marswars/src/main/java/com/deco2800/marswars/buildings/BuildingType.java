package com.deco2800.marswars.buildings;
/**
 * /**
 * A list of all Building Types
 */
public enum BuildingType {

	BASE(3f, 100, "base3"), 
	BARRACKS(3f, 40, "barracks3"), 
	BUNKER(2f, 15, "bunker3"), 
	TURRET(2f, 40, "turret3"),
	HEROFACTORY(3f, 60, "herofactory3"),// texture value is a placeholder
	TECHBUILDING(2f, 60, "tech3");// texture value is a placeholder


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
    	//return buildCost
    }
    public String getBuildTexture() {
    	return buildTexture;
    }
}
