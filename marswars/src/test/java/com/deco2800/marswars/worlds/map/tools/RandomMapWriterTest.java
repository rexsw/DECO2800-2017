package com.deco2800.marswars.worlds.map.tools;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.deco2800.marswars.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Nicholas on 14-Sep-17.
 */
public class RandomMapWriterTest extends BaseTest {
    String mapPath;
    int length;
    int width;
    ArrayList tilesToAdd;
    RandomMapWriter randomTiles;

    @Before
    public void setup(){
        mapPath = RandomMapWriter.FILENAME;
        tilesToAdd= new ArrayList<Integer>();
        tilesToAdd.add(new Integer(11));
        tilesToAdd.add(new Integer(16));
        tilesToAdd.add(new Integer(12));
        tilesToAdd.add(new Integer(18));

    }
    @Test
    public void constructorTest() throws Exception {
        randomTiles = new RandomMapWriter(100, 100, tilesToAdd, new NoiseMap(100,100));
        Assert.assertTrue(randomTiles != null);
    }
    @Test
    public void testWritingMap() throws Exception {
        randomTiles = new RandomMapWriter(100, 100, tilesToAdd, new NoiseMap(100,100));
        randomTiles.addTile(1,1,10);
        try{
            randomTiles.writeMap();
        }catch(Exception e){
        }
        Assert.assertEquals("resources/mapAssets/tmap.tmx", randomTiles.FILENAME);


    }
    @Test
    public void testLoadingMap() throws Exception {
        randomTiles = new RandomMapWriter(100, 100, tilesToAdd, new NoiseMap(100,100));
        randomTiles.addTile(1,1,10);
        try{
            randomTiles.writeMap();
        }catch(Exception e){
        }
        TiledMap mockMap = new TmxMapLoader().load(randomTiles.FILENAME);
        width = mockMap.getProperties().get("width", Integer.class);
        length = mockMap.getProperties().get("height", Integer.class);
        Assert.assertTrue(mockMap != null);
        Assert.assertEquals(100, width);
        Assert.assertEquals(100, length);
    }




}
