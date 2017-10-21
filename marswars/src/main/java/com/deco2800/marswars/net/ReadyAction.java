package com.deco2800.marswars.net;


public class ReadyAction implements Action {
    private boolean ready;
    private LobbyUser user;

    public ReadyAction() {
        //Blank Constructor required for kryonet
    }
    
    /**
     * Constructor for sending client information to the server.
     * @param ready
     */
    public ReadyAction(boolean ready) {
        this.ready = ready;
    }
    
    /**
     * Constructor for sending server updates to the clients.
     * @param user The user whose ready status has changed.
     * @param ready The ready status of the user.
     */
    public ReadyAction(LobbyUser user, boolean ready) {
        this.user = user;
        this.ready = ready;
    }
    
    /**
     * The ready status of the action.
     * 
     * @return The ready status to set for the user.
     */
    public boolean getReadyStatus() {
        return this.ready;
    }
    
    /**
     * Get the user whose ready status has changed.
     * @return The user whose ready status needs to be updated.
     */
    public LobbyUser getUser() {
        return this.user;
    }
    
    @Override
    public String toString() {
        return "Set ready to " + ready;
    }
}
