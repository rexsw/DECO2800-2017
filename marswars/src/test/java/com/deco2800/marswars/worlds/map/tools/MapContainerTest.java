package com.deco2800.marswars.worlds.map.tools;

import com.deco2800.marswars.BaseTest;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Spacman;
import com.deco2800.marswars.worlds.CustomizedWorld;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MapContainerTest extends BaseTest{
    MapContainer map;
    CustomizedWorld world;

    @Before
    public void setup(){
        map = new MapContainer("resources/mapAssets/tinyMars.tmx");
        world = new CustomizedWorld(map);
    }
    @Test
    public void generateEntitiesRandom() throws Exception {
        world.loadMapContainer(map);
        Assert.assertFalse(world.getEntities().isEmpty());
    }

    @Test
    public void generateEntities() throws Exception {
        map.generateEntities(false);
        Assert.assertTrue(world.getEntities().isEmpty());
    }

    @Test
    public void getMap() throws Exception {
        Assert.assertEquals("resources/mapAssets/tinyMars.tmx", map.getMap());
    }

    @Test
    public void passWorld() throws Exception {
        Assert.assertTrue(map.world.equals(world));
    }

    @Test
    public void checkForEntity() throws Exception {
        map.setEntity(new Spacman(0, 0,0));
        Assert.assertFalse(map.checkForEntity(0, 0));
    }

    @Test
    public void generateResourcePattern() throws Exception {
        map.generateResourcePattern();
        Assert.assertFalse(world.getEntities().isEmpty());
    }

    @Test
    public void getRandomEntity() throws Exception {
        map.getRandomEntity();
        Assert.assertFalse(world.getEntities().isEmpty());
    }

    @Test
    public void getRandomBuilding() throws Exception {
        map.getRandomBuilding();
        Assert.assertFalse(world.getEntities().isEmpty());
    }

    @Test
    public void getRandomResource() throws Exception {
        map.getRandomResource();
        Assert.assertFalse(world.getEntities().isEmpty());
    }

    @Test
    public void getRandomResourceWithXY() throws Exception {
        map.getRandomResource(0, 0);
        Assert.assertFalse(map.checkForEntity(0, 0));
    }

    @Test
    public void getRandomMap() throws Exception {
        map.getRandomMap();
        ArrayList<String> listOfMaps = new ArrayList<>();
        listOfMaps.add("resources/mapAssets/tinyMars.tmx");
        listOfMaps.add("resources/mapAssets/tinyMoon.tmx");
        listOfMaps.add("resources/mapAssets/tinySun.tmx");
        listOfMaps.add("resources/mapAssets/smallMars.tmx");
        listOfMaps.add("resources/mapAssets/smallMoon.tmx");
        listOfMaps.add("resources/mapAssets/smallSun.tmx");
        listOfMaps.add("resources/mapAssets/mediumMars.tmx");
        listOfMaps.add("resources/mapAssets/mediumMoon.tmx");
        listOfMaps.add("resources/mapAssets/mediumSun.tmx");
        listOfMaps.add("resources/mapAssets/largeMars.tmx");
        listOfMaps.add("resources/mapAssets/largeMoon.tmx");
        listOfMaps.add("resources/mapAssets/largeSun.tmx");
        listOfMaps.add("resources/mapAssets/veryLargeSun.tmx");
        listOfMaps.add("resources/mapAssets/veryLargeMoon.tmx");
        listOfMaps.add("resources/mapAssets/veryLargeMars.tmx");
        Assert.assertTrue(listOfMaps.contains(map.getMap()));
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
        map.setEntity(new Spacman(0, 0, 0));
        Assert.assertFalse(world.getEntities().isEmpty());
    }

    @Test
    public void setEntities() throws Exception {
        BaseEntity[] listOfEntities = new BaseEntity[2];
        listOfEntities[0] = new Spacman(0, 0, 0);
        listOfEntities[1] = new Spacman(1, 0, 0);
        map.setEntities(listOfEntities);
        Assert.assertFalse(world.getEntities().isEmpty());
    }

    @Test
    public void createCivilization() throws Exception {
    }

    @Test
    public void createRandomCivilization() throws Exception {
    }

}