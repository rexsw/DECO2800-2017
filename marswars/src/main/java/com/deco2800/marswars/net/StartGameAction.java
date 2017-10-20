package com.deco2800.marswars.net;

import com.deco2800.marswars.util.ServerGameInformation;

/**
 * An action that is sent to clients notifying them if a new game should be commenced, if true 
 * clients should launch the game with the specified actions.
 * 
 * @author James McCall
 *
 */
public class StartGameAction implements Action {
    private boolean canStart;
    private ServerGameInformation gameInfo;

    public StartGameAction() {
        // Blank constructor for kryonet
    }
    
    /**
     * Action sent when a new game should be initiated.
     * @param canStart True if the game is allowed to start, false otherwise.
     * @param gameInfo The information required to commence a new game.
     */
    public StartGameAction(boolean canStart, ServerGameInformation gameInfo) {
        this.canStart = canStart;
        this.gameInfo = gameInfo;
    }
    
    /**
     * 
     * @return true if game can start, false otherwise.
     */
    public boolean canGameStart() {
        return canStart;
    }
    
    /**
     * 
     * @return the game information required to launch the game.
     */
    public ServerGameInformation getGameInformation() {
        return gameInfo;
    }
    
    @Override
    public String toString() {
        return "Tried to start game: " + canStart + "| " + gameInfo.toString();  
    }
}
