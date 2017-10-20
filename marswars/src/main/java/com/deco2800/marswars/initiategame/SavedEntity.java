package com.deco2800.marswars.initiategame;

/**
 * Created by Treenhan on 10/13/17.
 */
public class SavedEntity {

    //never delete this, this is used by game loading
    SavedEntity(){};

    SavedEntity(String name, float x, float y, int teamId, int health){
        this.name = name;
        this.x = x;
        this.y = y;
        this.teamId = teamId;
        this.health = health;
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

    public int getHealth(){return health;}

    private int health;
    private float x;
    private float y;
    private int teamId;


}
