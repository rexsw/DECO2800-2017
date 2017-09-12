package com.deco2800.marswars.entities;

/**
 * This the gray tile that will be rendered on top of everything that is out of sight
 * Created by Treenhan on 9/6/17.
 */
public class GrayTile extends FogEntity {
    /**
     *
     * This is the constructor for a gray tile
     *
     * @author Treenhan
     * @param posX set X position for the line of sight tile
     * @param posY set Y position for the line of sight tile
     * @param posZ set Z position for the line of sight tile
     * @param height set the height for the line of sight tile
     * @param width set width for the line of sight tile
     */
    public GrayTile(float posX, float posY, float posZ, float height, float width) {
        super(posX, posY, posZ, height, width, 1f);
        this.setTexture("transparent_tile");
        this.canWalkOver = true;
    }
}
