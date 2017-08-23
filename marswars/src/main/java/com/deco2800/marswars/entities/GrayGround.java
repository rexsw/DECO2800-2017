package com.deco2800.marswars.entities;

/**
 * Created by Treenhan on 8/23/17.
 */
public class GrayGround extends BaseEntity {

    //the constructor for it
    public GrayGround(float posX, float posY, float posZ, float height, float width) {
        super(posX, posY, posZ, height, width, 1f);
        this.setTexture("ground_gray");
        this.canWalkOver = true;
        this.setCost(100);
    }
}
