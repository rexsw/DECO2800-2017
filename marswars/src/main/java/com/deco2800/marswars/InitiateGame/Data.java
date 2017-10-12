package com.deco2800.marswars.InitiateGame;

import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.util.Array2D;
import com.deco2800.marswars.worlds.MapSizeTypes;
import com.deco2800.marswars.worlds.map.tools.MapTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Treenhan on 10/12/17.
 */
public class Data {
    /**
     * this is the list of data will be saved
     * the order of saving will follow this order
     */
    //gray fog of war
    public Array2D<Integer> fogOfWar;

    //black fog of war
    public Array2D<Integer> blackFogOfWar;

    //list of entities
    public List<AbstractEntity> entities = new ArrayList<>();

    //list of walkables
    public List<AbstractEntity> walkables = new ArrayList<>();

    //the map
    public MapTypes mapType;
    public MapSizeTypes mapSize;
    public int aITeams;
    public int playerTeams;
}
