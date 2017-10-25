package com.deco2800.marswars;

import com.deco2800.marswars.entities.terrainelements.Obstacle;
import com.deco2800.marswars.entities.terrainelements.Resource;
import com.deco2800.marswars.initiategame.Data;
import com.deco2800.marswars.initiategame.SavedBuilding;
import com.deco2800.marswars.initiategame.SavedEntity;
import com.deco2800.marswars.util.Array2D;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Treenhan on 10/21/17.
 */
public class DataTest {
    @Test
    public void dataTest(){
        Data data = new Data();
        Assert.assertNotNull(data);

        data.getHour();
        data.getMin();
        data.getResource();
        data.getBuilding();
        data.getEntities();
        data.getFogOfWar();
        data.getBlackFogOfWar();
        data.getaITeams();
        data.getPlayerTeams();
        data.getaIStats();
        data.getPlayerStats();

        Array2D<Integer> fogOfWar;
        Array2D<Integer> blackFogOfWar;
        List<Resource> resource = new ArrayList<>();
        List<SavedBuilding> building = new ArrayList<>();
        List<SavedEntity> entities = new ArrayList<>();
        List<Obstacle> obstacles = new ArrayList<>();
        List<ArrayList<Integer>> aIStats = new ArrayList<>();
        List<ArrayList<Integer>> playerStats = new ArrayList<>();

        data.setHour(1l);
        data.setMin(1l);
        data.setResource(resource);
        data.setBuilding(building);
        data.setEntities(entities);
        data.setObstacles(obstacles);
        data.setaITeams(1);
        data.setPlayerTeams(1);
        data.setPlayerStats(playerStats);
        data.setaIStats(aIStats);

    }
}
