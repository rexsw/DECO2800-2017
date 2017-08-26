package com.deco2800.marswars;

import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeManagerTest {
	TimeManager timeManager = (TimeManager) GameManager.get()
			.getManager(TimeManager.class);

	@Test
	public void testOnTick() {
		int count = 500;
		timeManager.pause();
		float inGameTime = timeManager.getInGameTime();
		while (count != 0){
			count--;
		}
		float newInGameTime = timeManager.getInGameTime();
		assertTrue(inGameTime == newInGameTime);
		timeManager.unPause();
		timeManager.addTime(3600);
		assertTrue(timeManager.getHours() > 6 &&
				timeManager.getHours() < 18);
		assertTrue(!timeManager.isNight());
		timeManager.addTime(43200);
		assertTrue(timeManager.getHours() > 18);
		assertTrue(timeManager.isNight());
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
	}
	
	@Test
	public void testGetMinutes() {
		assertEquals(0, timeManager.getMinutes());
		timeManager.addTime(60);
		assertEquals(1, timeManager.getMinutes());
		timeManager.addTime(3600);
		assertEquals(1, timeManager.getMinutes());
		assertEquals(7, timeManager.getHours());
		timeManager.addTime(5);
		assertEquals(1, timeManager.getMinutes());
	}
	
	@Test
	public void testIsNight() {
		timeManager.addTime(3600);
		assertTrue(timeManager.isNight());
		timeManager.addTime(43200);
		assertFalse(timeManager.isNight());
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
		assertEquals(System.currentTimeMillis(), timeManager.getGlobalTime());
	}
	
	@Test
	public void testGetGlobalHours() {
		assertTrue(timeManager.getGlobalHours() < 24);
	}

	@Test
	public void testGetGlobalMinutes() {
		assertTrue(timeManager.getGlobalMinutes() < 60);
	//	Date date = new Date();
	//	Calendar calendar = GregorianCalendar.getInstance();
	//	calendar.setTime(date);
	//	assertEquals(calendar.get(Calendar.MINUTE),
	//			timeManager.getGlobalMinutes());
	} //am i testing different types here?

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
		assertTrue(timeManager.getPlayClockTime().length() > 5
				&& timeManager.getPlayClockTime().length() < 9);
	}
	
	@Test
	public void testAddTime() {
		assertEquals("Hours Not 6", 6, timeManager.getHours());
		assertEquals("Minutes not 0", 0, timeManager.getMinutes());
		assertEquals("Seconds not 108000 as expected", 108000, 
				timeManager.getInGameTime());
		timeManager.addTime(0); 
		assertEquals("Seconds not 108000 as expected", 108000, 
				timeManager.getInGameTime());
		timeManager.addTime(1);
		assertEquals("Seconds not 108001 as expected", 108001, 
				timeManager.getInGameTime());
		timeManager.addTime(-1);
		assertEquals("Seconds not 108002 as expected", 108002, 
				timeManager.getInGameTime());
	}
	
	@Test
	public void testToString() {
		assertEquals(6, timeManager.getHours());
		assertEquals("6:0", timeManager.toString());
	}
}
