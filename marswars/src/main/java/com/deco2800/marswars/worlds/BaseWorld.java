package com.deco2800.marswars.worlds;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Selectable;
import com.deco2800.marswars.renderers.Renderable;
import com.deco2800.marswars.util.Array2D;

import java.util.ArrayList;
import java.util.List;

/**
 * A base world for the game. Use this not AbstractWorld
 */
public class BaseWorld extends AbstractWorld {
	/* Crappy way of storing collision */
	protected Array2D<List<BaseEntity>> collisionMap;
	
	/**
	 * Basic Constructor using specified dimensions
	 * @param wide	Width of the world
	 * @param len	Length of the world
	 */
	public BaseWorld(int wide, int len) {
		this.setWidth(wide);
		this.setLength(len);
		this.collisionMap = new Array2D<List<BaseEntity>> (wide, len);
		for (int x = 0; x < this.getWidth(); x++) {
			for (int y = 0; y < this.getLength(); y++) {
				this.collisionMap.set(x, y, new ArrayList<BaseEntity>());
			}
		}
	}
	
	/**
	 * Constructor using a map file. Note: this was essentially pasted from 
	 * InitialWorld constructor so that InitialWorld can call this constructor.
	 * 
	 * @param path	Path of the map file.
	 */
	public BaseWorld(String path) {
		/* Load up the map for this world */
		this.map = new TmxMapLoader().load(path);

                /* Grab the width and length values from the map file to use as the world size */
		this.setWidth(this.getMap().getProperties().get("width", Integer.class));
		this.setLength(this.getMap().getProperties().get("height", Integer.class));

		this.collisionMap = new Array2D<>(this.getWidth(), this.getLength());

		/* Initialise the collision list */
		for (int x = 0; x < this.getWidth(); x++) {
			for (int y = 0; y < this.getLength(); y++) {
				this.collisionMap.set(x, y, new ArrayList<BaseEntity>());
			}
		}		
	}

	/**
	 * Adds an entity to this world
	 * @param entity
	 */
	public void addEntity(BaseEntity entity) {
		super.addEntity(entity);

		if (!entity.isCollidable())
			return;

		//Add to the collision map
		int left = (int)entity.getPosX();
		int right = (int)Math.ceil(entity.getPosX() + entity.getXLength());
		int bottom = (int)entity.getPosY();
		int top = (int)Math.ceil(entity.getPosY() + entity.getYLength());
		for (int x = left; x < right; x++) {
			for (int y = bottom; y < top; y++) {
				collisionMap.get(x, y).add(entity);
			}
		}
	}

	/**
	 * Gets the collision map of the world.
	 * yes this uses a lot of memory.
	 * @return
	 */
	public Array2D<List<BaseEntity>> getCollisionMap() {
		return collisionMap;
	}

	/**
	 * Returns true if there is an entity in here
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean hasEntity(int x, int y) {
		return collisionMap.get(x, y).size() > 0;
	}

	/**
	 * Gets the entity at an x y position
	 * @param x
	 * @param y
	 * @return
	 */
	public List<BaseEntity> getEntities(int x, int y) {
		try {
			return collisionMap.get(x, y);
		} catch (IndexOutOfBoundsException e) {
			throw e;
		}
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
