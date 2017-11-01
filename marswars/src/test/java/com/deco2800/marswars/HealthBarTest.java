package com.deco2800.marswars;

import com.deco2800.marswars.buildings.GateHorizontal;
import com.deco2800.marswars.entities.HealthBar;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.hud.MiniMap;
import com.deco2800.marswars.managers.ColourManager;
import com.deco2800.marswars.managers.GameBlackBoard;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Hayden Bird on 22/10/2017.
 */

public class HealthBarTest {
    Soldier t1,t3,t4,t5,t6;
    GateHorizontal t2;
    HealthBar healthBar;
    BaseWorld baseWorld;

    @Before
    public void setup(){
        baseWorld = new BaseWorld(10 ,10);
        GameManager.get().setWorld(baseWorld);
        GameManager.get().setMiniMap(new MiniMap());
        GameManager.get().getManager(GameBlackBoard.class);
		ColourManager cm = (ColourManager) GameManager.get()
				.getManager(ColourManager.class);
		ResourceManager rm = (ResourceManager) GameManager.get()
				.getManager(ResourceManager.class);
        cm.setColour(1);
        t1 = new Soldier(0,1,0,0);
        t2 = new GateHorizontal(baseWorld,3,2,0,1);
        t3 = new Soldier(0,3,0,0);
        t4 = new Soldier(0,4,0,0);
        t5 = new Soldier(0,5,0,0);
        t6 = new Soldier(0,6,0,0);
        baseWorld.addEntity(t1);
        baseWorld.addEntity(t2);
        baseWorld.addEntity(t3);
        baseWorld.addEntity(t4);
        baseWorld.addEntity(t5);
        baseWorld.addEntity(t6);
    }

    @Test
    public void GetHealthBarTest() {
        healthBar = t1.getHealthBar();
        Assert.assertTrue(healthBar != null);
    }

    @Test
    public void GetInvalidHealthBarTest() {
        healthBar = t2.getHealthBar();
        Assert.assertTrue(healthBar != null);
    }

    @Test
    public void SetHealthTest() {
        healthBar = t3.getHealthBar();
        for (int i = 400; i > 0; i -=40 ) {
            t3.setHealth(i);
            healthBar.update();
            Assert.assertTrue(healthBar.getState() == i*2/40);
        }
    }

    @Test
    public void TranslationTest() {
        t4.setPosition(5,4,2);
        healthBar = t4.getHealthBar();
        healthBar.update();
        Assert.assertTrue(healthBar.getPosX() == 5);
        Assert.assertTrue(healthBar.getPosY() == 4);
        Assert.assertFalse(healthBar.getPosZ() == 2);
    }

    @Test
    public void VisibilityTest() {
        healthBar = t5.getHealthBar();
        healthBar.update();
        Assert.assertTrue(healthBar.getPosX() == t5.getPosX());
        Assert.assertTrue(healthBar.getPosY() == t5.getPosY());
        healthBar.setVisible(false);
        healthBar.update();
        Assert.assertFalse(healthBar.getPosX() == t5.getPosX());
        Assert.assertFalse(healthBar.getPosY() == t5.getPosY());
    }

    @Ignore
    @Test
    public void DeletionTest() {
        t1.getHealthBar();
        t2.getHealthBar();
        t3.getHealthBar();
        t4.getHealthBar();
        t5.getHealthBar();
        t6.getHealthBar();
        Assert.assertTrue(baseWorld.getEntities().size() == 11);
        t1.setHealth(0);
        t2.setHealth(0);
        t3.setHealth(0);
        t4.setHealth(0);
        t5.setHealth(0);
        t6.setHealth(0);
        Assert.assertTrue(baseWorld.getEntities().size() == 0);
    }


}
