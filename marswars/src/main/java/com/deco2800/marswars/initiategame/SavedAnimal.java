package com.deco2800.marswars.initiategame;

/**
 * Created by Treenhan on 10/22/17.
 */
public class SavedAnimal {
    //never delete this, this is used by game loading
    SavedAnimal(){};

    SavedAnimal(String name, float x, float y, int health){
        this.name = name;
        this.x = x;
        this.y = y;
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

    public int getHealth(){return health;}

    private int health;
    private float x;
    private float y;
}
