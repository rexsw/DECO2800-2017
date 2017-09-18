package com.deco2800.marswars.worlds;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Selectable;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.InitiateGame.Game;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.WeatherManager;
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
		this.collisionMap = new Array2D<> (wide, len);
		for (int x = 0; x < this.getWidth(); x++) {
			for (int y = 0; y < this.getLength(); y++) {
				this.collisionMap.set(x, y, new ArrayList<>());
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
				this.collisionMap.set(x, y, new ArrayList<>());
			}
		}
	}

	/**
	 * Makes an int array of coordinates (left, right, bottom top) which would be used for updating the collision map 
	 * from a provided entity.
	 * 
	 * @param entity  the entity to get the collision coordinates for
	 * @return int array of the coordinates. would be in order of left, right, bottom top.
	 */
	public int[] makeCollisionCoords(BaseEntity entity) {
		int[] result = new int[4];
		result[0] = (int)entity.getPosX();
		result[1] = (int)Math.ceil(entity.getPosX() + entity.getXLength());
		result[2] = (int)entity.getPosY();
		result[3] = (int)Math.ceil(entity.getPosY() + entity.getYLength());
		return result;
	}
	
	/**
	 * Adds an entity to this world.
	 *
	 * @param entity the entity to be added.
	 */
	public void addEntity(BaseEntity entity) {
		super.addEntity(entity);

		if (!entity.isCollidable())
			return;

		if (entity instanceof AttackableEntity) {
			// put things that can be attacked on the minimap
			GameManager.get().getMiniMap().addEntity(entity);
		}

		//Add to the collision map
		int[] collisionCoords = makeCollisionCoords(entity);
		for (int x = collisionCoords[0]; x < collisionCoords[1]; x++) {
			for (int y = collisionCoords[2]; y < collisionCoords[3]; y++) {
				collisionMap.get(x, y).add(entity);
			}
		}
		//Fixes the collision models to better match rendered image
		if (entity.getFix()) {
			BuildingEntity ent = (BuildingEntity) entity;
			ent.fixPosition((int)(entity.getPosX() + ((ent.getBuildSize()-1)/2)), (int)(entity.getPosY() - ((ent.getBuildSize()-1)/2)), (int)entity.getPosZ());
		}

	}
	
	/**
	 * removes an entity from the BaseWorld. Removes in terms of removing from the list of entities that are in the 
	 * world and removes it from the collision map.
	 * 
	 * @param entity  The BaseEntity that is to be removed from the BaseWorld.  
	 */
	@Override
	public void removeEntity(BaseEntity entity) {
		super.removeEntity(entity);
		if (entity instanceof AttackableEntity) {
			// remove entity from the minimap when they are removed from the world
			GameManager.get().getMiniMap().removeEntity(entity);
		}
		if (!entity.isCollidable())
			return;
		int[] collisionCoords = makeCollisionCoords(entity);
		for (int x = collisionCoords[0]; x < collisionCoords[1]; x++) {
			for (int y = collisionCoords[2]; y < collisionCoords[3]; y++) {
				collisionMap.get(x, y).remove(entity);
			}
		}	
	}

	/**
	 * Gets the collision map of the world.
	 * yes this uses a lot of memory.
	 *
	 * @return the map of collisions of the world.
	 */
	public Array2D<List<BaseEntity>> getCollisionMap() {
		return collisionMap;
	}

	/**
	 * Returns true if there is an entity in here.
	 *
	 * @param x a tile x coordinate.
	 * @param y a tile y coordinate.
	 * @return whether it contains an entity
	 */
	public boolean hasEntity(int x, int y) {
		return collisionMap.get(x, y).size() > 0;
	}

	/**
	 * Gets the entity at an x y position.
	 *
	 * @param x a tile x coordinate
	 * @param y a tile y coordinate
	 * @return a list of entities found at the given tile.
	 */
	public List<BaseEntity> getEntities(int x, int y) {
		try {
			return collisionMap.get(x, y);
		} catch (IndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException("Invalid tile coordinate.");
		}
	}

	/**
	 * Gets the entities currently in the game.
	 *
	 * @return a list of all entities currently in the game.
	 */
	public List<BaseEntity> getEntities() {
		return super.getEntities();
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
	
	/**
	 * Checks if location is valid to build at on map
	 * 
	 * @param xPos a tile x coordinate
	 * @param yPos a tile y coordinate
	 * @param objectSize Size of the building
	 * @param fixPos float which fixes the position of building to line up with tile
	 * @return true if valid location
	 */
	public boolean checkValidPlace(float xPos, float yPos, float objectSize, float fixPos) {
		int left = (int) (xPos + fixPos);
		int right = (int) ((xPos + fixPos) + (objectSize));
		int bottom = (int) (yPos + fixPos);
		int top = (int) ((yPos + fixPos) + (objectSize));
		for (int x = left+1; x < right+1; x++) {
			for (int y = bottom-1; y < top-1; y++) {
				if (x >= 0 && y >= 0  && x <= this.getWidth() && y <= this.getLength()){
					if (hasEntity(x, y)) {
						return false;
					}
				}
				else {
					return false;
				}
				
			}
		}
		return true;
	}
}
