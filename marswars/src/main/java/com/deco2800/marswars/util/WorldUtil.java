package com.deco2800.marswars.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.buildings.CheckSelect;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.renderers.Renderable;
import com.deco2800.marswars.worlds.BaseWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A utility class for the BaseWorld instances
 * Created by timhadwen on 23/7/17.
 */
public class WorldUtil {

	private WorldUtil(){}

	private static final Logger LOGGER = LoggerFactory.getLogger(WorldUtil.class);

	/**
	 * Finds the closest entity to a position within a delta
	 * @param world
	 * @param x
	 * @param y
	 * @param delta
	 * @return Optional of AbstractEntity
	 */
	public static Optional<BaseEntity> closestEntityToPosition(BaseWorld world, float x, float y, float delta) {
		BaseEntity ret = null;
		double distance = Double.MAX_VALUE;
		for (Renderable e : world.getEntities()) {
			double tmp_distance = Math.sqrt(Math.pow(e.getPosX() - x, 2) + Math.pow(e.getPosY() - y, 2));

			if (tmp_distance < distance) {
				// Closer than current closest
				distance = tmp_distance;
				ret = (BaseEntity) e;
			}
		}
		if (ret == null) {
			LOGGER.info("Found nothing");
			return Optional.empty();
		}
		if (distance < delta){
			LOGGER.info("Closest is " + ret.toString());
			return Optional.of(ret);
		} else {
			LOGGER.info("Nothing is that close");
			return Optional.empty();
		}
	}

	/**
	 * Gets the entities of the provided class that are within a defined range around a given point.
	 * 
	 * @param c Class (subclass) of BaseEntity to search the area for.
	 * @param x  X coordinate of the point (center of the area) to find entities
	 * @param y  Y coordinate of the point (center of the area) to find entities
	 * @param radiusX  The radius of the area in terms of the X coordinate (i.e. how far away from X in both positive 
	 * 			and negative directions the area to look for entities should be)
	 * @param radiusY The radius of the area in terms of the Y coordinate (i.e. how far away from Y in both positive 
	 * 			and negative directions the area to look for entities should be)
	 * @return List of BaseEntities that are within the area provided and are of the class specified.
	 */
	public static List<BaseEntity> getEntitiesAroundWithClass(Class<?> c, float x, float y, float radiusX, 
			float radiusY) {
		List<BaseEntity> entities = WorldUtil.getEntitiesOfClass(GameManager.get().getWorld().getEntities(), c);
		return getEntitiesAround(entities, x, y, radiusX, radiusY);
	}
	
	/**
	 * Finds the BaseEntities in the provided list of BasesEntities that are within a defined range around a given 
	 * point.
	 * 
	 * @param entities  List of BaseEntities to search over.
	 * @param x  X coordinate of the point (center of the area) to find entities
	 * @param y  Y coordinate of the point (center of the area) to find entities
	 * @param radiusX  The radius of the area in terms of the X coordinate (i.e. how far away from X in both positive 
	 * 			and negative directions the area to look for entities should be)
	 * @param radiusY The radius of the area in terms of the Y coordinate (i.e. how far away from Y in both positive 
	 * 			and negative directions the area to look for entities should be)
	 * @return List of BaseEntities that are within the area provided.
	 */
	public static List<BaseEntity> getEntitiesAround(List<BaseEntity> entities, float x, float y, float radiusX, 
			float radiusY) {
		List<BaseEntity> result = new ArrayList<BaseEntity>();
		float lowerX = x - radiusX;
		float upperX = x + radiusX;
		float lowerY = y - radiusY;
		float upperY = y + radiusY;
		for (BaseEntity w : entities) {
			if (w.getPosX() >= lowerX && w.getPosX() <= upperX && w.getPosY() >= lowerY && w.getPosY() <= upperY) {
				result.add(w);
			}
		}
		return result;
	}
	
	/**
	 * Lists the entities of a class
	 * @param entities
	 * @param c
	 * @return
	 */
	public static List<BaseEntity> getEntitiesOfClass(List<BaseEntity> entities, Class<?> c) {
		List<BaseEntity> classEntities = new ArrayList<>();
		for (BaseEntity w : entities) {
			if (w.getClass() == c) {
				classEntities.add(w);
			}
		}
		return classEntities;
	}

	/**
	 * Gets the closest entity of a class
	 * @param c
	 * @param x
	 * @param y
	 * @return
	 */
	public static Optional<BaseEntity> getClosestEntityOfClass(Class<?> c, float x, float y) {
		List<BaseEntity> entities = WorldUtil.getEntitiesOfClass(GameManager.get().getWorld().getEntities(), c);

		BaseEntity closest = null;
		float dist = Float.MAX_VALUE;
		for (BaseEntity e : entities) {
			float tmp_distance = (float)(Math.sqrt(Math.pow(e.getPosX() - x, 2) + Math.pow(e.getPosY() - y, 2)));
			if (closest == null || dist > tmp_distance) {
				dist = tmp_distance;
				closest = e;
			}
		}

		if (closest == null) {
			return Optional.empty();
		} else {
			return Optional.of(closest);
		}
	}
	
