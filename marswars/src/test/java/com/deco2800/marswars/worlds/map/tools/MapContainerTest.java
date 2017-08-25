package com.deco2800.marswars.worlds.map.tools;

import com.deco2800.marswars.BaseTest;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.CustomizedWorld;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapContainerTest extends BaseTest {


    MapContainer map;
    CustomizedWorld world;

    @Before
    public void setup(){
        map = new MapContainer();
        //world = new CustomizedWorld(map);
    }
    @Test
    public void generateEntities() throws Exception {
        //Assert.assertFalse(world.getEntities().isEmpty());
    }

    @Test
    public void getMap() throws Exception {
        map = new MapContainer("marswars/out/production/resources/mapAssets/mediumMars.tmx");
        Assert.assertEquals("marswars/out/production/resources/mapAssets/mediumMars.tmx", map.getMap());
    }

    @Test
    public void passWorld() throws Exception {
    }

    @Test
    public void setTerrainElement() throws Exception {
    }

    @Test
    public void setTerrainElement1() throws Exception {
    }

    @Test
    public void setStructure() throws Exception {
    }

    @Test
    public void setStructure1() throws Exception {
    }

    @Test
    public void setStructure2() throws Exception {
    }

    @Test
    public void setStructure3() throws Exception {
    }

    @Test
    public void setCivilization() throws Exception {
    }

    @Test
    public void setEntity() throws Exception {
    }

    @Test
    public void setEntities() throws Exception {
    }

    @Test
    public void setEntity1() throws Exception {
    }

    @Test
    public void setEntities1() throws Exception {
    }

    @Test
    public void createCivilization() throws Exception {
    }

    @Test
    public void createRandomCivilization() throws Exception {
    }

    @Test
    public void getRandomStructure() throws Exception {
    }

}