package com.deco2800.marswars.worlds;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.renderers.Renderable;
import com.deco2800.marswars.util.Array2D;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by timhadwen on 19/7/17.
 */
public class InitialWorld extends BaseWorld {

	/**
	 * Constructor for InitialWorld
	 */
	public InitialWorld() {

 		/* Load up the map for this world */
		this.map = new TmxMapLoader().load("resources/placeholderassets/placeholder200.tmx");

                /* Grab the width and length values from the map file to use as the world size */
		this.setWidth(this.getMap().getProperties().get("width", Integer.class));
		this.setLength(this.getMap().getProperties().get("height", Integer.class));

		this.collisionMap = new Array2D<>(this.getWidth(), this.getLength());

		/* Initialise the collision list */
		for (int x = 0; x < this.getWidth(); x++) {
			for (int y = 0; y < this.getLength(); y++) {
				this.collisionMap.set(x, y, new ArrayList<>());
			}
		}
	}

	/**
	 * Adds entities to the world
	 */
	public void loadEntities() {
		for (int x = 0; x < this.getWidth(); x+=5) {
			for (int y = 0; y < this.getLength(); y+=5) {
				Random r = new Random();

				// CRYSTAL
				if (r.nextInt(10) < 0.1) {
					this.addEntity(new Resource(x, y, 0, 1f, 1f, ResourceType.CRYSTAL));
					continue;
				}
				
				// WATER
				if (r.nextInt(10) < 0.1) {
					this.addEntity(new Resource(x, y, 0, 1f, 1f, ResourceType.WATER));
					continue;
				}
				
				// ROCK
				if (r.nextInt(10) < 0.1) {
					this.addEntity(new Resource(x, y, 0, 1f, 1f, ResourceType.ROCK));
					continue;
				}
				
				// BIOMASS
				if (r.nextInt(10) < 0.1) {
					this.addEntity(new Resource(x, y, 0, 1f, 1f, ResourceType.BIOMASS));
					continue;
				}
			}
		}

		this.addEntity(new Spacman(0, 0, 0));
		this.addEntity(new Spacman(1, 1, 0)); // this spac man is for resource gather test
		this.addEntity(new HeroSpacman(this, 4, 4, 0));
		this.addEntity(new Base(this, 8, 8, 0));
		this.addEntity(new EnemySpacman(24, 24, 0));
		this.addEntity(new EnemyTank(20, 20, 0));
	}

	/**
	 * Deselects all entities
	 */
	public void deSelectAll() {
		for (Renderable r : this.getEntities()) {
			if (r instanceof Selectable) {
				((Selectable) r).deselect();
			}
		}
	}
}