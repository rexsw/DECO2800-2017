package com.deco2800.marswars;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.hud.MiniMap;
import com.deco2800.marswars.managers.AiManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.worlds.BaseWorld;

public class AiManagerTest {
	BaseWorld baseWorld;
    AttackableEntity entity;

    @Before
    public void setup(){
		BaseWorld baseWorld = new BaseWorld(10, 15);
		MiniMap m = null;
		GameManager.get().setMiniMap(m);
		GameManager.get().setWorld(baseWorld);
		GameManager.get().setSkin(null);
    }
    
	@Test
	public void aiTestBase() {
		entity =  new Base(GameManager.get().getWorld(), 1, 1, 0, 1);
		GameManager.get().getWorld().addEntity(entity);
		AiManager test = new AiManager();
		ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		rm.setRocks(500, 1);
		assertFalse(entity.showProgress());
		test.onTick(0);
		assertTrue(entity.showProgress());
		assertEquals(500-30, rm.getRocks(1));
		List<BaseEntity> entityList = GameManager.get().getWorld().getEntities();
		assertEquals(2,entityList.size());
	}
	
	
	
	
	@Test
	public void aiListTeams() {
		entity =  new Base(GameManager.get().getWorld(), 1, 1, 0, 1);
		GameManager.get().getWorld().addEntity(entity);
		AiManager test = new AiManager();
		assertTrue(test.getAiTeam().isEmpty());
		test.addTeam(1);
		assertTrue(test.getAiTeam().contains(1));
		test.kill(1);
		assertTrue(test.getAiTeam().isEmpty());
	}
}
