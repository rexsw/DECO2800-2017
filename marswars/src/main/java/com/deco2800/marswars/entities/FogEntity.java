package com.deco2800.marswars.entities;

import com.deco2800.marswars.util.Box3D;

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
     * 
     * @param position The position of the fog tile.
     */
    public FogEntity(Box3D position) {
        super(position);
    }


}
