package com.deco2800.marswars.initiateGame;

import com.deco2800.marswars.entities.terrainelements.Resource;
import com.deco2800.marswars.util.Array2D;

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
    private Array2D<Integer> fogOfWar;

    //black fog of war
    private Array2D<Integer> blackFogOfWar;

    //list of resources
    private List<Resource> resource = new ArrayList<>();

    //list of buildings
    private List<SavedBuilding> building = new ArrayList<>();

    //list of entities
    private List<SavedEntity> entities = new ArrayList<>();

    private int aITeams;
    private int playerTeams;


    //stats are saved in this order
    //biomass-rocks-crystal-water-population

    //aiStats
    private List<ArrayList<Integer>> aIStats = new ArrayList<>();

    //playerStats
    private List<ArrayList<Integer>> playerStats = new ArrayList<>();

    //gameTime
    private long hour;
    private long min;
    private long sec;

    public long getHour() {
        return hour;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getSec() {
        return sec;
    }

    public void setSec(long sec) {
        this.sec = sec;
    }




    /**
     * Returns the list of resources
     *
     * @return the list of resources
     */
    public List<Resource> getResource() {
        return resource;
    }

    /**
     * Sets a new list of resources
     *
     * @param resource the new list of resources
     */
    public void setResource(List<Resource> resource) {
        this.resource = resource;
    }

    /**
     * Returns a list of saved buildings
     *
     * @return a list of saved buildings
     */
    public List<SavedBuilding> getBuilding() {
        return building;
    }

    /**
     * Sets a new list of buildings
     *
     * @param building the new list of buildings
     */
    public void setBuilding(List<SavedBuilding> building) {
        this.building = building;
    }

    /**
     * Returns the list of saved entities
     *
     * @return the list of saved entities
     */
    public List<SavedEntity> getEntities() {
        return entities;
    }

    /**
     * Sets a new list of saved entities
     *
     * @param entities the new list of saved entities
     */
    public void setEntities(List<SavedEntity> entities) {
        this.entities = entities;
    }

    /**
     * Returns the fog of war array
     *
     * @return the fog of war array
     */
    Array2D<Integer> getFogOfWar() {
        return fogOfWar;
    }

    /**
     * Sets the fog of war
     *
     * @param fogOfWar the new fog of war
     */
    void setFogOfWar(Array2D<Integer> fogOfWar) {
        this.fogOfWar = fogOfWar;
    }

    /**
     * Returns the black fog of war array
     *
     * @return the black fog of war array
     */
    Array2D<Integer> getBlackFogOfWar() {
        return blackFogOfWar;
    }

    /**
     * Sets the black fog of war
     *
     * @param blackFogOfWar the new black fog of war
     */
    void setBlackFogOfWar(Array2D<Integer> blackFogOfWar) {
        this.blackFogOfWar = blackFogOfWar;
    }

    /**
     * Return the number of ai teams
     *
     * @return the number of ai teams
     */
    public int getaITeams() {
        return aITeams;
    }

    /**
     * Set the number of ai teams
     *
     * @param aITeams the number of ai teams
     */
    void setaITeams(int aITeams) {
        this.aITeams = aITeams;
    }

    /**
     * Get number of player teams
     *
     * @return the number of player teams
     */
    public int getPlayerTeams() {
        return playerTeams;
    }

    /**
     * Set the number of player teams
     *
     * @param playerTeams the new number of player teams
     */
    void setPlayerTeams(int playerTeams) {
        this.playerTeams = playerTeams;
    }

    /**
     * Returns the list of ai stats
     *
     * @return the list of ai stats
     */
    List<ArrayList<Integer>> getaIStats() {
        return aIStats;
    }

    /**
     * Sets a new list of ai stats
     *
     * @param aIStats the new list of ai stats
     */
    void setaIStats(List<ArrayList<Integer>> aIStats) {
        this.aIStats = aIStats;
    }

    /**
     * Returns the list of player stats
     *
     * @return the list of player stats
     */
    List<ArrayList<Integer>> getPlayerStats() {
        return playerStats;
    }

    /**
     * Sets a new list of player stats
     *
     * @param playerStats the new list of player stats
     */
    void setPlayerStats(List<ArrayList<Integer>> playerStats) {
        this.playerStats = playerStats;
    }
}
