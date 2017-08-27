package com.deco2800.marswars.worlds;

import com.deco2800.marswars.worlds.map.tools.MapContainer;


/**
 * Creates a new World that all elements on it can be procedural or customized generated.
 */
public final class CustomizedWorld extends BaseWorld {



    /**
     * Constructor of the customized world.
     *
     * @param mapContainer contains all elements to be loaded to the world.
     */
    public CustomizedWorld(MapContainer mapContainer) {
        super(mapContainer.getMap());
        mapContainer.passWorld(this);
    }

    /**
     * Reads a map container and loads all elements into the CustomisedWorld
     *
     * @param mapContainer the container to be read.
     */
    public void loadMapContainer(MapContainer mapContainer){
        mapContainer.generateEntities(true);
    }


}
