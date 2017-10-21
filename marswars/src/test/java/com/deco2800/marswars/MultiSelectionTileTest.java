package com.deco2800.marswars;

import com.deco2800.marswars.entities.MultiSelectionTile;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Treenhan on 10/21/17.
 */
public class MultiSelectionTileTest {
    @Test
    public void testCreatingMultiSelectionTile(){
        MultiSelectionTile tile = new MultiSelectionTile(1,1,1,1,1);
        Assert.assertNotNull(tile);
    }
}
