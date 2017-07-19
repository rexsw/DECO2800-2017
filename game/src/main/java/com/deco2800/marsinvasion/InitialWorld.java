package com.deco2800.marsinvasion;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.deco2800.marsinvasion.entities.Peon;
import com.deco2800.moos.worlds.AbstractWorld;

/**
 * Created by timhadwen on 19/7/17.
 */
public class InitialWorld extends AbstractWorld {

	/**
	 * Consturtor for InitialWorld
	 */
	public InitialWorld() {
 		/* Load up the map for this world */
		this.map = new TmxMapLoader().load("isometric_grass_and_water.tmx");

        /* Grab the width and length values from the map file to use as the world size */
		this.setWidth(this.getMap().getProperties().get("width", Integer.class));
		this.setLength(this.getMap().getProperties().get("height", Integer.class));

		this.addEntity(new Peon(10, 10, 0));
	}

	@Override
	public int getWidth() {
		return 25;
	}

	@Override
	public int getLength() {
		return 25;
	}
}
