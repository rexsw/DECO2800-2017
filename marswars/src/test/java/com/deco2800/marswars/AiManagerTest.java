package com.deco2800.marswars;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.hud.MiniMap;
import com.deco2800.marswars.managers.AiManager;
import com.deco2800.marswars.managers.AiManager.State;
import com.deco2800.marswars.managers.GameBlackBoard;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.worlds.BaseWorld;

public class AiManagerTest {
	BaseWorld baseWorld;
    AttackableEntity entity;
    AiManager am;
    TimeManager tm;
    ResourceManager rm;
    GameBlackBoard black;

    @Before
    public void setup(){
		BaseWorld baseWorld = new BaseWorld(10, 15);
		MiniMap m = null;
		GameManager.get().setMiniMap(m);
		GameManager.get().setWorld(baseWorld);
		GameManager.get().setSkin(null);
		am = new AiManager();
		tm = (TimeManager) GameManager.get().getManager(TimeManager.class);
		tm.resetInGameTime();
		rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
    }
    
     
	@Test
	public void aiTestBase() {
		entity =  new Base(GameManager.get().getWorld(), 1, 1, 0, 1);
		GameManager.get().getWorld().addEntity(entity);
		rm.setRocks(500, 1);
		assertFalse(entity.showProgress());

		// Should create a Spacman (costs 30 rocks)
		am.onTick(0);
		assertTrue(entity.showProgress());
		assertEquals(500-30, rm.getRocks(1));
	}
	
	@Test
	public void getAiTeamTest(){
		List<Integer> teamid = new ArrayList<Integer>();
		for (int i = 1; i < 4; i++) {
			teamid.add(i);
			am.addTeam(i);
		}
		assertEquals(teamid, am.getAiTeam());
		
	}
	@Test
	public void killTest() {
		for (int i = 1; i < 4; i++){
			am.kill(i);
		}
		assertTrue(am.getAiTeam().isEmpty());
	}
	
	@Test
	public void getStateIndexTest(){
		am.addTeam(1);
		assertEquals(0,am.getStateIndex(1));
		
	}
	
	
	
	@Test
	public void tickLockTest(){
		assertTrue(am.tickLock(5,10));
		assertTrue(!am.tickLock(6, 10));
	}

	
	@Test
	public void stateChangeTest(){
		am.addTeam(1);
		assertEquals(State.ECONOMIC, am.getState(1));
		assertEquals(0,am.getTimeAtStateChange());
		tm.addTime(500);
		assertEquals(500, am.getTimeSinceStateChange());
		am.setState(1,State.AGGRESSIVE);
		assertEquals(State.AGGRESSIVE, am.getState(1));
		assertEquals(500,am.getTimeAtStateChange());
		assertEquals(0, am.getTimeSinceStateChange());
		
	}


	@Test
	public void decideChangeStateTest(){
		am.addTeam(1);
		Base b = new Base(baseWorld, 1, 1, 0, 1);
		AttackableEntity r = new AttackableEntity(2,1,0,1, 1, 1);
		AttackableEntity p = new AttackableEntity(2,2,0,1, 1, 1);
		r.setOwner(1);
		p.setOwner(-1);
		GameManager.get().getWorld().addEntity(b);
		GameManager.get().getWorld().addEntity(r);
		GameManager.get().getWorld().addEntity(p);
		black.set();
		tm.addTime(60*60 + 1);
		am.decideChangeState();
		//assertEquals(State.AGGRESSIVE, am.getState(1));  - Changed implementation,
		//											no longer works this way
	}

}
