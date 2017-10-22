package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.buildings.TechBuilding;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * @author haoxuan on 12/10/17
 */

public class TechBuildingTest {
	BaseWorld world;
    TechBuilding t;
    
    @Before
    public void setup(){
        world = new BaseWorld(10, 20);
        t = new TechBuilding(world, 1, 1, 0, 1);
    }
    
    @Test
    public void constructorTest(){
        Assert.assertTrue(t != null);
        Assert.assertFalse(t.showProgress());

    }

    @Test
    public void testActions(){
        DecoAction action = Mockito.mock(DecoAction.class);
        t.setAction(action);
        Assert.assertTrue(t.showProgress());
    }
    
    @Test
    public void checkOwner() {
    	TechBuilding t = new TechBuilding(world, 0,0,1,0);
        TechBuilding t2 = new TechBuilding(world, 1,0,1,1);
        TechBuilding t3 = new TechBuilding(world, 1,0,1,1);
        assertEquals(t.getOwner(), 0);
        assertFalse(t.sameOwner(t2));
        assertTrue(t2.sameOwner(t3));
    }

    @Test
    public void checkProgress() {
        Assert.assertFalse(t.showProgress());
        Assert.assertEquals(0, t.getProgress());
        DecoAction action = Mockito.mock(DecoAction.class);
        t.giveAction(action);
        Assert.assertTrue(t.showProgress());
    }

    @Test
    public void checkSelection() {
        Assert.assertFalse(t.isSelected());
    }
    

}
