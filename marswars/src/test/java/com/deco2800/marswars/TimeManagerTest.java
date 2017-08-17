package com.deco2800.marswars;

import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class TimeManagerTest {
	
	@Test
	public void testGetHours(){
		TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);
		assertEquals(0, timeManager.getHours());
		timeManager.addTime(21600);
		assertEquals(6, timeManager.getHours());
		timeManager.addTime(64800);
		assertEquals(0, timeManager.getHours());
		timeManager.addTime(21600);
		assertEquals(6, timeManager.getHours());
	}
	
	@Test
	public void testGetMinutes(){
		TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);
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
	public void testIsNight(){
		TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);
		assertTrue(timeManager.isNight());
		
		timeManager.setDay();
		assertFalse(timeManager.isNight());
		
		timeManager.setNight();
		assertTrue(timeManager.isNight());
	}
	
	@Test
	public void testToString(){
		TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);
		assertEquals(6, timeManager.getHours());
		assertEquals("6:0", timeManager.toString());
	}
}
