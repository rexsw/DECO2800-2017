package com.deco2800.marswars.util;

import com.deco2800.marswars.worlds.MapSizeTypes;
import com.deco2800.marswars.worlds.map.tools.MapTypes;

/**
 * This class currently mirrors NewGameInformation however will be extended to include map data once
 * it is required to be tested.
 * 
 * @author James McCall
 *
 */
public class ServerGameInformation extends NewGameInformation {
    //Include map data
    
    /**
     * Instantiates and empty ServerGameInformaton, this is required for kyronet.
     */
    public ServerGameInformation() {
        super();
    }
    
    /**
     * Constructor for creating an instance of ServerGameInformation utilising
     * @param mapSize The size of the map.
     * @param mapType The type of the map.
     */
    public ServerGameInformation(MapSizeTypes mapSize, MapTypes mapType ) {
        super();
        setMapSize(mapSize);
        setMapType(mapType);
    }
}
