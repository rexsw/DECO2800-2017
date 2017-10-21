package com.deco2800.marswars.net;

/**
 * A class for storing the information related to player within a lobby.
 */
public class LobbyUser {
    // The User's username
    private String username;
    // The user's ready status
    private boolean ready;
    
    /**
     * Blank Constructor so object can be sent via Kryonet.
     */
    public LobbyUser() {
        // Blank constructor for kryonet
    } 
    
    /**
     * Constructor that assumes that a lobbyPlayer will not be ready, ie. used when 
     * initializing a lobby player.
     * @param username The player's username.
     */
    public LobbyUser(String username) {
        this.username = username;
        // by default user should not be ready
        this.ready = false;
    }
    
    /** 
     * lobby players.
     * @param username The player's username
     * @param ready The player's ready status.
     */
    public LobbyUser(String username, boolean ready) {
        this.username = username;
        this.ready = ready;
    }

    /**
     * Retrieve the name of the user.
     * @return The user's username.
     */
    public String getUsername() {
        return this.username;
    }
    
    /**
     * Check if the user has said they are ready for the game to start.
     * @return The user's ready status.
     */
    public boolean isReady() {
        return this.ready;
    }
    
    /**
     * Set a user's ready status to the given value.
     * 
     * @param ready The ready status to set for the user.
     */
    public void setReady(boolean ready) {
        this.ready = ready;
    }
    
  
    @Override
    public String toString() {
        return "| " + username + " : " + ready + " |";
    }
}
