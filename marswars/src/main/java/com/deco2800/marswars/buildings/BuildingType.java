package com.deco2800.marswars.buildings;

import java.util.ArrayList;

import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;

/**
 * /**
 * A list of all Building Types
 */
public enum BuildingType {

	WALL(1f, 5, "wall"), 
	GATE(1f, 0, "gate2"), 
	BASE(3f, 150, "base"), 
	BARRACKS(3f, 40, "barracks"), 
	BUNKER(2f, 15, "bunker"), 
	TURRET(2f, 70, "turret"),
	HEROFACTORY(3f, 50, "herofactory3"),// texture value is a placeholder
	TECHBUILDING(2f, 50, "tech3");// texture value is a placeholder


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
