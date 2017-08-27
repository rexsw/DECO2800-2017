package com.deco2800.marswars.entities;

import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class WaterTest {

    Water w;
    BaseWorld baseWorld;

    @Before
    public void setup(){
        baseWorld = new BaseWorld(10 ,15);
        w = new Water(baseWorld, 0, 0, 0, 0, 0);
    }

    @Test
    public void constructorTest() {
        Assert.assertTrue(w != null);
        Assert.assertEquals("water", w.getTexture());
    }

    @Test
    public void healthTest() {
        w.setHealth(20);
        Assert.assertEquals(0, w.getHealth());
    }
}
