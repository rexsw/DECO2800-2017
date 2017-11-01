package com.deco2800.marswars.buildings;

import java.util.ArrayList;

import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;

/**
 * /**
 * A list of all Building Types
 */
public enum BuildingType {

	WALL(1f, 5, "wall","WallHorizontal"), 
	GATE(1f, 0, "gate2", "GateHorizontal"), 
	BASE(3f, 150, "base", "Base"), 
	BARRACKS(3f, 65, "barracks", "Barracks"), 
	BUNKER(2f, 15, "bunker", "Bunker"), 
	TURRET(2f, 45, "turret", "Turret"),
	HEROFACTORY(2f, 120, "herofactory3", "HeroFactory"),// texture value is a placeholder
	SPACEX(2f, 85, "tech3", "TechBuilding");// texture value is a placeholder


	private final float buildingSize;
	private final int buildCost;
	private final String buildTexture;
	private final String textName;
    BuildingType(float buildingSize, int buildCost, String buildTexture, String textName) {
        this.buildingSize = buildingSize;
        this.buildCost = buildCost;
        this.buildTexture = buildTexture;	
        this.textName = textName;
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
    public String getTextName() {
    	return textName;
    }
}
