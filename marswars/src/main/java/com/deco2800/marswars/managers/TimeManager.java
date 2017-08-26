package com.deco2800.marswars.managers;

import java.util.concurrent.TimeUnit;

public class TimeManager extends Manager implements TickableManager {
	
	private static final int DAYBREAK = 6; //daybreak at 6am
	private static final int NIGHT = 18; //night at 6pm
	private boolean isNight = true;
	private boolean isPaused = false;
	private long time = 0;
	private long gameStartTime = 0;

	/**
	 * Calculate the current in-game hour
	 * @return the in-game hour of the day
	 */
	public long getHours() {
		return TimeUnit.HOURS.convert(getInGameTime(), TimeUnit.SECONDS) % 24;
	}

	/**
	 * Calculate the current in-game minute
	 * @return the in-game minute of the hour (i.e. will return between 0 
	 * and 59 inclusive)
	 */
	public long getMinutes() {
		return TimeUnit.MINUTES.convert(getInGameTime(), TimeUnit.SECONDS) % 60;
	}

	/**
	 * Get the current in-game time
	 * @return the time in the game in seconds. Counting starts at 0 when the
	 * game is initialised
	 */
	public long getInGameTime() {
		return time;
	}
	
	/**
	 * Check if it is night or day in the system
	 * @return true if it is 'Night', false otherwise
	 */
	public boolean isNight() {
		if (this.getHours() < DAYBREAK || this.getHours() > NIGHT) {
			setNight();
		} else {
			setDay();
		}
		return isNight;
	}

	/**
	 * Set the time of day to night
	 */
	private void setNight() {
		isNight = true;
	}

	/**
	 * Set the time of day to day
	 */
	private void setDay() {
		isNight = false;
	}
	
	/**
	 * Check if the timer is paused
	 * @return true if the timer is paused
	 */
	public boolean isPaused() {
		return isPaused;
	}
	/**
	 * Set the game to be paused
	 */
	public void pause() {
		isPaused = true;
	}
	/**
	 * Set the game to stop being paused
	 */
	public void unPause() {
		isPaused = false;
	}

	/**
	 * Returns the current system time in Milliseconds
	 * @return long integer representing system time in milliseconds
	 */
	public long getGlobalTime() {
		return System.currentTimeMillis();
	}

	/**
	 * Returns the hour value of the current time (for use when saving/ loading)
	 * @return long integer (0 to 23) representing the current hour
	 */
	public long getGlobalHours() {
		return TimeUnit.HOURS.convert(getGlobalTime(),
				TimeUnit.MILLISECONDS) % 24;
	}

	/**
	 * Returns the minute value of the current time (for use when saving/
	 * loading)
	 * @return long integer (0 to 59) representing the current minute
	 */
	public long getGlobalMinutes() {
		return TimeUnit.MINUTES.convert(getGlobalTime(),
				TimeUnit.MILLISECONDS) % 60;
	}

	/**
	 * Returns the second value of the current time (for use when saving/
	 * loading)
	 * @return long integer (0 to 59) representing the current second
	 */
	public long getGlobalSeconds() {
		return TimeUnit.SECONDS.convert(getGlobalTime(),
				TimeUnit.MILLISECONDS) % 60;
	}

	/**
	 * Returns the millisecond value of the time elapsed since the current
	 * game was launched
	 * (for use with game length timer)
	 * @return long integer representing the current millisecond
	 */
	public long getGameTimer() {
		long gameTimer = getGlobalTime() - gameStartTime;
		return gameTimer;
	}

	/**
	 * Returns the current second value of the time elapsed since the
	 * current game was launched (for use with game length timer)
	 * @return long integer representing the current second
	 */
	public long getPlaySeconds() {
		return TimeUnit.SECONDS.convert(getGameTimer(),
				TimeUnit.MILLISECONDS) % 60;
	}

	/**
	 * Returns the minutes value of the time elapsed since the current
	 * game was launched (for use with game length timer)
	 * @return long integer representing the current minute
	 */
	public long getPlayMinutes() {
		return TimeUnit.MINUTES.convert(getGameTimer(),
				TimeUnit.MILLISECONDS) % 60;
	}

	/**
	 * Returns the hour value of the time elapsed since the current
	 * game was launched (for use with game length timer)
	 * @return long integer representing the current hour
	 */
	public long getPlayHours() {
		return TimeUnit.HOURS.convert(getGameTimer(), TimeUnit.MILLISECONDS)
				 % 24;
	}

	/**
	 * Sets the variable that stores the time of game launch to the current
	 * system time (use when initalising game)
	 */
	public void setGameStartTime() {
		gameStartTime = getGlobalTime();
	}

	/**
	 * Add time to the In-Game time.
	 * Note: This will only increase the time (i.e. you cannot go
	 * backwards in time). Trying to add a negative value will add the
	 * absolute value.
	 * @param seconds The magnitude of seconds to be added
	 */
	public void addTime(long seconds) {
		time += Math.abs(seconds);
	}

	/**
	 * Provides the amount of time that the current game instance has
	 * been running since the initial loading of the HUD.
	 * @return the String representation of the real time spent in the
	 * current game
	 */
	public String getPlayClockTime() {
		return getPlayHours() + ":" + getPlayMinutes() + ":" + getPlaySeconds();
	}

	/**
	 * Updates time if the game is not paused
	 * Changes the time of day to night at 6pm, and to day at 6am
	 */
	@Override
	public void onTick(long i) {
		if (!isPaused) {
			time += 5;
			if (getHours() > NIGHT || getHours() < DAYBREAK) {
				setNight();
			} else {
				setDay();
			}
		}
	}

	/**
	 * Display the in-game time as a string
	 * @return in-game time in the format Hours:Minutes
	 */
	@Override
	public String toString() {
		return getHours() + ":" + getMinutes();
	}
}