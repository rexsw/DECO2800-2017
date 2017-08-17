package com.deco2800.marswars.managers;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeManager extends Manager implements TickableManager{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeManager.class);
	private static final int DAYBREAK = 6; //daybreak at 6am
	private static final int NIGHT = 18; //night at 6pm
	
	private boolean isNight = true;
	private boolean isPaused = false;
	private long time = 0; //time in seconds
	
	@Override
	public void onTick(long i){
		if (!isPaused){
		time += 10;
		if(getHours() > NIGHT || getHours() < DAYBREAK)
			setNight();
		else
			setDay();
		}
	}
	
	//bit of a lazy way of setting day/night tbh

	/**
	 * Convert time and find what hour
	 * @return the hour of the day
	 */
	public long getHours(){
		return TimeUnit.HOURS.convert(getSeconds(), TimeUnit.SECONDS)%24;
	}
	public long getMinutes(){
		return TimeUnit.MINUTES.convert(getSeconds(), TimeUnit.SECONDS)%60;
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
	public void pause(){
		isPaused = true;
	}
	public void unPause(){
		isPaused = false;
	}
	
	public long getSeconds(){
		return time;
	}
	public void addTime(long seconds){
		time += seconds;
	}
	
	/**
	 * Display time in hour:minute
	 */
	@Override
	public String toString(){
		return getHours() + ":" + getMinutes();
	}
}