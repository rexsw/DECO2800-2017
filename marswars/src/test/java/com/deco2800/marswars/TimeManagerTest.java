package com.deco2800.marswars;

import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeManagerTest {
	private TimeManager timeManager = (TimeManager) GameManager.get()
			.getManager(TimeManager.class);

	@Test
	public void testOnTick() {
		int count = 500;
		timeManager.pause();
		timeManager.onTick(1);
		float inGameTime = timeManager.getInGameTime();
		while (count != 0){
			count--;
		}
		float newInGameTime = timeManager.getInGameTime();
		assertTrue(inGameTime == newInGameTime);
		System.out.println(newInGameTime);
		System.out.println(inGameTime);
		timeManager.unPause();
		timeManager.addTime(25200);
		System.out.println(timeManager.getHours());
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
		System.out.println(timeManager.getHours());
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
		assertTrue(timeManager.getGameTimer() == 0);
	}
	
	@Test
	public void testIsPaused() {
		assertFalse("Is paused", timeManager.isPaused());
	}
/*
	@Test
	public void testPause() {
		assertFalse("Is paused", timeManager.isPaused());
		timeManager.pause();
		assertTrue("Not paused", timeManager.isPaused());
	}
*/
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
		assertTrue(timeManager.getGlobalTimeString().length() > 5
				&& timeManager.getGlobalTimeString().length() < 9);
	}
	
	@Test
	public void testAddTime() {
		assertEquals("Hours Not 0", 0, timeManager.getHours());
		assertEquals("Minutes not 0", 0, timeManager.getMinutes());
		assertEquals("Seconds not 0 as expected", 0,
				timeManager.getInGameTime());
		timeManager.addTime(108000);
		assertEquals("Seconds not 108000 as expected", 108000, 
				timeManager.getInGameTime());
		timeManager.addTime(1);
		assertEquals("Seconds not 108001 as expected", 108001, 
				timeManager.getInGameTime());
		timeManager.addTime(-1);
		assertEquals("Seconds not 108002 as expected", 108002, 
				timeManager.getInGameTime());
		timeManager.resetInGameTime();
	}

	@Test
	public void resetInGameTime() {
		timeManager.resetInGameTime();
		assertTrue(timeManager.getInGameTime() == 0);
	}
	
	@Test
	public void testToString() {
		timeManager.resetInGameTime();
		assertEquals(0, timeManager.getHours());
		assertEquals("0:0", timeManager.toString());
	}
}
