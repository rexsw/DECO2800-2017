package com.deco2800.marswars.net;

import com.deco2800.marswars.util.ServerGameInformation;

/**
 * An action that sends the current servers game information to clients.
 * @author James McCall
 *
 */
public class GameInfoAction implements Action {
    private ServerGameInformation gameInfo;

    public GameInfoAction() {
        // Blank constructor for kryonet
    }

    public GameInfoAction(ServerGameInformation gameInfo) {
        this.gameInfo = gameInfo;
    }

    /**
     * 
     * @return the game information sent by the server.
     */
    public ServerGameInformation getGameInfo() {
        return gameInfo;
    }
    
    @Override
    public String toString() {
        return "Game Information sent:" + gameInfo.toString();
    }
}
