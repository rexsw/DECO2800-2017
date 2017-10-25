package com.deco2800.marswars;

import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.hud.MiniMap;
import com.deco2800.marswars.managers.FogManager;
import com.deco2800.marswars.managers.GameBlackBoard;
import com.deco2800.marswars.managers.GameBlackBoard.Field;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class GameBlackBoardTest {
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
    }

	
	
	@Test
	public void setUpTest() {
		GameBlackBoard test = new GameBlackBoard();
		test.set();
		assertTrue(test.isSet());
		Assert.assertEquals(1,test.count(1, Field.UNITS));
	}
	
	@Test
	public void updateUnitTest() {
		GameBlackBoard test = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		test.set();
		Assert.assertEquals(1,test.count(1, Field.UNITS));
		Assert.assertEquals(1,test.count(1, Field.COMBAT_UNITS));
		entity.setHealth(0);
		Assert.assertEquals(0,test.count(1, Field.UNITS));
		Assert.assertEquals(1,test.count(1, Field.UNITS_LOST));
		Assert.assertEquals(0,test.count(1, Field.COMBAT_UNITS));
	}
	
	@Test
	public void teamsAlive() {
		GameBlackBoard test = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		test.set();
		Assert.assertEquals(1,test.count(1, Field.UNITS));
		Assert.assertEquals(1,test.teamsAlive());
		Assert.assertEquals(1,test.getAlive());
	}
	
	@Test
	public void getHistoryTest() {
		GameBlackBoard test = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		test.set();
		Assert.assertEquals(4,test.getHistory(1, Field.UNITS).length);
	}
	
	@Test
	public void buildingcounttest() {
		GameBlackBoard test = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		BaseWorld baseWorld = new BaseWorld(10, 15);
		FogManager.initialFog(10,15);
		MiniMap m = null;
		GameManager.get().setMiniMap(m);
		GameManager.get().setWorld(baseWorld);
		Base btest =  new Base(GameManager.get().getWorld(), 1 ,1 ,0,1);
		GameManager.get().getWorld().addEntity(btest);
		test.set();
		Assert.assertEquals(1,test.count(1, Field.UNITS));
		Assert.assertEquals(1,test.count(1, Field.BUILDINGS));
		//btest.setHealth(0); Doesn't work because of zoom
		//Assert.assertEquals(0,test.count(1, Field.UNITS));
		//Assert.assertEquals(1,test.count(1, Field.UNITS_LOST));
		//Assert.assertEquals(0,test.count(1, Field.BUILDINGS));
	}
	
	@Test
	public void twounittest() {
		GameBlackBoard test = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		Base btest =  new Base(GameManager.get().getWorld(), 1 ,1 ,0,1);
		GameManager.get().getWorld().addEntity(btest);
		test.set();
		Assert.assertEquals(2,test.count(1, Field.UNITS));
		Assert.assertEquals(1,test.count(1, Field.BUILDINGS));
		Assert.assertEquals(1,test.count(1, Field.COMBAT_UNITS));
	}
	
	@Test
	public void indextest() {
		GameBlackBoard test = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		test.set();
		Assert.assertEquals(0,test.getIndex());
		Assert.assertEquals(true,test.isSet());
		test.onTick(0);
		test.onTick(0);
		test.onTick(0);
		test.onTick(0);
		test.onTick(0);
		test.onTick(0);
		test.onTick(0);
		test.onTick(0);
		test.onTick(0);
		test.onTick(0);
		Assert.assertEquals(1,test.getIndex());
		Assert.assertEquals(6,test.getHistory(1, Field.UNITS).length);
	}
	
}
