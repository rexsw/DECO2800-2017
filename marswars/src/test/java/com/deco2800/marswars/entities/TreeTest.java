package com.deco2800.marswars.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TreeTest {

    Tree t;

    @Before
    public void setup(){
        t = new Tree(0, 0, 0);
    }

    @Test
    public void constructorTest() {
        Assert.assertTrue(t != null);
        Assert.assertEquals("tree", t.getTexture());
    }

    @Test
    public void checkProgress() {
        Assert.assertEquals(100, t.getProgress());
        Assert.assertTrue(t.showProgress());
    }
}
