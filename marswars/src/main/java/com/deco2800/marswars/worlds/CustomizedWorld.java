package com.deco2800.marswars.worlds;

import com.deco2800.marswars.initiategame.GameSave;
import com.deco2800.marswars.worlds.map.tools.MapContainer;
import com.deco2800.marswars.worlds.map.tools.MapTypes;


/**
 * Creates a new World that all elements on it can be procedural or customized generated.
 */
public class CustomizedWorld extends BaseWorld {

    // the map type
    private MapTypes mapType;

    // the map size
    private MapSizeTypes mapSizeType;

    /**
     * Constructor of the customized world.
     *
     * @param mapContainer contains all elements to be loaded to the world.
     */
    public CustomizedWorld(MapContainer mapContainer) {
        super(mapContainer.getMap());
        mapContainer.passWorld(this);
        this.mapType = mapContainer.getMapType();
        this.mapSizeType = mapContainer.getMapSizeType();
    }

    /**
     * Reads a map container and loads all elements into the CustomisedWorld
     *
     * @param mapContainer the container to be read.
     */
    public void loadMapContainer(MapContainer mapContainer) {
        mapContainer.generateEntities(true);
    }

    /**
     * this function is used with game loading for loading all the resources
     */
    public void loadAlreadyMapContainer(MapContainer mapContainer,GameSave loadedGame) {
        mapContainer.loadResourceEntities(loadedGame);
    }

    /**
     * Return the world map type
     *
     * @return Return the world map type
     */
    public MapTypes getMapType() {
        return mapType;
    }

    /**
     * Return the world map size
     *
     * @return Return the world map size
     */
    public MapSizeTypes getMapSizeType() {
        return mapSizeType;
    }
}
