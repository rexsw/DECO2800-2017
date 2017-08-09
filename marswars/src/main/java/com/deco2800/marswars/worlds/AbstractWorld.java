package com.deco2800.marswars.worlds;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.deco2800.marswars.entities.BaseEntity;

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
     * Returns a list of entities in this world
     * @return All Entities in the world
     */
    public List<BaseEntity> getEntities() {
        return new ArrayList<BaseEntity>(this.entities);
    }

    /**
     * Returns the current map for this world
     * @return Map object for this world
     */
    public TiledMap getMap() {
        return this.map;
    }


    /**
     * Adds an entity to the game
     * @param entity
     */
    public void addEntity(BaseEntity entity) {
        entities.add(entity);
    }

    /**
     * Removes an entity from the world
     * @param entity
     */
    public void removeEntity(BaseEntity entity) {
        entities.remove(entity);
    }

    /**
     * Sets a width for this world
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Sets the world length
     * @param length
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Gets the width of the world
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the length of the world
     * @return
     */
    public int getLength() {
        return length;
    }
}
