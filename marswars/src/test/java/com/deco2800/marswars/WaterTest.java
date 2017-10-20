package com.deco2800.marswars;

import com.deco2800.marswars.entities.weatherentities.Water;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test class for the Water entity.
 *
 * @author Isaac Doidge
 */
public class WaterTest {

    private Water testDrop;
    // Tests for weather-associated resources

    @Before
    public void initalise() {
        testDrop = new Water(1, 1, 0);
    }

    @Test
    public void testConstruction() {
        assertTrue(testDrop.getHealth() == 10);
        assertTrue(testDrop.isSurrounded() == false);
        testDrop.setSurrounded();
        assertTrue(testDrop.isSurrounded() == true);
        testDrop.setHealth(100);
        assertTrue(testDrop.getHealth() == 100);
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
}
