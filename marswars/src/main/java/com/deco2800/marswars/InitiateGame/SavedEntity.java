package com.deco2800.marswars.InitiateGame;

/**
 * Created by Treenhan on 10/13/17.
 */
public class SavedEntity {

    //never delete this, this is used by game loading
    SavedEntity(){};

    SavedEntity(String name, float x, float y, int teamId){
        this.name = name;
        this.x = x;
        this.y = y;
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    private String name;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getTeamId() {
        return teamId;
    }

    private float x;
    private float y;
    private int teamId;


}
