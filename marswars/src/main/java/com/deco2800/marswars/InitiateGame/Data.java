package com.deco2800.marswars.InitiateGame;

import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.TerrainElements.Resource;
import com.deco2800.marswars.entities.units.*;
import com.deco2800.marswars.managers.GameManager;
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

    //list of walkables
    public List<AbstractEntity> walkables = new ArrayList<>();

    //list of resources
    public List<Resource> resource = new ArrayList<>();

    //list of buildings
    public List<SavedBuilding> building = new ArrayList<>();

    //list of entities
    public List<SavedEntity> entities = new ArrayList<>();

    public int aITeams;
    public int playerTeams;


    //stats are saved in this order
    //biomass-rocks-crystal-water-population

    //aiStats
    public List<ArrayList<Integer>> aIStats = new ArrayList<ArrayList<Integer>>();

    //playerStats
    public List<ArrayList<Integer>> playerStats = new ArrayList<ArrayList<Integer>>();
}
