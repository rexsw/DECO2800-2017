package com.deco2800.marswars.worlds;

import com.deco2800.marswars.entities.FogOfWarLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Treenhan on 8/24/17.
 */
public class FogWorld {


    protected static ArrayList<FogOfWarLayer> fogMap = new ArrayList<FogOfWarLayer>();

    public static List<FogOfWarLayer> getFogMap() {
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
