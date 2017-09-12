package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.entities.buildings.Base;
import com.deco2800.marswars.managers.Manager;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Nicholas on 27-Aug-17.
 */
public class BaseTest {
    BaseWorld world;
    Base b;

    @Before
    public void setup(){
        world = new BaseWorld(10, 15);
        b = new Base(world, 0, 0, 0);
    }

    @Test
    public void constructorTest(){
        Assert.assertTrue(b != null);
        Assert.assertEquals("homeBase", b.getTexture());
        Assert.assertEquals(250, b.getCost());
        Assert.assertFalse(b.isWorking());

    }


    @Test
    public void testActions(){
        DecoAction action = Mockito.mock(DecoAction.class);
        b.setAction(action);
        Assert.assertTrue(b.isWorking());
    }

    @Test
    public void checkProgress() {
        Assert.assertFalse(b.showProgress());
        Assert.assertEquals(0, b.getProgress());
        DecoAction action = Mockito.mock(DecoAction.class);
        b.giveAction(action);
        Assert.assertTrue(b.showProgress());
    }

    @Test
    public void checkSelection() {
        Assert.assertFalse(b.isSelected());
    }
}
