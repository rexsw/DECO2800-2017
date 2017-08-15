package com.deco2800.marswars.util;

import java.util.concurrent.TimeUnit;

public class Time {
	private boolean isNight;
	private boolean isPaused;
	private long time; //time in milliseconds
	
	public Time(){
		isNight = false;
		isPaused = false;
		time = 0;
	}
	
	/**
	 * Constructor to set time to something else (primarily for testing?)
	 * @param initialTime Initial time in milliseconds
	 */
	public Time(long initialTime){
		isNight = false;
		isPaused = false;
		time = initialTime;
	}
	
	/**
	 * Convert time and find what hour
	 * @return the hour of the day
	 */
	public long getHours(){
		return TimeUnit.HOURS.convert(getMillis(), TimeUnit.MILLISECONDS)%24;
	}
	
	/**
	 * Check if it is night or day in the system
	 * @return true if it is 'Night'
	 */
	public boolean isNight(){
		return isNight;
	}
	public void setNight(){
		isNight = true;
	}
	public void setDay(){
		isNight = false;
	}
	
	/**
	 * Check if the timer is paused
	 * @return true if the timer is paused
	 */
	public boolean isPaused(){
		return isPaused;
	}
	
	public long getMillis(){
		return time;
	}
	
	/**
	 * Add time to the timer
	 * @param time the time to add, in milliseconds
	 */
	public void addTime(int time){
		this.time += time;
	}
	
	/**
	 * Display time in hour:minute:second
	 */
	@Override
	public String toString(){
		return "Time: " + TimeUnit.HOURS.convert(getMillis(), TimeUnit.MILLISECONDS)%24 + 
				":" + TimeUnit.MINUTES.convert(getMillis(), TimeUnit.MILLISECONDS)%60 +
				":" + TimeUnit.SECONDS.convert(getMillis(), TimeUnit.MILLISECONDS)%60;
	}
}
