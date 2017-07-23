package com.deco2800.marsinvasion;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.deco2800.marsinvasion.entities.Base;
import com.deco2800.marsinvasion.entities.Hero;
import com.deco2800.marsinvasion.entities.Peon;
import com.deco2800.moos.worlds.AbstractWorld;

/**
 * Created by timhadwen on 19/7/17.
 */
public class InitialWorld extends AbstractWorld {

	/**
	 * Constructor for InitialWorld
	 */
	public InitialWorld() {
 		/* Load up the map for this world */
		this.map = new TmxMapLoader().load("isometric_grass_and_water.tmx");

        /* Grab the width and length values from the map file to use as the world size */
		this.setWidth(this.getMap().getProperties().get("width", Integer.class));
		this.setLength(this.getMap().getProperties().get("height", Integer.class));

		this.addEntity(new Peon(this, 10, 10, 0));
		this.addEntity(new Hero(this, 10, 11, 0));
		this.addEntity(new Base(this, 8, 8, 0));
	}
}
