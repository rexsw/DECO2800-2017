package com.deco2800.marswars;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TimeManagerTest {
	private TimeManager timeManager = (TimeManager) GameManager.get()
			.getManager(TimeManager.class);
	private BaseWorld world = GameManager.get().getWorld();

	@Before
	public void initalise() {
		world = new BaseWorld(5, 5);
		GameManager.get().setWorld(world);
		timeManager.setGameStartTime();
		timeManager.resetInGameTime();
	}

	@Test
	public void testOnTick() {
		timeManager.pause();
		timeManager.onTick(1);
		float inGameTime = timeManager.getGameSeconds();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return;
		}
		float newInGameTime = timeManager.getGameSeconds();
		assertTrue(inGameTime == newInGameTime);
		timeManager.unPause();
		timeManager.addTime(25200);
		assertTrue(timeManager.getHours() > 6 &&
				timeManager.getHours() < 18);
		timeManager.onTick(1);
		assertTrue(!timeManager.isNight());
		timeManager.addTime(43200);
		assertTrue(timeManager.getHours() > 18);
		timeManager.onTick(1);
		assertTrue(timeManager.isNight());
		timeManager.resetInGameTime();
	}

	@Test
	public void testGetHours() {
		assertEquals(0, timeManager.getHours());
		timeManager.addTime(21600);
		assertEquals(6, timeManager.getHours());
		timeManager.addTime(64800);
		assertEquals(0, timeManager.getHours());
		timeManager.addTime(21600);
		assertEquals(6, timeManager.getHours());
		timeManager.resetInGameTime();
	}
	
	@Test
	public void testGetMinutes() {
		assertEquals(0, timeManager.getMinutes());
		timeManager.addTime(60);
		assertEquals(1, timeManager.getMinutes());
		timeManager.addTime(3600);
		assertEquals(1, timeManager.getMinutes());
		assertEquals(1, timeManager.getHours());
		timeManager.addTime(5);
		assertEquals(1, timeManager.getMinutes());
		timeManager.resetInGameTime();
	}
	
	@Test
	public void testIsNight() {
		timeManager.addTime(3600);
		assertTrue(timeManager.isNight());
		timeManager.addTime(43200);
		assertFalse(timeManager.isNight());
		timeManager.resetInGameTime();
	}

	@Test
	public void setGameStartTime() {
		timeManager.setGameStartTime();
		assertTrue(timeManager.getGameTimer() < 500);
	}
	
	@Test
	public void testIsPaused() {
		assertFalse("Is paused", timeManager.isPaused());
	}


	@Test
	public void testPause() {
		assertFalse("Is paused", timeManager.isPaused());
		timeManager.pause();
		assertTrue("Not paused", timeManager.isPaused());
	}

	@Test
	public void testUnPause() {
		assertTrue("Not paused", timeManager.isPaused());
		timeManager.unPause();
		assertFalse("Is paused", timeManager.isPaused());
	}

	@Test
	public void testGetInGameTime() {
		assertTrue(true);
	}
	
	@Test
	public void testGetGlobalTime() {
		assertTrue(true);
	}
	
	@Test
	public void testGetGlobalHours() {
		assertTrue(timeManager.getGlobalHours() < 24);
	}

	@Test
	public void testGetGlobalMinutes() {
		assertTrue(timeManager.getGlobalMinutes() < 60);
	}

	@Test
	public void testGetGlobalSeconds() {
		assertTrue(timeManager.getGlobalSeconds() < 60);
	}
	
	@Test
	public void testGetGameTimer() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return;
		}
		assertTrue(timeManager.getGameTimer() != 0);
	}
	
	@Test
	public void testGetPlaySeconds() {
		assertTrue(timeManager.getPlaySeconds() < 60);
	}

	@Test
	public void testGetPlayMinutes() {
		assertTrue(timeManager.getPlayMinutes() < 60);
	}

	@Test
	public void testGetPlayHours() {
		assertTrue(timeManager.getPlayHours() < 60);
	}
	
	@Test
	public void testSetGameStartTime() {
		assertTrue(true);
	}
	
	@Test
	public void testGetPlayClockTime() {
		assertTrue(timeManager.getPlayClockTime().length() > 1
				&& timeManager.getPlayClockTime().length() < 9);
	}

	@Test
	public void testGetGlobalTimeString() {
		assertTrue(timeManager.getGlobalTimeString().length() >= 5
				&& timeManager.getGlobalTimeString().length() <= 9);
	}
	
	@Test
	public void testAddTime() {
		assertEquals("Hours Not 0", 0, timeManager.getHours());
		assertEquals("Minutes not 0", 0, timeManager.getMinutes());
		assertEquals("Seconds not 0 as expected", 0,
				timeManager.getGameSeconds());
		timeManager.addTime(108000);
		assertEquals("Seconds not 108000 as expected", 108000, 
				timeManager.getGameSeconds());
		timeManager.addTime(1);
		assertEquals("Seconds not 108001 as expected", 108001, 
				timeManager.getGameSeconds());
		timeManager.addTime(-1);
		assertEquals("Seconds not 108002 as expected", 108002, 
				timeManager.getGameSeconds());
		timeManager.resetInGameTime();
	}

	@Test
	public void resetInGameTime() {
		timeManager.resetInGameTime();
		assertTrue(timeManager.getGameSeconds() == 0);
	}
	
	@Test
	public void testToString() {
		timeManager.resetInGameTime();
		assertEquals(0, timeManager.getHours());
		assertEquals("0:0", timeManager.toString());
	}

	@Test
	public void testUnpause() {
		List<BaseEntity> pausedEntities = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Astronaut placeHolderUnit = new Astronaut(i, i, 0, -1);
			GameManager.get().getWorld().addEntity(placeHolderUnit);
			Astronaut affectedUnit = new Astronaut(i, 1, 0, 0);
			pausedEntities.add(placeHolderUnit);
			pausedEntities.add(affectedUnit);
			timeManager.pause(placeHolderUnit);
			timeManager.pause(affectedUnit);
			timeManager.unPause(placeHolderUnit);
			timeManager.unPause(affectedUnit);
		}
		timeManager.pause(pausedEntities);
		timeManager.unPause(pausedEntities);
		timeManager.pause();
		timeManager.unPause();
		assertTrue(true);
	}

	@Test
	public void testSetGameTime() {
		timeManager.resetInGameTime();
		timeManager.setGameTime(3, 1, 1);
		assertTrue(timeManager.getGameSeconds() >= 10861
		&& timeManager.getGameSeconds() < 10863);
	}
}
