package com.deco2800.marswars.net;

import com.deco2800.marswars.util.ServerGameInformation;

public class StartGameAction implements Action {
    private boolean canStart;
    private ServerGameInformation gameInfo;

    /**
     * Blank constructor for kryonet
     */
    public StartGameAction() {}
    
    /**
     * Action sent when a new game should be initiated.
     * @param canStart True if the game is allowed to start, false otherwise.
     * @param gameInfo The information required to commence a new game.
     */
    public StartGameAction(boolean canStart, ServerGameInformation gameInfo) {
        this.canStart = canStart;
        this.gameInfo = gameInfo;
    }
    
    public boolean canGameStart() {
        return canStart;
    }
    
    public ServerGameInformation getGameInformation() {
        return gameInfo;
    }
    
    @Override
    public String toString() {
        return "Tried to start game: " + String.valueOf(canStart) + "| " + gameInfo.toString();  
    }
}
