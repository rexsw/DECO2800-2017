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
			double tmpDistance = Math.sqrt(Math.pow(e.getPosX() - x, 2) + Math.pow(e.getPosY() - y, 2));

			if (tmpDistance < distance) {
				// Closer than current closest
				distance = tmpDistance;
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
		List<BaseEntity> entities = WorldUtil.getEntitiesOfLikeClass(GameManager.get().getWorld().getEntities(), c);
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
		float lowerY = y - radiusY*0.75f;
		float upperY = y + radiusY*1.25f;
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
	 * Lists the entities of a class or is a subclass of the provided class.
	 * @param entities The list of entities to filter
	 * @param c  class/parent class to search for.
	 * @return  list of BaseEntities that are actually instances of the provided class or a subclass of the provided 
	 * 			class. 
	 */
	public static List<BaseEntity> getEntitiesOfLikeClass(List<BaseEntity> entities, Class<?> c) {
		List<BaseEntity> classEntities = new ArrayList<>();
		for (BaseEntity w : entities) {
			if (c.isAssignableFrom(w.getClass())) {
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
			float tmpDistance = (float)(Math.sqrt(Math.pow(e.getPosX() - x, 2) + Math.pow(e.getPosY() - y, 2)));
			if (closest == null || dist > tmpDistance) {
				dist = tmpDistance;
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
			float tmpDistance = (float)(Math.sqrt(Math.pow(e.getPosX() - x, 2) + Math.pow(e.getPosY() - y, 2)));
			if ((closest == null || dist > tmpDistance) && e instanceof HasOwner && ((HasOwner) e).getOwner() == m) {
				dist = tmpDistance;
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
	 * Gets the closest entity of a class (and its subclasses) with an given owner 
	 * @param c the class of entity searching for
	 * @param m the owner of the entities 
	 * @return a list of entities of type c if one is found 
	 */
	public static List<BaseEntity> getEntitiesOfClassAndOwner(List<BaseEntity> entities, Class<?> c,  int m) {
		List<BaseEntity> classEntities = new ArrayList<>();
		for (BaseEntity w : entities) {
			if (c.isAssignableFrom(w.getClass()) && w instanceof HasOwner && ((HasOwner) w).getOwner() == m) {
				classEntities.add(w);
			}
		}
		return classEntities;
	}
	
	/**
	 * Gets the entities of a class (and its subclasses) with an owners that are not the given owner. 
	 * @param c the class of entity searching for
	 * @param m the owner of the entities to exclude 
	 * @return a list of entities of type c and does not have an owner of m if found.
	 */
	public static List<BaseEntity> getEntitiesOfClassAndNotOwner(List<BaseEntity> entities, Class<?> c, int m) {
		List<BaseEntity> classEntities = new ArrayList<>();
		for (BaseEntity w : entities) {
			if (c.isAssignableFrom(w.getClass()) && w instanceof HasOwner && ((HasOwner) w).getOwner() != m) {
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
		boolean[] validSelect = {false};
		int tileWidth = GameManager.get().getWorld().getMap().getProperties().get("tilewidth", Integer.class);
		int tileHeight = GameManager.get().getWorld().getMap().getProperties().get("tileheight", Integer.class);
		if (temp != null) {
			GameManager.get().getWorld().removeEntity(temp);
			validSelect[0] = true;
		}
		OrthographicCamera camera = GameManager.get().getCamera();
		Vector3 worldCoords = camera.unproject(new
				Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		if (building == null) {
			temp = handleCheckSelect(temp, dimensions, proj, tileWidth, tileHeight, worldCoords);
		} else {
			temp = handleCheckSelect(temp, dimensions, proj, building, validSelect, tileWidth, tileHeight, worldCoords);
		}
		return temp;
	}
	
	/**
	 * Private helper method to handle accurate display of the overlay when choosing a spot to build a building.
	 * @param temp  CheckSelect entity that is the overlay.
	 * @param dimensions  the radius of the overlay
	 * @param proj  array of coordinates for describing where the user selected and the adjusting factor
	 * @param building  the type of building to for a building specific overlay
	 * @param validSelect  array of 1 boolean indicating whether the selection is valid (array so that changes persist)
	 * @param tileWidth  width of 1 tile in the game.
	 * @param tileHeight  height of 1 tile in the game.
	 * @param worldCoords  Vector3 object containing exactly where the user's cursor is.
	 * @return  CheckSelect updated with the new coordinates.
	 */
	private static CheckSelect handleCheckSelect(CheckSelect temp, float dimensions, float[] proj, BuildingType building, 
			boolean[] validSelect, int tileWidth, int tileHeight, Vector3 worldCoords) {
		proj[0] = worldCoords.x / tileWidth;
		proj[1] = -(worldCoords.y - tileHeight / 2f) / tileHeight + proj[0];
		proj[0] -= proj[1] - proj[0];
		proj[0] = (int) proj[0];
		proj[1] = (int) proj[1];
		if ((int)dimensions == 1) {
			if(!(proj[0] < 0 || proj[0] >
			GameManager.get().getWorld().getWidth()
			|| proj[1] < 0 || proj[1] > GameManager.get().getWorld().getLength())) {
				if (building == null) {
					temp = new CheckSelect(proj[0], proj[1], 0f, dimensions, 
							dimensions, 0f);
					temp.setGreen();
				} else {
					temp = new CheckSelect(proj[0], proj[1], 0f, dimensions, dimensions, 
							0f,	building);
					validSelect[0] = GameManager.get().getWorld().checkValidPlace(building, (int)temp.getPosX(), (int)temp.getPosY(), 
							dimensions, 0);
					if (validSelect[0]) {
						temp.setGreen();
					} else {
						temp.setRed();
					}
				}
				GameManager.get().getWorld().addEntity(temp);
			}
		} else if(!(proj[0] < (((dimensions + 1) / 2) - proj[2]) || proj[0] >
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
				validSelect[0] = GameManager.get().getWorld().checkValidPlace(building, (int)temp.getPosX(), (int)temp.getPosY(), 
						dimensions, proj[2]);
				if (validSelect[0]) {
					temp.setGreen();
				} else {
					temp.setRed();
				}
			}
			GameManager.get().getWorld().addEntity(temp);
		}
		return temp;
	}
	
	/**
	 * Private helper method to handle accurate display of the overlay when choosing a spot for using a special item.
	 * @param temp  CheckSelect entity that is the overlay.
	 * @param dimensions  the radius of the overlay
	 * @param proj  array of coordinates for describing where the user selected and the adjusting factor
	 * @param validSelect  array of 1 boolean indicating whether the selection is valid (array so that changes persist)
	 * @param tileWidth  width of 1 tile in the game.
	 * @param tileHeight  height of 1 tile in the game.
	 * @param worldCoords  Vector3 object containing exactly where the user's cursor is.
	 * @return  CheckSelect updated with the new coordinates.
	 */
	private static CheckSelect handleCheckSelect(CheckSelect temp, float dimensions, float[] proj, int tileWidth, 
			int tileHeight, Vector3 worldCoords) {
		proj[0] = worldCoords.x / tileWidth;
		proj[1] = -(worldCoords.y - tileHeight / 2f) / tileHeight + proj[0];
		proj[0] -= proj[1] - proj[0];
		proj[0] = (int) proj[0];
		proj[1] = (int) proj[1];
		int lower = (int) dimensions / 2;
		int higherY = GameManager.get().getWorld().getLength() - lower;
		int higherX = GameManager.get().getWorld().getWidth() - lower;
		if (proj[0] < lower) {
			proj[0] = lower;
		}
		if (proj[0] > higherX) {
			proj[0] = higherX;
		}
		if (proj[1] < lower) {
			proj[1] = lower;
		}
		if (proj[1] > higherY) {
			proj[1] = higherY;
		}
		temp = new CheckSelect(proj[0] - 4* lower, proj[1] + (lower / 2), 0f, dimensions*2, dimensions*2, 0f, false);
		temp.setGreen();
		GameManager.get().getWorld().addEntity(temp);
		return temp;
	}
	
	/**
	 * Method to remove overlay entities on the world.
	 */
	public static void removeOverlay() {
		List<BaseEntity> temp = WorldUtil.getEntitiesOfClass(GameManager.get().getWorld().getEntities(), CheckSelect.class);
		for (BaseEntity t : temp) {
			GameManager.get().getWorld().removeEntity(t);
		}
	}
}