	/**
	 * Gets the closest entity of a class with an given owner 
	 * @param c the class of entity searching for
	 * @param x the x co-ords to search from
	 * @param y the x co-ords to search from
	 * @param m the owner of the entity 
	 * @return an entity of type c if one is found 
	 */
	public static Optional<BaseEntity> getClosestEntityOfClassAndOwner(Class<?> c, float x, float y, int m) {
		List<BaseEntity> entities = WorldUtil.getEntitiesOfClass(GameManager.get().getWorld().getEntities(), c);

		BaseEntity closest = null;
		float dist = Float.MAX_VALUE;
		for (BaseEntity e : entities) {
			float tmp_distance = (float)(Math.sqrt(Math.pow(e.getPosX() - x, 2) + Math.pow(e.getPosY() - y, 2)));
			if ((closest == null || dist > tmp_distance) && e instanceof HasOwner && ((HasOwner) e).getOwner() == m) {
				dist = tmp_distance;
				closest = e;
			}
		}

		if (closest == null) {
			return Optional.empty();
		} else {
			return Optional.of(closest);
		}
	}

	/**
	 * Gets the closest entity of a class with an given owner 
	 * @param c the class of entity searching for
	 * @param m the owner of the entities 
	 * @return a list of entities of type c if one is found 
	 */
	public static List<BaseEntity> getEntitiesOfClassAndOwner(List<BaseEntity> entities, Class<?> c,  int m) {
		List<BaseEntity> classEntities = new ArrayList<>();
		for (BaseEntity w : entities) {
			if (w.getClass() == c && w instanceof HasOwner && ((HasOwner) w).getOwner() == m) {
				classEntities.add(w);
			}
		}
		return classEntities;
	}
	
	/**
	 * Gets the entities of a class with an owners that are not the given owner. 
	 * @param c the class of entity searching for
	 * @param m the owner of the entities to exclude 
	 * @return a list of entities of type c and does not have an owner of m if found.
	 */
	public static List<BaseEntity> getEntitiesOfClassAndNotOwner(List<BaseEntity> entities, Class<?> c, int m) {
		List<BaseEntity> classEntities = new ArrayList<>();
		for (BaseEntity w : entities) {
			System.err.println("w: " + w.getClass());
			if (w.getClass() == c && w instanceof HasOwner && ((HasOwner) w).getOwner() != m) {
				System.err.println("w2222: " + w.getClass());
				classEntities.add(w);
			}
		}
		return classEntities;
	}

	/**
	 * Function to handle the overlay of textures for when the player is required to select an area on the game map. The
	 * overlaid image would be green if valid and red if invalid.
	 * @param temp  CheckSelect object that is the object class for the overlaying image.
	 * @param dimensions  the square dimensions of the object the image represents
	 * @param proj  array of floats containing the center coordinates of the overlay (where the cursor is) which would 
	 * 				be updated per tick. Also contains fixPos. Structure is {x coordinate, y coordinate, fixPos}. Array
	 * 				so that the values outside of the function would also be updated.
	 * @param building  The building type to show a specific overlay for if not null. If null, the overlay is simply a 
	 * 			tile (green or red).
	 */
	public static CheckSelect selectionStage(CheckSelect temp, float dimensions, float[] proj, BuildingType building) {
		boolean validSelect;
		int tileWidth = GameManager.get().getWorld().getMap().getProperties().get("tilewidth", Integer.class);
		int tileHeight = GameManager.get().getWorld().getMap().getProperties().get("tileheight", Integer.class);
		if (temp != null) {
			GameManager.get().getWorld().removeEntity(temp);
			validSelect = true;
		}
		OrthographicCamera camera = GameManager.get().getCamera();
		Vector3 worldCoords = camera.unproject(new
				Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		proj[0] = worldCoords.x / tileWidth;
		proj[1] = -(worldCoords.y - tileHeight / 2f)
				/ tileHeight + proj[0];
		proj[0] -= proj[1] - proj[0];
		proj[0] = (int) proj[0];
		proj[1] = (int) proj[1];
		if (dimensions % 2 == 0) {
			proj[2] = .5f;
		}
		if (!(proj[0] < (((dimensions + 1) / 2) - proj[2]) || proj[0] >
				(GameManager.get().getWorld().getWidth() - dimensions - proj[2])
				|| proj[1] < (((dimensions + 1) / 2) - proj[2]) || proj[1] >
				GameManager.get().getWorld().getLength() - dimensions - proj[2])) {
			if (building == null) {
				temp = new CheckSelect(proj[0]+proj[2]-((int)((dimensions+1)/2)), proj[1]+proj[2], 0f, dimensions, 
						dimensions, 0f);
				temp.setGreen();
			} else {
				temp = new CheckSelect(proj[0]+proj[2]-((int)((dimensions+1)/2)), proj[1]+proj[2], 0f, dimensions, dimensions, 
						0f,	building);
				validSelect = GameManager.get().getWorld().checkValidPlace(building, temp.getPosX(), temp.getPosY(), 
						dimensions, proj[2]);
				if (validSelect) {
					temp.setGreen();
				} else {
					temp.setRed();
				}
			}
			GameManager.get().getWorld().addEntity(temp);
		}
		return temp;
	}
}
