package com.deco2800.marswars.entities;

/**
 * Created by Treenhan on 9/20/17.
 */
public class MultiSelectionTile extends AbstractEntity{
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
    public MultiSelectionTile(float posX, float posY, float posZ, float height, float width) {
        super(posX, posY, posZ, height, width, 1f);
        this.setTexture("multi_selection");
        this.canWalkOver = true;
    }
}
