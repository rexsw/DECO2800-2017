package com.deco2800.marswars;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

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
        config.addIcon("resources/SPLogo-32.png", FileType.Internal);

		config.title = "DECO2800 2017: MarsWars";
		config.initialBackgroundColor.add(Color.DARK_GRAY);
		new LwjglApplication(new MarsWars(), config);        
	}
}