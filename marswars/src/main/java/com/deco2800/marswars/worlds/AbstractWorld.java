package com.deco2800.marswars.worlds;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Selectable;
import com.deco2800.marswars.renderers.Renderable;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractWorld is the Game AbstractWorld
 *
 * It provides storage for the WorldEntities and other universal world level items.
 */
public abstract class AbstractWorld {

    private List<BaseEntity> entities = new ArrayList<>();
    protected TiledMap map;

    private int width;
    private int length;

    /**
     * Returns a list of entities in this world.
     *
     * @return All Entities in the world.
     */
    public List<BaseEntity> getEntities() {
        return new ArrayList<BaseEntity>(this.entities);
    }

    /**
     * Returns the current map for this world.
     *
     * @return Map object for this world.
     */
    public TiledMap getMap() {
        return this.map;
    }


    /**
     * Adds an entity to the game.
     *
     * @param entity the entity to be added.
     */
    public void addEntity(BaseEntity entity) {
        entities.add(entity);
    }

    /**
     * Removes an entity from the world.
     *
     * @param entity the entity to be removed.
     */
    public void removeEntity(BaseEntity entity) {

        entities.remove(entity);

    }

    /**
     * Sets a width for this world.
     *
     * @param width width's new value
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Sets the world length.
     *
     * @param length length's new value.
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Gets the width of the world.
     *
     * @return the width of the world.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the length of the world.
     *
     * @return the length of the world.
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets the number of tiles in the world.
     *
     * @return the number of tiles in the world.
     */
    public int getNumberOfTiles(){
        return width * length;
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
     * Gets the number of tiles in the world.
     *
     * @return the number of tiles in the world.
     */

}
