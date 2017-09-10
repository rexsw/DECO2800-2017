package com.deco2800.marswars.worlds;

import com.deco2800.marswars.entities.BlackTile;
import com.deco2800.marswars.entities.FogOfWarLayer;
import com.deco2800.marswars.entities.GrayTile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Treenhan on 8/24/17.
 */
public class FogWorld {


    protected static ArrayList<FogOfWarLayer> fogMap = new ArrayList<FogOfWarLayer>();
    protected static ArrayList<FogOfWarLayer> blackFogMap = new ArrayList<FogOfWarLayer>();

    public static List<FogOfWarLayer> getFogMap() {
        return fogMap;
    }

    public static List<FogOfWarLayer> getBlackFogMap() {
        return blackFogMap;
    }

    public FogWorld(int width, int length){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                addEntity(new GrayTile(x,y,1,1f,1f),fogMap);
                addEntity(new BlackTile(x,y,1,1f,1f),blackFogMap);
            }
        }
    }

//    /**
//     * fill the fogWorld with gray tiles
//     * @param width
//     * @param length
//     */
//    public void initializeFogWorld(int width, int length){
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < length; y++) {
//                addEntity(new GrayTile(x,y,1,1f,1f));
//            }
//        }
//    }

    /**
     * add FogOfWarLayer entity to the fog world
     * @param entity
     */
    public void addEntity(FogOfWarLayer entity,ArrayList<FogOfWarLayer> fogMap) {
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
