package com.deco2800.marswars;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.hud.MiniMap;
import com.deco2800.marswars.managers.GameBlackBoard;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.WinManager;
import com.deco2800.marswars.worlds.BaseWorld;

public class WinManagerTest {
	BaseWorld baseWorld;
    AttackableEntity entity;

    @Before
    public void setup(){
        entity =  new AttackableEntity(0, 0, 0, 0, 0, 0);
        entity.setOwner(1);
		BaseWorld baseWorld = new BaseWorld(10, 15);
		MiniMap m = null;
		GameManager.get().setMiniMap(m);
		GameManager.get().setWorld(baseWorld);
		GameManager.get().getWorld().addEntity(entity);
		GameManager.get().setSkin(null);
    }	
	
	@Test
	public void combatWinTest() {
		GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		black.set();
		WinManager test = new WinManager();
		assertFalse(test.isWinner());
		test.onTick(0);
		assertTrue(test.isWinner());
	}
	
	@Test
	public void resourceWinTest() {
		GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		black.set();
		WinManager test = new WinManager();
		ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		assertFalse(test.isWinner());
		rm.setBiomass(500, 1);
		rm.setCrystal(500, 1);
		rm.setRocks(500, 1);
		rm.setWater(500, 1);
		assertFalse(test.isWinner());
		test.onTick(0);
		assertTrue(test.isWinner());
	}
}
