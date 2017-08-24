package com.deco2800.marswars;

import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import org.junit.Test;

import static org.junit.Assert.*;

//how do i delete this manager
//test extreme cases
public class TimeManagerTest {
	
	@Test
	public void testGetHours(){
		TimeManager timeManager = (TimeManager) GameManager.get()
				.getManager(TimeManager.class);
		assertEquals("Not 0", 0, timeManager.getHours());
		timeManager.addTime(21600);
		assertEquals("Not 6", 6, timeManager.getHours());
		timeManager.addTime(64800);
		assertEquals("Not 0 again", 0, timeManager.getHours());
		timeManager.addTime(21600);
		assertEquals("Not 6 again", 6, timeManager.getHours());
	}
	
	@Test
	public void testGetMinutes(){
		TimeManager timeManager = (TimeManager) GameManager.get()
				.getManager(TimeManager.class);
		assertEquals("Not 0", 0, timeManager.getMinutes());
		timeManager.addTime(60);
		assertEquals("Not 1", 1, timeManager.getMinutes());
		timeManager.addTime(3600);
		assertEquals("Not 1 again", 1, timeManager.getMinutes());
		assertEquals("Hours were not 7", 7, timeManager.getHours());
		timeManager.addTime(5);
		assertEquals("Didn't stay at 1", 1, timeManager.getMinutes());
	}
	
	@Test
	public void testIsNight(){
		TimeManager timeManager = (TimeManager) GameManager.get()
				.getManager(TimeManager.class);
		assertTrue("Not Night", timeManager.isNight());
	}
	@Test
	public void testSetDay(){
		TimeManager timeManager = (TimeManager) GameManager.get()
				.getManager(TimeManager.class);
		assertTrue("Not Night", timeManager.isNight());
		
		timeManager.setDay();
		assertFalse("Not Day", timeManager.isNight());
	}
	@Test
	public void testSetNight(){
		TimeManager timeManager = (TimeManager) GameManager.get()
				.getManager(TimeManager.class);
		assertFalse("Not Day", timeManager.isNight());
		
		timeManager.setNight();
		assertTrue("Not Night", timeManager.isNight());
	}
	
	@Test
	public void testIsPaused(){
		TimeManager timeManager = (TimeManager) GameManager.get()
				.getManager(TimeManager.class);
		assertFalse("Is paused", timeManager.isPaused());
	}
	@Test
	public void testPause(){
		TimeManager timeManager = (TimeManager) GameManager.get()
				.getManager(TimeManager.class);
		assertFalse("Is paused", timeManager.isPaused());
		
		timeManager.pause();
		assertTrue("Not paused", timeManager.isPaused());
	}
	@Test
	public void testUnPause(){
		TimeManager timeManager = (TimeManager) GameManager.get()
				.getManager(TimeManager.class);
		assertTrue("Not paused", timeManager.isPaused());
		
		timeManager.unPause();
		assertFalse("Is paused", timeManager.isPaused());
	}
	
	@Test
	public void testGetGlobalTime(){}
	
	@Test
	public void testGetGlobalHours(){}
	@Test
	public void testGetGlobalMinutes(){}
	@Test
	public void testGetGlobalSeconds(){}
	
	@Test
	public void testGetGameTimer(){}
	
	@Test
	public void testGetPlaySeconds(){}
	@Test
	public void testGetPlayMinutes(){}
	@Test
	public void testGetPlayHours(){}
	
	@Test
	public void testSetGameStartTime(){}
	
	@Test
	public void testAddTime(){
		TimeManager timeManager = (TimeManager) GameManager.get()
				.getManager(TimeManager.class);
		assertEquals("Hours Not 6", 6, timeManager.getHours());
		assertEquals("Minutes not 0", 0, timeManager.getMinutes());
		assertEquals("Seconds not 108000 as expected", 108000, timeManager.getSeconds());
		timeManager.addTime(0); 
		assertEquals("Seconds not 108000 as expected", 108000, timeManager.getSeconds());
		timeManager.addTime(1);
		assertEquals("Seconds not 108001 as expected", 108001, timeManager.getSeconds());
		timeManager.addTime(-1);
		assertEquals("Seconds not 108002 as expected", 108002, timeManager.getSeconds());
	}
	
	@Test
	public void testToString(){
		TimeManager timeManager = (TimeManager) GameManager.get()
				.getManager(TimeManager.class);
		assertEquals("Hours are not 6", 6, timeManager.getHours());
		assertEquals("These strings were not equal", "6:0", timeManager.toString());
	}
	@Test
	public void testOnTick(){}
}
