package com.deco2800.marswars;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.HeroSpacman;
import com.deco2800.marswars.util.Array2D;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for all methods of the BaseWorld class and the AbstractWorld class.
 *
 * @author @luanlucho
 *
 * @see com.deco2800.marswars.worlds.AbstractWorld
 * @see BaseWorld
 */
public class BaseWorldTest extends BaseTest {

    // a base world mock object
    BaseWorld baseWorld;
    // an entity mock object
    BaseEntity entity;

    @Before
    public void setup(){
        baseWorld = new BaseWorld(10 ,15);
        entity = new HeroSpacman(baseWorld,0f,0f,0f);
    }


    /**
     * Test constructor with a string parameter
     */
    @Test
    public void testBaseWorldConstructorWithStrings(){
        baseWorld = new BaseWorld("resources/mapAssets/tinyMars.tmx");
        //This test proofs a collisionMap was instantiated therefore map was successfully loaded
        Assert.assertTrue(baseWorld.getEntities().size() == 0);
    }

    /**
     * Test adding entity in a world that hasn't load its map yet.
     */
    @Test
    public void testAddEntity(){
        baseWorld.addEntity(entity);
        List<BaseEntity> entities = new ArrayList<>(baseWorld.getEntities(0,0));
        Assert.assertEquals(1,entities.size());
    }

    /**
     * Test the initial state of the collision map before loading a map
     */
    @Test
    public void testGetCollisionMap(){
        Array2D<List<BaseEntity>> collisionMap = baseWorld.getCollisionMap();
        for(int x = 0; x < collisionMap.getWidth(); x++){
            // NOTE: length = height
            for(int y = 0; y < collisionMap.getLength(); y++){
                Assert.assertTrue(collisionMap.get(x,y).isEmpty());
            }
        }
    }

    /**
     * Test if has entities.
     */
    @Test
    public void testHasEntity(){
        Assert.assertFalse(baseWorld.hasEntity(0,0));
        baseWorld.addEntity(entity);
        Assert.assertTrue(baseWorld.hasEntity(0,0));
    }

    /**
     * Test if can get an entity in a world that hasn't load its map yet.
     */
    @Test
    public void testGetEntity(){
        baseWorld.addEntity(entity);
        BaseEntity clone = baseWorld.getEntities(0,0).get(0);
        Assert.assertTrue(clone.equals(entity));
    }

    /**
     * Test if can get an entity with invalid coordinates.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntityWithIndexOutOfBounds(){
        baseWorld.addEntity(entity);
        baseWorld.getEntities(-10,0).get(0);
    }

    /**
     * Test removing an entity in a world that hasn't load its map yet.
     */
    @Test
    public void testRemoveEntity(){
        // should't fail because removeEntity() is called in AbstractWold.class
        // removes entity on an empty world
        baseWorld.removeEntity(entity);
    }

    /**
     * Test setting the width of a world that hasn't load its map yet.
     */
    @Test
    public void testSetWidth(){
        baseWorld.setWidth(12);
        Assert.assertEquals("World's width should be 12", 12,
                baseWorld.getWidth());
    }

    /**
     * Test setting the length(height) of a world that hasn't load its map yet.
     */
    @Test
    public void testSetLength(){
        baseWorld.setLength(17);
        Assert.assertEquals("World's length should be 17", 17,
                baseWorld.getLength());
    }

    /**
     * Test setting the length(height) of a world that hasn't load its map yet,
     * after setting its width and height.
     */
    @Test
    public void testGetNumberOfTiles(){
        baseWorld.setLength(15);
        baseWorld.setWidth(10);
        Assert.assertEquals("Number of tiles should be 150",
                150, baseWorld.getNumberOfTiles());
    }
}
