package com.deco2800.marswars;

import com.deco2800.marswars.buildings.Turret;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.entities.weatherentities.Water;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.managers.WeatherManager;
import com.deco2800.marswars.util.Point;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The test class for WeatherManager. Simulates a flood.
 *
 * @author Isaac Doidge
 */
public class WeatherManagerTest {


    private TimeManager timeManager =
            (TimeManager) GameManager.get().getManager(TimeManager.class);
    private Water testDrop;
    private BaseWorld world;

    @Before
    public void initalise() {
        world = new BaseWorld(5, 5);
        GameManager.get().setWorld(world);
        testDrop = new Water(2, 3, 0);
        GameManager.get().getWorld().addEntity(testDrop);
        timeManager.setGameStartTime();
        timeManager.resetInGameTime();
    }

//    @Test
    public void testSetWeatherEvent() {
    /* Set weatherManager in each class in case unforseen changes occur to
        class variables in WeatherManager (prevent build errors) */

        WeatherManager weatherManager = (WeatherManager)
                GameManager.get().getManager(WeatherManager.class);
        GameManager.get().setWorld(world);
        timeManager.resetInGameTime();
        timeManager.addTime(57600);
        // check flood is in effect
        weatherManager.toggleFlood(true);
        assertTrue(weatherManager.setWeatherEvent());
        // initalise building for testing
        Turret affectedBuilding = new Turret(world, 2,
                3, 0, 0);
        GameManager.get().getWorld().addEntity(affectedBuilding);
        affectedBuilding.setBuilt(true);
        assertFalse(affectedBuilding.isFlooded());

        // set up entities to be affected by flood
        for (int i = 0; i < 5; i++) {
        	Astronaut placeHolderUnit = new Astronaut(i, i, 0, -1);
            GameManager.get().getWorld().addEntity(placeHolderUnit);
            /* Astronauts are added to the collision map on construction  and as
            such are still affected by DoT */
            Astronaut affectedUnit = new Astronaut(i, i, 0, 0);
            affectedUnit.setMaxHealth(1000);
        }

        Soldier soldier1 = new Soldier(4, 3, 0, -1);
        Soldier soldier2 = new Soldier(2, 3, 0, -1);
        GameManager.get().getWorld().addEntity(soldier1);
        GameManager.get().getWorld().addEntity(soldier2);
        soldier2.setLoaded();

        /* Generate multiple water entities in order to test efficacy of private
        methods and their various conditions: Covers checking for existing water
        and bad water placement. */

        while (GameManager.get().getWorld().getEntities().size() < 14) {
            assertTrue(weatherManager.setWeatherEvent());
        }

        assertTrue(affectedBuilding.isFlooded());

        // Wait requisite time for continuous damage application
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        // check waters retreat correctly
        timeManager.resetInGameTime();
        timeManager.addTime(7200);
        assertTrue(weatherManager.retreatWaters());
        weatherManager.setWeatherEvent();

        // Remove all floodwater from map
        while (weatherManager.setWeatherEvent()) {
        /*Wait for system time to advance sufficiently for interval between
        retreatWaters() calls to be satisfied */
            try {
                Thread.sleep(11);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        // ensure flood has receded
        assertFalse(weatherManager.setWeatherEvent());
        assertFalse(affectedBuilding.isFlooded());
    }

    @Test
    public void testGenerateFirstDrop() {
        world.removeEntity(testDrop);
        timeManager.resetInGameTime();
        timeManager.addTime(57600);
        WeatherManager weatherManager = (WeatherManager)
                GameManager.get().getManager(WeatherManager.class);
        assertTrue(weatherManager.setWeatherEvent());
    }

    @Test
    public void testIsRaining() {
        timeManager.resetInGameTime();
        timeManager.addTime(3700);
        WeatherManager weatherManager = (WeatherManager)
                GameManager.get().getManager(WeatherManager.class);
        assertFalse(weatherManager.isRaining());
    }

    @Test
    public void testCheckPosition() {
        WeatherManager weatherManager = (WeatherManager)
                GameManager.get().getManager(WeatherManager.class);
        BaseWorld testWorld = GameManager.get().getWorld();
        int width = testWorld.getWidth();
        int length = testWorld.getLength();
        Point validPoint = new Point(width - 1, length - 1);
        Point boundary1 = new Point(0, length);
        Point boundary2 = new Point(width, 0);
        Point badX = new Point(width + 1, length - 1);
        Point badX2 = new Point(-1, length - 1);
        Point badY = new Point(width - 1, length + 1);
        Point badY2 = new Point(width - 1, -1);

        assertFalse(weatherManager.checkPosition(validPoint));
        assertFalse(weatherManager.checkPosition(boundary1));
        assertFalse(weatherManager.checkPosition(boundary2));
        assertTrue(weatherManager.checkPosition(badX));
        assertTrue(weatherManager.checkPosition(badX2));
        assertTrue(weatherManager.checkPosition(badY));
        assertTrue(weatherManager.checkPosition(badY2));
    }

    @Test
    public void testOnTick() {
        WeatherManager weatherManager = (WeatherManager)
                GameManager.get().getManager(WeatherManager.class);
        timeManager.resetInGameTime();
        timeManager.addTime(3700);
        weatherManager.onTick(0);
        assertFalse(weatherManager.isRaining());
    }


}
