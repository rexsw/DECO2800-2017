package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;

import java.util.Optional;

/**
 * Created by Treenhan on 8/23/17.
 */
public class LineOfSight extends FogOfWarLayer {

    //the constructor for it
    public LineOfSight(float posX, float posY, float posZ, float height, float width) {
        super(posX, posY, posZ, height, width, 1f);
        this.setTexture("ground_gray");
        this.canWalkOver = true;
    }
}
