package com.deco2800.marswars.entities;

import com.deco2800.marswars.entities.units.Tank;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Nick on 22/9/17.
 */
public class TankTest {
    @Test
    public void constructorTest() {
        Tank tank = new Tank(0, 0, 0 , 1);
        Assert.assertTrue(tank != null);
    }

    @Test
    public void getStatTest(){
        Tank tank = new Tank(0, 0, 0 , 1);
        Assert.assertTrue(tank.getStats() != null);
        Assert.assertEquals(2500, tank.getStats().getHealth());
        Assert.assertEquals(2500, tank.getStats().getMaxHealth());
        Assert.assertEquals("Tank", tank.getStats().getName());
    }
}
