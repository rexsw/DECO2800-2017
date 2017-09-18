package com.deco2800.marswars;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.hud.MiniMap;
import com.deco2800.marswars.managers.GameBlackBoard;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;


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
		assertFalse(test.isSet());
		test.set();
		assertTrue(test.isSet());
		Assert.assertEquals(1,test.count(1, "Units"));
	}
	
	@Test
	public void updateUnitTest() {
		GameBlackBoard test = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		test.set();
		Assert.assertEquals(1,test.count(1, "Units"));
		entity.setHealth(0);
		Assert.assertEquals(0,test.count(1, "Units"));
		Assert.assertEquals(1,test.count(1, "Units Lost"));
		Assert.assertEquals(0,test.count(1, "Combat Units"));
	}
	
	@Test
	public void teamsAlive() {
		GameBlackBoard test = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		test.set();
		Assert.assertEquals(1,test.count(1, "Units"));
		Assert.assertEquals(1,test.teamsAlive());
		Assert.assertEquals(1,test.getAlive());
	}
	
	@Test
	public void getHistoryTest() {
		GameBlackBoard test = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
		test.set();
		Assert.assertEquals(1,test.getHistory(1, "Units").length);
	}
	
}
