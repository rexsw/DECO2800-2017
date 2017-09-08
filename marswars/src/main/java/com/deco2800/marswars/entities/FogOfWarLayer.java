package com.deco2800.marswars.entities;

/**
 * @author Treenhan
 * Created by Treenhan on 8/24/17.
 *
 * This class will holds the line of sight and these objects will be rendered on a separated layer
 *
 *
 */
public class FogOfWarLayer extends AbstractEntity {

    public FogOfWarLayer(float posX, float posY, float posZ, float xLength, float yLength, float zLength) {
        super(posX, posY, posZ, xLength, yLength, zLength);
    }

    /**
     * Sets the current position of the Fog Of War Tile
     *
     * @param x
     * @param y
     * @param z
     */
    @Override
    public void setPosition(float x, float y, float z) {
        super.setPosition(x, y, z);
    }

    /**
     * Sets the Position X
     * @param x
     */
    @Override
    public void setPosX(float x) {
        super.setPosX(x);
    }

    /**
     * Sets the position Y
     * @param y
     */
    @Override
    public void setPosY(float y) {
        super.setPosY(y);
    }

    /**
     * Sets the position Z
     * @param z
     */
    @Override
    public void setPosZ(float z) {
        super.setPosZ(z);
    }


}
