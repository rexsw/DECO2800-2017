package com.deco2800.marswars.heros;


import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.deco2800.marswars.BaseTest;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.GenerateAction;
import com.deco2800.marswars.buildings.HeroFactory;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.managers.ColourManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class HeroFactoryTest extends BaseTest{

    HeroFactory heroFactory;
    DecoAction action;
    Commander hero;
    BaseWorld world;

    @Before
    public void setUp() {
		ColourManager cm = (ColourManager) GameManager.get()
				.getManager(ColourManager.class);
        cm.setColour(-1);
        cm.setColour(0);
        cm.setColour(1);
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
        assertTrue(heroFactory.getBuildSize() == 2f);
        assertTrue(heroFactory.getHealth() == 2800);
        assertTrue(heroFactory.toString() == "HeroFactory");
        assertTrue(heroFactory.isFlooded() == false);
        assertTrue(heroFactory.getBuilt() == true);
        assertTrue(heroFactory.getAction().equals(Optional.empty()));
        assertTrue(heroFactory.isSelected() == false);
        assertTrue(heroFactory.showProgress() == false);
        assertTrue(heroFactory.getProgress() == 0);
    }

    /**
     * Test to check that the giveAction and getAction methods work correctly.
     */
    @Test @Ignore
    public void actionTest() {
    	 heroFactory.setAction(action);
         assertTrue(heroFactory.getAction().get().equals(action));
      //   assertTrue(heroFactory.isWorking());
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
    @Test @Ignore
    public void selectionTest() {
        assertFalse(heroFactory.isSelected());
      //  heroFactory.select();
        assertTrue(heroFactory.isSelected());
        heroFactory.deselect();
        assertFalse(heroFactory.isSelected());
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
    
    /**
     * Test the create hero button of hero factory
     * also, fire up the pressed method, check if the resource has decreased accordingly
     */
    @Test @Ignore
    public void buttonTest() {
    	TextButton button = (TextButton) heroFactory.getButton();
    	
    	assertEquals("Create Hero", button.getText().toString());
    	
    	ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
    	rm.setBiomass(25, heroFactory.getOwner());
    	heroFactory.buttonWasPressed();
    	assertFalse(rm.getBiomass(heroFactory.getOwner()) == -5);
    	
    	rm.setBiomass(100, heroFactory.getOwner());
    	heroFactory.buttonWasPressed();
    	assertEquals(70, rm.getBiomass(heroFactory.getOwner()));
    }

    /**
     * Test if the help message is what we expect
     */
    @Test @Ignore
    public void helperTest() {
    	Label actual = heroFactory.getHelpText();
    	assertEquals("You have clicked on a hero factory.", actual.getText().toString());
    }
}
