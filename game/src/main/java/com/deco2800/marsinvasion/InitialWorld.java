package com.deco2800.marsinvasion;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.deco2800.marsinvasion.entities.Base;
import com.deco2800.marsinvasion.entities.HeroSpacman;
import com.deco2800.marsinvasion.entities.Spacman;
import com.deco2800.marsinvasion.entities.Selectable;
import com.deco2800.moos.renderers.Renderable;
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
		this.map = new TmxMapLoader().load("resources/placeholderassets/placeholder.tmx");

        /* Grab the width and length values from the map file to use as the world size */
		this.setWidth(this.getMap().getProperties().get("width", Integer.class));
		this.setLength(this.getMap().getProperties().get("height", Integer.class));

		for (int i = 0; i < 100; i++) {
			this.addEntity(new Spacman(this, 2, 2, 0));
		}

		this.addEntity(new HeroSpacman(this, 1, 1, 0));
		this.addEntity(new Base(this, 8, 8, 2));
	}

	public void deSelectAll() {
		for (Renderable r : this.getEntities()) {
			if (r instanceof Selectable) {
				((Selectable) r).deselect();
			}
		}
	}
}