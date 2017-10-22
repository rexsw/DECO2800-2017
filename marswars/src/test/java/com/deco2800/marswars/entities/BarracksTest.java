package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.buildings.Barracks;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * @author haoxuan on 12/10/17
 */

public class BarracksTest {
    BaseWorld world;
    Barracks bk;

    @Before
    public void setup(){
        world = new BaseWorld(15, 15);
        bk = new Barracks(world, 0, 0, 0, 1);
    }
    
    @Test
    public void constructorTest(){
        Assert.assertTrue(bk != null);
        Assert.assertFalse(bk.showProgress());

    }

    @Test
    public void testActions(){
        DecoAction action = Mockito.mock(DecoAction.class);
        bk.setAction(action);
        Assert.assertTrue(bk.showProgress());
    }
    
    @Test
    public void checkOwner() {
        Barracks bk = new Barracks(world, 0,1,1,0);
        Barracks bk2 = new Barracks(world, 0,0,1,1);
        Barracks bk3 = new Barracks(world, 0,0,1,1);
        assertEquals(bk.getOwner(), 0);
        assertFalse(bk.sameOwner(bk2));
        assertTrue(bk2.sameOwner(bk3));
    }

    @Test
    public void checkProgress() {
        Assert.assertFalse(bk.showProgress());
        Assert.assertEquals(0, bk.getProgress());
        DecoAction action = Mockito.mock(DecoAction.class);
        bk.giveAction(action);
        Assert.assertTrue(bk.showProgress());
    }

    @Test
    public void checkSelection() {
        Assert.assertFalse(bk.isSelected());
    }
    
}
