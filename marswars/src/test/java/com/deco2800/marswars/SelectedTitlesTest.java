package com.deco2800.marswars;

import com.deco2800.marswars.entities.MultiSelectionTile;
import com.deco2800.marswars.worlds.SelectedTiles;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Nick on 22/9/17.
 */
public class SelectedTitlesTest {
    @Test
    public void constructorTest() {
        SelectedTiles tiles = new SelectedTiles();
        Assert.assertTrue(tiles != null);
        Assert.assertTrue(tiles.getMultiSelectionTile() != null);
    }

    @Test
    public void initializeTest() {
        SelectedTiles tiles = new SelectedTiles();
        Assert.assertTrue(tiles != null);
        tiles.initializeSelectedTiles(10, 10);
    }
}
