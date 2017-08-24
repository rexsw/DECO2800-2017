package com.deco2800.marswars.entities.buildings;

import com.deco2800.marswars.entities.BaseEntity;

/**
 * Basic building object from which all buildings will inherit.
 */
public class Building extends BaseEntity{

    /**
     * Basic Building constructor
     *
     * @param posX its x position
     * @param posY its y position
     * @param posZ its z position
     * @param xLength its length
     * @param yLength its depth
     * @param zLength its height
     */
    public Building(float posX, float posY, float posZ, float xLength, float yLength, float zLength) {
        super(posX, posY, posZ, xLength, yLength, zLength);
    }
}


//buildings people implement me please!!!