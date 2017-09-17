package com.deco2800.marswars.worlds;

import com.deco2800.marswars.entities.BlackTile;
import com.deco2800.marswars.entities.FogEntity;
import com.deco2800.marswars.entities.GrayTile;

import java.util.ArrayList;
import java.util.List;

/**
 * this class contains the structure to render out the fog of war
 * Created by Treenhan on 8/24/17.
 */
public class FogWorld {


    protected static ArrayList<FogEntity> fogMap = new ArrayList<FogEntity>();
    protected static ArrayList<FogEntity> blackFogMap = new ArrayList<FogEntity>();

    /**
     * this returns the fog map
     * @return
     */
    public static List<FogEntity> getFogMap() {
        return fogMap;
    }

    /**
     * this return the black fog map
     * @return
     */
    public static List<FogEntity> getBlackFogMap() {
        return blackFogMap;
    }

    /**
     * this function initialize the fog world by filling everything with gray and black tiles
     * @param width
     * @param length
     */
    public static void initializeFogWorld(int width, int length){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                addEntity(new GrayTile(x,y,1,1f,1f),fogMap);
                addEntity(new BlackTile(x,y,1,1f,1f),blackFogMap);
            }
        }
    }


    /**
     * add FogEntity entity to the fog world
     * @param entity
     */
    public static void addEntity(FogEntity entity, ArrayList<FogEntity> fogMap) {
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
