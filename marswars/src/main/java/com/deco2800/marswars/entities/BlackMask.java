package com.deco2800.marswars.entities;

/**
 * 
 * Created by hwkhoo on 24/08/2017
 *
 */
public class BlackMask extends BaseEntity {

    
    public BlackMask(float posX, float posY, float posZ, float height, float width) {
        super(posX, posY, posZ, height, width, 1f);
        this.setTexture("ground_black");
        this.canWalkOver = true;
        this.setCost(100);
    }
}
