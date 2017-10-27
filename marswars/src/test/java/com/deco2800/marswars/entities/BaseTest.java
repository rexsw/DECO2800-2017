package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.managers.ColourManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Created by Nicholas on 27-Aug-17.
 */
public class BaseTest {
    BaseWorld world;
    Base b;

    @Before
    public void setup(){
        world = new BaseWorld(10, 15);
		GameManager.get().getManager(TextureManager.class);
		ColourManager cm = (ColourManager) GameManager.get()
				.getManager(ColourManager.class);
        cm.setColour(-1);
        cm.setColour(0);
        cm.setColour(1);
        b = new Base(world, 0, 0, 0, -1);
    }

    @Test
    public void constructorTest(){
        Assert.assertTrue(b != null);
        Assert.assertFalse(b.showProgress());

    }


    @Test
    public void testActions(){
        DecoAction action = Mockito.mock(DecoAction.class);
        b.setAction(action);
        Assert.assertTrue(b.showProgress());
    }

    @Test
    public void checkOwner() {
        Base b = new Base(world, 1,1,1, 0);
        Base b2 = new Base(world, 1,1,1, 1);
        Base b3 = new Base(world, 1,1,1, 1);
        assertEquals(b.getOwner(), 0);
        assertFalse(b.sameOwner(b2));
        assertTrue(b2.sameOwner(b3));
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
