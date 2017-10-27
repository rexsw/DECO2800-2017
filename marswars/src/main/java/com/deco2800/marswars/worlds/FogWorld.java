package com.deco2800.marswars.worlds;

import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.BlackTile;
import com.deco2800.marswars.entities.GrayTile;
import com.deco2800.marswars.entities.MultiSelectionTile;

import java.util.ArrayList;
import java.util.List;

/**
 * this class contains the structure to render out the fog of war
 * Created by Treenhan on 8/24/17.
 */
public class FogWorld {
    private FogWorld(){super();}


    protected static ArrayList<AbstractEntity> fogMap = new ArrayList<AbstractEntity>();
    protected static ArrayList<AbstractEntity> blackFogMap = new ArrayList<AbstractEntity>();

    //the selected tiles are integrated into the fog world
    protected static ArrayList<AbstractEntity> selectedTileMap = new ArrayList<AbstractEntity>();

    /**
     * this returns the fog map
     * @return
     */
    public static List<AbstractEntity> getFogMap() {
        return fogMap;
    }


    /**
     * this return the selected tiles grid
     * @return
     */
    public static List<AbstractEntity> getMultiSelectionTile() {
        return selectedTileMap;
    }

    /**
     * clear the selected tiles
     */
    public static void clearSelectedTiles(){
        selectedTileMap = new ArrayList<AbstractEntity>();
    }

    /**
     * add a selected tile to render
     */
    public static void addSelectedTile(int x, int y){
        addEntity(new MultiSelectionTile(x,y,0,1f,1f),selectedTileMap);
    }

    /**
     * this function initialize selected tiles by putting the tile everywhere throughout the array
     * @param width
     * @param length
     */
    public static void initializeSelectedTiles(int width, int length){
        selectedTileMap = new ArrayList<AbstractEntity>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                addEntity(new MultiSelectionTile(x,y,0,1f,1f),selectedTileMap);

            }
        }
    }
    /**
     * this return the black fog map
     * @return
     */
    public static List<AbstractEntity> getBlackFogMap() {
        return blackFogMap;
    }

    /**
     * this function initialize the fog world by filling everything with gray and black tiles
     * @param width
     * @param length
     */
    public static void initializeFogWorld(int width, int length){
        fogMap = new ArrayList<AbstractEntity>();
        blackFogMap = new ArrayList<AbstractEntity>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                addEntity(new GrayTile(x,y,0,1f,1f),fogMap);
                addEntity(new BlackTile(x,y,0,1f,1f),blackFogMap);
            }
        }
    }


    /**
     * add FogEntity entity to the maps
     * @param entity
     */
    public static void addEntity(AbstractEntity entity, List<AbstractEntity> map) {
        //Add to the fog map
        int left = (int)entity.getPosX();
        int right = (int)Math.ceil(entity.getPosX() + entity.getXLength());
        int bottom = (int)entity.getPosY();
        int top = (int)Math.ceil(entity.getPosY() + entity.getYLength());
        for (int x = left; x < right; x++) {
            for (int y = bottom; y < top; y++) {
                map.add(entity);
            }
        }
    }


}
