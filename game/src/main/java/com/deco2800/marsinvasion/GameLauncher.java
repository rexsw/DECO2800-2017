package com.deco2800.marsinvasion;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * DesktopLauncher
 * Launches the MOOS game engine in LibGDX
 * @Author Tim Hadwen
 */
public class GameLauncher {
	/**
	 * Main function for the game
	 * @param arg Command line arguments (we wont use these)
	 */
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.title = "DECO2800 2017: MarsWars";
		LwjglApplication game = new LwjglApplication(new MarsWars(), config);
	}
}