package com.deco2800.marswars.util;

import com.deco2800.marswars.worlds.MapSizeTypes;
import com.deco2800.marswars.worlds.map.tools.MapTypes;

public class ServerGameInformation extends NewGameInformation {
    //Include map data
    
    public ServerGameInformation() {
        super();
    }
    
    public ServerGameInformation(MapSizeTypes mapSize, MapTypes mapType ) {
        super();
        setMapSize(mapSize);
        setMapType(mapType);
    }
    
    @Override
    public String toString() {
        //TODO implement a proper to string
        return "ServerGameInformation";
    }
}
