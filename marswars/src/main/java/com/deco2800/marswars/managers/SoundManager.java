package com.deco2800.marswars.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SoundManager
 * For now all this class does is return a Sound instance of the file in the sounds folder
 * With the returned Sound instance, id can be assigned and Sound can be acted on by
 * commands belonging to the Sound library
 * @Author Tim Hadwen
 */
public class SoundManager extends Manager {

	private static boolean blockSound = false;

	public static void blockSound(){blockSound = true;}
	public static void unblockSound(){blockSound = false;}


	private static final Logger LOGGER = LoggerFactory.getLogger(SoundManager.class);

	/**
	 * Plays a fun test sound on a new thread
	 * @param soundString file name to be loaded as a sound
	 */
	public Sound loadSound(String soundString) {
		LOGGER.info("Loading sound effect");
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/ree1.wav"));
		try {
			 sound = Gdx.audio.newSound(Gdx.files.internal("sounds/" + soundString));
		} catch (GdxRuntimeException e) {
			LOGGER.error("Could not load sound effect " + soundString);
		}
		return sound;
	}
	
	/**
	 * Plays the Sound object
	 * @param sound Sound object to be played
	 * @return returns the ID of the sound being played
	 */
	public long playSound(Sound sound) {
			if(!blockSound){
			LOGGER.info("Playing sound effect");
			if (sound == null) {
				LOGGER.error("Sound effect not found");
				return 0;
			}
			try {
				return sound.play(1f);
			} catch (GdxRuntimeException e) {
				LOGGER.error("Could not play sound effect");
			}

		}

			return 0;

	}
	
	/**
	 * Loops a sound which is playing
	 * @param sound Sound object to be played
	 * @param id The id of the played sound
	 */
	public void loopSound(Sound sound, long id) {
		LOGGER.info("Looping sound effect");
		try {
			sound.setLooping(id, true);
		} catch (GdxRuntimeException e) {
			LOGGER.error("Could not loop sound effect ");
		}
	}
	
	/**
	 * Stops a sound being played and disposes of the Sound object
	 * @param sound Sound object to be played
	 * @param id The id of the played sound
	 */
	public void stopSound(Sound sound, long id) {
		LOGGER.info("Stopping sound effect");
		try {
			sound.stop(id);
			sound.dispose();
		} catch (GdxRuntimeException e) {
			LOGGER.error("Could not stop sound effect ");
		}
	}
	
}
