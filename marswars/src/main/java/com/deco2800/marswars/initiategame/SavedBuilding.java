package com.deco2800.marswars.initiategame;

import com.deco2800.marswars.buildings.BuildingType;

/**
 * Created by Treenhan on 10/15/17.
 */
public class SavedBuilding {

    //never delete this, this is used by game loading
    SavedBuilding(){}

    SavedBuilding(float x, float y, BuildingType buildingType, int teamId, int health){
        this.buildingType = buildingType;
        this.x = x;
        this.y = y;
        this.teamId = teamId;
        this.health = health;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getHealth(){return  health;}

    public int getTeamId() {
        return teamId;
    }

    private int health;
    private float x;
    private float y;
    private int teamId;
    private BuildingType buildingType;
}
