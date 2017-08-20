package com.deco2800.marswars;

import com.deco2800.marswars.worlds.BaseWorld;


/**
 * Tests for all methods of the BaseWorld class and the AbstractWorld class.
 *
 * @author @luanlucho
 *
 * @see com.deco2800.marswars.worlds.AbstractWorld
 * @see BaseWorld
 */
public class BaseWorldTest {

//    // a base world mock object
//    BaseWorld baseWorld;
//    // an entity mock object
//    BaseEntity entity;
//
//    @Before
//    public void setup(){
//        baseWorld = new BaseWorld();
//        entity = new HeroSpacman(baseWorld,0f,0f,0f);
//    }
//
//    /**
//     * Test the number of tiles a world should have before loading its map.
//     */
//    @Test
//    public void testGetNumberOfTilesBeforeLoadingAMap(){
//        Assert.assertEquals("Number of tiles should be 0"
//                ,0, baseWorld.getNumberOfTiles());
//    }
//
//    /**
//     * Test adding entity in a world that hasn't load its map yet.
//     */
//    @Test (expected = NullPointerException.class)
//    public void testAddEntityWithoutLoadingAMap(){
//        baseWorld.addEntity(entity);
//    }
//
//    /**
//     * Test the initial state of the collision map before loading a map
//     */
//    @Test(expected = NullPointerException.class)
//    public void testGetCollisionMapWithoutLoadingAMap(){
//        Array2D<List<BaseEntity>> collisionMap = baseWorld.getCollisionMap();
//        for(int x = 0; x < collisionMap.getWidth(); x++){
//            // NOTE: length = height
//            for(int y = 0; y < collisionMap.getLength(); y++){
//                // make it throw exception
//                collisionMap.get(x,y);
//            }
//        }
//    }
//
//    /**
//     * Test if has entities in a world that hasn't load its map yet.
//     */
//    @Test (expected = NullPointerException.class)
//    public void testHasEntityWithoutLoadingAMap(){
//        baseWorld.hasEntity(0,0);
//    }
//
//    /**
//     * Test if can get an entity in a world that hasn't load its map yet.
//     */
//    @Test (expected = NullPointerException.class)
//    public void testGetEntityWithoutLoadingAMap(){
//        baseWorld.getEntities(0,0);
//    }
//
//    /**
//     * Test removing an entity in a world that hasn't load its map yet.
//     */
//    @Test
//    public void testRemoveEntityWithoutLoadingAMap(){
//        // should't fail because removeEntity() is called in AbstractWold.class
//        baseWorld.removeEntity(entity);
//    }
//
//    /**
//     * Test setting the width of a world that hasn't load its map yet.
//     */
//    @Test
//    public void testSetWidthWithoutLoadingAMap(){
//        baseWorld.setWidth(10);
//        Assert.assertEquals("World's width should be 10", 10,
//                baseWorld.getWidth());
//    }
//
//    /**
//     * Test setting the length(height) of a world that hasn't load its map yet.
//     */
//    @Test
//    public void testSetLengthWithoutLoadingAMap(){
//        baseWorld.setLength(15);
//        Assert.assertEquals("World's length should be 15", 15,
//                baseWorld.getLength());
//    }
//
//    /**
//     * Test setting the length(height) of a world that hasn't load its map yet,
//     * after setting its width and height.
//     */
//    @Test
//    public void testGetNumberOfTilesWithoutLoadingAMapAfterSettingDimensions(){
//        baseWorld.setLength(15);
//        baseWorld.setWidth(10);
//        Assert.assertEquals("Number of tiles should be 150",
//                150, baseWorld.getNumberOfTiles());
//    }
}
