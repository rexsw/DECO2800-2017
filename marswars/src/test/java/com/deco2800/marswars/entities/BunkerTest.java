package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.buildings.Bunker;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author haoxuan on 12/10/17
 */

public class BunkerTest {
	BaseWorld world;
    Bunker br;
    
    @Before
    public void setup(){
        world = new BaseWorld(10, 10);
        br = new Bunker(world, 1, 0, 0, 1);
    }
    
    @Test
    public void constructorTest(){
        Assert.assertTrue(br != null);
        Assert.assertFalse(br.showProgress());

    }

    @Test
    public void testActions(){
        DecoAction action = Mockito.mock(DecoAction.class);
        br.setAction(action);
        Assert.assertTrue(br.showProgress());
    }
    

    @Test
    public void checkProgress() {
        Assert.assertFalse(br.showProgress());
        Assert.assertEquals(0, br.getProgress());
        DecoAction action = Mockito.mock(DecoAction.class);
        br.giveAction(action);
        Assert.assertTrue(br.showProgress());
    }

    @Test
    public void checkSelection() {
        Assert.assertFalse(br.isSelected());
    }
  
}
