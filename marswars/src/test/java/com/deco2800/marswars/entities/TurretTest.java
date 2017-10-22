package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.buildings.Turret;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * @author haoxuan on 17/10/17
 */

public class TurretTest {
	BaseWorld world;
    Turret tu;
	
	@Before
	public void setUp(){
		world = new BaseWorld(10, 10);
		tu = new Turret(world, 1, 1, 1, -1);
	}

	@Test
	public void constrcutorTest() {
		Assert.assertTrue(tu != null);
        Assert.assertFalse(tu.showProgress());
	}
	
	@Test
    public void testActions(){
        DecoAction action = Mockito.mock(DecoAction.class);
        tu.setAction(action);
        Assert.assertTrue(tu.showProgress());
    }
	
	@Test
    public void checkOwner() {
        Turret tu = new Turret(world, 0,1,1,0);
        Turret tu2 = new Turret(world, 0,0,1,1);
        Turret tu3 = new Turret(world, 0,0,1,1);
        assertEquals(tu.getOwner(), 0);
        assertFalse(tu.sameOwner(tu2));
        assertTrue(tu2.sameOwner(tu3));
    }

    @Test
    public void checkProgress() {
        Assert.assertFalse(tu.showProgress());
        Assert.assertEquals(0, tu.getProgress());
        DecoAction action = Mockito.mock(DecoAction.class);
        tu.giveAction(action);
        Assert.assertTrue(tu.showProgress());
    }

    @Test
    public void checkSelection() {
        Assert.assertFalse(tu.isSelected());
    }

}
