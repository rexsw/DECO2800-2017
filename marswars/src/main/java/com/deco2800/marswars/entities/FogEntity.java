package com.deco2800.marswars.entities;

/**
 * @author Treenhan
 * Created by Treenhan on 8/24/17.
 *
 * This class will holds the Fog Tiles and these objects will be rendered on a separated layer
 *
 *
 */
public class FogEntity extends AbstractEntity {


    /**
     * the constructor for the Fog Entity
     * @param posX
     * @param posY
     * @param posZ
     * @param xLength
     * @param yLength
     * @param zLength
     */
    public FogEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength) {
        super(posX, posY, posZ, xLength, yLength, zLength);
    }


}
