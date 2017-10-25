package com.deco2800.marswars.initiategame;

import com.deco2800.marswars.entities.terrainelements.Obstacle;
import com.deco2800.marswars.entities.terrainelements.Resource;
import com.deco2800.marswars.managers.AiManager;
import com.deco2800.marswars.managers.WinManager;
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

    //list of obstacles
    private List<Obstacle> obstacles = new ArrayList<>();

    //list of animals
    private List<SavedAnimal> animals = new ArrayList<>();

    public List<SavedAnimal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<SavedAnimal> animals) {
        this.animals = animals;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public void setObstacles(List<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    //index for team colouring
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int aITeams;
    private int playerTeams;

    private WinManager.WINS winCondition;


    //stats are saved in this order

    public WinManager.WINS getWinCondition() {
        return winCondition;
    }

    public void setWinCondition(WinManager.WINS winCondition) {
        this.winCondition = winCondition;
    }

    //biomass-rocks-crystal-water-population

    //aiStats
    private List<ArrayList<Integer>> aIStats = new ArrayList<>();

    //playerStats
    private List<ArrayList<Integer>> playerStats = new ArrayList<>();

    //gameTime
    private long hour;
    private long min;

    public AiManager.Difficulty getAiDifficulty() {
        return aiDifficulty;
    }

    public void setAiDifficulty(AiManager.Difficulty aiDifficulty) {
        this.aiDifficulty = aiDifficulty;
    }

    //aiDifficulties
    AiManager.Difficulty aiDifficulty;


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
    public Array2D<Integer> getFogOfWar() {
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
    public Array2D<Integer> getBlackFogOfWar() {
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
    public void setaITeams(int aITeams) {
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
    public void setPlayerTeams(int playerTeams) {
        this.playerTeams = playerTeams;
    }

    /**
     * Returns the list of ai stats
     *
     * @return the list of ai stats
     */
    public List<ArrayList<Integer>> getaIStats() {
        return aIStats;
    }

    /**
     * Sets a new list of ai stats
     *
     * @param aIStats the new list of ai stats
     */
    public void setaIStats(List<ArrayList<Integer>> aIStats) {
        this.aIStats = aIStats;
    }

    /**
     * Returns the list of player stats
     *
     * @return the list of player stats
     */
    public List<ArrayList<Integer>> getPlayerStats() {
        return playerStats;
    }

    /**
     * Sets a new list of player stats
     *
     * @param playerStats the new list of player stats
     */
    public void setPlayerStats(List<ArrayList<Integer>> playerStats) {
        this.playerStats = playerStats;
    }
}
