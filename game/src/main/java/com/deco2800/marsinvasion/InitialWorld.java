package com.deco2800.marsinvasion;

import com.deco2800.moos.entities.Tree;
import com.deco2800.moos.worlds.AbstractWorld;

import java.util.Random;

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

        /* Create random trees to test the engine */
		for (int i = 0; i < this.getLength(); i++){
			for (int j = 0; j < this.getWidth(); j++) {
                /* Spawn trees with a 10% chance */
				Random r = new Random();
				if (r.nextInt(100) < 10) {
					this.addEntity(new Tree(i, j, 0));
				}

			}
		}
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
