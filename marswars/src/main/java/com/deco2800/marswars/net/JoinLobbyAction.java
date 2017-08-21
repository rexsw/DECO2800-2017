package com.deco2800.marswars.net;

/**
 * Network action object for when somebody joins the lobby.
 */
public class JoinLobbyAction {
    private String username;

    public JoinLobbyAction() {}

    public JoinLobbyAction(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
}
