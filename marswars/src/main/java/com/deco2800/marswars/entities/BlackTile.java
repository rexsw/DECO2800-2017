package com.deco2800.marswars.entities;

import com.deco2800.marswars.util.Box3D;

/**
 * Created by Treenhan on 9/6/17.
 * This is an entity of FogOfWar indicating the black out area
 */
public class BlackTile extends FogEntity {
    /**
     *
     * This is the constructor for a black tile
     *
     * @author Treenhan
     * @param posX set X position for the line of sight tile
     * @param posY set Y position for the line of sight tile
     * @param posZ set Z position for the line of sight tile
     * @param height set the height for the line of sight tile
     * @param width set width for the line of sight tile
     */
    //the constructor for it
    public BlackTile(float posX, float posY, float posZ, float height, float width) {
        super(new Box3D(posX, posY, posZ, height, width, 1f));
        this.setTexture("black_tile");
        this.canWalkOver = true;
    }
}
