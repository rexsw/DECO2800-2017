package com.deco2800.marswars.net;

/**
 * Network action object for when somebody leaves the lobby.
 */
public class LeaveLobbyAction extends ChatAction {
    private String username;

    public LeaveLobbyAction() {
        // Blank constructor needed due to Kryonet
    }

    public LeaveLobbyAction(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
    
    @Override
    public String toString() {
        return "*" + this.username + "* left the lobby.";
    }
}
