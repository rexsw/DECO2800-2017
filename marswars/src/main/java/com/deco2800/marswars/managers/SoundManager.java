package com.deco2800.marswars.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SoundManager
 * Required to play sounds in the game engine.
 * @Author Tim Hadwen
 */
public class SoundManager extends Manager {

	private static final Logger LOGGER = LoggerFactory.getLogger(SoundManager.class);

	/**
	 * Plays a fun test sound on a new thread
	 */
	public void playSound(String soundString) {
		LOGGER.info("Playing sound effect");
		try {
			Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/" + soundString));
			sound.play(1f);
		} catch (GdxRuntimeException e) {
			LOGGER.error("Could not load sound effect " + soundString);
		}
	}
}