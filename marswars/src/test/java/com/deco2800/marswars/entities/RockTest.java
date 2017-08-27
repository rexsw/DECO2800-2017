package com.deco2800.marswars.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class RockTest {

    Rock r;

    @Before
    public void setup(){
        r = new Rock(0, 0, 0, 0, 0);
    }

    @Test
    public void constructorTest() {
        Assert.assertTrue(r != null);
    }

    @Test
    public void healthTest() {
        r.setHealth(20);
        Assert.assertEquals(20, r.getHealth());
    }
}
