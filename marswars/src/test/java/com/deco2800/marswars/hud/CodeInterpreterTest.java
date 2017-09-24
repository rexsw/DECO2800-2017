package com.deco2800.marswars.hud;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.hud.CodeInterpreter;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.deco2800.marswars.MarsWars;
import com.deco2800.marswars.managers.*;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This test is used to test the CodeInterpreter class.
 */
public class CodeInterpreterTest {
    private static TimeManager tm;
    private static ResourceManager rm;
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
        FogManager fogOfWar = (FogManager)(GameManager.get().getManager(FogManager.class));
        FogManager.initialFog(2, 2);
        a = new CodeInterpreter();
        Manager manager = GameManager.get().getManager(ResourceManager.class);
        rm = (ResourceManager)manager;
        Manager manager2 = GameManager.get().getManager(TimeManager.class);
        tm = (TimeManager)manager2;
    }

    /**
     * to test if reduceOneEnemy() can delete one enemy soldier successfully;
     */
    @Test
    public void reduceOneEnemyTest() throws Exception {
        int num1 = GameManager.get().getWorld().getEntities().size();
        a.reduceOneEnemy();
        int num2 = GameManager.get().getWorld().getEntities().size();
        assertEquals(num1,num2+1);
    }

    /**
     * to test if reduceAllEnemy() can delete all enemy soldiers successfully;
     */
    @Test
    public void reduceAllEnemyTest() throws Exception {
        int num1 = GameManager.get().getWorld().getEntities().size();
        assertEquals(num1,4);
        a.reduceAllEnemy();
        int num2 = GameManager.get().getWorld().getEntities().size();
        assertEquals(num2,0);
    }

    /**
     * to test if addRock() can add specified rock successfully;
     */
    @Test
    public void addRock() throws Exception {
        int or = rm.getRocks(-1);
        a.addRock(100);
        assertEquals(or+100,rm.getRocks(-1));
    }

    /**
     * to test if addBiomass() can add specified biomass successfully;
     */
    @Test
    public void addBiomass() throws Exception {
        int or = rm.getBiomass(-1);
        a.addBiomass(100);
        assertEquals(or+100,rm.getBiomass(-1));
    }

    /**
     * to test if addCrystal() can add specified crystal successfully;
     */
    @Test
    public void addCrystal() throws Exception {
        int or = rm.getCrystal(-1);
        a.addCrystal(100);
        assertEquals(or+100,rm.getCrystal(-1));
    }

    /**
     * to test if addWater() can add specified water successfully;
     */
    @Test
    public void addWater() throws Exception {
        int or = rm.getWater(-1);
        a.addWater(100);
        assertEquals(or+100,rm.getWater(-1));
    }

    /**
     * to test if game time hour becomes 6 am after switchDay();
     */
    @Test
    public void switchDay() throws Exception {
        a.switchDay();
        assertEquals(tm.getHours(),6);
    }


    /**
     * to test if game time hour becomes 21 pm after switchNight();
     */
    @Test
    public void switchNight() throws Exception {
        a.switchNight();
        assertEquals(tm.getHours(),21);
    }

}