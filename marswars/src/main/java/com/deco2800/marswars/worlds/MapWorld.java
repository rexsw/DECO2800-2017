package com.deco2800.marswars.worlds;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

/**
 * Creates the map view of the entire game world
 */
public class MapWorld extends AbstractWorld {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MapWorld.class);


    private InitialWorld world;

    public MapWorld(InitialWorld initialWorld) {
        this.world = initialWorld;
        constructMap();
    }

    /**
     * constructs a map
     */
    private void constructMap() {
        // TODO link the two maps being loaded somehow
        this.map = new TmxMapLoader().load("resources/placeholderassets/mega200.tmx");

        LOGGER.info("Length: " + world.getLength() + ", Width: " + world.getWidth());

    }

}
