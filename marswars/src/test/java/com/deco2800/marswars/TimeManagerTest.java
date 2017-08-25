package com.deco2800.marswars;

import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
	public void testGetGlobalTime(){
		TimeManager timeManager = (TimeManager) GameManager.get()
				.getManager(TimeManager.class);
		assertEquals(System.currentTimeMillis(), timeManager.getGlobalTime());
	}
	
	@Test
	public void testGetGlobalHours(){
		TimeManager timeManager = (TimeManager) GameManager.get()
				.getManager(TimeManager.class);
		Date date = new Date();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		//assertEquals(calendar.get(Calendar.HOUR_OF_DAY), timeManager.getGlobalHours());
		//assertTrue(calendar.get(Calendar.HOUR_OF_DAY) == timeManager.getGlobalHours());
	}
	@Test
	public void testGetGlobalMinutes(){
		TimeManager timeManager = (TimeManager) GameManager.get()
				.getManager(TimeManager.class);
		Date date = new Date();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		assertEquals(calendar.get(Calendar.MINUTE), timeManager.getGlobalMinutes());
	} //am i testing different types here?
	@Test
	public void testGetGlobalSeconds(){
		TimeManager timeManager = (TimeManager) GameManager.get()
				.getManager(TimeManager.class);
		Date date = new Date();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		//assertTrue(calendar.get(Calendar.SECOND) == timeManager.getGlobalSeconds());
	}
	
	@Test
	public void testGetGameTimer(){
		TimeManager timeManager = (TimeManager) GameManager.get()
				.getManager(TimeManager.class);
	} //idk how to test this!!
	
	@Test
	public void testGetPlaySeconds(){}
	@Test
	public void testGetPlayMinutes(){}
	@Test
	public void testGetPlayHours(){} //not sure how to test these 3 either
	
	@Test
	public void testSetGameStartTime(){} //could we rename this maybe?
	//again not sure how to test
	
	@Test
	public void testGetPlayClockTime(){} //test??
	
	@Test
	public void testAddTime(){
		TimeManager timeManager = (TimeManager) GameManager.get()
				.getManager(TimeManager.class);
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
	public void testToString(){
		TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);
		assertEquals(6, timeManager.getHours());
		assertEquals("6:0", timeManager.toString());
	}
}
