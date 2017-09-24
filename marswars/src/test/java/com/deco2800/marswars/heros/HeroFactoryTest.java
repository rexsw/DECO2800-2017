package com.deco2800.marswars.heros;


import static org.junit.Assert.*;

import com.deco2800.marswars.buildings.HeroFactory;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import com.deco2800.marswars.actions.*;

/**
 * Created by  Zeid Ismail on 23/09/17
 */
public class HeroFactoryTest {

    HeroFactory heroFactory;
    DecoAction action;
    Commander hero;
    BaseWorld world;

    @Before
    public void setUp() {
        world = new BaseWorld(10, 10);
        hero = new Commander(3,3,3,-1);
        heroFactory = new HeroFactory(world, 2, 2, 2, -1);
        action = new GenerateAction(hero);
    }
    /**
     * Constructor test. Checks to see that hero factory has the right owner,
     * is the right size, has the right health, toString, name and progress, is
     * not selected or flooded.
     */
    @Test
    public void constructorTest() {
        assertFalse(heroFactory.getOwner() == 1);
        // -1 means not AI
        assertTrue(heroFactory.getOwner() == -1);
        assertTrue(heroFactory.getBuildSize() == 3f);
        assertTrue(heroFactory.getHealth() == 3000);
        assertTrue(heroFactory.toString() == "Hero Factory");
        assertTrue(heroFactory.getBuildSize() == 3f);
        assertTrue(heroFactory.isFlooded() == false);
        assertTrue(heroFactory.getBuilt() == true);
        assertTrue(heroFactory.getAction().equals(Optional.empty()));
        assertTrue(heroFactory.isSelected() == false);
        assertTrue(heroFactory.showProgress() == false);
        assertTrue(heroFactory.getProgress() == 0);
        assertTrue(heroFactory.getbuilding() == "Hero Factory");
    }

    /**
     * Test to check that the giveAction and getAction methods work correctly.
     */
    @Test
    public void actionTest() {
    	 heroFactory.giveAction(action);
         assertTrue(heroFactory.getAction().get().equals(action));
    }

    /**
     * Test to check that the setBuilt and getBuilt methods work correctly.
     */
    @Test
    public void builtTest() {
        heroFactory.setBuilt(false);
        assertTrue(heroFactory.getBuilt() == false);
    }

    /**
     * Test to check that the setFlooded and isFlooded methods work correctly.
     */
    @Test
    public void floodingTest() {
        heroFactory.setFlooded(true);
        assertTrue(heroFactory.isFlooded() == true);
        heroFactory.setFlooded(false);
        assertTrue(heroFactory.isFlooded() == false);
    }

    /**
     * Test to check that hero factory is not selected.
     */
    @Test
    public void selectionTest() {
        assertFalse(heroFactory.isSelected());
        heroFactory.select();
        assertTrue(heroFactory.isSelected());
    }

    /**
     * Test to check that hero factories with different owners have their
     * ownership working correctly.
     */
    @Test
    public void checkOwner() {
        HeroFactory heroFactory1 = new HeroFactory(world, 1,1,1, 0);
        HeroFactory heroFactory2 = new HeroFactory(world, 1,1,1, 1);
        HeroFactory heroFactory3 = new HeroFactory(world, 1,1,1, 1);
        assertEquals(heroFactory1.getOwner(), 0);
        assertFalse(heroFactory1.sameOwner(heroFactory2));
        assertTrue(heroFactory2.sameOwner(heroFactory3));
    }

}