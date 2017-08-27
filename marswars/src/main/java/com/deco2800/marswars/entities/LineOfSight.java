package com.deco2800.marswars.entities;

/**
 * @author Treenhan
 * Created by Treenhan on 8/23/17.
 *
 * This is an entity of Fog Of War which limits the region in which an entity can see
 *
 *
 */
public class LineOfSight extends FogOfWarLayer {


    /**
     *
     * This is the constructor for a line-of-sight tile
     *
     * @author Treenhan
     * @param posX set X position for the line of sight tile
     * @param posY set Y position for the line of sight tile
     * @param posZ set Z position for the line of sight tile
     * @param height set the height for the line of sight tile
     * @param width set width for the line of sight tile
     */
    //the constructor for it
    public LineOfSight(float posX, float posY, float posZ, float height, float width) {
        super(posX, posY, posZ, height, width, 1f);
        this.setTexture("ground_gray");
        this.canWalkOver = true;
    }
}
