package com.deco2800.marswars;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.apache.log4j.BasicConfigurator;

/**
 * DesktopLauncher
 * Launches the MOOS game engine in LibGDX
 * @author Tim Hadwen
 */
public class GameLauncher {
	/**
	 * Main function for the game
	 * @param arg Command line arguments (we wont use these)
	 *
	 *
	 */

	private GameLauncher(){}
	public static void main (String[] arg) {
		BasicConfigurator.configure();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.title = "DECO2800 2017: MarsWars"; //$NON-NLS-1$
		// LwjglApplication game =
		new LwjglApplication(new MarsWars(), config);
	}
}