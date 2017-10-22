package com.deco2800.marswars.managers;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.HasAction;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A TimeManager class for SpacWars. Provides methods for tracking InGameTime,
 * GameLengthTime and GlobalTime, as well as methods for pausing and unpausing
 * the game and it's entities.
 *
 * @author Isaac Doidge
 */
public class TimeManager extends Manager implements TickableManager {
	
	private static final int DAYBREAK = 6; //daybreak at 6am
	private static final int NIGHT = 18; //night at 6pm
	private boolean isNight = true;
	private boolean isGamePaused = false;
	private static long time = 0;
	private long gameStartTime = 0;
	private boolean daysIncremented = false;

	/**
	 * Calculate the current in-game hour
	 * @return the in-game hour of the day
	 */
	public long getHours() {
		return TimeUnit.HOURS.convert(getGameSeconds(), TimeUnit.SECONDS) % 24;
	}

	/**
	 * Calculate the current in-game minute
	 * @return the in-game minute of the hour (i.e. will return between 0 
	 * and 59 inclusive)
	 */
	public long getMinutes() {
		return TimeUnit.MINUTES.convert(getGameSeconds(), TimeUnit.SECONDS) % 60;
	}

	/**
	 * Get the current in-game time
	 * @return the time in the game in seconds. Counting starts at 0 when the
	 * game is initialised
	 */
	public long getGameSeconds() {
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
		return isGamePaused;
	}

	/**
	 * Pauses the game by stopping all actions currently being undertaken
	 * by entities and ceasing the incrementation of the in-game timer.
	 */
	public void pause() {
		isGamePaused = true;
		List<BaseEntity> entities =
				GameManager.get().getWorld().getEntities();
		for (BaseEntity e: entities) {
			if (e instanceof HasAction &&
					((HasAction) e).getCurrentAction().isPresent()) {
					((HasAction) e).getCurrentAction().get().pauseAction();
			}
		}
	}

	/**
	 * Pauses the actions of the given entity.
	 * @param entity the entity to pause
	 */
	public void pause(BaseEntity entity) {
		if (entity instanceof HasAction &&
				((HasAction) entity).getCurrentAction().isPresent()) {
				((HasAction) entity).getCurrentAction().get().pauseAction();
		}
	}

	/**
	 * Pauses the actions of all entities in the passed list.
	 * @param entities - the list of entities to be paused
	 */
	public void pause(List<BaseEntity> entities) {
		for (BaseEntity e: entities) {
			if (e instanceof HasAction &&
					((HasAction) e).getCurrentAction().isPresent()) {
					((HasAction) e).getCurrentAction().get().pauseAction();
			}
		}
	}

	/**
	 * Resumes all paused entity actions and the in-game timer.
	 */
	public void unPause() {
		isGamePaused = false;
		List<BaseEntity> entities =
				GameManager.get().getWorld().getEntities();
		for (BaseEntity e: entities) {
			if (e instanceof HasAction &&
					((HasAction) e).getCurrentAction().isPresent()) {
					((HasAction) e).getCurrentAction().get().resumeAction();
			}
		}
	}

	/**
	 * Resumes all paused entity actions for the entities in the given list.
	 */
	public void unPause(List<BaseEntity> entities) {
		for (BaseEntity e: entities) {
			if (e instanceof HasAction &&
					((HasAction) e).getCurrentAction().isPresent()) {
					((HasAction) e).getCurrentAction().get().resumeAction();
			}
		}
	}

	/**
	 * Resumes all paused entity actions for the entities in the given list.
	 */
	public void unPause(BaseEntity entity) {
		if (entity instanceof HasAction &&
				((HasAction) entity).getCurrentAction().isPresent()) {
				((HasAction) entity).getCurrentAction().get().resumeAction();
		}
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
		return (TimeUnit.HOURS.convert(getGlobalTime(),
				TimeUnit.MILLISECONDS) + 10) % 24;
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
		return getGlobalTime() - gameStartTime;
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
	public static void addTime(long seconds) {
		time += Math.abs(seconds);
	}

	/**
	 * Adds the given 24 hour time values (hours, minutes, seconds), to the
	 * in-game clock.
	 * @param hours - the number of hours to be added
	 * @param minutes - the number of minutes to be added
	 * @param seconds - the number of seconds to be added
	 */
	public static void setGameTime(int hours, int minutes, int seconds) {
		long hourLength = 3600;
		long minuteLength = 60;
		addTime(hours * hourLength + minutes * minuteLength + seconds);
	}

	/**
	 * Sets the In-Game Time to be 0 (Resets current clock)
	 */
	public static void resetInGameTime() {
		time = 0;
	}

	/**
	 * Provides the System Time (AEST) in human-readable string format.
	 * @return the String representation of the System Time
	 */
	public String getGlobalTimeString() {
		return getGlobalHours() + ":" + getGlobalMinutes() + ":" +
				getGlobalSeconds();
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
		if (!isGamePaused) {
			int dayLength = 24;
			int window = 1;
			addTime(2);
			// Some duplicated code here (also in isNight) find way to resolve
			// May not need isNight, or at least qualifiers
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