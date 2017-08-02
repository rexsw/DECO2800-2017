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


    public void addEntity(BaseEntity entity) {
        entities.add(entity);
    }

    public void removeEntity(BaseEntity entity) {
        entities.remove(entity);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }
}
