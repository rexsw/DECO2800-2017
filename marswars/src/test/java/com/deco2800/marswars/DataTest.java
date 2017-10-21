package com.deco2800.marswars;

import com.deco2800.marswars.initiategame.Data;
import org.junit.Assert;
import org.junit.Test;

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
        data.getSec();
        data.getResource();
        data.getBuilding();
        data.getEntities();
        data.getFogOfWar();
        data.getBlackFogOfWar();
        data.getaITeams();
        data.getPlayerTeams();
        data.getaIStats();
        data.getPlayerStats();
    }
}
