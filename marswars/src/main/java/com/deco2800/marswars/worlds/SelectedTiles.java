package com.deco2800.marswars.worlds;

import com.deco2800.marswars.entities.BlackTile;
import com.deco2800.marswars.entities.FogEntity;
import com.deco2800.marswars.entities.GrayTile;
import com.deco2800.marswars.entities.MultiSelectionTile;
import com.deco2800.marswars.managers.MultiSelection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Treenhan on 9/20/17.
 */
public class SelectedTiles {

    protected static ArrayList<MultiSelectionTile> selectedTileMap = new ArrayList<MultiSelectionTile>();


    /**
     * this return the black fog map
     * @return
     */
    public static List<MultiSelectionTile> getMultiSelectionTile() {
        return selectedTileMap;
    }

    /**
     * this function initialize the fog world by filling everything with gray and black tiles
     * @param width
     * @param length
     */
    public static void initializeSelectedTiles(int width, int length){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                addEntity(new MultiSelectionTile(x,y,1,1f,1f),selectedTileMap);

            }
        }
    }


    /**
     * add FogEntity entity to the fog world
     * @param entity
     */
    public static void addEntity(MultiSelectionTile entity, ArrayList<MultiSelectionTile> selectedTileMap) {
        //Add to the fog map
        int left = (int)entity.getPosX();
        int right = (int)Math.ceil(entity.getPosX() + entity.getXLength());
        int bottom = (int)entity.getPosY();
        int top = (int)Math.ceil(entity.getPosY() + entity.getYLength());
        for (int x = left; x < right; x++) {
            for (int y = bottom; y < top; y++) {
                selectedTileMap.add(entity);
            }
        }
    }
}
