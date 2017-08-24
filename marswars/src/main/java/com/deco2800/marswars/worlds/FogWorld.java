package com.deco2800.marswars.worlds;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.FogOfWarLayer;
import com.deco2800.marswars.util.Array2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Treenhan on 8/24/17.
 */
public class FogWorld {
    private int width;
    private int length;

    protected static ArrayList<FogOfWarLayer> fogMap = new ArrayList<FogOfWarLayer>();

    public static ArrayList<FogOfWarLayer> getFogMap() {
        return fogMap;
    }

    public void addEntity(FogOfWarLayer entity) {
        //Add to the fog map
        int left = (int)entity.getPosX();
        int right = (int)Math.ceil(entity.getPosX() + entity.getXLength());
        int bottom = (int)entity.getPosY();
        int top = (int)Math.ceil(entity.getPosY() + entity.getYLength());
        for (int x = left; x < right; x++) {
            for (int y = bottom; y < top; y++) {
                fogMap.add(entity);
            }
        }
    }


}
