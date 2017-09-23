package com.deco2800.marswars;

import com.deco2800.marswars.entities.weatherEntities.Water;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.WeatherManager;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class WeatherManagerTest {

    private WeatherManager weatherManager = (WeatherManager)
            GameManager.get().getManager(WeatherManager.class);
    private Water testDrop;
/*
    @Before
    public void initalise() {
        testDrop = new Water(1, 1, 0);
    }


    @Test
    public void testSetSurrounded() {
        assertTrue(testDrop.isSurrounded() == false);
        testDrop.setSurrounded();
        assertTrue(testDrop.isSurrounded() == true);
    }

    @Test
    public void testSetHealth() {
        testDrop.setHealth(100);
        assertTrue(testDrop.getHealth() == 100);
    }

    @Test
    public void testOnTick() {
        GameManager.get().setWorld(new BaseWorld(20, 20));
        testDrop.onTick(0);
        assertTrue(true);
    }
    */
}
