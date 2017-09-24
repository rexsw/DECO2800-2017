package com.deco2800.marswars.net;

import com.deco2800.marswars.util.ServerGameInformation;;
public class GameInfoAction implements Action {
    private ServerGameInformation gameInfo;
    
    /**
     * Blank Constructor for Kryonet
     */
    public GameInfoAction() {}

    public GameInfoAction(ServerGameInformation gameInfo) {
        this.gameInfo = gameInfo;
    }
    
    public ServerGameInformation getGameInfo() {
        return gameInfo;
    }
    
    @Override
    public String toString() {
        return "Game Information sent:" + gameInfo.toString();
    }
}
