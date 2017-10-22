package com.deco2800.marswars.hud;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.managers.*;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test is used to test the CodeInterpreter class.
 */
public class CodeInterpreterTest {
    private static TimeManager tm = (TimeManager)GameManager.get().getManager(TimeManager.class);
    private static ResourceManager rm = (ResourceManager)GameManager.get().getManager(ResourceManager.class);
    private static TechnologyManager tem = (TechnologyManager)GameManager.get().getManager(TechnologyManager.class);
    private static FogManager fm = (FogManager)GameManager.get().getManager(FogManager.class);
    private static HeadlessApplication game;
    private CodeInterpreter a;
    BaseWorld baseWorld;
    Soldier entity1;
    Soldier entity2;
    Soldier entity3;
    Soldier entity4;


    @Before
    public void setUp() throws Exception {
        entity1 =  new Soldier(0,0,0,1);
        entity2 =  new Soldier(0,0,0,1);
        entity3 =  new Soldier(0,0,0,1);
        entity4 =  new Soldier(0,0,0,1);
        baseWorld = new BaseWorld(20, 20);
        MiniMap m = new MiniMap();
        GameManager.get().setMiniMap(m);
        GameManager.get().setWorld(baseWorld);
        GameManager.get().getWorld().addEntity(entity1);
        GameManager.get().getWorld().addEntity(entity2);
        GameManager.get().getWorld().addEntity(entity3);
        GameManager.get().getWorld().addEntity(entity4);
        fm.initialFog(2, 2);
        a = new CodeInterpreter();

    }

    /**
     * to test if killone() can delete one enemy soldier successfully;
     */
    @Test
    public void reduceOneEnemy() throws Exception {
        int num1 = GameManager.get().getWorld().getEntities().size();
        a.killone();
        int num2 = GameManager.get().getWorld().getEntities().size();
        assertEquals(num1,num2+1);
    }

    /**
     * to test if killall() can delete all enemy soldiers successfully;
     */
    @Test
    public void reduceAllEnemy() throws Exception {
        int num1 = GameManager.get().getWorld().getEntities().size();
        assertEquals(num1,4);
        a.killall();
        int num2 = GameManager.get().getWorld().getEntities().size();
        assertEquals(num2,0);
    }

    /**
     * to test if rock() can add specified rock successfully;
     */
    @Test
    public void addRock() throws Exception {
        int or = rm.getRocks(-1);
        a.rock(100);
        assertEquals(or+100,rm.getRocks(-1));






    }

    /**
     * to test if biomass() can add specified biomass successfully;
     */
    @Test
    public void addBiomass() throws Exception {
        int or = rm.getBiomass(-1);
        a.biomass(100);
        assertEquals(or+100,rm.getBiomass(-1));
    }

    /**
     * to test if crystal() can add specified crystal successfully;
     */
    @Test
    public void addCrystal() throws Exception {
        int or = rm.getCrystal(-1);
        a.crystal(100);
        assertEquals(or+100,rm.getCrystal(-1));
    }

    /**
     * to test if game time hour becomes 6 am after day();
     */
    @Test
    public void switchDay() throws Exception {
        a.day();
        assertEquals(tm.getHours(),6);
    }


    /**
     * to test if game time hour becomes 21 pm after night();
     */
    @Test
    public void switchNight() throws Exception {
        a.night();
        assertEquals(tm.getHours(),21);
    }




}