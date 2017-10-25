package com.deco2800.marswars.entities;

import com.deco2800.marswars.entities.units.Hacker;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author haoxuan on 23/09/17
 *
 */

public class HackerTest {
    @Test
    public void constructorTest() {
        Hacker hacker = new Hacker(1, 0, 0 , 1);
        Assert.assertTrue(hacker != null);
    }

}
