package com.deco2800.marswars.util;

import com.deco2800.marswars.worlds.MapSizeTypes;
import com.deco2800.marswars.worlds.map.tools.MapTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class to keep track of the currently selected options for a new game.
 * The isNewGameValid method should be called and checked before the stored values are used to instantiate a game to
 * ensure that they are valid.
 * 
 * @author James McCall
 *
 */
public class NewGameInformation {
    // Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(NewGameInformation.class);
    
    // The size of the map
    private MapSizeTypes mapSize;
    // The planet the game will take place on
    private MapTypes mapType;
    
    /**
     * Constructor for creating a new NewGameInformation object, all stored values are set to null.
     */
    public NewGameInformation() {
        setMapSize(null);
        setMapType(null);
    }

    /**
     * @return The currently selected map type, returns null if no map is selected. 
     */
    public MapTypes getMapType() {
        return mapType;
    }

    /**
     * Set the map type to the given map.
     * @param mapType The new map type selected.
     */
    public void setMapType(MapTypes mapType) {
        LOGGER.info("Map type set to:", mapType);
        this.mapType = mapType;
    }

    /**
     * @return The currently selected map size, returns null if no map size is selected.
     */
    public MapSizeTypes getMapSize() {
        return mapSize;
    }

    /**
     * Sets the currently selected map size to the given size.
     * @param mapSize The new map size selected.
     */
    public void setMapSize(MapSizeTypes mapSize) {
        LOGGER.info("Map size set to:", mapSize);
        this.mapSize = mapSize;
    }
    
    /**
     * Checks to see if all fields contain valid values that could be used to start a new game.
     * 
     * @return True if all information is valid, false otherwise.
     */
    public boolean isNewGameValid() {
        if (mapSize == null) {
            return false;
        }
        if (mapType == null) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String map = "null";
        String size = "null";
        String bool = String.valueOf(isNewGameValid());
        if (mapSize != null) {
            size = mapSize.toString();
        }
        if (mapType != null) {
            map = mapType.toString();
        }
        return "NewGameInformation, Map: " + map + " Size: " + size + " Valid " + bool;
    }
}
