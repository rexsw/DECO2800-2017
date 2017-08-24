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
	private long time = 0; //in-game time in seconds
	private long gameStartTime = 0;
	private long gameTimer = 0;

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
	public long getSeconds(){
		return time;
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

	/**
	 * Returns the current system time in Milliseconds
	 * @return long integer representing system time in milliseconds
	 */
	public long getGlobalTime(){
		return System.currentTimeMillis();
	}

	/**
	 * Returns the second value of the current time (for use when saving/ loading)
	 * @return long integer (0 to 59) representing the current second
	 */
	public long getGlobalHours(){
		return TimeUnit.HOURS.convert(getGlobalTime(), TimeUnit.MILLISECONDS)%24;
	}
	//think the comments for these two got switched around
		
	/**
	 * Returns the minute value of the current time (for use when saving/ loading)
	 * @return long integer (0 to 59) representing the current minute
	 */
	public long getGlobalMinutes(){
		return TimeUnit.MINUTES.convert(getGlobalTime(), TimeUnit.MILLISECONDS)%60;
	}

	/**
	 * Returns the hour value of the current time (for use when saving/ loading)
	 * @return long integer (0 to 23) representing the current hour
	 */
	public long getGlobalSeconds(){
		return TimeUnit.SECONDS.convert(getGlobalTime(), TimeUnit.MILLISECONDS)%60;
	}

	/**
	 * Returns the millisecond value of the time elapsed since the current game was launched
	 * (for use with game length timer)
	 * @return long integer representing the current millisecond
	 */
	public long getGameTimer(){
		gameTimer = getGlobalTime() - gameStartTime;
		return gameTimer;
	}
	//facility for pausing?
	//need to initialise?

	/**
	 * Returns the current second value of the time elapsed since the current game was launched
	 * (for use with game length timer)
	 * @return long integer representing the current second
	 */
	public long getPlaySeconds(){
		return TimeUnit.SECONDS.convert(getGameTimer(), TimeUnit.MILLISECONDS)%60;
	}

	/**
	 * Returns the minutes value of the time elapsed since the current game was launched
	 * (for use with game length timer)
	 * @return long integer representing the current minute
	 */
	public long getPlayMinutes(){
		return TimeUnit.MINUTES.convert(getGameTimer(), TimeUnit.MILLISECONDS)%60;
	}

	/**
	 * Returns the hour value of the time elapsed since the current game was launched
	 * (for use with game length timer)
	 * @return long integer representing the current hour
	 */
	public long getPlayHours(){
		return TimeUnit.HOURS.convert(getGameTimer(), TimeUnit.MILLISECONDS)%24;
	}

	/**
	 * Sets the variable that stores the time of game launch to the current system time
	 * (use when initalising game)
	 */
	public void setGameStartTime(){
		gameStartTime = getGlobalTime();
	}

	//this will do negative time but it's bad practice. Consider doing abs(seconds)?
	//can't think of any reason we would go back in time so gonna change it
	public void addTime(long seconds){
		time += Math.abs(seconds);
	}

	/**
	 *
	 * @return the String representation of the real time spent in the current game
	 */
	public String getPlayClockTime() {
		return getPlayHours() + ":" + getPlayMinutes() + ":" + getPlaySeconds();
	}

	/**
	 *
	 * @return the String representation of the real time spent in the current game
	 */
	public String getPlayClockTime() {
		return getPlayHours() + ":" + getPlayMinutes() + ":" + getPlaySeconds();
	}

	/**
	 * Display time in hour:minute
	 */
	@Override
	public String toString(){
		return getHours() + ":" + getMinutes();
	}
	}