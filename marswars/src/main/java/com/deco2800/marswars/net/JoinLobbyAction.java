package com.deco2800.marswars.net;

/**
 * Network action object for when somebody joins the lobby.
 */
public class JoinLobbyAction extends ChatAction {
    private String username;

    public JoinLobbyAction() {
        // Blank constructor needed due to Kryonet
    }

    public JoinLobbyAction(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
    
    @Override
    public String toString() {
        return "*" + this.username + "* joined the lobby.";
    }
}
